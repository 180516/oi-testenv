package processing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class PostProcessedImage {

    private final ModifiedImage modifiedImage;

    public PostProcessedImage(ModifiedImage modifiedImage) {
        this.modifiedImage = modifiedImage;
    }

    public BufferedImage toBufferedImage() {
        BufferedImage bufferedImage = new BufferedImage(modifiedImage.getImageFromFile().getWidth(),
                modifiedImage.getImageFromFile().getHeight(), BufferedImage.TYPE_INT_RGB);
        modifiedImage.getPixels().forEach(pixel -> bufferedImage.setRGB(pixel.getxPosition(), pixel.getyPosition(),
        ((pixel.getRedValue() & 0x0ff) << 16) | ((pixel.getGreenValue() & 0x0ff) << 8) | (pixel.getBlueValue() & 0x0ff)));
        return bufferedImage;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(this.toBufferedImage(), "bmp", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
