package app;

import bsq.BSQImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * Created by Filip-PC on 05.06.2016.
 */
public class SimpleSolution {

    public static void main(String[] args) throws IOException, URISyntaxException {
        new SimpleSolution().run();
    }

    private void run() throws IOException, URISyntaxException {
        File directory;
        if (getClass().getClassLoader().getResource(".") != null)
            directory = new File(getClass().getClassLoader().getResource(".").getPath());
        else
            directory = Paths.get(".").toFile();

        File imageFile = new File(directory, "input.bsq");
        File propFile = new File(directory, "input-properties.txt");

        int bands, width, height;
        try (BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream(propFile)))) {
            bands = Integer.valueOf(stream.readLine());
            width = Integer.valueOf(stream.readLine());
            height = Integer.valueOf(stream.readLine());
        }

        Solution solution = new Solution(new BSQImage(imageFile, bands, new Dimension(width, height)));
        ImageIO.write(solution.generate(), "png", new File(directory, "output.png"));
    }
}
