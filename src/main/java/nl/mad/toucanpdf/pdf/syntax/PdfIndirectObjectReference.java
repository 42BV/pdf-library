package nl.mad.toucanpdf.pdf.syntax;

/**
 * Holds the reference to an indirect object, this class should be used whenever another object wishes to refer to an indirect object.
 * @author Dylan de Wolff
 */
public class PdfIndirectObjectReference extends AbstractPdfObject {
    private String reference;
    /**
     * The syntax used in PDF to specify a reference.
     */
    private static final char REFERENCE_SYNTAX = 'R';
    /**
     * Resource references have a different syntax than normal references.
     */
    private String resourceReference;

    /**
     * Generates a new instance of PdfIndirectObjectReference.
     * @param number Object number of the object being referred to.
     * @param generation Generation number of the object being referred to.
     */
    public PdfIndirectObjectReference(int number, int generation) {
        super(PdfObjectType.REFERENCE);
        this.updateReference(number, generation);
    }

    /**
     * Sets the reference string.
     * @param reference String containing the reference.
     */
    public final void setReference(String reference) {
        this.reference = reference;
        this.setByteRepresentation(this.reference);
    }

    /**
     * Updates the reference string.
     * @param number Number of the object being referred to.
     * @param generation Generation of the object being referred to.
     */
    public final void updateReference(int number, int generation) {
        String newReference = number + " " + generation + " " + PdfIndirectObjectReference.REFERENCE_SYNTAX;
        this.setReference(newReference);
    }

    public String getReference() {
        return this.reference;
    }

    public String getResourceReference() {
        return this.resourceReference;
    }

    public void setResourceReference(String resourceReference) {
        this.resourceReference = resourceReference;
    }
}
