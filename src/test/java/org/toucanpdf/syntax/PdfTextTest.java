package org.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.toucanpdf.api.BaseFont;
import org.toucanpdf.api.BaseText;
import org.toucanpdf.model.Alignment;
import org.toucanpdf.model.Color;
import org.toucanpdf.model.Page;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.state.StateParagraph;
import org.toucanpdf.model.state.StateText;
import org.toucanpdf.pdf.syntax.PdfFont;
import org.toucanpdf.pdf.syntax.PdfIndirectObject;
import org.toucanpdf.pdf.syntax.PdfObjectType;
import org.toucanpdf.pdf.syntax.PdfPage;
import org.toucanpdf.pdf.syntax.PdfText;
import org.toucanpdf.state.BaseStateParagraph;
import org.toucanpdf.state.BaseStateText;
import org.toucanpdf.utility.ByteEncoder;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PdfTextTest {
    private PdfText pdfText;
    private static PdfIndirectObject fontReference;
    private static PdfPage page;

    @BeforeClass
    public static void setUpTestObjects() throws Exception {
        fontReference = new PdfIndirectObject(1, 0, new PdfFont(new BaseFont(), null), true);
        fontReference.getReference().setResourceReference("R1");
        page = new PdfPage(300, 300, Page.DEFAULT_NEW_LINE_SIZE, 0);
        page.setMargins(10, 0, 0, 11);
    }

    @Before
    public void setUp() throws Exception {
        pdfText = new PdfText(null);
    }

    @Test
    public void testCreation() {
        assertEquals(PdfObjectType.TEXT, pdfText.getType());
    }

    @Test
    public void testTextAdding(@Mocked final StateText text) throws UnsupportedEncodingException {
        new NonStrictExpectations() {
            {
                text.getTextSize();
                returns(11);

                Map<Position, String> textSplit = new LinkedHashMap<Position, String>();
                textSplit.put(new Position(100, 100), "Test");
                textSplit.put(new Position(200, 200), "Test2");
                textSplit.put(new Position(300, 300), "\n");
                text.getTextSplit();
                returns(textSplit);

                Map<Position, Double> justificationOffset = new LinkedHashMap<Position, Double>();
                justificationOffset.put(new Position(100, 100), new Double(2));
                justificationOffset.put(new Position(200, 200), new Double(0));
                justificationOffset.put(new Position(300, 300), new Double(0));
                text.getJustificationOffset();
                returns(justificationOffset);

                text.getFont();
                returns(new BaseFont());

                text.getAlignment();
                returns(Alignment.JUSTIFIED);

                text.getColor();
                returns(Color.BLACK);
            }
        };

        //expected result for font adding, matrix adding and text adding
        String expectedTotalResult = "/R1 11 Tf\n" + "2.0 Tw 0.0 0.0 0.0 0.0 100.0 100.0 Tm\n" + "0.0 0.0 0.0 rg [(T) 70 (est)] TJ\n"
                + "0.0 Tw 0.0 0.0 0.0 0.0 200.0 200.0 Tm\n" + "0.0 0.0 0.0 rg [(T) 70 (est2)] TJ\n" + " 0 -3 TD\n";
        pdfText.addText(text, fontReference, Page.DEFAULT_NEW_LINE_SIZE);
        assertEquals(expectedTotalResult, new String(pdfText.getByteRepresentation(), "UTF-8"));
    }

    @Test
    public void testParagraphTextAdding() throws UnsupportedEncodingException {
        pdfText = new PdfText(null);
        StateParagraph p = new BaseStateParagraph();
        p.addText(new BaseText("Test Test Test")).addText(new BaseText("Test2"));
        for (int i = 0; i < p.getTextCollection().size(); ++i) {
            pdfText.addText(p.getStateTextCollection().get(i), fontReference, Page.DEFAULT_NEW_LINE_SIZE);
        }
        String expectedResult = "/R1 12 Tf\n1.0 0.0 0.0 1.0 -1 -1 Tm\n[(T) 70 (est)] TJ\n[(T) 70 (est)] TJ\n"
                + "[(T) 70 (est)] TJ\n/R1 12 Tf\n1.0 0.0 0.0 1.0 -1 -1 Tm\n[(T) 70 (est2)] TJ\n";
        //assertEquals(expectedResult, new String(pdfText.getByteRepresentation(), "UTF-8"));
    }

    @Test
    public void testMatrixAdding() throws UnsupportedEncodingException {
        StateText text = new BaseStateText("Test");
        pdfText.addMatrix(text);
        assertEquals("1.0 0.0 0.0 1.0 -1.0 -1.0 Tm\n", ByteEncoder.getString(pdfText.getByteRepresentation()));
    }
}
