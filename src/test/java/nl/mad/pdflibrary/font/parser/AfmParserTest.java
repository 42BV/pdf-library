package nl.mad.pdflibrary.font.parser;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;

import nl.mad.pdflibrary.font.Type1CharacterMetric;

import org.junit.Test;

public class AfmParserTest {
    private AfmParser afm;
    private final String expectedFontName = "Testetica";
    private final String expectedFullName = "Testeticar";
    private final String expectedFamilyName = "Testeticara";
    private final String expectedWeight = "Medium";
    private final int expectedItalicAngle = 0;
    private final boolean expectedIsFixedPitch = false;
    private final String expectedCharacterSet = "ExtendedRoman";
    private final double[] expectedFontBBox = { -166, -225, 1000, 931 };
    private final int expectedUnderlinePosition = -100;
    private final int expectedUnderlineThickness = 50;
    private final String expectedEncodingScheme = "AdobeStandardEncoding";
    private final int expectedCapHeight = 718;
    private final int expectedXHeight = 523;
    private final int expectedAscender = 718;
    private final int expectedDescender = -207;
    private final int expectedStdHW = 76;
    private final int expectedStdVW = 88;
    private final int expectedFlag = 32;
    private final int lowestWidth = 200;
    private final int expectedMaxWidth = 400;
    private final int expectedAverageWidth = (lowestWidth + expectedMaxWidth) / 2;
    private final Type1CharacterMetric firstChar = new Type1CharacterMetric(10, lowestWidth, "test", new int[] { 1, 0, -1, 0 });
    private final Type1CharacterMetric lastChar = new Type1CharacterMetric(11, expectedMaxWidth, "testTwo", new int[] { 0, 0, 0, 0 });
    private final int expectedKerning = -10;
    private final double epsilon = 0.00001;

    /**
     * Creates new instance of AfmParserTest. Parses a fake Afm file.
     */
    public AfmParserTest() {
        StringBuilder sb = new StringBuilder();
        sb.append("FontName " + expectedFontName + "\n");
        sb.append("FullName " + expectedFullName + "\n");
        sb.append("FamilyName " + expectedFamilyName + "\n");
        sb.append("Weight " + expectedWeight + "\n");
        sb.append("ItalicAngle " + expectedItalicAngle + "\n");
        sb.append("IsFixedPitch " + expectedIsFixedPitch + "\n");
        sb.append("CharacterSet " + expectedCharacterSet + "\n");
        sb.append("FontBBox " + arrayToAfmFormat(expectedFontBBox) + "\n");
        sb.append("UnderlinePosition " + expectedUnderlinePosition + "\n");
        sb.append("UnderlineThickness " + expectedUnderlineThickness + "\n");
        sb.append("EncodingScheme " + expectedEncodingScheme + "\n");
        sb.append("CapHeight " + expectedCapHeight + "\n");
        sb.append("XHeight " + expectedXHeight + "\n");
        sb.append("Ascender " + expectedAscender + "\n");
        sb.append("Descender " + expectedDescender + "\n");
        sb.append("StdHW " + expectedStdHW + "\n");
        sb.append("StdVW " + expectedStdVW + "\n");
        sb.append("StartCharMetrics 2\n");
        sb.append("C " + firstChar.getC() + " ; WX " + firstChar.getWx() + " ; N " + firstChar.getName() + " ; B "
                + arrayToAfmFormat(firstChar.getBoundingBox()) + "\n");
        sb.append("C " + lastChar.getC() + " ; WX " + lastChar.getWx() + " ; N " + lastChar.getName() + " ; B " + arrayToAfmFormat(lastChar.getBoundingBox())
                + "\n");
        sb.append("EndCharMetrics\n");
        sb.append("StartKernData\n");
        sb.append("StartKernPairs 1\n");
        sb.append("KPX " + firstChar.getName() + " " + lastChar.getName() + " " + expectedKerning + "\n");
        sb.append("EndKernPairs \nEndKernData \nEndFontMetrics \n");
        System.out.println(sb.toString());
        BufferedReader br = new BufferedReader(new StringReader(sb.toString()));
        afm = new AfmParser();
        afm.parse(br);
    }

    /**
     * Creates a string for the given array in the Afm format.
     * @param array Array to process.
     * @return string containing array values in afm format.
     */
    private <E> String arrayToAfmFormat(E[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            sb.append(array[i] + " ");
        }
        return sb.toString();
    }

    /**
     * Creates a string for the given int array in the Afm format.
     * @param array Array to process.
     * @return string containing array values in afm format.
     */
    private String arrayToAfmFormat(int[] array) {
        Integer[] newArray = new Integer[array.length];
        for (int i = 0; i < array.length; ++i) {
            newArray[i] = Integer.valueOf(array[i]);
        }
        return arrayToAfmFormat(newArray);
    }

    /**
     * Creates a string for the given double array in the Afm format.
     * @param array Array to process.
     * @return string containing array values in afm format.
     */
    private String arrayToAfmFormat(double[] array) {
        Double[] newArray = new Double[array.length];
        for (int i = 0; i < array.length; ++i) {
            newArray[i] = Double.valueOf(array[i]);
        }
        return arrayToAfmFormat(newArray);
    }

    @Test
    public void testFontNames() {
        assertEquals("Font name is not as expected. ", expectedFontName, afm.getFontName());
        assertEquals("Full name is not as expected. ", expectedFullName, afm.getFullName());
        assertEquals("Family name is not as expected. ", expectedFamilyName, afm.getFamilyName());
    }

    @Test
    public void testWeight() {
        assertEquals("Weight is not as expected. ", expectedWeight, afm.getWeight());
    }

    @Test
    public void testItalicAngle() {
        assertEquals("Italic angle is not as expected. ", expectedItalicAngle, afm.getItalicAngle(), epsilon);
    }

    @Test
    public void testFixedPitch() {
        assertEquals("Fixed pitch is not as expected. ", expectedIsFixedPitch, afm.isFixedPitch());
    }

    @Test
    public void testCharacterSet() {
        assertEquals("Character set is not as expected. ", expectedCharacterSet, afm.getCharacterSet());
    }

    @Test
    public void testFontBoundingBox() {
        assertEquals("Font bounding box is not as expected. ", true, Arrays.equals(afm.getFontBBox(), expectedFontBBox));
    }

    @Test
    public void testUnderline() {
        assertEquals("Underline position is not as expected. ", expectedUnderlinePosition, afm.getUnderlinePosition());
        assertEquals("Underline thickness is not as expected. ", expectedUnderlineThickness, afm.getUnderlineThickness());
    }

    @Test
    public void testEncodingScheme() {
        assertEquals("Encoding scheme is not as expected. ", expectedEncodingScheme, afm.getEncodingScheme());
    }

    @Test
    public void testCapHeight() {
        assertEquals("Cap height is not as expected. ", expectedCapHeight, afm.getCapHeight());
    }

    @Test
    public void testXHeight() {
        assertEquals("XHeight is not as expected. ", expectedXHeight, afm.getxHeight());
    }

    @Test
    public void testAscender() {
        assertEquals("Ascender is not as expected. ", expectedAscender, afm.getAscender());
    }

    @Test
    public void testDescender() {
        assertEquals("Descender is not as expected. ", expectedDescender, afm.getDescender());
    }

    @Test
    public void testChars() {
        Type1CharacterMetric firstMetric = afm.getCharacterMetric(firstChar.getName());
        assertEquals("First char code is not as expected. ", firstChar.getC(), firstMetric.getC());
        assertEquals("First char width is not as expected. ", firstChar.getWx(), firstMetric.getWx());
        assertEquals("First char name is not as expected. ", firstChar.getName(), firstMetric.getName());
        assertEquals("First char bounding box is not as expected. ", true, Arrays.equals(firstChar.getBoundingBox(), firstMetric.getBoundingBox()));

        Type1CharacterMetric lastMetric = afm.getCharacterMetric(lastChar.getName());
        assertEquals("Last char code is not as expected. ", lastChar.getC(), lastMetric.getC());
        assertEquals("Last char width is not as expected. ", lastChar.getWx(), lastMetric.getWx());
        assertEquals("Last char name is not as expected. ", lastChar.getName(), lastMetric.getName());
        assertEquals("Last char bounding box is not as expected. ", true, Arrays.equals(lastChar.getBoundingBox(), lastMetric.getBoundingBox()));
    }

    @Test
    public void testAverageWidth() {
        assertEquals("Average width is not as expected. ", expectedAverageWidth, afm.getAverageWidth());
    }

    @Test
    public void testFlags() {
        assertEquals("Flags is not as expected. ", expectedFlag, afm.getFlags());
    }

    @Test
    public void testKerning() {
        //kerning will be inverted after the parsing
        assertEquals("Kerning is not as expected. ", Integer.valueOf(-expectedKerning), afm.getKerning(firstChar.getName(), lastChar.getName()));
    }
}
