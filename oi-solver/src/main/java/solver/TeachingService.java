package solver;

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

    public void teach() throws InterruptedException, IOException {
        int[] outputValues = new RegularImage(Paths.get(teachingDirPath, perfectMapName).toFile()).toBinaryMask();
        List<double[]> inputValues = new ArrayList<>();
        File dir = Paths.get(teachingDirPath).toFile();
        Arrays.asList(dir.listFiles()).forEach(file -> {
            if(file.getName().endsWith(".bin")) {
                try {
                    inputValues.addAll(new BSQImage(file, 8, new Dimension(946, 892)).toPixelList());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        DataSet trainingSet = new DataSet(8, 1);
        for (int i = 0; i < outputValues.length; i++) {
            trainingSet.addRow(new DataSetRow(inputValues.get(i), new double[]{outputValues[i]}));
        }
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 8, 8, 1);
        MomentumBackpropagation momentumBackpropagation = new MomentumBackpropagation();
        //momentumBackpropagation.setMaxError(0.001);
        momentumBackpropagation.setMaxIterations(10);
        LOGGER.info("Teaching started");
        myMlPerceptron.learn(trainingSet, momentumBackpropagation);
        LOGGER.info("Teaching ended");
        myMlPerceptron.save(weightsFilePath);
    }
}
