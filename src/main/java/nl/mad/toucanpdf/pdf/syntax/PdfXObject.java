package nl.mad.toucanpdf.pdf.syntax;

import nl.mad.toucanpdf.model.PdfNameValue;

public class PdfXObject extends PdfStream {
    public PdfXObject() {
        super(PdfObjectType.XOBJECT);
        this.put(PdfNameValue.TYPE, PdfNameValue.XOBJECT);
    }

}
