package nl.mad.pdflibrary.api;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    private Document document;
    private final int testPageSize = 200;

    @Before
    public void setUp() throws Exception {
        document = new Document(testPageSize, testPageSize, "TestAuthor", "TestTitle", "TestSubject");
    }

    @Test
    public void testAddNewPage() {
        int expectedAmount = document.getPageNumber() + 1;
        document.addNewPage();
        assertEquals("The new page has not been added correctly.", expectedAmount, document.getPageNumber());
    }

    @Test
    public void testFinish() throws IOException {
        document.finish();
        assertEquals("The document has not been finished.", true, document.getFinished());
    }

    @Test
    public void testAuthor() {
        assertEquals("The author was not set correctly. ", "TestAuthor", document.getAuthor());
    }

    @Test
    public void testTitle() {
        assertEquals("The title was not set correctly. ", "TestTitle", document.getTitle());
    }

    @Test
    public void testSubject() {
        assertEquals("The subject was not set correctly. ", "TestSubject", document.getSubject());
    }

}
