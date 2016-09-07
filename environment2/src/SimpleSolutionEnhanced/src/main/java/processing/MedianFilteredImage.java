package processing;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MedianFilteredImage implements ModifiedImage {

private final ImageFromFile imageFromFile;

private final int maskLength;

public MedianFilteredImage(ImageFromFile imageFromFile, int maskLength) throws ImageProcessingException {
    if(maskLength % 2 == 0 || maskLength < 1) {
        throw new ImageProcessingException("Ineligible mask length");
    }
    this.maskLength = maskLength;
    this.imageFromFile = imageFromFile;
}

@Override
public List<Pixel> getPixels() {
    return imageFromFile.getPixels().stream().map(pixel -> {
        List<Pixel> mask = new MaskProvider(imageFromFile).readMask(maskLength, pixel);
        return getMedianPixel(mask, pixel);
    }).collect(Collectors.toList());
}

private Pixel getMedianPixel(List<Pixel> mask, Pixel basePixel) {
    Collections.sort(mask, (a, b) -> a.getRedValue() < b.getRedValue() ? -1 :
            a.getRedValue() == b.getRedValue() ? 0 : 1);
    int red = mask.get(mask.size() / 2).getRedValue();
    Collections.sort(mask, (a, b) -> a.getGreenValue() < b.getGreenValue() ? -1 :
            a.getGreenValue() == b.getGreenValue() ? 0 : 1);
    int green = mask.get(mask.size() / 2).getGreenValue();
    Collections.sort(mask, (a, b) -> a.getBlueValue() < b.getBlueValue() ? -1 :
            a.getBlueValue() == b.getBlueValue() ? 0 : 1);
    int blue = mask.get(mask.size() / 2).getBlueValue();
    return new Pixel(red, green, blue, basePixel.getxPosition(), basePixel.getyPosition());
}

@Override
public ImageFromFile getImageFromFile() {
    return imageFromFile;
}
}
