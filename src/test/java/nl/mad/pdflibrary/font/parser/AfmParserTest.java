package nl.mad.pdflibrary.font.parser;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import nl.mad.pdflibrary.model.FontMetrics;

import org.junit.Before;
import org.junit.Test;

public class AfmParserTest {
    private AfmParser afm;
    private final String filename = "helvetica.afm";
    private final String expectedFontName = "Helvetica";
    private final String expectedFullName = "Helvetica";
    private final String expectedFamilyName = "Helvetica";
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
    private final int expectedAverageWidth = 542;
    private final int expectedMaxWidth = 1015;
    private final int expectedFirstChar = 32;
    private final int expectedLastChar = 251;
    private final double epsilon = 0.00001;

    @Before
    public void setUp() throws Exception {
        InputStream in = getClass().getResourceAsStream(FontMetrics.RESOURCE_LOCATION + filename);
        if (in == null) {
            in = this.getClass().getClassLoader().getResourceAsStream(filename);
        }
        afm = new AfmParser(in);
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
        assertEquals("Font bounding box is not as expected. ", expectedFontBBox, afm.getFontBBox());
    }

    @Test
    public void testUnderlinePosition() {
        assertEquals("Underline position is not as expected. ", expectedUnderlinePosition, afm.getUnderlinePosition());
    }

    @Test
    public void testUnderlineThickness() {
        assertEquals("Underline thickness is not as expected. ", expectedUnderlineThickness, afm.getUnderlineThickness());
    }
}
