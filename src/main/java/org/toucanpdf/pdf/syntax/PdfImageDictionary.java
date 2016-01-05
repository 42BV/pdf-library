package org.toucanpdf.pdf.syntax;

import org.toucanpdf.model.Image;
import org.toucanpdf.model.ImageParser;
import org.toucanpdf.model.PdfNameValue;

/**
 * Represents a XObject image dictionary from the PDF specification.
 * @author Dylan de Wolff
 */
public class PdfImageDictionary extends PdfXObject {
    private Image image = null;

    /**
     * Creates a new instance of PdfImageDictionary and fills the dictionary based on the given image.
     * @param part Image to embed.
     */
    public PdfImageDictionary(Image part) {
        if (part != null) {
            image = part;
            ImageParser parser = image.getImageParser();
            this.put(PdfNameValue.SUB_TYPE, PdfNameValue.IMAGE);
            this.put(PdfNameValue.WIDTH, new PdfNumber(parser.getWidth()));
            this.put(PdfNameValue.HEIGHT, new PdfNumber(parser.getHeight()));
            this.put(PdfNameValue.COLOR_SPACE, parser.getColorSpace().getPdfName());
            this.put(PdfNameValue.BITS_PER_COMPONENT, new PdfNumber(parser.getBitsPerComponent()));
            this.addDecodeArray();
            this.addFilter(parser.getFilter());
            this.addFilter(image.getCompressionMethod());
            this.add(new PdfFile(parser.getData()));
        }
    }

    private void addDecodeArray() {
        if (image.getInvertColors()) {
            PdfArray decodeArray = new PdfArray();
            int componentAmount = image.getImageParser().getRequiredComponentsForColorSpace(image.getImageParser().getColorSpace());
            for (int i = 0; i < componentAmount; ++i) {
                decodeArray.addValue(new PdfNumber(1));
                decodeArray.addValue(new PdfNumber(0));
            }
            this.put(PdfNameValue.DECODE, decodeArray);
        }
    }
}
