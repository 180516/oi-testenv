package measures;

import java.awt.image.Raster;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public class MeanSquareError extends QualityMeasure {

    @Override
    public double calculate(Raster result, Raster reference) {
        double sum = 0;
        for (int i = result.getMinY(); i < result.getMinY() + result.getHeight(); i++) {
            for (int j = result.getMinX(); j < result.getMinX() + result.getWidth(); j++) {
                sum += Math.pow(
                        reference.getPixel(j, i, new double[reference.getNumBands()])[0] -
                                result.getPixel(j, i, new double[result.getNumBands()])[0], 2);
            }
        }
        return sum / (result.getHeight() * result.getWidth());
    }

    @Override
    public String name() {
        return "Mean Square Error";
    }
}
