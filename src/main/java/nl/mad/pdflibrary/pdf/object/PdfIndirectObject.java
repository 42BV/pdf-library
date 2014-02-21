package nl.mad.pdflibrary.pdf.object;

import java.io.IOException;
import java.io.OutputStream;

import nl.mad.pdflibrary.pdf.PdfCrossReferenceTable;
import nl.mad.pdflibrary.pdf.utility.ByteEncoder;

/**
 * This represents a PDF indirect object. This means it contains an object and a object number plus generation number. 
 * Other PDF objects can refer to this object by using it's object number. It also stores a byte start position, this is needed
 * to create the cross reference table.
 * 
 *  @author Dylan de Wolff
 *  @see PdfCrossReferenceTable
 *  @see PdfIndirectObjectReference
 */
public class PdfIndirectObject {
    private int number;
    private int generation;
    /**
     * contains the PDF syntax used to specify an indirect object 
     */
    protected static final String START = "obj\n";
    /**
     * contains the PDF syntax used to specify the end of an indirect object 
     */
    protected static final String END = "\nendobj\n\n";
    /**
     * The actual PdfObject that is contained within this indirect object
     */
    private AbstractPdfObject object;
    /**
     * This reference should be used by other objects that wish to refer to this indirect object
     */
    private PdfIndirectObjectReference reference;
    /**
     * The starting position of this object in the PDF file
     */
    private int startByte = 0;
    private boolean objectInUse;

    /**
     * Creates a new instance of PDfIndirectObject
     * @param number Object number
     * @param generation Object generation (always 0, unless you are changing an existing object)
     * @param object The object contained within this indirect object
     * @param objectInUse States if the object is actually used in the mad
     */
    public PdfIndirectObject(int number, int generation, AbstractPdfObject object, boolean objectInUse) {
        this.number = number;
        this.generation = generation;
        this.object = object;
        this.reference = new PdfIndirectObjectReference(number, generation);
        this.objectInUse = objectInUse;
    }

    /**
     * Writes the file to the given OutputStream
     * @param os
     * @throws IOException
     */
    public void writeToFile(OutputStream os) throws IOException {
        String objectLine = number + " " + generation + " " + START;
        os.write(ByteEncoder.getBytes(objectLine));
        this.object.writeToFile(os);
        os.write(ByteEncoder.getBytes(END));
    }

    public PdfIndirectObjectReference getReference() {
        return this.reference;
    }

    public void setNumber(int number) {
        this.number = number;
        this.reference.updateReference(this.number, this.generation);
    }

    public int getNumber() {
        return this.number;
    }

    public int getGeneration() {
        return this.generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
        this.reference.updateReference(this.number, this.generation);
    }

    public void setStartByte(int startByte) {
        this.startByte = startByte;
    }

    public int getStartByte() {
        return this.startByte;
    }

    public boolean getInUse() {
        return this.objectInUse;
    }

    public AbstractPdfObject getObject() {
        return this.object;
    }
}
