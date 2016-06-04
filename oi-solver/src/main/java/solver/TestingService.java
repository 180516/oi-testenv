package solver;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import solver.image.BSQImage;
import solver.image.ImageMaskSaver;
import solver.image.Pixel;
import solver.image.RegularImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class TestingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestingService.class);

    @Value("${testingMapPath}")
    private String testingMapPath;

    @Value("${outputMapPath}")
    private String outputMapPath;

    @Value("${weightsFilePath}")
    private String weightsFilePath;

    public void test() throws IOException {
        NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(weightsFilePath);

        List<double[]> inputValues = new ArrayList<>();
        File testingFile = Paths.get(testingMapPath).toFile();
        inputValues.addAll(new BSQImage(testingFile, 8, new Dimension(954, 954)).toPixelList());
        DataSet test = new DataSet(8, 1);
        for (int i = 0; i < inputValues.size(); i++) {
            test.addRow(new DataSetRow(inputValues.get(i), new double[]{1})); //setting all output values to 1, I guess output is not needed here?
        }
        double[] networkOutput = new double[inputValues.size()];
        LOGGER.info("Testing started");
        for (int i = 0; i < test.getRows().size(); i++) {
            neuralNetwork.setInput(test.getRows().get(i).getInput());
            neuralNetwork.calculate();
            networkOutput[i] = neuralNetwork.getOutput()[0];
        }
        LOGGER.info("Testing ended");
        new ImageMaskSaver(954, 954, outputMapPath).saveFromDoubleArray(networkOutput);

    }
}
