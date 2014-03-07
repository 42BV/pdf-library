package nl.mad.pdflibrary.syntax;

import java.io.IOException;
import java.io.OutputStream;

import nl.mad.pdflibrary.utility.PdfConstants;

public class PdfFile extends AbstractPdfObject {

    public PdfFile(byte[] byteRepresentation) {
        super(byteRepresentation, PdfObjectType.FILE);
    }

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        os.write(this.getByteRepresentation());
        os.write(PdfConstants.LINE_SEPARATOR);
    }

}
