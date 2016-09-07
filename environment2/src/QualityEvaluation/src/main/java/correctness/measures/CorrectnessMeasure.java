package correctness.measures;

import correctness.CorrectnessResults;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public interface CorrectnessMeasure {
    double get(CorrectnessResults results);
    String name();
}
