package nl.pdflibrary.test.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import nl.pdflibrary.document.Text;
import nl.pdflibrary.pdf.PdfDocument;
import nl.pdflibrary.pdf.object.PdfDictionaryType;
import nl.pdflibrary.pdf.object.PdfTextStream;

import org.junit.Before;
import org.junit.Test;


public class PdfDocumentTest {
    private PdfDocument document;

    @Before
    public void setUp() throws Exception {
        document = new PdfDocument();
    }

    @Test
    public void testAddWithEmptyStream() {
        document.addPage(200, 200);
        Text text = new Text("Test", 10, 10);
        document.add(text);
        PdfTextStream testStream = new PdfTextStream();
        testStream.addCommands(text);

        // assertEquals("Text was not added correctly to the current page. ", testStream.getByteRepresentation(), document.getCurrentPage().getCurrentStream()
        //       .getByteRepresentation());

        fail("Not yet implemented");
    }

    @Test
    public void testAddFont() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetPdfFont() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddPage() {
        assertEquals("There should not yet be a current page. ", null, document.getCurrentPage());
        document.addPage(200, 200);
        assertEquals("Page was added and set as current page. ", PdfDictionaryType.PAGE, document.getCurrentPage().getType());
    }

    @Test
    public void testFinish() {
        fail("Not yet implemented");
    }

}
