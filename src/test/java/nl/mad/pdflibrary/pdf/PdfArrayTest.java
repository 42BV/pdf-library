package nl.mad.pdflibrary.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import nl.mad.pdflibrary.syntax.PdfArray;
import nl.mad.pdflibrary.syntax.PdfName;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class PdfArrayTest {

    private PdfArray array;

    @Before
    public void setUp() throws Exception {
        array = new PdfArray();
    }

    @Test
    public void testAddValue() {
        PdfName test = new PdfName("nl");
        array.addValue(test);
        assertEquals("The PdfObject was not added correctly to the array", test, array.getValues().get(0));
    }

    @Test
    public void testWriteToFile() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfName test = new PdfName("nl");
        array.addValue(test);
        PdfName test2 = new PdfName("test2");
        array.addValue(test2);
        array.writeToFile(os);

        String expectedResult = "[/nl /test2 ]";
        assertEquals("The write output was not as expected.", expectedResult, os.toString());
    }
}
