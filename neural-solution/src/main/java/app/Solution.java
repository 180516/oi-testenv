package app;

import bsq.BSQImage;
import bsq.BSQPixels;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.PerceptronLearning;
import org.neuroph.util.TransferFunctionType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class Solution {
    private NeuralNetwork neuralNetwork;

    public Solution(File path) {
        this.neuralNetwork = NeuralNetwork.createFromFile(path);
    }

    public Solution(int bands) {
        this.neuralNetwork = new Perceptron(bands, 1, TransferFunctionType.LINEAR);
        neuralNetwork.randomizeWeights();
    }

    public BufferedImage generate(BSQImage image, String type) throws Exception {
        BufferedImage output =
                new BufferedImage(image.dimension().width, image.dimension().height, BufferedImage.TYPE_BYTE_GRAY);
        try (BSQPixels pixels = image.pixels()) {
            for (int i = 0; i < image.dimension().height; i++) {
                for (int j = 0; j < image.dimension().width; j++) {
                    float neuralOutput = (float) getNeuralOutput(normalize(pixels.get(j, i)));
                    neuralOutput = (neuralOutput + 1.f) / 2.f;
                    if (type.equals("sm") && neuralOutput < 0.1f) {
                        neuralOutput = 1f;
                    } else if (type.equals("ls")) {
                        if (neuralOutput > 0.15f) {
                            neuralOutput = 1;
                        } else {
                            neuralOutput = 0;
                        }
                    }
                    if (neuralOutput > 1f) {
                        neuralOutput = 1;
                    } else if (neuralOutput < 0f) {
                        neuralOutput = 0;
                    }

                    output.setRGB(j, i, new Color(1 - neuralOutput, 1 - neuralOutput, 1 - neuralOutput).getRGB());
                }
            }
        }
        MedianFilter.filter(output);
        return output;
    }

    public void train(BSQImage image, int maxIterations) {
        final DataSet dataSet = new DataSet(image.bands());
        try (BSQPixels pixels = image.pixels()) {
            File directory = image.getImageFile().toPath().getParent().resolve("references").toFile();
            Raster referenceImage = ImageIO.read(new File(directory, image.getImageFile().getName().replace(".bsq", ".png"))).getRaster();
            Stream.generate(() -> Pair.of(RandomUtils.nextInt(0, image.dimension().width), RandomUtils.nextInt(0, image.dimension().height))).limit(1000L).forEach(pair -> {
                try {
                    int[] pix = referenceImage.getPixel(pair.getLeft(), pair.getRight(), new int[referenceImage.getNumBands()]);
                    dataSet.addRow(new DataSetRow(
                            normalize(pixels.get(pair.getLeft(), pair.getRight())),
                            getFirstChannel(pix)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            PerceptronLearning learning = new PerceptronLearning();
            learning.setMaxIterations(maxIterations);
            System.out.println("Starting learning");
            neuralNetwork.learn(dataSet, learning);
            System.out.println("Finished learning");
            Stream.of(neuralNetwork.getInputNeurons()).forEach(neuron -> Stream.of(neuron.getWeights()).forEach(weight -> System.out.println(weight.getValue())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(String path) {
        neuralNetwork.load(path);
    }

    private double[] normalize(int... input) {
        return Arrays.stream(input).mapToDouble(i -> i).map(x -> map(x, 0, 255, -1.0, 1.0)).toArray();
    }

    private double map(double x, double a, double b, double c, double d) {
        return (x - a) / (b - a) * (d - c) + c;
    }

    private double[] getFirstChannel(int... channels) {
        return new double[]{normalize(channels)[0]};
    }

    private double getNeuralOutput(double[] input) {
        neuralNetwork.setInput(input);
        neuralNetwork.calculate();
        return neuralNetwork.getOutput()[0];
    }

    public void save(String path) {
        neuralNetwork.save(path);
    }
}
