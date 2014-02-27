package nl.mad.pdflibrary.structure;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import nl.mad.pdflibrary.utility.PdfConstants;

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
     * @param h PdfHeader of the api.
     * @param b PdfBody of the api.
     * @param xref Xref table of the api.
     * @param t PdfTrailer of the api.
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void write(OutputStream os, PdfHeader h, PdfBody b, PdfCrossReferenceTable xref, PdfTrailer t) throws UnsupportedEncodingException, IOException {
        DataOutputStream dos = new DataOutputStream(os);
        h.writeToFile(dos);
        dos.write(PdfConstants.LINE_SEPARATOR);
        b.writeToFile(dos);
        xref.fillTableWithIndirectObjects(b.getAllIndirectObjects());
        xref.writeToFile(dos);
        t.setObjectAmount(b.getTotalIndirectObjectsAmount() + 1);
        t.setCrossReferenceStartByte(xref.getStartByte());
        t.fillObjectSpecification(b.getCatalogReference());
        t.writeToFile(dos);
        dos.flush();
        dos.close();
    }
}
