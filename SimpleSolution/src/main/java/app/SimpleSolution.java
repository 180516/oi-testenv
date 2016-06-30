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

/**
 * Created by Filip-PC on 05.06.2016.
 */
public class SimpleSolution {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) throw new IllegalArgumentException("Invalid arguments count");
        File dataDirectory = new File(args[0]);
        if (!dataDirectory.isDirectory()) throw new IllegalArgumentException("Given path is not directory");
        String dataType = args[1];
        if (!dataType.equals("ls") && !dataType.equals("sm")) throw new IllegalArgumentException("Illegal data type");
        new SimpleSolution().run(new File(dataDirectory, "test"), dataType);
    }

    private void run(File dataDirectory, String dataType) throws Exception {
        File solutionDirectory = getClass().getClassLoader().getResource(".") != null ?
                new File(getClass().getClassLoader().getResource(".").getPath()) :   //debug
                Paths.get(".").toFile();    //release

        int dataFilesCount = dataDirectory.listFiles().length;
        if (dataFilesCount % 2 == 0) throw new IllegalStateException("Odd data files size");
        int picturesCount = (dataFilesCount - 1) / 2;

        if (dataType.equals("ls")) {
            runLandSeaAnalysis(dataDirectory, solutionDirectory, picturesCount);
        } else {
            runSoilMoistureAnalysis(dataDirectory, solutionDirectory, picturesCount);
        }
    }

    private BufferedImage analyzeLandSea(File imageFile, File propFile, int borderValue) throws Exception {
        int bands, width, height;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(propFile)))) {
            bands = Integer.valueOf(reader.readLine());
            width = Integer.valueOf(reader.readLine());
            height = Integer.valueOf(reader.readLine());
        }

        return new LandSeaSolution(new BSQImage(imageFile, bands, new Dimension(width, height)), borderValue).generate();
    }

    private BufferedImage analyzeSoilMoisture(File imageFile, File propFile) throws Exception {
        int bands, width, height;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(propFile)))) {
            bands = Integer.valueOf(reader.readLine());
            width = Integer.valueOf(reader.readLine());
            height = Integer.valueOf(reader.readLine());
        }

        return new SoilMoistureSolution(new BSQImage(imageFile, bands, new Dimension(width, height))).generate();
    }

    private void runLandSeaAnalysis(File dataDirectory, File solutionDirectory, int picturesCount) throws Exception {
        int borderValue;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(solutionDirectory, "land-sea-boundary.txt"))))) {
            borderValue = Integer.valueOf(reader.readLine());
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
            ImageIO.write(analyzeLandSea(imageFile, propFile, borderValue),
                    "png", new File(resultsDirectory, (i + 1) + ".png"));
            System.out.println(String.format("#%d solution generated in %.3f s",
                    i + 1, (System.currentTimeMillis() - start) / 1000.0));
        }
    }

    private void runSoilMoistureAnalysis(File dataDirectory, File solutionDirectory, int picturesCount) throws Exception {
        File resultsDirectory = new File(solutionDirectory, "results");
        resultsDirectory.mkdir();
        for (File file : resultsDirectory.listFiles()) {
            file.delete();
        }
        for (int i = 0; i < picturesCount; i++) {
            File imageFile = new File(dataDirectory, (i + 1) + ".bsq");
            File propFile = new File(dataDirectory, (i + 1) + ".txt");
            long start = System.currentTimeMillis();
            ImageIO.write(analyzeSoilMoisture(imageFile, propFile),
                    "png", new File(resultsDirectory, (i + 1) + ".png"));
            System.out.println(String.format("#%d solution generated in %.3f s",
                    i + 1, (System.currentTimeMillis() - start) / 1000.0));
        }
    }
}
