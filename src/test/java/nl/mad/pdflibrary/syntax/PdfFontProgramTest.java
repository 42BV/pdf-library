package nl.mad.pdflibrary.syntax;

import static org.junit.Assert.assertEquals;
import nl.mad.pdflibrary.model.PdfNameValue;
import nl.mad.pdflibrary.utility.ByteEncoder;
import nl.mad.pdflibrary.utility.FloatEqualityTester;

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
