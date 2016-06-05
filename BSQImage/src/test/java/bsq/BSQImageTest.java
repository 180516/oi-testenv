package bsq;

import bsq.BSQImage;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Filip-PC on 01.06.2016.
 */
public class BSQImageTest {

    private BSQImage image;
    private final String imagePath = getClass().getResource("/test2.bin").getPath();

    @Before
    public void initialize() {
        image = new BSQImage(new File(imagePath), 2, new Dimension(6, 6));
    }

    @Test
    public void shouldReturn_CorrectBandsCount() {
        assertEquals(image.bands(), 2);
    }

    @Test
    public void shouldReturn_CorrectDimension() {
        assertEquals(image.dimension(), new Dimension(6, 6));
    }

    @Test
    public void shouldReturn_PixelOfCorrectSize() throws IOException {
        assertEquals(image.pixel(5, 5).length, 2);
    }

    @Test
    public void shouldReturn_PixelOfCorrectValues() throws IOException {
        assertArrayEquals(image.pixel(0, 0), new int[] {255, 144});
    }

    @Test
    public void shouldReturn_LastPixelOfCorrectValues() throws IOException {
        assertArrayEquals(image.pixel(5, 5), new int[] {52, 94});
    }
}