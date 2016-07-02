package processing;

import java.util.ArrayList;
import java.util.List;

public final class MaskProvider {
    private final ImageFromFile imageFromFile;

    public MaskProvider(ImageFromFile imageFromFile) {
        this.imageFromFile = imageFromFile;
    }

    List<Pixel> readMask(int maskLength, Pixel basePixel) {
        int size = (maskLength - 1) / 2;
        List<Pixel> mask = new ArrayList<>();
        for (int yMask = -1 * size; yMask <= size; yMask++) {
            for (int xMask = -1 * size; xMask <= size; xMask++) {
                try {
                    mask.add(imageFromFile.getPixel(basePixel.getxPosition() + xMask, basePixel.getyPosition() + yMask));
                } catch (ImageProcessingException e) {
                }
            }
        }
        return mask;
    }

}
