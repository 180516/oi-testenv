package measures;

import java.awt.image.Raster;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public class AverageDifference extends QualityMeasure {
    @Override
    protected double calculate(Raster result, Raster reference) {
        double sum = 0;
        for (int i = result.getMinY(); i < result.getMinY() + result.getHeight(); i++) {
            for (int j = result.getMinX(); j < result.getMinX() + result.getWidth(); j++) {
                double ref = reference.getPixel(j, i, new double[reference.getNumBands()])[0];
                double res = result.getPixel(j, i, new double[result.getNumBands()])[0];
                double diff = Math.abs(ref - res);
                sum += diff;
            }
        }
        return sum / (result.getWidth() * result.getHeight());
    }

    @Override
    public String name() {
        return "Average Difference";
    }
}
