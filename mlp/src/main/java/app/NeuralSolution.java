package app;

import bsq.BSQImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class NeuralSolution {

    public static void main(String[] args) throws Exception {
        new NeuralSolution().run();
    }

    private void run() throws Exception {
        File directory;
        if (getClass().getClassLoader().getResource(".") != null)
            directory = new File(getClass().getClassLoader().getResource(".").getPath());   //debug
        else
            directory = Paths.get(".").toFile();    //release

        File imageFile = new File(directory, "input.bsq");
        File propFile = new File(directory, "input-properties.txt");
        File paramsFile = new File(directory, "solution-params.txt");

        int bands, width, height;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(propFile)))) {
            bands = Integer.valueOf(reader.readLine());
            width = Integer.valueOf(reader.readLine());
            height = Integer.valueOf(reader.readLine());
        }
        int maxIterations, hiddenLayers, pixelRadius;
        int[] neuronsInLayer;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(paramsFile)))) {
            hiddenLayers = Integer.valueOf(reader.readLine());
            neuronsInLayer = new int[hiddenLayers];
            for (int i = 0; i < hiddenLayers; i++) {
                neuronsInLayer[i] = Integer.valueOf(reader.readLine());
            }
            maxIterations = Integer.valueOf(reader.readLine());
            pixelRadius = Integer.valueOf(reader.readLine());
        }

        long start = System.currentTimeMillis();
        BufferedImage solution =
                new Solution(new BSQImage(imageFile, bands, new Dimension(width, height)), maxIterations, pixelRadius, neuronsInLayer).generate();
        long end = System.currentTimeMillis();
        System.out.println(String.format("Solution found in %d ms", end - start));

        ImageIO.write(solution, "png", new File(directory, "output.png"));
    }
}
