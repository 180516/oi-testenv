package correctness;

import java.awt.image.Raster;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public class Correctness {

    public CorrectnessResults calculate(Raster result, Raster reference) {
        int truePositives = 0;
        int trueNegatives = 0;
        int falsePositives = 0;
        int falseNegatives = 0;
        for (int i = result.getMinY(); i < result.getMinY() + result.getHeight(); i++) {
            for (int j = result.getMinX(); j < result.getMinX() + result.getWidth(); j++) {
                double ref = reference.getPixel(j, i, new double[reference.getNumBands()])[0];
                double res = result.getPixel(j, i, new double[result.getNumBands()])[0];
                if (ref >= 125 && res >= 125) { //both positive
                    truePositives++;
                } else if (ref >= 125 && res < 125) { //ref positive, res negative
                    falseNegatives++;
                } else if (ref < 125 && res < 125) { //both negatives
                    trueNegatives++;
                } else if (ref < 125 && res >= 125) { //ref negative, res positive
                    falsePositives++;
                } else {
                    throw new IllegalStateException("Cannot specify correct state");
                }
            }
        }
        return new CorrectnessResults(
                new Correct(truePositives, trueNegatives), new Incorrect(falsePositives, falseNegatives));
    }
}
