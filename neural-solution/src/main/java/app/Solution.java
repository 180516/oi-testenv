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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class Solution {

    private final BSQImage image;
    private NeuralNetwork neuralNetwork;

    public Solution(BSQImage image, int maxIterations) {
        this.image = image;
        this.neuralNetwork = new Perceptron(image.bands(), 1);
        train(maxIterations);
    }

    public BufferedImage generate() throws Exception {
        BufferedImage output =
                new BufferedImage(image.dimension().width, image.dimension().height, BufferedImage.TYPE_BYTE_GRAY);
        try (BSQPixels pixels = image.pixels()) {
            for (int i = 0; i < image.dimension().height; i++) {
                for (int j = 0; j < image.dimension().width; j++) {
                    double neuralOutput = getNeuralOutput(normalize(pixels.get(j, i)));
                    if (neuralOutput > 0.5) {
                        output.setRGB(j, i, Color.black.getRGB());
                    } else {
                        output.setRGB(j, i, Color.white.getRGB());
                    }
                }
            }
        }
        return output;
    }

    private void train(int maxIterations) {
        neuralNetwork.randomizeWeights();
        final DataSet dataSet = new DataSet(image.bands());
        try (BSQPixels pixels = image.pixels()) {
            File directory;
            if (getClass().getClassLoader().getResource(".") != null)
                directory = new File(getClass().getClassLoader().getResource(".").getPath());   //debug
            else
                directory = Paths.get(".").toFile();    //release
            Raster referenceImage = ImageIO.read(new File(directory, "reference.png")).getRaster();
            Stream.generate(() -> Pair.of(RandomUtils.nextInt(0, image.dimension().width), RandomUtils.nextInt(0, image.dimension().height))).limit(1000L).forEach(pair -> {
                try {
                    int[] pix = referenceImage.getPixel(pair.getLeft(), pair.getRight(), new int[referenceImage.getNumBands()]);
                    dataSet.addRow(new DataSetRow(
                            normalize(pixels.get(pair.getLeft(), pair.getRight())),
                            new double[] {isWhite(pix) ? 0 : 1}));
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

    private double[] normalize(int... input) {
        return Arrays.stream(input).mapToDouble(i -> i).map(x -> map(x, 0, 255, -1.0, 1.0)).toArray();
    }

    private double map(double x, double a, double b, double c, double d) {
        return (x-a)/(b-a)*(d-c)+c;
    }

    private boolean isWhite(int... channels) {
        return Arrays.stream(channels).allMatch(i -> i == 255);
    }

    private double getNeuralOutput(double[] input) {
        neuralNetwork.setInput(input);
        neuralNetwork.calculate();
        return neuralNetwork.getOutput()[0];
    }
}
