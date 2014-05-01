package nl.mad.toucanpdf.structure;

import static org.junit.Assert.assertEquals;
import nl.mad.toucanpdf.api.BasePage;
import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.pdf.structure.PdfDocument;
import nl.mad.toucanpdf.pdf.syntax.PdfObjectType;

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
        document.addPage(new BasePage(200, 200));
        BaseText text = new BaseText("Test");
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
        document.addPage(new BasePage(200, 200));
        assertEquals("Page was added and set as current page. ", PdfObjectType.PAGE, document.getCurrentPage().getType());
    }

    @Test
    public void testFinish() {
        //fail("Not yet implemented");
    }

}
