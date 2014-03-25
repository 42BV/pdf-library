package nl.mad.pdflibrary.api;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontFamily;
import nl.mad.pdflibrary.model.FontFamilyType;
import nl.mad.pdflibrary.model.FontStyle;
import nl.mad.pdflibrary.utility.Constants;

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
    public void testDefaultConstructor() {
        font = new BaseFont();
        assertEquals("The font family was incorrectly copied from the default font. ", Constants.DEFAULT_FONT.getFamily(), font.getFamily());
        assertEquals("The font style was incorrectly copied from the default font. ", Constants.DEFAULT_FONT.getStyle(), font.getStyle());
    }
}
