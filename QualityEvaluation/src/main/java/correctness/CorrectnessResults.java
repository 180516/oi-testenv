package correctness;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public class CorrectnessResults {

    private final Correct correct;
    private final Incorrect incorrect;

    public CorrectnessResults(Correct correct, Incorrect incorrect) {
        this.correct = correct;
        this.incorrect = incorrect;
    }

    public Correct getCorrect() {
        return correct;
    }

    public Incorrect getIncorrect() {
        return incorrect;
    }
}
