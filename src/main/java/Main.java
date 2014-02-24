import java.io.IOException;

import nl.mad.pdflibrary.api.Document;
import nl.mad.pdflibrary.api.Font;
import nl.mad.pdflibrary.api.FontFamily;
import nl.mad.pdflibrary.api.FontStyle;
import nl.mad.pdflibrary.api.Paragraph;
import nl.mad.pdflibrary.api.Text;

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

        Font f = new Font(FontFamily.COURIER, FontStyle.BOLD);
        Text t = new Text("Hello, Batman!!", 12, f);
        Text t2 = new Text("Good day Robin.", 8);
        Text t3 = new Text("Good day Robby.", 46, f);

        Paragraph p = new Paragraph();
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
        Font f = new Font(FontFamily.COURIER, FontStyle.BOLD);
        Text t = new Text("The book of Batman", 42, f, 150, 500);
        Text t2 = new Text("..and Robin..", 11, f);
        f = new Font(FontFamily.HELVETICA, FontStyle.ITALIC);
        Text t3 = new Text("Written by Batman", 30, f, 600, 200, 1, 3, 0, 0);
        Text t4 = new Text("Tom is dom", 120, new Font(FontFamily.COURIER, FontStyle.BOLDITALIC), 25, 700);

        Paragraph p = new Paragraph();
        Paragraph p2 = new Paragraph(450, 200);

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
