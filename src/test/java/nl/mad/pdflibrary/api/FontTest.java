package nl.mad.pdflibrary.api;

import static junit.framework.TestCase.assertEquals;

import java.io.IOException;

import nl.mad.pdflibrary.model.BaseFontFamily;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontFamily;
import nl.mad.pdflibrary.model.FontStyle;

import org.junit.Before;
import org.junit.Test;

public class FontTest {
    private Font font;

    @Before
    public void setUp() throws Exception {
        font = new BaseFont(FontFamily.HELVETICA, FontStyle.NORMAL);
    }

    @Test
    public void testType() {
        assertEquals("Document part type was not set correctly to FONT", DocumentPartType.FONT, font.getType());
    }

    @Test
    public void testFamilyAdjustment() {
        FontFamily expectedFamily = FontFamily.COURIER;
        String expectedBaseFontFamilyName = "Courier";
        font.setFamily(expectedFamily);
        assertEquals("The font family was set incorrectly.", expectedFamily, font.getFamily());
        assertEquals("The base font family was not adjusted.", expectedBaseFontFamilyName, font.getBaseFontFamily().getName());
    }

    @Test
    public void testStyleAdjustment() {
        FontStyle expectedStyle = FontStyle.BOLD;
        font.setStyle(expectedStyle);
        assertEquals("The font style was set incorrectly.", expectedStyle, font.getStyle());
    }

    @Test
    public void testBaseFontFamilyAdjustment() throws IOException {
        BaseFontFamily bff = BaseFontFamily.getDefaultBaseFontFamily(FontFamily.TIMES_ROMAN);
        font = new BaseFont(FontFamily.HELVETICA, FontStyle.BOLD, bff);
        assertEquals("The BaseFontFamily was not set correctly. ", bff, font.getBaseFontFamily());
    }
}
