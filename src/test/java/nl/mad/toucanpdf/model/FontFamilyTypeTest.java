package nl.mad.toucanpdf.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FontFamilyTypeTest {

    @Test
    public void testToString() {
        assertEquals("Times-Roman", FontFamilyType.TIMES_ROMAN.toString());
    }

}
