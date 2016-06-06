package app;

import bsq.BSQImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;

/**
 * Created by Filip-PC on 05.06.2016.
 */
public class SimpleSolution {

    public static void main(String[] args) throws Exception {
        new SimpleSolution().run();
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

        int borderValue;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(paramsFile)))) {
            borderValue = Integer.valueOf(reader.readLine());
        }

        long start = System.currentTimeMillis();
        BufferedImage solution =
                new Solution(new BSQImage(imageFile, bands, new Dimension(width, height)), borderValue).generate();
        long end = System.currentTimeMillis();
        System.out.println(String.format("Solution found in %d ms", end - start));

        ImageIO.write(solution, "png", new File(directory, "output.png"));
    }
}
