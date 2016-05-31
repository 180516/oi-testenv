package solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class SolvingRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolvingRunner.class);

    @Override
    public void run(String... strings) throws Exception {
    }
}
