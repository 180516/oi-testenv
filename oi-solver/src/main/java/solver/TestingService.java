package solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import solver.image.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class TestingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestingService.class);

    @Value("${testingMapPath}")
    private String testingMapPath;

    @Value("${outputMapPath}")
    private String outputMapPath;

    public void test() throws InterruptedException, IOException {
        LOGGER.info("Found testing map: {}", Paths.get(testingMapPath).toFile().getName());
        Thread.sleep(5000);
        saveRandomImage();
        LOGGER.info("Generated output map: {}", outputMapPath);
    }

    public void saveRandomImage() throws IOException {
        BufferedImage bufferedImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 512; i++) {
            for (int j = 0; j < 512; j++) {
                int value = ThreadLocalRandom.current().nextInt(0, 255 + 1);
                Pixel pixel = new Pixel(value, value, value, j, i);
                bufferedImage.setRGB(j, i, ((pixel.getRedValue() & 0x0ff) << 16) | ((pixel.getGreenValue() & 0x0ff) << 8) | (pixel.getBlueValue() & 0x0ff));
            }
        }
        File file = new File(outputMapPath);
        ImageIO.write(bufferedImage, "bmp", file);
    }
}
