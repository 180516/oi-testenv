package processing;

public final class Pixel {

    private final int redValue;

    private final int greenValue;

    private final int blueValue;

    private final int xPosition;

    private final int yPosition;

    public Pixel(int redValue, int greenValue, int blueValue, int xPosition, int yPosition) {
        this.redValue = setProperRgbValue(redValue);
        this.greenValue = setProperRgbValue(greenValue);
        this.blueValue = setProperRgbValue(blueValue);
        this.xPosition = xPosition;
        this.yPosition = yPosition;

    }

    private int setProperRgbValue(int value) {
        if(value < 0) {
            return 0;
        }
        if(value > 255) {
            return 255;
        }
        return value;
    }

    public int luminosity() {
        return (int) Math.round(0.2126 * redValue + 0.7152 * greenValue + 0.0722 * blueValue);
    }

    public int getRedValue() {
        return redValue;
    }

    public int getGreenValue() {
        return greenValue;
    }

    public int getBlueValue() {
        return blueValue;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }
}