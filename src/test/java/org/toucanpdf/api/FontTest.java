package org.toucanpdf.api;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.toucanpdf.model.DocumentPartType;
import org.toucanpdf.model.Font;
import org.toucanpdf.model.FontFamily;
import org.toucanpdf.model.FontFamilyType;
import org.toucanpdf.model.FontStyle;
import org.toucanpdf.utility.Constants;
import org.toucanpdf.utility.FloatEqualityTester;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FontTest {
    private Font font;

    @Before
    public void setUp() throws Exception {
        font = new BaseFont(FontFamilyType.HELVETICA, FontStyle.NORMAL);
    }

    @Test
    public void testType() {
        assertEquals("Document part type was not set correctly to FONT", DocumentPartType.FONT, font.getType());
    }

    @Test
    public void testFamilyAdjustment() {
        FontFamilyType expectedFamily = FontFamilyType.COURIER;
        String expectedBaseFontFamilyName = "Courier";
        font.family(expectedFamily);
        assertEquals("The font family was set incorrectly.", expectedFamily, font.getFamily());
        assertEquals("The base font family was not adjusted.", expectedBaseFontFamilyName, font.getFontFamily().getName());
    }

    @Test
    public void testStyleAdjustment() {
        font.style(FontStyle.BOLD);
        assertEquals("The font style was set incorrectly.", FontStyle.BOLD, font.getStyle());
        font.italic();
        assertEquals("The font style was set incorrectly. ", FontStyle.ITALIC, font.getStyle());
        font.bold();
        assertEquals("The font style was set incorrectly. ", FontStyle.BOLD, font.getStyle());
        font.boldItalic();
        assertEquals("The font style was set incorrectly. ", FontStyle.BOLDITALIC, font.getStyle());
    }

    @Test
    public void testBaseFontFamilyAdjustment() throws IOException {
        FontFamily bff = FontFamily.getDefaultFontFamily(FontFamilyType.TIMES_ROMAN);
        font = new BaseFont(FontFamilyType.HELVETICA, FontStyle.BOLD, bff);
        assertEquals("The BaseFontFamily was not set correctly. ", bff, font.getFontFamily());
    }

    @Test
    public void testConstructor() {
        font = new BaseFont();
        Assert.assertEquals("The font family was incorrectly copied from the default font. ", Constants.DEFAULT_FONT.getFamily(), font.getFamily());
        assertEquals("The font style was incorrectly copied from the default font. ", Constants.DEFAULT_FONT.getStyle(), font.getStyle());
        font = new BaseFont(font);
        Assert.assertEquals("The font family was incorrectly copied from the default font. ", Constants.DEFAULT_FONT.getFamily(), font.getFamily());
        assertEquals("The font style was incorrectly copied from the default font. ", Constants.DEFAULT_FONT.getStyle(), font.getStyle());
    }

    @Test
    public void testLineHeightCalculation() {
        Assert.assertEquals(11.1, font.getLineHeight(12), FloatEqualityTester.EPSILON);
    }
}
