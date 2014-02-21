package nl.mad.pdflibrary.pdf;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PdfWriter {

    public PdfWriter() {

    }

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
