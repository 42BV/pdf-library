import java.awt.Font;
import java.io.IOException;

import nl.pdflibrary.document.Document;
import nl.pdflibrary.document.Paragraph;
import nl.pdflibrary.document.Text;

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
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 16);

        Text t = new Text("Hello, Batman!!", f);
        Text t2 = new Text("Good day Robin.");
        Text t3 = new Text("Good day Robby.", f);

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
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        Text t = new Text("Hello, Batman!", f);
        Text t2 = new Text("Dudududu", f, 600, 200);
        Paragraph p = new Paragraph(200, 500);

        p.addText(t);
        p.addText(t2);
        d.addPart(t);
        d.finish();

    }
}
