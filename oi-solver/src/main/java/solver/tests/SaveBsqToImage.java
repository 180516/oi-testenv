package solver.tests;

import solver.image.BSQImage;
import solver.image.ImageSaver;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SaveBsqToImage {
    public static void main(String[] args) throws IOException {
        List<double[]> inputValues = new ArrayList<>();
        File file = Paths.get("/home/robert/workspace/oi-testenv/environment2/solutions/simple-solution/input.bsq").toFile();
        inputValues.addAll(new BSQImage(file, 2, new Dimension(1703, 1235)).toPixelList());
        double[] channel = new double[1703 * 1235];
        for (int i = 0; i < inputValues.size(); i++) {
            channel[i] = inputValues.get(i)[1];
        }
        new ImageSaver(1703, 1235, "/home/robert/workspace/oi-testenv/output2.png", false).saveFromDoubleArray(channel);
    }
}
