package nl.pdflibrary.test.document;

import static org.junit.Assert.assertEquals;

import java.awt.Font;
import java.io.IOException;

import nl.pdflibrary.document.Document;
import nl.pdflibrary.document.Text;

import org.junit.Before;
import org.junit.Test;



public class DocumentTest {
    private Document document;

    @Before
    public void setUp() throws Exception {
        document = new Document();
    }

    @Test
    public void testDocumentCreation() {
        int pageNumberExpected = 1;
        int actualPageNumber = document.getPageNumber();
        assertEquals("The first page was not added correctly.", pageNumberExpected, actualPageNumber);
        assertEquals("The document is already finished.", false, document.getFinished());
        assertEquals("The default font was not added to the font list.", Document.DEFAULT_FONT, document.getFonts().get(0));
        assertEquals("There is content in the newly created document.", 0, document.getDocumentParts().size());
    }

    @Test
    public void testAddPart() {
        Text part = new Text("Test", 10, 10);
        document.addPart(part);
        assertEquals("The part was not added to the document.", part, document.getDocumentParts().get(0));
        assertEquals("The default font was not added to the text.", Document.DEFAULT_FONT, part.getFont());

        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        part = new Text("Test", f, 10, 10);
        document.addPart(part);
        assertEquals("The new font was not added to the document correctly.", document.getFonts().get(1), part.getFont());
    }

    @Test
    public void testAddFont() {
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 13);
        document.addFont(f);
        assertEquals("The new font was not added to the document correctly.", document.getFonts().get(1), f);
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
