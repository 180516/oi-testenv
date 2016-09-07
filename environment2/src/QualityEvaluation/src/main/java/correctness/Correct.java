package correctness;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public class Correct {

    private final int positives;
    private final int negatives;

    public Correct(int positives, int negatives) {
        this.positives = positives;
        this.negatives = negatives;
    }

    public int getPositives() {
        return positives;
    }

    public int getNegatives() {
        return negatives;
    }
}
