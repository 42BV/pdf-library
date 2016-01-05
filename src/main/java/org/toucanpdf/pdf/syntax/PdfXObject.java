package org.toucanpdf.pdf.syntax;

import org.toucanpdf.model.PdfNameValue;

/**
 * Represents an XObject stream in the PDF specification.
 * @author Dylan de Wolff
 *
 */
public abstract class PdfXObject extends PdfStream {

    /**
     * Creates a new instance of PdfXObject.
     */
    public PdfXObject() {
        super(PdfObjectType.XOBJECT);
        this.put(PdfNameValue.TYPE, PdfNameValue.XOBJECT);
    }

}
