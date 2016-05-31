package solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SolvingRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolvingRunner.class);

    @Value("${runningMode}")
    private String mode;

    @Autowired
    private ApplicationContext applicationContext;

    private final TeachingService teachingService;

    private final TestingService testingService;

    @Autowired
    public SolvingRunner(TeachingService teachingService, TestingService testingService) {
        this.teachingService = teachingService;
        this.testingService = testingService;
    }

    @Override
    public void run(String... strings) throws Exception {
        switch (mode) {
            case "teaching": {
                LOGGER.info("Teaching mode selected . . .");
                teachingService.teach();
                LOGGER.info("Teaching complete");
            }
            break;
            case "testing": {
                LOGGER.info("Testing mode selected . . .");
                testingService.test();
                LOGGER.info("Testing complete");
            }
            break;
            default: {
                LOGGER.error("Wrong mode selected");
            }
            break;
        }
        SpringApplication.exit(applicationContext);
    }
}
