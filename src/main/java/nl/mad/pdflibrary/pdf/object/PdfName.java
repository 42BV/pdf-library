package nl.mad.pdflibrary.pdf.object;

import java.io.UnsupportedEncodingException;

/**
 * PdfName represents the name object of PDF. Names are used to specify all data within PdfObjects. 
 * 
 * @author Dylan de Wolff
 */
public class PdfName extends AbstractPdfObject {
    private String name;

    /**
     * Creates a new instance of PdfName with the given String
     * @param name
     */
    public PdfName(String name) {
        super(PdfObjectType.NAME);
        this.setName(name);
    }

    /**
     * creates a new instance of PdfName with the given PdfNameValue
     * @param name
     * @see PdfNameValue
     */
    public PdfName(PdfNameValue name) {
        super(PdfObjectType.NAME);
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the name and prefixes the name to conform with the PDF
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        if (!name.startsWith("/")) {
            String prefixName = "/" + name;
            try {
                this.setByteRepresentation(prefixName.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void setName(PdfNameValue name) {
        this.setName(name.toString());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PdfName) {
            return this.name.equals(((PdfName) (o)).getName());
        }
        return false;
    }
}
