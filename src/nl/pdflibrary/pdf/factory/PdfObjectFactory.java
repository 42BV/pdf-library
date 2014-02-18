package nl.pdflibrary.pdf.factory;

import java.awt.Font;

import nl.pdflibrary.document.Text;
import nl.pdflibrary.pdf.object.PdfFont;
import nl.pdflibrary.pdf.object.PdfPage;
import nl.pdflibrary.pdf.object.PdfTextStream;


public class PdfObjectFactory {

    public static PdfTextStream createTextStream(Text text) {
        return new PdfTextStream(text);
    }

    public static PdfFont createPdfFont(Font font) {
        return new PdfFont(font);
    }

    public static PdfPage createPdfPage(int width, int height) {
        return new PdfPage(width, height);
    }
}
