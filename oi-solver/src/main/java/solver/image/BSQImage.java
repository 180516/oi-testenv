package solver.image;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public double[] pixel(int x, int y) throws IOException {
        if (x < 0 || y < 0)
            throw new IllegalArgumentException("Pixel dimension below zero");
        if (x >= dimension.width || y >= dimension.height)
            throw new IllegalArgumentException("Pixel dimension above size");

        double[] pixel = new double[bands];
        try (FileInputStream stream = new FileInputStream(imageFile)) {
            int row = y * dimension.width * bands + x;
            stream.skip(row);
            for (int i = 0; i < bands; i++) {
                pixel[i] = stream.read();
                if (i < bands - 1) stream.skip(dimension.width - 1);
            }
        }
        return pixel;
    }

    public List<double[]> toPixelList() throws IOException {
        List<double[]> dataSet = new ArrayList<>();
        for (int y = 0; y < dimension.getHeight(); y++) {
            for (int x = 0; x < dimension.getWidth(); x++) {
                dataSet.add(pixel(x, y));
            }
        }
        return dataSet;
    }

    public int bands() {
        return bands;
    }

    public Dimension dimension() {
        return dimension;
    }
}
