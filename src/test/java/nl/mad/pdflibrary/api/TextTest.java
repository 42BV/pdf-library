package nl.mad.pdflibrary.api;

import static org.junit.Assert.assertEquals;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.FontFamily;
import nl.mad.pdflibrary.model.FontStyle;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.Text;

import org.junit.Before;
import org.junit.Test;

public class TextTest {
    private BaseText text;
    private final int textSize = 10;
    private final double testScaleShear = 2.0;
    private final double delta = 0.0001;

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
    public void testScaling() {
        text.setScaleX(testScaleShear);
        assertEquals("Scale X was not set correctly. ", testScaleShear, text.getScaleX(), delta);
        text.setScaleY(testScaleShear);
        assertEquals("Scale Y was not set correctly. ", testScaleShear, text.getScaleY(), delta);
    }

    @Test
    public void testShearing() {
        text.setShearX(testScaleShear);
        assertEquals("Shear X was not set correctly. ", testScaleShear, text.getShearX(), delta);
        text.setShearY(testScaleShear);
        assertEquals("Shear Y was not set correctly. ", testScaleShear, text.getShearY(), delta);
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
        text = new BaseText("Test", textSize, Document.DEFAULT_FONT, new Position(), 1, testScaleShear, testScaleShear, 1);
        Text t = new BaseText("", textSize, Document.DEFAULT_FONT, new Position(), 1, testScaleShear, testScaleShear, 1);
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
