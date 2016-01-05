package org.toucanpdf.pdf.syntax;

import org.toucanpdf.model.PdfNameValue;

/**
 * This class represents the PDF syntax of an embedded font program. 
 * @author Dylan de Wolff
 *
 */
public class PdfFontProgram extends PdfStream {

    /**
     * Creates a new instance of PdfFontProgram.
     */
    public PdfFontProgram() {
        super();
    }

    /**
     * Sets the font program to the given font program.
     * @param fontProgram The program to add.
     */
    public void setFontProgram(PdfFile fontProgram) {
        this.add(fontProgram);
    }

    /**
     * Sets the length values for the font program.
     * @param lengths Int array containing the lengths.
     */
    public void setLengths(int[] lengths) {
        this.put(PdfNameValue.LENGTH1, new PdfNumber(lengths[0]));
        this.put(PdfNameValue.LENGTH2, new PdfNumber(lengths[1]));
        this.put(PdfNameValue.LENGTH3, new PdfNumber(lengths[2]));
    }

}
