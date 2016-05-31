import measures.MeanSquareError;

import javax.imageio.ImageIO;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

/**
 * Created by Filip-PC on 31.05.2016.
 */
public class Evaluation {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) throw new IllegalArgumentException("Invalid number of arguments");
        Raster result = ImageIO.read(new File(args[0])).getData();
        Raster reference = ImageIO.read(new File(args[1])).getData();

        MeanSquareError mse = new MeanSquareError();
        System.out.println(mse.name() + " " + mse.calculate(result, reference));
    }
}
