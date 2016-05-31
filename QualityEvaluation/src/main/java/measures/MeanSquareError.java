package measures;

import java.awt.image.Raster;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public class MeanSquareError {

    public double calculate(Raster result, Raster reference) {
        if (!result.getBounds().equals(reference.getBounds()))
            throw new IllegalStateException("Images have different size");
//        if (result.getNumBands() != 1 || reference.getNumBands() != 1)
//            throw new IllegalStateException("Images bands other than 1");

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

    public String name() {
        return "Mean Square Error";
    }
}
