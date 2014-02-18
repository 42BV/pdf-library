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
        Document d = new Document();
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        Text t = new Text("Hello, Batman!!", f, 0, 0);
        f = new Font(Font.SERIF, Font.BOLD, 36);
        Text t2 = new Text("Good day Robin.", f, 0, 0);
        Text t3 = new Text("Good day Robby.", f, 30, 30);
        Paragraph p = new Paragraph(50, 200);

        p.addText(t);
        p.addText(t2);
        p.addText(t3);
        d.addPart(p);
        d.addNewPage();
        d.addPart(t3);
        d.finish();
    }
}
