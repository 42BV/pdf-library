package nl.mad.toucanpdf;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import nl.mad.toucanpdf.DocumentBuilder;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Font;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.Text;

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
        assertEquals(false, builder.getCreationDate() == null);
    }

    @Test
    public void testFont() {
        Font f = builder.addFont();
        assertEquals(DocumentPartType.FONT, builder.getPage(1).getContent().get(0).getType());
    }

    @Test
    public void testText() {
        Text t = builder.addText();
        assertEquals(DocumentPartType.TEXT, builder.getPage(1).getContent().get(0).getType());
        assertEquals("", t.getText());

        t = builder.createText();
        assertEquals(1, builder.getPage(1).getContent().size());
        assertEquals("", t.getText());
    }

    @Test
    public void testParagraph() {
        Paragraph p = builder.addParagraph();
        assertEquals(DocumentPartType.PARAGRAPH, builder.getPage(1).getContent().get(0).getType());
    }

    @Test
    public void testPage() {
        int expectedAmount = builder.getPageAmount() + 1;
        builder.addPage().size(200, 200);
        assertEquals("The new page has not been added correctly.", expectedAmount, builder.getPageAmount());
        Page testPage = builder.addPage(1);
        assertEquals("The new page has not been added correctly on the given position. ", testPage, builder.getPage(1));
        builder.addPage(0);
        assertEquals("The new page has been added on an incorrect position. ", 2, builder.getPageAmount());
        Page page = builder.getPage(-1);
        assertEquals(null, page);
        page = builder.getPage(999);
        assertEquals(null, page);
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
    public void testAuthor() {
        builder.writtenBy("TestAuthor");
        assertEquals("The author was not set correctly. ", "TestAuthor", builder.getAuthor());
    }

    @Test
    public void testTitle() {
        builder.title("TestTitle");
        assertEquals("The title was not set correctly. ", "TestTitle", builder.getTitle());
    }

    @Test
    public void testSubject() {
        builder.about("TestSubject");
        assertEquals("The subject was not set correctly. ", "TestSubject", builder.getSubject());
    }

    @Test
    public void testCreationDate() {
        Calendar cal = Calendar.getInstance();
        builder.on(cal);
        assertEquals("The creation date was not set correctly. ", cal, builder.getCreationDate());
    }

    @Test
    public void testFilename() throws IOException {
        File tempFile = File.createTempFile("document", ".pdf");

        //test filename acquisition with empty filename and title
        builder.finish();
        assertEquals("The filename was not set correctly. ", "document.pdf", builder.getFilename());
        tempFile.delete();

        //test filename acquisition with empty filename and filled title
        builder.filename("");
        builder.title("Test");
        tempFile = File.createTempFile("Test", ".pdf");
        builder.finish();
        assertEquals("The filename was not set correctly. ", "Test.pdf", builder.getFilename());

        //test filename acquisition with filename without extension
        builder.filename("Test");
        builder.finish();
        assertEquals("The filename was not set correctly. ", "Test.pdf", builder.getFilename());
        tempFile.delete();

    }

    @Test
    public void testFinish() throws IOException {
        builder.addPage();
        builder.addPage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        builder.finish(baos);
        assertEquals(true, baos.size() > 0);
    }
}
