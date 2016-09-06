package bsq;

import java.awt.*;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Filip-PC on 06.06.2016.
 */
public class BSQPixels implements AutoCloseable {

    private final RandomAccessFile imageFile;
    private final int bands;
    private final Dimension dimension;

    public BSQPixels(RandomAccessFile imageFile, int bands, Dimension dimension) {
        this.imageFile = imageFile;
        this.bands = bands;
        this.dimension = dimension;
    }

    public int[] get(int x, int y) throws IOException {
        if (x < 0 || y < 0)
            throw new IllegalArgumentException("Pixel dimension below zero");
        if (x >= dimension.width || y >= dimension.height)
            throw new IllegalArgumentException("Pixel dimension above size");

        int[] pixel = new int[bands];
        int row = y * dimension.width * bands + x;
        imageFile.seek(row);
        for (int i = 0; i < bands; i++) {
            pixel[i] = imageFile.read();
            if (i < bands - 1) imageFile.skipBytes(dimension.width - 1);
        }
        return pixel;
    }

    @Override
    public void close() throws Exception {
        imageFile.close();
    }
}
