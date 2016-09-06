package bsq;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Filip-PC on 22.06.2016.
 */
public class InMemoryPixels {
    private final int[] pixels;
    private final int bands;
    private final Dimension dimension;

    public InMemoryPixels(int[] pixels, int bands, Dimension dimension) {
        this.pixels = pixels;
        this.bands = bands;
        this.dimension = dimension;
    }

    public int[] get(int x, int y) throws IOException {
        if (x < 0 || y < 0)
            throw new IllegalArgumentException("Pixel dimension below zero");
        if (x >= dimension.width || y >= dimension.height)
            throw new IllegalArgumentException("Pixel dimension above size");

        int[] pixel = new int[bands];
        int row = y * dimension.width * bands;
        for (int i = 0; i < bands; i++) {
            pixel[i] = pixels[row + x + (dimension.width * i)];
        }
        return pixel;
    }

}
