package nl.mad.pdflibrary.utility;

/**
 * Utility class for testing the equality of two floating point values.
 * @author Dylan de Wolff
 */
public final class FloatEqualityTester {
    /**
     * The default epsilon value. This value determines how large the difference between two floating point values is allowed to be.
     */
    public static final double EPSILON = 1E-5;

    private FloatEqualityTester() {
    }

    /**
     * Checks if the difference between the given double values is below the default epsilon value (0.00001).
     * @param value First value
     * @param value2 Second value
     * @return True if the difference is smaller than the default epsilon, false otherwise.
     */
    public static boolean equals(double value, double value2) {
        return equals(value, value2, EPSILON);
    }

    /**
     * Checks if the difference between the given double values is below the given epsilon value.
     * @param value First value
     * @param value2 Second value
     * @param epsilon Maximal difference allowed.
     * @return True if the difference is smaller than the given epsilon value, false otherwise.
     */
    public static boolean equals(double value, double value2, double epsilon) {
        return (Math.abs(value - value2) < epsilon);
    }

    /**
     * Checks if the difference between the given float values is below the default epsilon value (0.00001).
     * @param value First value
     * @param value2 Second value
     * @return True if the difference is smaller than the default epsilon, false otherwise.
     */
    public static boolean equals(float value, float value2) {
        return equals(value, value2, EPSILON);
    }

    /**
     * Checks if the difference between the given float values is below the given epsilon value.
     * @param value First value
     * @param value2 Second value
     * @param epsilon Maximal difference allowed.
     * @return True if the difference is smaller than the given epsilon value, false otherwise.
     */
    public static boolean equals(float value, float value2, double epsilon) {
        return (Math.abs(value - value2) < epsilon);
    }
}
