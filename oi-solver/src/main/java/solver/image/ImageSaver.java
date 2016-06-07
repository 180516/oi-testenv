package solver.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageSaver {

    private final int width;

    private final int height;

    private final String outputPath;

    private boolean scale;

    public ImageSaver(int width, int height, String outputPath, boolean scale) {
        this.width = width;
        this.height = height;
        this.outputPath = outputPath;
        this.scale = scale;
    }

    public void saveFromDoubleArray(double[] array) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = array[width * y + x];
                if(scale) {
                    value = value * 255.0;
                }
                Pixel pixel = new Pixel((int)value, (int)value, (int)value, x, y);
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
