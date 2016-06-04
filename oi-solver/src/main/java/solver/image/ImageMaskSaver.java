package solver.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageMaskSaver {

    private final int width;

    private final int height;

    private final String outputPath;

    public ImageMaskSaver(int width, int height, String outputPath) {
        this.width = width;
        this.height = height;
        this.outputPath = outputPath;
    }

    public void saveFromDoubleArray(double[] array) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = (int)array[width * y + x] * 255;
                Pixel pixel = new Pixel(value, value, value, x, y);
                bufferedImage.setRGB(x, y, ((pixel.getRedValue() & 0x0ff) << 16) | ((pixel.getGreenValue() & 0x0ff) << 8) | (pixel.getBlueValue() & 0x0ff));
            }
        }
        File file = new File(outputPath);
        ImageIO.write(bufferedImage, "png", file);
    }
    public void saveFromIntArray(int[] array) throws IOException {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        saveFromDoubleArray(doubleArray);
    }
}
