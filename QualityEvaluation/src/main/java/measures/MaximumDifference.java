package measures;

import java.awt.image.Raster;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public class MaximumDifference extends QualityMeasure {

    @Override
    public double calculate(Raster result, Raster reference) {
        double current = 0;
        for (int i = result.getMinY(); i < result.getMinY() + result.getHeight(); i++) {
            for (int j = result.getMinX(); j < result.getMinX() + result.getWidth(); j++) {
                double ref = reference.getPixel(j, i, new double[reference.getNumBands()])[0];
                double res = result.getPixel(j, i, new double[result.getNumBands()])[0];
                double diff = Math.abs(ref - res);
                if (diff > current) current = diff;
            }
        }
        return current;
    }

    @Override
    public String name() {
        return "Maximum Difference";
    }
}
