package bsq;

import java.awt.*;
import java.io.*;

/**
 * Created by Filip-PC on 01.06.2016.
 */
public class BSQImage {

    private final File imageFile;
    private final int bands;
    private final Dimension dimension;

    public BSQImage(File imageFile, int bands, Dimension dimension) {
        this.imageFile = imageFile;
        this.bands = bands;
        this.dimension = dimension;
    }

    public BSQPixels pixels() throws FileNotFoundException {
        return new BSQPixels(new RandomAccessFile(imageFile, "r"), bands, dimension);
    }

    public InMemoryPixels inMemoryPixels() throws IOException {
        int[] pixels = new int[dimension.width * bands * dimension.height];
        FileInputStream stream = new FileInputStream(imageFile);
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = stream.read();
        }
        stream.close();
        return new InMemoryPixels(pixels, bands, dimension);
    }

    public int bands() {
        return bands;
    }

    public Dimension dimension() {
        return dimension;
    }
}
