package app;

import bsq.BSQImage;
import bsq.InMemoryPixels;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Filip-PC on 23.06.2016.
 */
public class SoilMoistureSolution {

    private final BSQImage image;

    public SoilMoistureSolution(BSQImage image) {
        this.image = image;
    }

    public BufferedImage generate() throws IOException {
        BufferedImage output =
                new BufferedImage(image.dimension().width, image.dimension().height, BufferedImage.TYPE_BYTE_GRAY);
        InMemoryPixels pixels = image.inMemoryPixels();
        for (int i = 0; i < image.dimension().height; i++) {
            for (int j = 0; j < image.dimension().width; j++) {
                double average = Arrays.stream(pixels.get(j, i)).average().getAsDouble();
                int value;
                if (average < 1) { //no data
                    value = 0;
                } else if (average > 100 && average < 180) {
                    value = 255 - (int) average / 6;
                } else {
                    value = 255 - (int) average / 2;
                }
                output.setRGB(j, i, new Color(value, value, value).getRGB());
            }
        }
        return output;
    }
}
