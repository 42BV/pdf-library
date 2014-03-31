package nl.mad.pdflibrary.syntax;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import nl.mad.pdflibrary.pdf.syntax.PdfIndirectObject;
import nl.mad.pdflibrary.pdf.syntax.PdfName;

import org.junit.Before;
import org.junit.Test;

public class PdfIndirectObjectTest {
    private PdfName testObject;
    private PdfIndirectObject indirectObject;

    @Before
    public void setUp() throws Exception {
        testObject = new PdfName("test");
        indirectObject = new PdfIndirectObject(1, 0, testObject, true);
    }

    @Test
    public void testReferenceUpdating() {
        indirectObject.setNumber(2);
        assertEquals(2, indirectObject.getNumber());
        assertEquals("2 0 R", indirectObject.getReference().getReference());
        indirectObject.setGeneration(1);
        assertEquals(1, indirectObject.getGeneration());
        assertEquals("2 1 R", indirectObject.getReference().getReference());
    }

    @Test
    public void testWriteToFile() throws IOException {
        String expectedResult = "1 0 obj\n/test\nendobj\n\n";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        indirectObject.writeToFile(baos);
        assertEquals(expectedResult, baos.toString());
    }
}
