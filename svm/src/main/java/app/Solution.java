package app;

import bsq.BSQImage;
import bsq.InMemoryPixels;
import libsvm.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;

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
    private int radius;
    private svm_parameter param;
    private svm_model model;

    public Solution(File path, int radius) throws IOException {
        this.radius = radius;
        this.model = svm.svm_load_model(path.getAbsolutePath());
    }

    public Solution(int radius, String dataType) {
        this.radius = radius;

        param = new svm_parameter();
        param.probability = 1;
        param.gamma = 0.5;
        param.nu = 0.5;
        param.C = 1;
        if ("ls".equals(dataType)) {
            param.svm_type = svm_parameter.C_SVC;
        } else {
            param.svm_type = svm_parameter.EPSILON_SVR;
        }
        param.kernel_type = svm_parameter.LINEAR;
        param.cache_size = 20000;
        param.eps = 0.001;
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
                if ("sm".equals(dataType)) {
                    neuralOutput = neuralOutput > 0.001 ? neuralOutput * 4.7f : .0f;
                }
                if (neuralOutput > 1.f) {
                    neuralOutput = 1.f;
                }
                if (neuralOutput < 0.f) {
                    neuralOutput = 0.f;
                }
                output.setRGB(j, i, new Color(neuralOutput, neuralOutput, neuralOutput).getRGB());
            }
        }
        return output;
    }

    public void train(List<BSQImage> images) {
        List<Pair<svm_node[], Double>> trainSet = new ArrayList<>();
        images.forEach(image -> {
            try {
                InMemoryPixels pixels = image.inMemoryPixels();
                File directory = image.getImageFile().toPath().getParent().resolve("references").toFile();
                Raster referenceImage = ImageIO.read(new File(directory, image.getImageFile().getName().replace(".bsq", ".png"))).getRaster();
                Stream.generate(() -> Pair.of(RandomUtils.nextInt(0, image.dimension().width), RandomUtils.nextInt(0, image.dimension().height))).limit(1000L).forEach(pair -> {
                    int[] pix = referenceImage.getPixel(pair.getLeft(), pair.getRight(), new int[referenceImage.getNumBands()]);
                    int x = pair.getLeft(), y = pair.getRight();
                    double[] inputVector = normalize(getPixelsBlock(image, pixels, x, y, radius));
                    svm_node[] nodes = new svm_node[inputVector.length];
                    for (int i = 0; i < nodes.length; i++) {
                        svm_node node = new svm_node();
                        node.value = inputVector[i];
                        node.index = i;
                        nodes[i] = node;
                    }
                    double output = getFirstChannel(pix);
                    trainSet.add(Pair.of(nodes, output));
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        svm_problem problem = new svm_problem();
        problem.l = trainSet.size();
        problem.y = trainSet.stream().map(Pair::getValue).mapToDouble(Double::doubleValue).toArray();
        problem.x = trainSet.stream().map(Pair::getKey).toArray(svm_node[][]::new);
        System.out.println("Starting learning");
        model = svm.svm_train(problem, param);
        System.out.println("Finished learning");
    }

    private double[] normalize(int... input) {
        return Arrays.stream(input).mapToDouble(i -> i).map(x -> map(x, 0, 255, 0.0, 1.0)).toArray();
    }

    private double map(double x, double a, double b, double c, double d) {
        return (x-a)/(b-a)*(d-c)+c;
    }

    private double getNeuralOutput(double[] input) {
        svm_node[] nodes = new svm_node[input.length];
        for (int i = 0; i < input.length; i++) {
            svm_node node = new svm_node();
            node.index = i;
            node.value = input[i];
            nodes[i] = node;
        }
        return svm.svm_predict(model, nodes);
    }

    private double getFirstChannel(int... channels) {
        return normalize(channels)[0];
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
        try {
            svm.svm_save_model(path, model);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
