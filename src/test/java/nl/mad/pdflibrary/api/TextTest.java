package nl.mad.pdflibrary.api;

import static org.junit.Assert.assertEquals;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.FontFamilyType;
import nl.mad.pdflibrary.model.FontStyle;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.Text;
import nl.mad.pdflibrary.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class TextTest {
    private Text text;

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
        text.text(test);
        assertEquals("Text was not set correctly.", test, text.getText());
    }

    @Test
    public void testSetFont() {
        BaseFont f = new BaseFont(FontFamilyType.COURIER, FontStyle.NORMAL);
        text.font(f);
        assertEquals("Font was not set correctly.", f, text.getFont());
    }

    @Test
    public void testScaling() {
        text.scaleX(2);
        assertEquals("Scale X was not set correctly. ", 2, text.getScaleX(), FloatEqualityTester.EPSILON);
        text.scaleY(2);
        assertEquals("Scale Y was not set correctly. ", 2, text.getScaleY(), FloatEqualityTester.EPSILON);
        text.scale(3, 3);
        assertEquals("Scale X was not set correctly. ", 3, text.getScaleX(), FloatEqualityTester.EPSILON);
        assertEquals("Scale Y was not set correctly. ", 3, text.getScaleY(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testShearing() {
        text.shearX(2);
        assertEquals("Shear X was not set correctly. ", 2, text.getShearX(), FloatEqualityTester.EPSILON);
        text.shearY(2);
        assertEquals("Shear Y was not set correctly. ", 2, text.getShearY(), FloatEqualityTester.EPSILON);
        text.shear(3, 3);
        assertEquals("Shear X was not set correctly. ", 3, text.getShearX(), FloatEqualityTester.EPSILON);
        assertEquals("Shear Y was not set correctly. ", 3, text.getShearY(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testTextSize() {
        text.size(10);
        assertEquals("Text size was not set correctly. ", 10, text.getTextSize());
        text.size(-1);
        assertEquals("Text size was adjusted to negative number. ", 0, text.getTextSize());
    }

    @Test
    public void testPositioning() {
        text.on(10, 11);
        assertEquals("Text position was set incorrectly. ", 10, text.getPosition().getX());
        assertEquals("Text position was set incorrectly. ", 11, text.getPosition().getY());
    }

    @Test
    public void testMatrixEquals() {
        text = new BaseText("Test").size(10).on(new Position(10, 10)).shear(2, 2).scale(2, 2);
        Text t = new BaseText("").size(10).on(new Position(10, 10)).shear(2, 2).scale(2, 2);
        assertEquals("Text matrix equals method returned wrong result. ", true, text.textMatrixEquals(t));

        t = new BaseText("");
        assertEquals("Text matrix equals method returned wrong result. ", false, text.textMatrixEquals(t));
    }

    @Test
    public void testCopyFromOtherText() {
        text.text("Test");
        text.scaleX(2);
        Text t = new BaseText(text);
        boolean actualResult = text.getText().equals(t.getText()) && text.getScaleX() == t.getScaleX();
        assertEquals("The text object was not copied correctly. ", true, actualResult);
    }
}
