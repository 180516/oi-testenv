package app;

import bsq.BSQImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NeuralSolution {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) throw new IllegalArgumentException("Invalid arguments count");
        File dataDirectory = new File(args[0]);
        if (!dataDirectory.isDirectory()) throw new IllegalArgumentException("Given path is not directory");
        String dataType = args[1];
        if (!dataType.equals("ls") && !dataType.equals("sm")) throw new IllegalArgumentException("Illegal data type");
        File neuralFile = new File(args[2]);
        if (neuralFile.isDirectory()) throw new IllegalArgumentException("Expected neural path to be a file, not a directory");
        new NeuralSolution().run(dataDirectory, neuralFile, dataType);
    }

    private void run(File dataDirectory, File neuralFile, String dataType) throws Exception {
        File solutionDirectory;
        if (getClass().getClassLoader().getResource(".") != null)
            solutionDirectory = new File(getClass().getClassLoader().getResource(".").getPath());   //debug
        else
            solutionDirectory = Paths.get(".").toFile();    //release
        File paramsFile = new File(solutionDirectory, "solution-params.txt");
        int pixelRadius;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(paramsFile)))) {
            pixelRadius = Integer.valueOf(reader.readLine());
        }
        Solution solution;
        if (!neuralFile.exists()) {
            neuralFile.createNewFile();
            solution = new Solution(pixelRadius, dataType);
            List<BSQImage> imageList = new ArrayList<>();
            Arrays.stream(dataDirectory.toPath().resolve("train").toFile().listFiles((file, s) -> s.endsWith(".bsq"))).forEach(file -> {
                File propFile = file.toPath().getParent().resolve(file.getName().replace(".bsq", ".txt")).toFile();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(propFile)))) {
                    int bands, width, height;
                    bands = Integer.valueOf(reader.readLine());
                    width = Integer.valueOf(reader.readLine());
                    height = Integer.valueOf(reader.readLine());
                    System.out.println(String.format("Added %s to training set", file.getName()));
                    imageList.add(new BSQImage(file, bands, new Dimension(width, height)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            solution.train(imageList);
            solution.save(neuralFile.getAbsolutePath());
        } else {
            solution = new Solution(neuralFile, pixelRadius);
        }
        long start = System.currentTimeMillis();
        File resultDirectory = new File(solutionDirectory, "results");
        resultDirectory.mkdir();
        Arrays.stream(dataDirectory.toPath().resolve("test").toFile().listFiles((file, s) -> s.endsWith(".bsq"))).forEach(file -> {
            File propFile = file.toPath().getParent().resolve(file.getName().replace(".bsq", ".txt")).toFile();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(propFile)))) {
                int bands, width, height;
                bands = Integer.valueOf(reader.readLine());
                width = Integer.valueOf(reader.readLine());
                height = Integer.valueOf(reader.readLine());
                System.out.println(String.format("Starting testing %s", file.getName()));
                BufferedImage imageOutput = solution.generate(new BSQImage(file, bands, new Dimension(width, height)), dataType);
                ImageIO.write(imageOutput, "png", new File(resultDirectory, file.getName().replace(".bsq", ".png")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        long end = System.currentTimeMillis();
        System.out.println(String.format("Solution found in %d ms", end - start));
    }
}
