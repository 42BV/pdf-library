import java.io.FileOutputStream;
import java.io.IOException;

import nl.mad.pdflibrary.api.BaseFont;
import nl.mad.pdflibrary.api.BaseParagraph;
import nl.mad.pdflibrary.api.BaseText;
import nl.mad.pdflibrary.api.Document;
import nl.mad.pdflibrary.model.FontFamily;
import nl.mad.pdflibrary.model.FontStyle;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Text;

public class Main {

    /**
     * This is only for testing purposes
     * @param args 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        easyDocumentCreation();
        inDepthDocumentCreation();
    }

    private static void easyDocumentCreation() throws IOException {
        Document d = new Document();
        Text t = d.createText("Test text", 26);
        d.addPart(t);
        t = d.createText("Test paragraph.", 16);
        Text t2 = d.createText("Follow up to test paragraph.", 12);
        Paragraph p = d.createParagraph();
        p.addText(t);
        p.addText(t2);
        d.addPart(p);
        d.finish("easyDocument");
    }

    private static void inDepthDocumentCreation() throws IOException {
        Document d = new Document(Document.A4_WIDTH, Document.A4_HEIGHT, "D. de Wolff", "Test Document", "Nothing really.");
        BaseFont f = new BaseFont(FontFamily.COURIER, FontStyle.BOLD);
        BaseText t = new BaseText("Test Document", 42, f, 70, 500);
        BaseText t2 = new BaseText("Test Document Two", 11, f);
        f = new BaseFont(FontFamily.HELVETICA, FontStyle.ITALIC);
        BaseText t3 = new BaseText("Written by test", 30, f, 100, 200, 1, 3, 0, 0);
        BaseText t4 = new BaseText("Page 2 Test", 120, new BaseFont(FontFamily.COURIER, FontStyle.BOLDITALIC), 25, 700);

        BaseParagraph p = new BaseParagraph();
        BaseParagraph p2 = new BaseParagraph(300, 200);

        p.addText(t);
        p.addText(t2);
        p2.addText(t3);
        p2.addText(t2);

        d.addPart(p);
        d.addPart(p2);
        d.addNewPage();
        d.addPart(t4);

        d.finish(new FileOutputStream("inDepthDocument.pdf"));

    }
}
