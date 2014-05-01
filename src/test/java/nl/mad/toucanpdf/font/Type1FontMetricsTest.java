package nl.mad.toucanpdf.font;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import nl.mad.toucanpdf.font.Type1FontMetrics;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class Type1FontMetricsTest {
    private Type1FontMetrics metrics;
    private final int spaceWidth = 278;
    private final int aWidth = 556;
    private final int capitalAWidth = 667;
    private final int capitalCWidth = 722;
    private final int capitalACKerningOffset = -30;
    private final int textSize = 11;
    private final int characterAmount = 315;
    private final int firstChar = 32;
    private final int lastChar = 36;
    private final int ascend = 718;

    @Before
    public void setUp() throws Exception {
        metrics = new Type1FontMetrics("Helvetica");
    }

    @Test(expected = FileNotFoundException.class)
    public void testFilename() throws FileNotFoundException {
        assertEquals("Filename was not set correctly. ", "Helvetica", metrics.getFilename());
        metrics = new Type1FontMetrics("nonexisting.afm");
    }

    @Test
    public void testFontFile() throws IOException {
        boolean correctResult = false;
        byte[] fontFile = metrics.getFontFile();
        assertEquals("Parsing .pfb did not go correctly, this could be caused by not being able to find the document. ", true, fontFile.length > 0);
        //on second retrieval it will not parse and simply return the array
        assertEquals("Second font file retrieval went wrong.", true, metrics.getFontFile().length > 0);

    }

    //
    //    @Test
    //    public void testNonExistingFontFile() {
    //
    //    }

    @Test
    public void testGetWidth() {
        int width = metrics.getWidth((int) 'a');
        assertEquals("Width of the metric was incorrect. ", aWidth, width);
        width = metrics.getWidth("ikbestaniet");
        assertEquals(0, width);
    }

    @Test
    public void testGetWidths() {
        assertEquals("Total amount of character metrics is incorrect. ", characterAmount, metrics.getWidths().size());
        assertEquals("Subset amount of character metrics is incorrect. ", lastChar - firstChar + 1, metrics.getWidths(firstChar, lastChar).size());
    }

    @Test
    public void testGetWidthOfString() {
        int expectedWidth = (spaceWidth * 2 + capitalAWidth) * textSize;
        assertEquals("Width of string is incorrect. ", expectedWidth, metrics.getWidthOfString(" A ", textSize, false));
        assertEquals("Width of string in points is incorrect. ", expectedWidth * metrics.getConversionToPointsValue(),
                metrics.getWidthPointOfString(" A ", textSize, false), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testGetWidthOfStringWithKerning() {
        int expectedWidth = (capitalAWidth + capitalCWidth + capitalACKerningOffset) * textSize;
        assertEquals("Width of string with kerning is incorrect. ", expectedWidth, metrics.getWidthOfString("AC", textSize, true));
        assertEquals("Width of string with kerning in points is incorrect. ", expectedWidth * metrics.getConversionToPointsValue(),
                metrics.getWidthPointOfString("AC", textSize, true), FloatEqualityTester.EPSILON);
    }

    //    @Test
    //    public void testGetLeading() {
    //        int expectedLeading = (int) ((ascend * textSize * metrics.getConversionToPointsValue()) + FontMetrics.DEFAULT_LEADING_ADDITION);
    //        assertEquals("Calculated leading is incorrect. ", expectedLeading, metrics.getLeadingForSize(textSize));
    //        assertEquals("Parsed leading is incorrect. ", 0, metrics.getLeading());
    //    }
}
