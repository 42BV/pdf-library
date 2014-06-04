package nl.mad.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import nl.mad.toucanpdf.model.ColorSpace;
import nl.mad.toucanpdf.model.Compression;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.ImageParser;
import nl.mad.toucanpdf.model.PdfNameValue;
import nl.mad.toucanpdf.pdf.syntax.PdfArray;
import nl.mad.toucanpdf.pdf.syntax.PdfFile;
import nl.mad.toucanpdf.pdf.syntax.PdfImageDictionary;
import nl.mad.toucanpdf.pdf.syntax.PdfName;
import nl.mad.toucanpdf.pdf.syntax.PdfNumber;

import org.junit.Before;
import org.junit.Test;

public class PdfImageDictionaryTest {

    @Test
    public void testCreation(@Mocked final Image image, @Mocked final ImageParser parser) {
        new NonStrictExpectations() {
            {
                image.getImageParser();
                returns(parser);

                parser.getWidth();
                returns(100);

                parser.getHeight();
                returns(110);

                parser.getColorSpace();
                returns(ColorSpace.CAL_RGB);

                parser.getBitsPerComponent();
                returns(3);

                parser.getFilter();
                returns(Compression.DCT);

                image.getCompressionMethod();
                returns(Compression.FLATE);

                parser.getData();
                returns(new byte[0]);
            }
        };

        PdfImageDictionary img = new PdfImageDictionary(image);
        assertEquals(new PdfNumber(100), img.get(PdfNameValue.WIDTH));
        assertEquals(new PdfNumber(110), img.get(PdfNameValue.HEIGHT));
        assertEquals(new PdfName(ColorSpace.CAL_RGB.getPdfName()), img.get(PdfNameValue.COLOR_SPACE));
        assertEquals(new PdfNumber(3), img.get(PdfNameValue.BITS_PER_COMPONENT));
        assertEquals(2, ((PdfArray) img.get(PdfNameValue.FILTER)).getSize());
        assertEquals(1, img.getContentSize());
    }
}
