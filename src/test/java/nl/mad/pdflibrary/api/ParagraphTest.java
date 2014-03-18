package nl.mad.pdflibrary.api;

import static org.junit.Assert.assertEquals;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Text;

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
}
