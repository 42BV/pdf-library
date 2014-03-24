package nl.mad.pdflibrary.api;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.Page;

import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() throws Exception {
        document = new Document();
    }

    @Test
    public void testAddNewPage() {
        int expectedAmount = document.getPageAmount() + 1;
        document.addPage(new BasePage(200, 200));
        assertEquals("The new page has not been added correctly.", expectedAmount, document.getPageAmount());
        Page testPage = new BasePage(300, 300);
        document.addPage(testPage, 1);
        assertEquals("The new page has not been added correctly on the given position. ", testPage, document.getPage(1));
        document.addPage(testPage, 0);
        assertEquals("The new page has been added on an incorrect position. ", 2, document.getPageAmount());
        document.addPage(null);
        assertEquals("The null page has been added. ", 2, document.getPageAmount());
        document.addPage(null, 0);
        assertEquals("The null page has been added. ", 2, document.getPageAmount());
    }

    @Test
    public void testAuthor() {
        document.writtenBy("TestAuthor");
        assertEquals("The author was not set correctly. ", "TestAuthor", document.getAuthor());
    }

    @Test
    public void testTitle() {
        document.title("TestTitle");
        assertEquals("The title was not set correctly. ", "TestTitle", document.getTitle());
    }

    @Test
    public void testSubject() {
        document.about("TestSubject");
        assertEquals("The subject was not set correctly. ", "TestSubject", document.getSubject());
    }

    @Test
    public void testCreationDate() {
        Calendar cal = Calendar.getInstance();
        document.on(cal);
        assertEquals("The creation date was not set correctly. ", cal, document.getCreationDate());
    }

    @Test
    public void testFilename() throws IOException {
        File tempFile = File.createTempFile("document", ".pdf");
        //test filename acquisition with empty filename and title
        document.finish();
        assertEquals("The filename was not set correctly. ", "document.pdf", document.getFilename());
        tempFile.delete();
        //test filename acquisition with empty filename and filled title
        document.filename("");
        document.title("Test");
        tempFile = File.createTempFile("Test", ".pdf");
        document.finish();
        assertEquals("The filename was not set correctly. ", "Test.pdf", document.getFilename());
        //test filename acquisition with filename without extension
        document.filename("Test");
        document.finish();
        assertEquals("The filename was not set correctly. ", "Test.pdf", document.getFilename());
        tempFile.delete();

    }

    @Test
    public void testAddPart() {
        //try adding page using addPart to empty document
        document.addPart(new BasePage(200, 200));
        assertEquals("The page was not added correctly. ", 1, document.getPageAmount());

        //try adding part to empty document
        document = new Document();
        document.addPart(new BaseText("Test"));
        assertEquals("The part was not added correctly. ", 1, document.getPage(1).getContent().size());
        //try automatically adding part to latest page
        document.addPage(new BasePage(200, 200));
        document.addPart(new BaseText("Test"));
        assertEquals("The part was not added correctly to the latest page. ", 1, document.getPage(2).getContent().size());
        //try manually adding part to first page
        document.addPart(new BaseText("Test"), 1);
        assertEquals("The part was not added correctly to the given page. ", 2, document.getPage(1).getContent().size());
        //try adding null 
        document.addPart(null);
        assertEquals("Null part was added. ", 1, document.getPage(2).getContent().size());
        //try adding part to non-existing pages
        document.addPart(new BaseText("Test"), 10);
        assertEquals("Part was added. ", null, document.getPage(10));
        document.addPart(new BaseText("Test"), 0);
        assertEquals("Part was added. ", null, document.getPage(0));
        //try adding list of parts
        List<DocumentPart> testList = new ArrayList<>();
        testList.add(new BaseText("Test"));
        testList.add(new BaseText("Test"));
        document.addParts(testList);
        assertEquals("The parts were not added correctly. ", 3, document.getPage(2).getContent().size());
    }

    @Test
    public void testFinish() throws IOException {
        document.addPage(new BasePage(100, 100));
        document.addPage(new BasePage(100, 100));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.finish(baos);
        assertEquals(true, baos.size() > 0);
    }

    @Test
    public void testNewLines() {
        //TODO
    }
}
