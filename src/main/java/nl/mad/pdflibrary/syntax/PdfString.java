package nl.mad.pdflibrary.syntax;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Represents a PDF String object. It contains a single string and is able to write this string to an OutputStream.
 * @author Dylan de Wolff
 */
public class PdfString extends AbstractPdfObject {
    private static final String DATE_PREFIX = "D:";
    private String string;

    /**
     * Creates a new instance of PdfString.
     */
    public PdfString() {
        super(PdfObjectType.STRING);
        string = "";
    }

    /**
     * Creates a new instance of PdfString.
     * @param string String to represent in this PdfString.
     */
    public PdfString(String string) {
        this();
        this.string = string;
        this.setByteRepresentation(string);
    }

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        this.setByteRepresentation(string);
        os.write('(');
        super.writeToFile(os);
        os.write(')');
    }

    public String getString() {
        return string;
    }

    /**
     * Sets the string this object represents.
     * @param string String to use.
     */
    public void setString(String string) {
        this.string = string;
        this.setByteRepresentation(string);
    }

    /**
     * Creates a PDF string from the given date.
     * @param date Date to create a string from.
     */
    public void setString(Calendar date) {
        String stringDate = DATE_PREFIX;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        stringDate += dateFormat.format(date.getTime());
        this.setString(stringDate);
    }
}
