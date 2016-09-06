package app;

import bsq.BSQImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

public class NeuralSolution {

    public static void main(String[] args) throws Exception {
        if (args.length != 2 && args.length != 3) throw new IllegalArgumentException("Invalid arguments count");
        File dataDirectory = new File(args[0]);
        if (!dataDirectory.isDirectory()) throw new IllegalArgumentException("Given path is not directory");
        String dataType = args[1];
        if (!dataType.equals("ls") && !dataType.equals("sm")) throw new IllegalArgumentException("Illegal data type");
        if (args.length == 3) {
            new NeuralSolution().train(dataDirectory, dataType);
        } else {
            new NeuralSolution().test(dataDirectory, dataType);
        }
    }

    private void train(File dataDirectory, String dataType) throws Exception {
        File solutionDirectory;
        if (getClass().getClassLoader().getResource(".") != null)
            solutionDirectory = new File(getClass().getClassLoader().getResource(".").getPath());   //debug
        else
            solutionDirectory = Paths.get(".").toFile();    //release
        File paramsFile = new File(solutionDirectory, "solution-params.txt");
        int inputLayers, maxIterations;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(paramsFile)))) {
            inputLayers = Integer.valueOf(reader.readLine());
            maxIterations = Integer.valueOf(reader.readLine());
        }
        Solution solution;

        File neuralFile;
        if (dataType.equals("sm")) {
            neuralFile = new File(solutionDirectory, "neural-file-sm");
        } else {
            neuralFile = new File(solutionDirectory, "neural-file-ls");
        }

        if (neuralFile.exists()) {
            neuralFile.delete();
        }
        neuralFile.createNewFile();
        solution = new Solution(inputLayers);
        Arrays.stream(dataDirectory.toPath().resolve("train").toFile().listFiles((file, s) -> s.endsWith(".bsq"))).forEach(file -> {
            File propFile = file.toPath().getParent().resolve(file.getName().replace(".bsq", ".txt")).toFile();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(propFile)))) {
                int bands, width, height;
                bands = Integer.valueOf(reader.readLine());
                width = Integer.valueOf(reader.readLine());
                height = Integer.valueOf(reader.readLine());
                System.out.println(String.format("Starting training from %s", file.getName()));
                solution.train(new BSQImage(file, bands, new Dimension(width, height)), maxIterations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        solution.save(neuralFile.getAbsolutePath());
    }

    private void test(File dataDirectory, String dataType) throws Exception {
        File solutionDirectory;
        if (getClass().getClassLoader().getResource(".") != null)
            solutionDirectory = new File(getClass().getClassLoader().getResource(".").getPath());   //debug
        else
            solutionDirectory = Paths.get(".").toFile();    //release
        File paramsFile = new File(solutionDirectory, "solution-params.txt");
        int inputLayers;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(paramsFile)))) {
            inputLayers = Integer.valueOf(reader.readLine());
        }
        Solution solution = new Solution(inputLayers);
        File neuralFile;
        if (dataType.equals("sm")) {
            neuralFile = new File(solutionDirectory, "neural-file-sm");
        } else {
            neuralFile = new File(solutionDirectory, "neural-file-ls");
        }
        solution.load(neuralFile.getAbsolutePath());
        long start = System.currentTimeMillis();
        File resultDirectory = new File(solutionDirectory, "results");
        resultDirectory.mkdir();
        for (File file : resultDirectory.listFiles()) {
            file.delete();
        }
        Arrays.stream(dataDirectory.toPath().resolve("test").toFile().listFiles((file, s) -> s.endsWith(".bsq"))).forEach(file -> {
            File propFile = file.toPath().getParent().resolve(file.getName().replace(".bsq", ".txt")).toFile();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(propFile)))) {
                int bands, width, height;
                bands = Integer.valueOf(reader.readLine());
                width = Integer.valueOf(reader.readLine());
                height = Integer.valueOf(reader.readLine());
                System.out.println(String.format("Starting testing %s", file.getName()));
                BufferedImage imageOutput = solution.generate(new BSQImage(file, bands, new Dimension(width, height)));
                ImageIO.write(imageOutput, "png", new File(resultDirectory, file.getName().replace(".bsq", ".png")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        long end = System.currentTimeMillis();
        System.out.println(String.format("Solutions found in %d ms", end - start));

    }
}
