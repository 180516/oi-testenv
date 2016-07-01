package measures;

import java.awt.image.Raster;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public class PeakSNR extends QualityMeasure {
    @Override
    protected double calculate(Raster result, Raster reference) {
        double sum = 0;
        for (int i = result.getMinY(); i < result.getMinY() + result.getHeight(); i++) {
            for (int j = result.getMinX(); j < result.getMinX() + result.getWidth(); j++) {
                sum += Math.pow(
                        reference.getPixel(j, i, new double[reference.getNumBands()])[0] -
                                result.getPixel(j, i, new double[result.getNumBands()])[0], 2);
            }
        }
        return 10 * Math.log(result.getHeight() * result.getWidth() * 255 / sum);
    }

    @Override
    public String name() {
        return "Peak Signal to Noise Ratio";
    }
}
