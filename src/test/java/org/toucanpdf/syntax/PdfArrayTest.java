package org.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.toucanpdf.pdf.syntax.AbstractPdfObject;
import org.toucanpdf.pdf.syntax.PdfArray;
import org.toucanpdf.pdf.syntax.PdfName;

import org.junit.Before;
import org.junit.Test;

public class PdfArrayTest {

    private PdfArray array;

    @Before
    public void setUp() throws Exception {
        array = new PdfArray();
    }

    @Test
    public void testAddValue() {
        PdfName test = new PdfName("test");
        array.addValue(test);
        assertEquals("The PdfObject was not added correctly to the array", test, array.getValues().get(0));

        List<AbstractPdfObject> testList = new ArrayList<AbstractPdfObject>();
        testList.add(new PdfName("test2"));
        testList.add(new PdfName("test3"));
        array.addValues(testList);
        assertEquals("The PdfObjects were not added correctly to the array", 3, array.getValues().size());
    }

    @Test
    public void testWriteToFile() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfName test = new PdfName("test");
        array.addValue(test);
        PdfName test2 = new PdfName("test2");
        array.addValue(test2);
        array.writeToFile(os);

        String expectedResult = "[ /test /test2 ]";
        assertEquals("The write output was not as expected.", expectedResult, os.toString());
    }
}
