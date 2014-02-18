package nl.pdflibrary.test.document;

import static org.junit.Assert.assertEquals;

import java.awt.Font;

import nl.pdflibrary.document.DocumentPartType;
import nl.pdflibrary.document.Text;

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
        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        text.setFont(f);
        assertEquals("Font was not set correctly.", f, text.getFont());
    }

    @Test
    public void testSetPosition() {
        int posX = 200;
        int posY = 200;
        text.setPositionX(posX);
        assertEquals("Position X was not set correctly.", posX, text.getPositionX());
        text.setPositionY(posY);
        assertEquals("Position Y was not set correctly.", posY, text.getPositionY());
    }
}
