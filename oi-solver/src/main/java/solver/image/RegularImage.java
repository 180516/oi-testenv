package solver.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public class RegularImage {

    private final BufferedImage bufferedImage;

    public RegularImage(File file) throws IOException {
        this.bufferedImage = ImageIO.read(file);
    }

    public int[] toBinaryMask() {
        int[] values = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];

//        Raster raster = bufferedImage.getRaster();
//        boolean isColor;
//        if (raster.getNumBands() == 1) { //greyscale
//            isColor = false;
//        } else if (raster.getNumBands() == 3) { //colorscale
//            isColor = true;
//        } else {
//            throw new IllegalStateException();
//        }
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int clr = bufferedImage.getRGB(x, y);
                int v = (clr & 0x00ff0000) >> 16;
                if (v == 0) {
                    values[y * bufferedImage.getWidth() + x] = 0;
                } else {
                    values[y * bufferedImage.getWidth() + x] = 1;
                }
//                if (isColor) {
//                    int[] canals = raster.getPixel(x, y, new int[3]);
//                    if(canals[0] == 0) {
//                        values[y * bufferedImage.getWidth() + x] = 0;
//                    } else {
//                        values[y * bufferedImage.getWidth() + x] = 1;
//                    }
//                } else {
//                    int canal = raster.getPixel(x, y, new int[1])[0];
//                    if(canal == 0) {
//                        values[y * bufferedImage.getWidth() + x] = 0;
//                    } else {
//                        values[y * x + x] = 1;
//                    }
//                }
            }
        }
        int c = 0;
        for (int i = 0; i < values.length; i++) {
            if(values[i] == 0) c++;
        }
        return values;
    }
}