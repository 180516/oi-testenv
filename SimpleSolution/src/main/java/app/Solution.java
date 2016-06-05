package app;

import bsq.BSQImage;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;

/**
 * Created by Filip-PC on 05.06.2016.
 */
public class Solution {

    private final BSQImage image;

    public Solution(BSQImage image) {
        this.image = image;
    }

    public BufferedImage generate() {
        BufferedImage output =
                new BufferedImage(image.dimension().width, image.dimension().height, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < image.dimension().height; i++) {
            for (int j = 0; j < image.dimension().width; j++) {
                output.setRGB(j, i, Color.grayRgb((int)(Math.random() * 256)).hashCode());
            }
        }
        return output;
    }
}
