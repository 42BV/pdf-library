package nl.mad.pdflibrary.structure;

import static junit.framework.TestCase.assertEquals;
import nl.mad.pdflibrary.api.BaseText;
import nl.mad.pdflibrary.api.Document;
import nl.mad.pdflibrary.syntax.PdfObjectType;

import org.junit.Before;
import org.junit.Test;

public class PdfDocumentTest {
    private PdfDocument document;

    @Before
    public void setUp() throws Exception {
        document = new PdfDocument(Document.A4_WIDTH, Document.A4_HEIGHT);
    }

    @Test
    public void testAddWithEmptyStream() {
        document.addPage(200, 200);
        BaseText text = new BaseText("Test", 12);
        document.add(text);
        //PdfTextStream testStream = new PdfTextStream();
        //testStream.addCommands(text);

        // assertEquals("Text was not added correctly to the current page. ", testStream.getByteRepresentation(), mad.getCurrentPage().getCurrentStream()
        //       .getByteRepresentation());

        //fail("Not yet implemented");
    }

    @Test
    public void testAddFont() {
        //fail("Not yet implemented");
    }

    @Test
    public void testGetPdfFont() {
        //fail("Not yet implemented");
    }

    @Test
    public void testAddPage() {
        assertEquals("There should not yet be a current page. ", null, document.getCurrentPage());
        document.addPage(200, 200);
        assertEquals("Page was added and set as current page. ", PdfObjectType.PAGE, document.getCurrentPage().getType());
    }

    @Test
    public void testFinish() {
        //fail("Not yet implemented");
    }

}
