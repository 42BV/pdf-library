package nl.mad.toucanpdf.pdf.syntax;

import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.ImageParser;
import nl.mad.toucanpdf.model.PdfNameValue;

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
            this.addFilter(parser.getFilter());
            this.addFilter(image.getCompressionMethod());
            this.add(new PdfFile(parser.getData()));
        }
    }
}
