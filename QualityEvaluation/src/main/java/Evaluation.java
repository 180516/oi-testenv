import measures.MeanSquareError;

import javax.imageio.ImageIO;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public class Evaluation {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) throw new IllegalArgumentException("Invalid number of arguments");
        File resultDirectory = new File(args[1]);
        File referenceDirectory = new File(args[2]);
        MeanSquareError mse = new MeanSquareError();
        Arrays.stream(resultDirectory.listFiles((file, s) -> s.endsWith(".png"))).forEach(result -> {
            System.out.println(result.getName());
            File reference = new File(referenceDirectory, result.getName());
            try {
                Raster resultRaster = ImageIO.read(result).getData();
                Raster referenceRaster = ImageIO.read(reference).getData();
                System.out.println(mse.name() + " " + mse.calculate(resultRaster, referenceRaster));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
