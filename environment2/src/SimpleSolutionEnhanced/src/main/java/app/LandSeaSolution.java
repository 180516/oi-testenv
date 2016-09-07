package app;

import bsq.BSQImage;
import bsq.InMemoryPixels;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class LandSeaSolution {

    private final BSQImage image;
    private final int borderValue;

    public LandSeaSolution(BSQImage image, int borderValue) {
        this.image = image;
        this.borderValue = borderValue;
    }

    public BufferedImage generate() throws Exception {
        BufferedImage output =
                new BufferedImage(image.dimension().width, image.dimension().height, BufferedImage.TYPE_BYTE_GRAY);
        InMemoryPixels pixels = image.inMemoryPixels();
        for (int i = 0; i < image.dimension().height; i++) {
            for (int j = 0; j < image.dimension().width; j++) {
                double average = Arrays.stream(pixels.get(j, i)).average().getAsDouble();
                if (average > borderValue) output.setRGB(j, i, Color.black.getRGB());
                else output.setRGB(j, i, Color.white.getRGB());
            }
        }
        return output;
    }
}
