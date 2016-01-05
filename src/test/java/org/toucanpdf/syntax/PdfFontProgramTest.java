package org.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;
import org.toucanpdf.model.PdfNameValue;
import org.toucanpdf.pdf.syntax.PdfFile;
import org.toucanpdf.pdf.syntax.PdfFontProgram;
import org.toucanpdf.pdf.syntax.PdfNumber;
import org.toucanpdf.utility.ByteEncoder;
import org.toucanpdf.utility.FloatEqualityTester;

import org.junit.Assert;
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
        Assert.assertEquals(lengths[0], ((PdfNumber) program.get(PdfNameValue.LENGTH1)).getNumber(), FloatEqualityTester.EPSILON);
        assertEquals(lengths[1], ((PdfNumber) program.get(PdfNameValue.LENGTH2)).getNumber(), FloatEqualityTester.EPSILON);
        assertEquals(lengths[2], ((PdfNumber) program.get(PdfNameValue.LENGTH3)).getNumber(), FloatEqualityTester.EPSILON);
    }
}
