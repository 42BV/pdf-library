import java.io.IOException;

import nl.mad.pdflibrary.api.*;
import nl.mad.pdflibrary.api.AbstractFont;
import nl.mad.pdflibrary.model.FontFamily;
import nl.mad.pdflibrary.model.FontStyle;

public class Main {

    /**
     * This is only for testing purposes
     * @param args 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        //easyDocumentCreation();
        inDepthDocumentCreation();
    }

    private static void easyDocumentCreation() throws IOException {
        Document d = new Document();

        AbstractFont f = new AbstractFont(FontFamily.COURIER, FontStyle.BOLD);
        AbstractText t = new AbstractText("Hello, Batman!!", 12, f);
        AbstractText t2 = new AbstractText("Good day Robin.", 8);
        AbstractText t3 = new AbstractText("Good day Robby.", 46, f);

        AbstractParagraph p = new AbstractParagraph();
        d.addPart(t);

        p.addText(t);
        p.addText(t2);
        p.addText(t3);

        d.addPart(p);
        d.addNewPage();
        d.addPart(t3);
        d.finish();
    }

    private static void inDepthDocumentCreation() throws IOException {
        Document d = new Document(Document.A4_WIDTH, Document.A4_HEIGHT, "D. de Wolff", "Test Document", "Nothing really.");
        AbstractFont f = new AbstractFont(FontFamily.COURIER, FontStyle.BOLD);
        AbstractText t = new AbstractText("The book of Batman", 42, f, 150, 500);
        AbstractText t2 = new AbstractText("..and Robin..", 11, f);
        f = new AbstractFont(FontFamily.HELVETICA, FontStyle.ITALIC);
        AbstractText t3 = new AbstractText("Written by Batman", 30, f, 600, 200, 1, 3, 0, 0);
        AbstractText t4 = new AbstractText("Tom is dom", 120, new AbstractFont(FontFamily.COURIER, FontStyle.BOLDITALIC), 25, 700);

        AbstractParagraph p = new AbstractParagraph();
        AbstractParagraph p2 = new AbstractParagraph(450, 200);

        p.addText(t);
        p.addText(t2);
        p2.addText(t3);
        p2.addText(t2);

        d.addPart(p);
        d.addPart(p2);

        d.addNewPage();
        d.addPart(t4);

        d.finish();

    }
}
