package app;

import bsq.BSQImage;
import bsq.InMemoryPixels;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.ResilientPropagation;
import org.neuroph.util.TransferFunctionType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Solution {
    private NeuralNetwork neuralNetwork;
    private int radius;
    private int inputVectorSize;

    public Solution(File path, int radius) {
        this.radius = radius;
        this.neuralNetwork = NeuralNetwork.createFromFile(path);
    }

    public Solution(int bands, int radius, int... hiddenLayersNeurons) {
        this.radius = radius;
        int a = 2 * radius + 1;
        inputVectorSize = bands * a * a;
        List<Integer> neuronsInLayer = new ArrayList<>();
        neuronsInLayer.add(inputVectorSize);
        for (int hiddenLayerNeurons : hiddenLayersNeurons) {
            neuronsInLayer.add(hiddenLayerNeurons);
        }
        neuronsInLayer.add(1);
        this.neuralNetwork = new MultiLayerPerceptron(neuronsInLayer, TransferFunctionType.TANH);
        neuralNetwork.randomizeWeights();
    }

    public BufferedImage generate(BSQImage image, String dataType) throws Exception {
        BufferedImage output =
                new BufferedImage(image.dimension().width, image.dimension().height, BufferedImage.TYPE_BYTE_GRAY);
        InMemoryPixels pixels = image.inMemoryPixels();
        for (int i = 0; i < image.dimension().height; i++) {
            for (int j = 0; j < image.dimension().width; j++) {
                float neuralOutput = (float) getNeuralOutput(normalize(
                        getPixelsBlock(image, pixels, j, i, radius)
                ));
                neuralOutput = (neuralOutput + 1.f) / 2.f;
                output.setRGB(j, i, new Color(neuralOutput, neuralOutput, neuralOutput).getRGB());
            }
        }
        if ("ls".equals(dataType)) {
            MedianFilter.filter(output);
        }
        return output;
    }

    public void train(BSQImage image, int maxIterations) {
        final DataSet dataSet = new DataSet(inputVectorSize);
        try {
            InMemoryPixels pixels = image.inMemoryPixels();
            File directory = image.getImageFile().toPath().getParent().resolve("references").toFile();
            Raster referenceImage = ImageIO.read(new File(directory, image.getImageFile().getName().replace(".bsq", ".png"))).getRaster();
            Stream.generate(() -> Pair.of(RandomUtils.nextInt(0, image.dimension().width), RandomUtils.nextInt(0, image.dimension().height))).limit(1000L).forEach(pair -> {
                int[] pix = referenceImage.getPixel(pair.getLeft(), pair.getRight(), new int[referenceImage.getNumBands()]);
                int x = pair.getLeft(), y = pair.getRight();
                double[] inputVector = normalize(getPixelsBlock(image, pixels, x, y, radius));
                double[] outputVector = getFirstChannel(pix);
                dataSet.addRow(new DataSetRow(
                        inputVector,
                        outputVector));
            });
            BackPropagation learning = new ResilientPropagation();
            learning.setMaxIterations(maxIterations);
            System.out.println("Starting learning");
            neuralNetwork.learn(dataSet, learning);
            System.out.println("Finished learning");
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

    private double getNeuralOutput(double[] input) {
        neuralNetwork.setInput(input);
        neuralNetwork.calculate();
        return neuralNetwork.getOutput()[0];
    }

    private double[] getFirstChannel(int... channels) {
        return new double[] {normalize(channels)[0]};
    }

    private int[] getPixelsInput(BSQImage image, InMemoryPixels pixels, int x, int y){
        if (x < 0 || x >= image.dimension().width || y < 0 || y >= image.dimension().height) {
            return new int[image.bands()];
        }
        try {
            return pixels.get(x, y);
        } catch (IOException e) {
            return new int[image.bands()];
        }
    }

    private int[] getPixelsBlock(BSQImage image, InMemoryPixels pixels, int x, int y, int radius) {
        return IntStream.rangeClosed(x-radius, x+radius).mapToObj(i -> IntStream.rangeClosed(y-radius, y+radius).mapToObj(j -> getPixelsInput(image, pixels, i, j)).reduce(new int[]{}, ArrayUtils::addAll)).reduce(new int[]{}, ArrayUtils::addAll);
    }

    public void save(String path) {
        neuralNetwork.save(path);
    }
}
