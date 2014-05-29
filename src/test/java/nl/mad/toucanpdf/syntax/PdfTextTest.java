package nl.mad.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import nl.mad.toucanpdf.api.BaseFont;
import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.state.StatePage;
import nl.mad.toucanpdf.model.state.StateParagraph;
import nl.mad.toucanpdf.model.state.StateText;
import nl.mad.toucanpdf.pdf.syntax.PdfFont;
import nl.mad.toucanpdf.pdf.syntax.PdfIndirectObject;
import nl.mad.toucanpdf.pdf.syntax.PdfObjectType;
import nl.mad.toucanpdf.pdf.syntax.PdfPage;
import nl.mad.toucanpdf.pdf.syntax.PdfText;
import nl.mad.toucanpdf.state.BaseStateParagraph;
import nl.mad.toucanpdf.state.BaseStateText;
import nl.mad.toucanpdf.utility.ByteEncoder;

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
				  }
			  };

        //expected result for font adding, matrix adding and text adding
        String expectedTotalResult = "/R1 11 Tf\n" + "2.0 Tw 0.0 0.0 0.0 0.0 100.0 100.0 Tm\n" + "[(T) 70 (est)] TJ\n" + "0.0 Tw 0.0 0.0 0.0 0.0 200.0 200.0 Tm\n" + "[(T) 70 (est2)] TJ\n" + " 0 -3 TD\n";
        pdfText.addText(text, fontReference, Page.DEFAULT_NEW_LINE_SIZE);
        System.out.println("Helo: " + ByteEncoder.getString(pdfText.getByteRepresentation()));
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
