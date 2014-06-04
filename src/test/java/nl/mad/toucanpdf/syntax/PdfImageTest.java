package nl.mad.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.pdf.syntax.PdfImage;
import nl.mad.toucanpdf.utility.ByteEncoder;

public class PdfImageTest {

    @Test
    public void testCreation(@Mocked final Image image) throws IOException {
        new NonStrictExpectations() {
            {
                image.getPosition();
                returns(new Position(100, 100));

                image.getHeight();
                returns(10.0);

                image.getWidth();
                returns(20.0);
            }
        };
        PdfImage img = new PdfImage("2 0 R", image);
        String expectedResult = "q\n" + "20.0 0 0 10.0 100.0 90.0 cm\n" + "/2 0 R Do\n" + "Q\n";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.writeToFile(baos);
        assertEquals(expectedResult, ByteEncoder.getString(baos));
    }

}
