package app;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MedianFilter {

    public static void filter(BufferedImage img) {
        Color[] pixel = new Color[9];
        int[] R = new int[9];
        int[] B = new int[9];
        int[] G = new int[9];
        for(int i=1;i<img.getWidth()-1;i++) {
            for (int j = 1; j < img.getHeight() - 1; j++) {
                pixel[0] = new Color(img.getRGB(i - 1, j - 1));
                pixel[1] = new Color(img.getRGB(i - 1, j));
                pixel[2] = new Color(img.getRGB(i - 1, j + 1));
                pixel[3] = new Color(img.getRGB(i, j + 1));
                pixel[4] = new Color(img.getRGB(i + 1, j + 1));
                pixel[5] = new Color(img.getRGB(i + 1, j));
                pixel[6] = new Color(img.getRGB(i + 1, j - 1));
                pixel[7] = new Color(img.getRGB(i, j - 1));
                pixel[8] = new Color(img.getRGB(i, j));
                for (int k = 0; k < 9; k++) {
                    R[k] = pixel[k].getRed();
                    B[k] = pixel[k].getBlue();
                    G[k] = pixel[k].getGreen();
                }
                Arrays.sort(R);
                Arrays.sort(G);
                Arrays.sort(B);
                img.setRGB(i, j, new Color(R[4], B[4], G[4]).getRGB());
            }
        }
    }
}
