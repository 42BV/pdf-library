package nl.mad.toucanpdf.utility;

public final class PointsConverter {
    private static final double PIXELS_TO_POINTS = 0.75;
    private static final double POINTS_TO_PIXELS = 3;

    private PointsConverter() {
    }

    public static double getPointsForPixels(double pixels) {
        return pixels * PIXELS_TO_POINTS;
    }

    public static double getPixelsForPoints(double points) {
        if (points != 0) {
            return points + (points / POINTS_TO_PIXELS);
        }
        return 0;
    }
}
