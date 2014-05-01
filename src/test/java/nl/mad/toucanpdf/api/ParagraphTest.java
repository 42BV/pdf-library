package nl.mad.toucanpdf.api;

import static org.junit.Assert.assertEquals;
import nl.mad.toucanpdf.api.BaseParagraph;
import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class ParagraphTest {
    private Paragraph paragraph;

    @Before
    public void setUp() throws Exception {
        paragraph = new BaseParagraph();
    }

    @Test
    public void testType() {
        assertEquals("Document part type was not set correctly to PARAGRAPH", DocumentPartType.PARAGRAPH, paragraph.getType());
    }

    @Test
    public void testPartAdding() {
        Text t = new BaseText();
        paragraph.addText(t);
        assertEquals("The size of the text collection is incorrect. ", 1, paragraph.getTextCollection().size());
        assertEquals("The added text object is incorrect. ", t, paragraph.getTextCollection().get(0));
    }

    @Test
    public void testPositioning() {
        paragraph.on(200, 201);
        assertEquals("The position was not set correctly. ", 200, paragraph.getPosition().getX(), FloatEqualityTester.EPSILON);
        assertEquals("The position was not set correctly. ", 201, paragraph.getPosition().getY(), FloatEqualityTester.EPSILON);
    }
}
