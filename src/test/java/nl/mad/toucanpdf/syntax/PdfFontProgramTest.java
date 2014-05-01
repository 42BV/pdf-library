package nl.mad.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;
import nl.mad.toucanpdf.model.PdfNameValue;
import nl.mad.toucanpdf.pdf.syntax.PdfFile;
import nl.mad.toucanpdf.pdf.syntax.PdfFontProgram;
import nl.mad.toucanpdf.pdf.syntax.PdfNumber;
import nl.mad.toucanpdf.utility.ByteEncoder;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class PdfFontProgramTest {
    private PdfFontProgram program;

    @Before
    public void setUp() throws Exception {
        program = new PdfFontProgram();
    }

    @Test
    public void testFileSetting() {
        PdfFile file = new PdfFile(ByteEncoder.getBytes("test"));
        program.setFontProgram(file);
        assertEquals(1, program.getContentSize());
    }

    @Test
    public void testFontProgramLengths() {
        int[] lengths = { 1, 2, 3 };
        program.setLengths(lengths);
        assertEquals(lengths[0], ((PdfNumber) program.get(PdfNameValue.LENGTH1)).getNumber(), FloatEqualityTester.EPSILON);
        assertEquals(lengths[1], ((PdfNumber) program.get(PdfNameValue.LENGTH2)).getNumber(), FloatEqualityTester.EPSILON);
        assertEquals(lengths[2], ((PdfNumber) program.get(PdfNameValue.LENGTH3)).getNumber(), FloatEqualityTester.EPSILON);
    }
}
