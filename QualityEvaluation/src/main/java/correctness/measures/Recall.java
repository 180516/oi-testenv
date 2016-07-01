package correctness.measures;

import correctness.CorrectnessResults;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public class Recall implements CorrectnessMeasure {

    @Override
    public double get(CorrectnessResults results) {
        return (double) results.getCorrect().getPositives() /
                (results.getCorrect().getPositives() + results.getIncorrect().getNegatives());
    }

    @Override
    public String name() {
        return "Recall";
    }
}
