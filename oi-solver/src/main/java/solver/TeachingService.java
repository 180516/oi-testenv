package solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class TeachingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeachingService.class);

    @Value("${teachingPatternsDirPath}")
    private String teachingPatternsDirPath;

    @Value("${perfectMapPath}")
    private String perfectMapPath;

    public void teach() throws InterruptedException {
        File dir = Paths.get(teachingPatternsDirPath).toFile();
        Arrays.asList(dir.listFiles()).forEach(file -> LOGGER.info("Found teaching pattern map: {}", file.getName()));
        File map = Paths.get(perfectMapPath).toFile();
        if(map.exists()) {
            LOGGER.info("Found perfect map: {}", map.getName());
        }
        Thread.sleep(5000);
    }
}
