package solver;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import solver.image.BSQImage;
import solver.image.ImageSaver;
import solver.image.RegularImage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TeachingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeachingService.class);

    @Value("${teachingDirPath}")
    private String teachingDirPath;

    @Value("${perfectMapName}")
    private String perfectMapName;

    @Value("${weightsFilePath}")
    private String weightsFilePath;

    @Value("${outputMapPath}")
    private String outputMapPath;

    public void teach() throws InterruptedException, IOException {
        int[] outputValues = new RegularImage(Paths.get(teachingDirPath, perfectMapName).toFile()).toBinaryMask();
        List<double[]> inputValues = new ArrayList<>();
        File dir = Paths.get(teachingDirPath).toFile();
        Arrays.asList(dir.listFiles()).forEach(file -> {
            if(file.getName().endsWith(".bsq")) {
                try {
                    inputValues.addAll(new BSQImage(file, 2, new Dimension(1703, 1235)).toPixelList());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        DataSet trainingSet = new DataSet(2, 1);
        for (int i = 0; i < outputValues.length; i++) {
            if(i % 5000 == 0) {
                trainingSet.addRow(new DataSetRow(inputValues.get(i), new double[]{outputValues[i]}));
            }
        }
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 8, 1);
        MomentumBackpropagation momentumBackpropagation = new MomentumBackpropagation();
        momentumBackpropagation.setMaxIterations(5000);
        LOGGER.info("Teaching started");
        for (int i = 0; i < 100; i++) {
            myMlPerceptron.learn(trainingSet, momentumBackpropagation);
        }
        LOGGER.info("Teaching ended");
        myMlPerceptron.save(weightsFilePath);

        // testing
        DataSet testingSet = new DataSet(2, 1);
        for (int i = 0; i < inputValues.size(); i++) {
            testingSet.addRow(new DataSetRow(inputValues.get(i), new double[]{outputValues[i]}));
        }
        NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile(weightsFilePath);
        double[] networkOutput = new double[testingSet.size()];
        for (int i = 0; i < testingSet.getRows().size(); i++) {
            loadedMlPerceptron.setInput(testingSet.getRows().get(i).getInput());
            loadedMlPerceptron.calculate();
            networkOutput[i] = loadedMlPerceptron.getOutput()[0];
        }
        LOGGER.info("Testing ended");
        new ImageSaver(1703, 1235, outputMapPath, true).saveFromDoubleArray(networkOutput);

    }
}
