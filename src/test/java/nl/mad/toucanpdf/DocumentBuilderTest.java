package nl.mad.toucanpdf;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import nl.mad.toucanpdf.api.BasePage;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Font;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.Table;
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
    public void testAddPart() {
        builder.addPart(null);
        assertEquals(0, builder.getPageAmount());
        builder.addPart(new BasePage(200, 200));
        assertEquals(0, builder.getPageAmount());
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
    public void testImage() {
        //TODO: this
        //    	Image i = builder.addImage(null, ImageType.JPEG);
        //    	assertEquals(DocumentPartType.IMAGE, builder.getPage(1).getContent().get(0).getType());
        //    	i = builder.createImage(null, "test.jpg");
        //    	assertTrue(i != null);
    }

    @Test
    public void testTable() {
        Table t = builder.addTable();
        assertEquals(DocumentPartType.TABLE, builder.getPage(1).getContent().get(0).getType());
    }

    @Test
    public void testMargins() {
        builder.setDefaultMarginLeft(5);
        builder.setDefaultMarginBottom(10);
        builder.setDefaultMarginRight(6);
        builder.setDefaultMarginTop(30);
        assertEquals(5, builder.getDefaultMarginLeft());
        assertEquals(10, builder.getDefaultMarginBottom());
        assertEquals(6, builder.getDefaultMarginRight());
        assertEquals(30, builder.getDefaultMarginTop());
        Text text = builder.addText();
        assertEquals(5, text.getMarginLeft());
        assertEquals(10, text.getMarginBottom());
        assertEquals(6, text.getMarginRight());
        assertEquals(30, text.getMarginTop());
    }

    @Test
    public void testPage() {
        int expectedAmount = builder.getPageAmount() + 1;
        builder.setDefaultPageSize(300, 300);
        Page page = builder.addPage();
        assertEquals("The new page has not been added correctly.", expectedAmount, builder.getPageAmount());
        assertEquals("The default page size was set incorrectly.", 300, page.getWidth());
        builder.setDefaultPageSize(0, 0);
        Page testPage = builder.addPage(1);
        assertEquals("The default page size was set incorrectly.", 300, testPage.getWidth());
        assertEquals("The new page has not been added correctly on the given position. ", testPage, builder.getPage(1));
        builder.setDefaultPageSize(100, 0);
        testPage = builder.addPage(1);
        assertEquals("The default page size was set incorrectly.", 300, testPage.getWidth());
        builder.addPage(0);
        assertEquals("The new page has been added on an incorrect position. ", 3, builder.getPageAmount());
        page = builder.getPage(-1);
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
    public void testPreviewRetrieval() {
        assertEquals(true, builder.getPreview() != null);
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
