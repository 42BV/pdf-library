package nl.mad.pdflibrary.api;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

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
        int expectedAmount = document.getPageNumber() + 1;
        document.addNewPage();
        assertEquals("The new page has not been added correctly.", expectedAmount, document.getPageNumber());
    }

    @Test
    public void testFinish() throws IOException {
        document.finish();
        assertEquals("The document has not been finished.", true, document.getFinished());
    }

}
