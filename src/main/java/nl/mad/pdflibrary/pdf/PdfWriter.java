package nl.mad.pdflibrary.pdf;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Writes the different sections to an OutputStream.
 * @author Dylan de Wolff
 *
 */
public class PdfWriter {

    public PdfWriter() {

    }

    /**
     * Writes the given sections to an OutputStream.
     * @param h PdfHeader of the document.
     * @param b PdfBody of the document.
     * @param xref Xref table of the document.
     * @param t PdfTrailer of the document.
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void write(PdfHeader h, PdfBody b, PdfCrossReferenceTable xref, PdfTrailer t) throws UnsupportedEncodingException, IOException {
        DataOutputStream os = new DataOutputStream(new FileOutputStream("testpdf.pdf"));
        h.writeToFile(os);
        os.write(PdfDocument.LINE_SEPARATOR);
        b.writeToFile(os);
        xref.fillTableWithIndirectObjects(b.getAllIndirectObjects());
        xref.writeToFile(os);
        t.setObjectAmount(b.getTotalIndirectObjectsAmount() + 1);
        t.setCrossReferenceStartByte(xref.getStartByte());
        t.fillObjectSpecification(b.getCatalogReference());
        t.writeToFile(os);
        os.flush();
        os.close();
    }
}
