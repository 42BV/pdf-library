package nl.mad.pdflibrary.syntax;

import java.io.IOException;
import java.io.OutputStream;

import nl.mad.pdflibrary.utility.Constants;

/**
 * Represents an embedded file. This class only uses the byte representation. 
 * @author Dylan de Wolff
 */
public class PdfFile extends AbstractPdfObject {

    /**
     * Creates a new instance of PdfFile.
     * @param byteRepresentation The byte representation of the file.
     */
    public PdfFile(byte[] byteRepresentation) {
        super(byteRepresentation, PdfObjectType.FILE);
    }

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        os.write(this.getByteRepresentation());
        os.write(Constants.LINE_SEPARATOR);
    }

}
