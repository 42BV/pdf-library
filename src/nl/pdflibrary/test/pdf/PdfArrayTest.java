package nl.pdflibrary.test.pdf;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import nl.pdflibrary.pdf.object.PdfArray;
import nl.pdflibrary.pdf.object.PdfName;

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
    }

    @Test
    public void testWriteToFile() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfName test = new PdfName("test");
        array.addValue(test);
        PdfName test2 = new PdfName("test2");
        array.addValue(test2);
        array.writeToFile(os);

        String expectedResult = "[/test /test2 ]";
        assertEquals("The write output was not as expected.", expectedResult, os.toString());
    }
}
