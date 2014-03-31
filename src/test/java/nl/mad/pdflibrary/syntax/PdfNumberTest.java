package nl.mad.pdflibrary.syntax;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.mad.pdflibrary.pdf.syntax.AbstractPdfObject;
import nl.mad.pdflibrary.pdf.syntax.PdfNumber;
import nl.mad.pdflibrary.pdf.syntax.PdfObjectType;
import nl.mad.pdflibrary.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class PdfNumberTest {
    private PdfNumber number;

    @Before
    public void setUp() throws Exception {
        number = new PdfNumber(1);
    }

    @Test
    public void testCreation() {
        assertEquals(1, number.getNumber(), FloatEqualityTester.EPSILON);
        assertEquals(PdfObjectType.NUMBER, number.getType());
    }

    @Test
    public void testListConvert() {
        List<AbstractPdfObject> testList = PdfNumber.convertListOfValues(new double[] { 2.5, 1.5 });
        assertEquals(2, testList.size());
        assertEquals(2.5, ((PdfNumber) testList.get(0)).getNumber(), FloatEqualityTester.EPSILON);
        assertEquals(1.5, ((PdfNumber) testList.get(1)).getNumber(), FloatEqualityTester.EPSILON);

        List<Integer> testList2 = new ArrayList<Integer>();
        testList2.add(2);
        testList2.add(1);

        testList = PdfNumber.convertListOfValues(testList2);
        assertEquals(2, testList.size());
        assertEquals(2, ((PdfNumber) testList.get(0)).getNumber(), FloatEqualityTester.EPSILON);
        assertEquals(1, ((PdfNumber) testList.get(1)).getNumber(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testWriteToFile() throws IOException {
        //test int write
        String expectedResult = "1";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        number.writeToFile(baos);
        assertEquals(expectedResult, baos.toString());
        //test double write
        number.setNumber(2.1);
        expectedResult = "2.1";
        baos.reset();
        number.writeToFile(baos);
        assertEquals(expectedResult, baos.toString());

    }
}
