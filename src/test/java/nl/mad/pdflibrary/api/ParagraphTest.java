package nl.mad.pdflibrary.api;

import static junit.framework.TestCase.assertEquals;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Text;

import org.junit.Before;
import org.junit.Test;

public class ParagraphTest {
    private Paragraph paragraph;
    private final int testPosition = 200;

    @Before
    public void setUp() throws Exception {
        paragraph = new BaseParagraph();
    }

    @Test
    public void testType() {
        assertEquals("Document part type was not set correctly to PARAGRAPH", DocumentPartType.PARAGRAPH, paragraph.getType());
    }

    @Test
    public void testCustomPositioning() {
        assertEquals("The custom positioning was not set correctly.", false, paragraph.getCustomPositioning());
        paragraph = new BaseParagraph(testPosition, testPosition);
        assertEquals("The custom positioning was not set correctly.", true, paragraph.getCustomPositioning());
    }

    @Test
    public void testPositionining() {
        paragraph.setPositionX(testPosition);
        assertEquals("The X position was not set correctly.", testPosition, paragraph.getPositionX());
        paragraph.setPositionY(testPosition);
        assertEquals("The Y position was not set correctly.", testPosition, paragraph.getPositionY());
    }

    @Test
    public void testPartAdding() {
        Text t = new BaseText();
        paragraph.addText(t);
        assertEquals("The size of the text collection is incorrect. ", 1, paragraph.getTextCollection().size());
        assertEquals("The added text object is incorrect. ", t, paragraph.getTextCollection().get(0));
    }
}
