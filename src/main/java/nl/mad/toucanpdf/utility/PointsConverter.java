package nl.mad.toucanpdf.utility;

public class PointsConverter {
    private static final double PIXELS_TO_POINTS = 0.75;

    private PointsConverter() {

    }

    public static double getPointsForPixels(double pixels) {
        return pixels * PIXELS_TO_POINTS;
    }

    public static double getPixelsForPoints(double points) {
        if (points != 0) {
            return points + (points / 3);
        }
        return 0;
    }

}
