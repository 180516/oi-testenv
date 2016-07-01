package measures;

import java.awt.image.Raster;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public abstract class QualityMeasure {

    protected abstract double calculate(Raster result, Raster reference);
    public abstract String name();

    public double get(Raster result, Raster reference) {
        if (!result.getBounds().equals(reference.getBounds()))
            throw new IllegalStateException("Images have different size");
        return calculate(result, reference);
    }
}
