package app;

import bsq.BSQImage;
import bsq.BSQPixels;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.DynamicBackPropagation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Solution {

    private final BSQImage image;
    private NeuralNetwork neuralNetwork;
    private final int radius;

    public Solution(BSQImage image, int maxIterations, int radius, int... hiddenLayersNeurons) {
        this.image = image;
        this.radius = radius;
        int a = 2 * radius + 1;
        List<Integer> neuronsInLayer = new ArrayList<>();
        neuronsInLayer.add(image.bands() * a * a);
        for (int hiddenLayerNeurons : hiddenLayersNeurons) {
            neuronsInLayer.add(hiddenLayerNeurons);
        }
        neuronsInLayer.add(1);
        this.neuralNetwork = new MultiLayerPerceptron(neuronsInLayer);
        train(maxIterations);
    }

    public BufferedImage generate() throws Exception {
        BufferedImage output =
                new BufferedImage(image.dimension().width, image.dimension().height, BufferedImage.TYPE_BYTE_GRAY);
        try (BSQPixels pixels = image.pixels()) {
            for (int i = 0; i < image.dimension().height; i++) {
                for (int j = 0; j < image.dimension().width; j++) {
                    double neuralOutput = getNeuralOutput(normalize(
                            getPixelsBlock(image, pixels, j, i, radius)
                    ));
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
                int[] pix = referenceImage.getPixel(pair.getLeft(), pair.getRight(), new int[referenceImage.getNumBands()]);
                int x = pair.getLeft(), y = pair.getRight();
                dataSet.addRow(new DataSetRow(
                        normalize(
                                getPixelsBlock(image, pixels, x, y, radius)
                        ),
                        new double[] {isWhite(pix) ? 0 : 1}));
            });
            BackPropagation learning = new DynamicBackPropagation();
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

    private int[] getPixelsInput(BSQImage image, BSQPixels pixels, int x, int y){
        if (x < 0 || x >= image.dimension().width || y < 0 || y >= image.dimension().height) {
            return new int[image.bands()];
        }
        try {
            return pixels.get(x, y);
        } catch (IOException e) {
            return new int[image.bands()];
        }
    }

    private int[] getPixelsBlock(BSQImage image, BSQPixels pixels, int x, int y, int radius) {
        return IntStream.rangeClosed(x-radius, x+radius).mapToObj(i -> IntStream.rangeClosed(y-radius, y+radius).mapToObj(j -> getPixelsInput(image, pixels, i, j)).reduce(new int[]{}, ArrayUtils::addAll)).reduce(new int[]{}, ArrayUtils::addAll);
    }
}
