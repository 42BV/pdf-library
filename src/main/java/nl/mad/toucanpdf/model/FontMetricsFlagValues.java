package nl.mad.toucanpdf.model;

/**
 * Contains the different values that can be set for the font metrics flag and their bit value.
 * @author Dylan de Wolff
 */
public enum FontMetricsFlagValues {
    FIXED_PITCH(1),
    SERIF(2),
    SYMBOLIC(4),
    SCRIPT(8),
    NON_SYMBOLIC(32),
    ITALIC(64),
    ALL_CAP(65536),
    SMALL_CAP(131072),
    FORCE_BOLD(262144);

    private int bitValue;

    private FontMetricsFlagValues(int bitValue) {
        this.bitValue = bitValue;
    }

    public int getBitValue() {
        return bitValue;
    }
}
