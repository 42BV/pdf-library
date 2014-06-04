package nl.mad.toucanpdf.structure;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import nl.mad.toucanpdf.pdf.structure.PdfTrailer;
import nl.mad.toucanpdf.pdf.syntax.PdfIndirectObject;
import nl.mad.toucanpdf.pdf.syntax.PdfName;
import nl.mad.toucanpdf.utility.ByteEncoder;

import org.junit.Test;

public class PdfTrailerTest {

    @Test
    public void testCreation() throws IOException {
        PdfIndirectObject test = new PdfIndirectObject(1, 0, new PdfName("Test"), true);
        PdfTrailer trailer = new PdfTrailer(3, new byte[0], test.getReference(), test);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        trailer.writeToFile(baos);
        String expectedResult = "trailer\n" + "<<\n" + " /Root 1 0 R\n" + " /Info 1 0 R\n" + " /Size 4\n" + ">>\n" + "startxref\n\n" + "%%EOF";
        assertEquals(expectedResult, ByteEncoder.getString(baos));
    }

}
