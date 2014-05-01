package nl.mad.toucanpdf.pdf.syntax;

import nl.mad.toucanpdf.image.ImageParser;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.PdfNameValue;

public class PdfImageDictionary extends PdfXObject {
    private Image image;

    public PdfImageDictionary(Image part) {
        image = part;
        ImageParser parser = image.getImageParser();
        this.put(PdfNameValue.SUB_TYPE, PdfNameValue.IMAGE);
        this.put(PdfNameValue.WIDTH, new PdfNumber(parser.getWidth()));
        this.put(PdfNameValue.HEIGHT, new PdfNumber(parser.getHeight()));
        this.put(PdfNameValue.COLOR_SPACE, parser.getColorSpace().getPdfName());
        this.put(PdfNameValue.BITS_PER_COMPONENT, new PdfNumber(parser.getBitsPerComponent()));
        this.addFilter(new PdfName(parser.getFilter().getPdfName()));
        this.add(new PdfFile(parser.getData()));
    }

}
