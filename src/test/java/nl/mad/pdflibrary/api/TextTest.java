package nl.mad.pdflibrary.api;

import static junit.framework.TestCase.assertEquals;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.FontFamily;
import nl.mad.pdflibrary.model.FontStyle;
import nl.mad.pdflibrary.model.Text;

import org.junit.Before;
import org.junit.Test;

public class TextTest {
    private BaseText text;
    private final int textSize = 10;
    private final double testScaleShear = 2.0;

    @Before
    public void setUp() throws Exception {
        text = new BaseText();
    }

    @Test
    public void testType() {
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

    @Test
    public void testCustomPositioning() {
        assertEquals("Custom positioning was not set correctly. ", false, text.getCustomPositioning());
        text = new BaseText("Test", 1, 1, 1);
        assertEquals("Custom positioning was not set correctly. ", true, text.getCustomPositioning());
    }

    @Test
    public void testPositioning() {
        text.setPositionX(1);
        assertEquals("Position X was not set correctly. ", 1, text.getPositionX());
        text.setPositionY(1);
        assertEquals("Position Y was not set correctly. ", 1, text.getPositionY());
    }

    @Test
    public void testScaling() {
        text.setScaleX(testScaleShear);
        assertEquals("Scale X was not set correctly. ", testScaleShear, text.getScaleX());
        text.setScaleY(testScaleShear);
        assertEquals("Scale Y was not set correctly. ", testScaleShear, text.getScaleY());
    }

    @Test
    public void testShearing() {
        text.setShearX(testScaleShear);
        assertEquals("Shear X was not set correctly. ", testScaleShear, text.getShearX());
        text.setShearY(testScaleShear);
        assertEquals("Shear Y was not set correctly. ", testScaleShear, text.getShearY());
    }

    @Test
    public void testTextSize() {
        text.setTextSize(textSize);
        assertEquals("Text size was not set correctly. ", textSize, text.getTextSize());
        text.setTextSize(-1);
        assertEquals("Text size was adjusted to negative number. ", 0, text.getTextSize());
    }

    @Test
    public void testMatrixEquals() {
        text = new BaseText("Test", textSize, Document.DEFAULT_FONT, 0, 0, 1, testScaleShear, testScaleShear, 1);
        Text t = new BaseText("", textSize, Document.DEFAULT_FONT, 0, 0, 1, testScaleShear, testScaleShear, 1);
        assertEquals("Text matrix equals method returned wrong result. ", true, text.textMatrixEquals(t));
    }

    @Test
    public void testCopyFromOtherText() {
        text.setText("Test");
        text.setScaleX(testScaleShear);
        Text t = new BaseText(text);
        boolean actualResult = text.getText().equals(t.getText()) && text.getScaleX() == t.getScaleX();
        assertEquals("The text object was not copied correctly. ", true, actualResult);
    }
}
