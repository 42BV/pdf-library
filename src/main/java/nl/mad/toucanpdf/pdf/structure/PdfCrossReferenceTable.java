package nl.mad.toucanpdf.pdf.structure;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.mad.toucanpdf.pdf.syntax.PdfIndirectObject;
import nl.mad.toucanpdf.utility.ByteEncoder;
import nl.mad.toucanpdf.utility.Constants;

/**
 * 
 * This class represents the cross reference table (hereafter referred to as the 'xref table'). The xref table
 * contains the number of indirect objects in the file, the byte point where each of these objects start and the generation number
 * of each object. The class is responsible for maintaining and writing these references.
 * 
 * @author Dylan de Wolff
 * @see PdfBody
 */
public class PdfCrossReferenceTable {
    /**
     * All crossreferences are stored in this map. The key is the object number.
     */
    private Map<Integer, CrossReference> crossReferences;

    /**
     * The PDF syntax used to specify that the xref table is underneath this line.
     */
    private static final String XREF_INDICATOR = "xref";
    //check if this is needed
    private Integer lowestObjectNumber = 0;

    /**
     * The reference stated here is always the first one to be printed in a PDF 
     * and does not directly represent any of the indirect objects in the body. 
     * Therefore it is stored separately as an attribute.
     */
    private static final String DEFAULT_FIRST_REFERENCE = "0000000000 65535 f";

    /**
     * Stores the byte starting position of the xref table itself.
     */
    private byte[] startByte;

    /**
     * Creates a new instance of the cross reference table.
     */
    public PdfCrossReferenceTable() {
        crossReferences = new HashMap<Integer, CrossReference>();
    }

    /**
     * Creates a new instance of the cross reference table and directly filling it with the given objects.
     * 
     * @param indirectObjects The indirect objects that will be referred to in the xref table.
     */
    public PdfCrossReferenceTable(List<PdfIndirectObject> indirectObjects) {
        crossReferences = new HashMap<Integer, CrossReference>();
        this.fillTableWithIndirectObjects(indirectObjects);
    }

    /**
     * Fills the table with the given indirect objects.
     * 
     * @param indirectObjects The indirect objects that will be referred to in the xref table.
     */
    public final void fillTableWithIndirectObjects(List<PdfIndirectObject> indirectObjects) {
        for (PdfIndirectObject indirectObject : indirectObjects) {
            this.addReferenceToIndirectObject(indirectObject);
        }
    }

    /**
     * Adds the reference from the given indirect object to the table.
     * 
     * @param indirectObject Object that will be referred to.
     */
    public void addReferenceToIndirectObject(PdfIndirectObject indirectObject) {
        crossReferences.put(Integer.valueOf(indirectObject.getNumber()), new CrossReference(indirectObject.getStartByte(), indirectObject.getInUse(),
                indirectObject.getGeneration()));
        updateLowestObjectNumber(indirectObject.getNumber());
    }

    private void updateLowestObjectNumber(int number) {
        if (lowestObjectNumber == null) {
            this.lowestObjectNumber = Integer.valueOf(number);
        } else if (number < lowestObjectNumber.intValue()) {
            this.lowestObjectNumber = Integer.valueOf(number);
        }
    }

    public int getCrossReferenceAmount() {
        return crossReferences.size();
    }

    public boolean isObjectInTable(int objectNumber) {
        if (this.crossReferences.containsKey(objectNumber)) {
            return true;
        }
        return false;
    }

    /**
     * Writes the xref table to the given output stream.
     * @param os OutputStream that will be written to.
     * @throws IOException 
     */
    public void writeToFile(DataOutputStream os) throws IOException {
        setStartByte(os.size());
        os.write(ByteEncoder.getBytes(XREF_INDICATOR));
        os.write(Constants.LINE_SEPARATOR);
        os.write(ByteEncoder.getBytes(this.getObjectAmountLine()));
        os.write(Constants.LINE_SEPARATOR);
        os.write(ByteEncoder.getBytes(DEFAULT_FIRST_REFERENCE));
        os.write(Constants.LINE_SEPARATOR);

        for (Entry<Integer, CrossReference> xref : crossReferences.entrySet()) {
            xref.getValue().writeToFile(os);
            os.write(Constants.LINE_SEPARATOR);
        }
    }

    private void setStartByte(int start) {
        this.startByte = ByteEncoder.getBytes(("" + start));
    }

    public byte[] getStartByte() {
        return this.startByte;
    }

    private String getObjectAmountLine() {
        return lowestObjectNumber.intValue() + " " + (this.crossReferences.size() + 1);
    }

    /**
     * @author Dylan de Wolff
     *
     * Inner class containing the information needed per reference.
     */
    private class CrossReference {
        private String startByte;
        private String generation;
        private boolean inUse;

        /**
         * The format used to specify the starting point of indirect objects
         */
        private static final String START_BYTE_FORMAT = "0000000000";

        /**
         * The format used to specify the generation number of indirect objects
         */
        private static final String GENERATION_FORMAT = "00000";
        /**
         * Specifies the syntax for objects that are or are not in use
         */
        private static final char NOT_IN_USE = 'f';
        private static final char IN_USE = 'n';

        /**
         * Creates a new instance of crossReference.
         * @param byteStart The byte starting point of the indirect object that needs to be referred to.
         * @param inUse Specifies whether the object is actually used in the api.
         * @param generation The generation number of the object.
         */
        public CrossReference(int byteStart, boolean inUse, int generation) {
            this.processByteStart(byteStart);
            this.setInUse(inUse);
            this.processGeneration(generation);
        }

        /**
         * Ensures the generation number is stored as the PDF format requires.
         * @param generationNumber Generation number of object.
         */
        private void processGeneration(int generationNumber) {
            String generationNumberString = String.valueOf(generationNumber);
            this.generation = GENERATION_FORMAT.subSequence(0, GENERATION_FORMAT.length() - generationNumberString.length()) + generationNumberString;
        }

        /**
         * Ensures the byte starting point is stored as the PDF format requires.
         * @param byteStart Byte start of object.
         */
        private void processByteStart(int byteStart) {
            String bytes = String.valueOf(byteStart);
            this.startByte = START_BYTE_FORMAT.subSequence(0, START_BYTE_FORMAT.length() - bytes.length()) + bytes;
        }

        private void setInUse(boolean inUse) {
            this.inUse = inUse;
        }

        /**
         * Writes the cross reference to the given OutputStream.
         * @param os OutputStream which will be written to.
         * @throws IOException
         */
        public void writeToFile(OutputStream os) throws IOException {
            String line = startByte + " " + generation + " " + getInUseSyntax();
            os.write(ByteEncoder.getBytes(line));
        }

        private char getInUseSyntax() {
            if (inUse) {
                return IN_USE;
            } else {
                return NOT_IN_USE;
            }
        }
    }
}
