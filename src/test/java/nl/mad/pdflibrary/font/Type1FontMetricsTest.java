package nl.mad.pdflibrary.font;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

//getters are not tested here because they simply pass along the variables to a parser. 
//Therefore testing the parsers also tests the behaviour of this class. The only get methods that are tested 
//have their own behaviour and don't simply pass the request along.
public class Type1FontMetricsTest {
    private Type1FontMetrics metrics;
    private final int spaceWidth = 278;
    private final int AWidth = 667;
    private final int CWidth = 722;
    private final int ACKerningOffset = -30;
    private final int textSize = 11;
    private final int characterAmount = 315;
    private final int firstChar = 32;
    private final int lastChar = 36;
    private final double epsilon = 0.0001;

    @Before
    public void setUp() throws Exception {
        metrics = new Type1FontMetrics("Helvetica");
    }

    @Test
    public void testFilename() {
        assertEquals("Filename was not set correctly. ", "Helvetica", metrics.getFilename());
    }

    @Test
    public void testParsePfb() {
        boolean correctResult = false;
        if (metrics.getFontFile().length > 0) {
            correctResult = true;
        }
        assertEquals("Parsing .pfb did not go correctly, this could be caused by not being able to find the document. ", true, correctResult);
    }

    @Test
    public void testGetWidth() {
        int width = metrics.getWidth("space");
        assertEquals("Width of the metric was incorrect. ", spaceWidth, width);
    }

    @Test
    public void testGetWidths() {
        assertEquals("Total amount of character metrics is incorrect. ", characterAmount, metrics.getWidths().size());
        assertEquals("Subset amount of character metrics is incorrect. ", lastChar - firstChar + 1, metrics.getWidths(firstChar, lastChar).size());
    }

    @Test
    public void testGetWidthOfString() {
        int expectedWidth = (spaceWidth * 2 + AWidth) * textSize;
        //assertEquals("Width of string is incorrect. ", expectedWidth, metrics.getWidthOfString(" A ", textSize, false));
    }

    public void testGetWidthOfStringWithKerning() {
        int expectedWidth = (AWidth + CWidth - ACKerningOffset) * textSize;
        assertEquals("Width of string with kerning is incorrect. ", expectedWidth, metrics.getWidthOfString("AC", textSize, true));
        assertEquals("Width of string with kerning in points is incorrect. ", expectedWidth * metrics.getConversionToPointsValue(),
                metrics.getWidthPointOfString("AC", textSize, true), epsilon);
    }
}
