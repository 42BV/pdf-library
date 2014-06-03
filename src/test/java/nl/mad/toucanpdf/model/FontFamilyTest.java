package nl.mad.toucanpdf.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FontFamilyTest {
    FontFamily fam = new FontFamily(FontType.TRUETYPE, "Times");

    @Test
    public void testNames() {
        fam.setBoldItalicName("Times-BoldItalic2");
        fam.setItalicName("Times-Italic2");
        fam.setBoldName("Times-Bold2");
        fam.setName("Times-Roman2");
        assertEquals("Times-Roman2", fam.getName());
        assertEquals("Times-Bold2", fam.getBoldName());
        assertEquals("Times-Italic2", fam.getItalicName());
        assertEquals("Times-BoldItalic2", fam.getBoldItalicName());
    }

    @Test
    public void testSubType() {
        fam.setSubType(FontType.TRUETYPE);
        assertEquals(FontType.TRUETYPE, fam.getSubType());
    }

}
