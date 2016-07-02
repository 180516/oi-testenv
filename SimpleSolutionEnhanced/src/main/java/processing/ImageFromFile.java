package processing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ImageFromFile {

    private final List<Pixel> pixels;

    private final int width;

    private final int height;

    public ImageFromFile(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        this.pixels = initializePixels(bufferedImage);
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
    }

    public ImageFromFile(BufferedImage image) {
        this.pixels = initializePixels(image);
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public Pixel getPixel(int x, int y) throws ImageProcessingException {
        int index = y * width + x;
        if(index >= pixels.size() || index < 0 || x < 0 || y < 0) {
            throw new ImageProcessingException("Pixel out of range, x:" + x + " y: " + y);
        }
        return pixels.get(index);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Pixel> getPixels() {
        return pixels;
    }

    private List<Pixel> initializePixels(BufferedImage bufferedImage) {
        List<Pixel> pixels = new ArrayList<>();
        Raster raster = bufferedImage.getRaster();
        boolean isColor = false;
        if (raster.getNumBands() == 1) { //greyscale
            isColor = false;
        } else if (raster.getNumBands() == 3) { //colorscale
            isColor = true;
        } else {
            throw new IllegalStateException();
        }
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                if (isColor) {
                    int[] canals = raster.getPixel(j, i, new int[3]);
                    pixels.add(new Pixel(canals[0], canals[1], canals[2], j, i));
                } else {
                    int canal = raster.getPixel(j, i, new int[1])[0];
                    pixels.add(new Pixel(canal, canal, canal, j, i));
                }
            }
        }
        return pixels;
    }
}