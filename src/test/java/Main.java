import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import nl.mad.pdflibrary.api.BaseFont;
import nl.mad.pdflibrary.api.BaseParagraph;
import nl.mad.pdflibrary.api.BaseText;
import nl.mad.pdflibrary.api.Document;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontFamilyType;
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
        ultimateDocumentCreation();
    }

    private static void ultimateDocumentCreation() {
        //create new document
        DocumentBuilder builder = new DocumentBuilder();

        //set metadata
        builder.getDocument().title("Simple Document Example").writtenBy("Example Author").on(Calendar.getInstance()).about("Toucan-PDF").filename("document");

        //add font to the document and store it for the upcoming text
        Font font = builder.addFont().family(FontFamilyType.HELVETICA).boldItalic();

        //add text to the document
        builder.addText("Example text").size(11).font(font);

        //create another font
        Font secondFont = builder.addFont().family(FontFamilyType.TIMES_ROMAN).bold();

        //add paragraph to the document and add text objects to the paragraph
        builder.addParagraph().on(10, 10).addText(builder.createText().text("This stays").font(secondFont)).addText(builder.createText("together"));

        //finish up the document by creating a PDF file
        builder.finish();
    }

    private static void ultimateDocumentCreationSkippingDocumentBuilder() {
        //create a new document
        Document document = new Document();

        //creates a new font instance, the Base* indicates this is the basic implementation of the corresponding interface.
        //In this case, the font interface.
        Font font = new BaseFont(FontFamilyType.HELVETICA, FontStyle.BOLD);

        //create a new text instance.
        Text text = new BaseText("Example text").on(10, 10).size(11).font(font);

        //add the text instance to the document
        document.addPart(text);

        //create a new paragraph instance
        Paragraph paragraph = new BaseParagraph();

        //add text to the paragraph
        paragraph.addText(new BaseText("Paragraph example text").size(11));

        //add the paragraph to the document
        document.addPart(paragraph);

        //finish the document
        document.finish();
        //or pass along your own OutputStream
        OutputStream baos = new ByteArrayOutputStream();
        document.finish(baos);
    }
}
