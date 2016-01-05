package org.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.toucanpdf.pdf.syntax.AbstractPdfObject;
import org.toucanpdf.pdf.syntax.PdfName;
import org.toucanpdf.pdf.syntax.PdfNumber;
import org.toucanpdf.pdf.syntax.PdfObjectType;
import org.toucanpdf.utility.FloatEqualityTester;

import org.junit.Assert;
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
        Assert.assertEquals(1, number.getNumber(), FloatEqualityTester.EPSILON);
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

    @Test
    public void testEquals() {
        PdfNumber test1 = new PdfNumber(1);
        PdfNumber test2 = new PdfNumber(2);
        PdfName test3 = new PdfName("1");

        assertEquals(number.hashCode(), test1.hashCode());
        assertEquals(true, number.equals(test1));

        assertTrue(number.hashCode() != test2.hashCode());
        assertEquals(false, number.equals(test2));

        assertTrue(number.hashCode() != test3.hashCode());
        assertEquals(false, number.equals(test3));

        assertEquals(false, number.equals(null));
    }
}
