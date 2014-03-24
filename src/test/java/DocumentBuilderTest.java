import static org.junit.Assert.assertEquals;

import java.io.IOException;

import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Text;

import org.junit.Before;
import org.junit.Test;

public class DocumentBuilderTest {
    private DocumentBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new DocumentBuilder();
    }

    @Test
    public void testCreation() {
        assertEquals(false, builder.getDocument() == null);
    }

    @Test
    public void testFont() {
        Font f = builder.addFont();
        assertEquals(DocumentPartType.FONT, builder.getDocument().getPage(1).getContent().get(0).getType());
    }

    @Test
    public void testText() {
        Text t = builder.addText();
        assertEquals(DocumentPartType.TEXT, builder.getDocument().getPage(1).getContent().get(0).getType());
        assertEquals("", t.getText());

        t = builder.createText();
        assertEquals(1, builder.getDocument().getPage(1).getContent().size());
        assertEquals("", t.getText());
    }

    @Test
    public void testParagraph() {
        Paragraph p = builder.addParagraph();
        assertEquals(DocumentPartType.PARAGRAPH, builder.getDocument().getPage(1).getContent().get(0).getType());
    }

    @Test
    public void testPage() {
        Page p = builder.addPage();
        assertEquals(p, builder.getDocument().getPage(1));
    }

    @Test
    public void testSwitchingCurrentPage() {
        Page p = builder.addPage();
        Page p2 = builder.addPage();
        //test if currentpage has been updated to the second page
        builder.addText("Test");
        assertEquals(0, p.getContent().size());
        assertEquals(1, p2.getContent().size());
        //test adding to first page
        builder.setCurrentPage(builder.getPage(1));
        builder.addText("Test");
        assertEquals(1, p.getContent().size());
        assertEquals(1, p2.getContent().size());
        //test setting non-existing page (not bigger than 0)
        builder.setCurrentPage(0);
        builder.addText("Test");
        assertEquals(2, p.getContent().size());
        assertEquals(1, p2.getContent().size());
        //test setting non-existing page (bigger than total pages)
        builder.setCurrentPage(5);
        builder.addText("Test");
        assertEquals(3, p.getContent().size());
        assertEquals(1, p2.getContent().size());
    }

    @Test
    public void testFinish() throws IOException {
        builder.getDocument().title("test");
        builder.finish();
        assertEquals("test.pdf", builder.getDocument().getFilename());
    }
}
