import correctness.Correctness;
import correctness.CorrectnessResults;
import correctness.measures.CorrectnessMeasure;
import correctness.measures.Precision;
import correctness.measures.Recall;
import measures.AverageDifference;
import measures.MaximumDifference;
import measures.MeanSquareError;
import measures.QualityMeasure;

import javax.imageio.ImageIO;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public class Evaluation {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) throw new IllegalArgumentException("Invalid number of arguments");
        String dataType = args[0];
        if (!dataType.equals("ls") && !dataType.equals("sm"))
            throw new IllegalArgumentException("Invalid evaluation type, choose ls for land/sea or sm for soil moisture");
        File resDirectory = new File(args[1]);
        File refDirectory = new File(args[2]);
        if (!resDirectory.isDirectory() || !refDirectory.isDirectory())
            throw new IllegalArgumentException("Specified files are not directories");

        File[] results = resDirectory.listFiles();
        File[] references = refDirectory.listFiles();
        if (results.length != references.length)
            throw new IllegalArgumentException("Results and references files not equal");

        List<QualityMeasure> measures = new ArrayList<>();
        measures.addAll(Arrays.asList(new MeanSquareError(), new AverageDifference()));
        if (dataType.equals("sm")) {
            measures.add(new MaximumDifference());
        }

        List<CorrectnessMeasure> correctnessMeasures = new ArrayList<>();
        if (dataType.equals("ls")) {
            correctnessMeasures.addAll(Arrays.asList(new Precision(), new Recall()));
        }

        HashMap<String, Double> accumulatedResults = new HashMap<>();
        measures.forEach(m -> accumulatedResults.put(m.name(), 0.0));
        correctnessMeasures.forEach(m -> accumulatedResults.put(m.name(), 0.0));
        for (File result : results) {
            File reference = new File(refDirectory, result.getName());
            Raster resultImage = ImageIO.read(result).getData();
            Raster referenceImage = ImageIO.read(reference).getData();

            for (QualityMeasure measure : measures) {
                accumulatedResults.put(measure.name(),
                        accumulatedResults.get(measure.name()) + measure.get(resultImage, referenceImage));
            }

            CorrectnessResults correctnessResults = new Correctness().calculate(resultImage, referenceImage);
            for (CorrectnessMeasure measure : correctnessMeasures) {
                accumulatedResults.put(measure.name(),
                        accumulatedResults.get(measure.name()) + measure.get(correctnessResults));
            }
        }

        for (QualityMeasure measure : measures) {
            System.out.println(measure.name());
            System.out.println(accumulatedResults.get(measure.name()) / results.length);
        }

        for (CorrectnessMeasure measure : correctnessMeasures) {
            System.out.println(measure.name());
            System.out.println(accumulatedResults.get(measure.name()) / results.length);
        }
    }
}