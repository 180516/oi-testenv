package app;

/**
 * Created by Filip-PC on 22.06.2016.
 */
public class SimpleSolutionTest {


    @org.junit.Test
    public void runLandSea() throws Exception {
        SimpleSolution.main(new String[]{
                "C:\\Users\\Filip-PC\\OneDrive\\sem8\\oi\\esa\\test-environment\\oi-testenv\\environment2\\run\\data\\ls\\test",
                "ls"
        });
    }

    @org.junit.Test
    public void runSoilMoisture() throws Exception {
        SimpleSolution.main(new String[]{
                "C:\\Users\\Filip-PC\\OneDrive\\sem8\\oi\\esa\\test-environment\\oi-testenv\\environment2\\run\\data\\sm\\test",
                "sm"
        });
    }
}