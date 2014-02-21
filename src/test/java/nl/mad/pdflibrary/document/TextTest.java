package nl.mad.pdflibrary.document;

import java.awt.Font;

import nl.mad.pdflibrary.document.DocumentPartType;
import nl.mad.pdflibrary.document.Text;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

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
        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        text.setFont(f);
        assertEquals("Font was not set correctly.", f, text.getFont());
    }
}
