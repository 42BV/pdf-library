package nl.mad.pdflibrary.api;

import static junit.framework.TestCase.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TextTest {
    private Text text;

    @Before
    public void setUp() throws Exception {
        text = new Text();
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
        Font f = new Font(FontFamily.COURIER, FontStyle.NORMAL);
        text.setFont(f);
        assertEquals("Font was not set correctly.", f, text.getFont());
    }
}
