package nl.pdflibrary.pdf;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class represents the header section of a PDF file and contains data such as the PDF version.
 * It is also responsible for writing the header.
 * @author Dylan de Wolff
 */
public class PdfHeader {
    private static final byte[] VERSION = "%PDF-1.7".getBytes();

    /**
     * Creates a new instance of PdfHeader
     */
    public PdfHeader() {
    }

    /**
     * Writes the header to the OutputStream
     * @param os OutputStream
     * @throws IOException
     */
    public void writeToFile(OutputStream os) throws IOException {
        os.write(VERSION);
        os.write(PdfDocument.LINE_SEPARATOR);
    }
}
