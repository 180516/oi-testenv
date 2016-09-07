package app;

import bsq.BSQImage;
import processing.ImageFromFile;
import processing.MedianFilteredImage;
import processing.PostProcessedImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class SimpleSolution {

    private static String dataType;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException("Invalid arguments count");
        }
        File dataDirectory = new File(args[0]);
        dataType = args[1];
        if (!dataDirectory.isDirectory()) {
            throw new IllegalArgumentException("Given path is not directory");
        }
        new SimpleSolution().run(new File(dataDirectory, "test"));
    }

    private void run(File dataDirectory) throws Exception {
        File solutionDirectory = getClass().getClassLoader().getResource(".") != null ?
                new File(getClass().getClassLoader().getResource(".").getPath()) :   //debug
                Paths.get(".").toFile();    //release

        int dataFilesCount = dataDirectory.listFiles().length;
        if (dataFilesCount % 2 == 0) throw new IllegalStateException("Odd data files size");
        int picturesCount = (dataFilesCount - 1) / 2;

        runAnalysis(dataDirectory, solutionDirectory, picturesCount);
    }

    private BufferedImage analyze(File imageFile, File propFile, int borderValue) throws Exception {
        int bands, width, height;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(propFile)))) {
            bands = Integer.valueOf(reader.readLine());
            width = Integer.valueOf(reader.readLine());
            height = Integer.valueOf(reader.readLine());
        }
        if(dataType.equals("ls")) {
            return new LandSeaSolution(new BSQImage(imageFile, bands, new Dimension(width, height)), borderValue).generate();
        }
        else {
            return new SoilMoistureSolution(new BSQImage(imageFile, bands, new Dimension(width, height))).generate();
        }
    }

    private void runAnalysis(File dataDirectory, File solutionDirectory, int picturesCount) throws Exception {
        int borderValue;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(solutionDirectory, "land-sea-boundary.txt"))))) {
            borderValue = Integer.valueOf(reader.readLine());
        }
        int maskSize;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(solutionDirectory, "median-filter-mask-size.txt"))))) {
            maskSize = Integer.valueOf(reader.readLine());
        }

        File resultsDirectory = new File(solutionDirectory, "results");
        resultsDirectory.mkdir();
        for (File file : resultsDirectory.listFiles()) {
            file.delete();
        }
        for (int i = 0; i < picturesCount; i++) {
            File imageFile = new File(dataDirectory, (i + 1) + ".bsq");
            File propFile = new File(dataDirectory, (i + 1) + ".txt");
            long start = System.currentTimeMillis();
            BufferedImage bufferedImage = analyze(imageFile, propFile, borderValue);

            ImageFromFile imageFromFile = new ImageFromFile(bufferedImage);
            MedianFilteredImage medianFilteredImage = new MedianFilteredImage(imageFromFile, maskSize);
            BufferedImage bufferedImage1 = new PostProcessedImage(medianFilteredImage).toBufferedImage();

            ImageIO.write(bufferedImage1,
                    "png", new File(resultsDirectory, (i + 1) + ".png"));

            System.out.println(String.format("#%d solution generated in %.3f s",
                    i + 1, (System.currentTimeMillis() - start) / 1000.0));
        }
    }
}
