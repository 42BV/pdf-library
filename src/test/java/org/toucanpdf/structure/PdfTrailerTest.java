package org.toucanpdf.structure;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.toucanpdf.pdf.structure.PdfTrailer;
import org.toucanpdf.pdf.syntax.PdfIndirectObject;
import org.toucanpdf.pdf.syntax.PdfName;
import org.toucanpdf.utility.ByteEncoder;

import org.junit.Test;

public class PdfTrailerTest {

    @Test
    public void testCreation() throws IOException {
        PdfIndirectObject test = new PdfIndirectObject(1, 0, new PdfName("Test"), true);
        PdfTrailer trailer = new PdfTrailer(3, new byte[0], test.getReference(), test);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        trailer.writeToFile(baos);
        String result = ByteEncoder.getString(baos);
        assertTrue(result.startsWith("trailer\n" + "<<\n"));
        assertTrue(result.contains("/Info 1 0 R\n"));
        assertTrue(result.contains("/Root 1 0 R\n"));
        assertTrue(result.contains("/Size 4\n"));
        assertTrue(result.endsWith(">>\n" + "startxref\n\n" + "%%EOF"));
    }
}
