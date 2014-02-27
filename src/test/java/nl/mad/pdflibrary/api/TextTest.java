package nl.mad.pdflibrary.api;

import static junit.framework.TestCase.assertEquals;

import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.FontFamily;
import nl.mad.pdflibrary.model.FontStyle;
import org.junit.Before;
import org.junit.Test;

public class TextTest {
    private BaseText text;

    @Before
    public void setUp() throws Exception {
        text = new BaseText();
    }

    @Test
    public void testTextCreation() {
        assertEquals("Document part type was not set correctly to TEXT", DocumentPartType.TEXT, text.getType());
    }

    @Test
    public void testSetText() {
        String test = "Test";
        text.setText(test);
        assertEquals("Text was not set correctly.", test, text.getText());
    }

    @Test
    public void testSetFont() {
        BaseFont f = new BaseFont(FontFamily.COURIER, FontStyle.NORMAL);
        text.setFont(f);
        assertEquals("Font was not set correctly.", f, text.getFont());
    }
}
