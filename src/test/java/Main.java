import java.io.FileOutputStream;
import java.io.IOException;

import nl.mad.pdflibrary.api.BaseFont;
import nl.mad.pdflibrary.api.BaseParagraph;
import nl.mad.pdflibrary.api.BaseText;
import nl.mad.pdflibrary.api.Document;
import nl.mad.pdflibrary.model.Font;
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
        Document document = new Document();
        Font f = document.createFont(FontFamily.HELVETICA, FontStyle.NORMAL);
        Text t = document.createText("Test Document", 26);
        Paragraph p = document.createParagraph();
        t.setFont(f);
        document.addPart(t);

        t = document.createText("Test Document Two", 11);
        Text t2 = document.createText("Test Document Three", 13);
        p = document.createParagraph();
        p.addText(t);
        p.addText(t2);
        document.addPart(p);
        document.finish("easyDocument");
    }

    private static void inDepthDocumentCreation() throws IOException {
        Document d = new Document(Document.A4_WIDTH, Document.A4_HEIGHT, "D. de Wolff", "Test Document", "Nothing really.");
        BaseFont f = new BaseFont(FontFamily.TIMES_ROMAN, FontStyle.NORMAL);
        BaseText t = new BaseText("Test Document", 42, f, 70, 500);
        BaseText t2 = new BaseText("Test Document Twoooooooooooooooooooo", 11, f);
        BaseText t5 = new BaseText(
                "JAnJanJan JanJan JanJanJanJanJAnJanJanJanJanJanJanJanJan JAnJ an Jan Jan JanJ anJ anJa nJa JAn Jan Jan Jan Jan Jan Jan Jan Jan", 16, f);
        f = new BaseFont(FontFamily.HELVETICA, FontStyle.ITALIC);
        BaseText t3 = new BaseText("Written by test", 30, f, 100, 200);
        BaseText t4 = new BaseText("Page 2 Test", 30, new BaseFont(FontFamily.COURIER, FontStyle.BOLDITALIC), 25, 700);

        BaseParagraph p = new BaseParagraph();
        BaseParagraph p2 = new BaseParagraph(300, 200);

        p.addText(t);
        p.addText(t2);
        p.addText(t5);
        p2.addText(t3);
        p2.addText(t2);

        d.addPart(p);
        d.addPart(p2);
        d.addNewPage();
        d.addPart(t4);

        d.finish(new FileOutputStream("inDepthDocument.pdf"));

    }
}
