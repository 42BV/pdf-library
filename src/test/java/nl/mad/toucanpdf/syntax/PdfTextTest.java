package nl.mad.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import nl.mad.toucanpdf.api.BaseFont;
import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.state.StateParagraph;
import nl.mad.toucanpdf.model.state.StateText;
import nl.mad.toucanpdf.pdf.syntax.PdfFont;
import nl.mad.toucanpdf.pdf.syntax.PdfIndirectObject;
import nl.mad.toucanpdf.pdf.syntax.PdfObjectType;
import nl.mad.toucanpdf.pdf.syntax.PdfPage;
import nl.mad.toucanpdf.pdf.syntax.PdfText;
import nl.mad.toucanpdf.state.BaseStateParagraph;
import nl.mad.toucanpdf.state.BaseStateText;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PdfTextTest {
    private PdfText pdfText;
    private static PdfIndirectObject fontReference;
    private static PdfPage page;

    @BeforeClass
    public static void setUpTestObjects() throws Exception {
        fontReference = new PdfIndirectObject(1, 0, new PdfFont(new BaseFont()), true);
        fontReference.getReference().setResourceReference("R1");
        page = new PdfPage(300, 300, Page.DEFAULT_NEW_LINE_SIZE);
        page.setMargins(10, 0, 0, 11);
    }

    @Before
    public void setUp() throws Exception {
        pdfText = new PdfText();
    }

    @Test
    public void testCreation() {
        assertEquals(PdfObjectType.TEXT, pdfText.getType());
    }

    @Test
    public void testTextAdding() throws UnsupportedEncodingException {
        StateText text = new BaseStateText("Test");
        text.size(10).on(20, 20);

        //expected result for font adding, matrix adding and text adding
        String expectedTotalResult = "/R1 10 Tf\n" + "1.0 0.0 0.0 1.0 20 20 Tm\n" + "[(T) 70 (est)] TJ\n";
        pdfText.addText(text, fontReference, Page.DEFAULT_NEW_LINE_SIZE);
        //assertEquals(expectedTotalResult, new String(pdfText.getByteRepresentation(), "UTF-8"));
    }

    @Test
    public void testParagraphTextAdding() throws UnsupportedEncodingException {
        pdfText = new PdfText();
        StateParagraph p = new BaseStateParagraph();
        p.addText(new BaseText("Test Test Test")).addText(new BaseText("Test2"));
        for (int i = 0; i < p.getTextCollection().size(); ++i) {
            pdfText.addText(p.getStateTextCollection().get(i), fontReference, Page.DEFAULT_NEW_LINE_SIZE);
        }
        String expectedResult = "/R1 12 Tf\n1.0 0.0 0.0 1.0 -1 -1 Tm\n[(T) 70 (est)] TJ\n[(T) 70 (est)] TJ\n"
                + "[(T) 70 (est)] TJ\n/R1 12 Tf\n1.0 0.0 0.0 1.0 -1 -1 Tm\n[(T) 70 (est2)] TJ\n";
        //assertEquals(expectedResult, new String(pdfText.getByteRepresentation(), "UTF-8"));
    }
}
