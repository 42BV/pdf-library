package nl.mad.toucanpdf.pdf.structure;

import java.io.IOException;
import java.io.OutputStream;

import nl.mad.toucanpdf.utility.ByteEncoder;
import nl.mad.toucanpdf.utility.Constants;

/**
 * This class represents the header section of a PDF file and contains data such as the PDF version.
 * It is also responsible for writing the header.
 * @author Dylan de Wolff
 */
public class PdfHeader {
    private static final byte[] VERSION = ByteEncoder.getBytes("%PDF-1.7");
    /**
     * Indicates to file readers that this document contains binary data.
     */
    private static final byte[] BINARY_INDICATOR = ByteEncoder.getBytes("%âãÏÓ");

    /**
     * Creates a new instance of PdfHeader.
     */
    public PdfHeader() {
    }

    /**
     * Writes the header to the OutputStream.
     * @param os OutputStream which will be written to.
     * @throws IOException throws an IOException if an error occured during the writing.
     */
    public void writeToFile(OutputStream os) throws IOException {
        os.write(VERSION);
        os.write(Constants.LINE_SEPARATOR);
        os.write(BINARY_INDICATOR);
        os.write(Constants.LINE_SEPARATOR);
    }
}
