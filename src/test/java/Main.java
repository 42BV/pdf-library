import java.io.IOException;
import java.util.Calendar;

import nl.mad.pdflibrary.api.DocumentBuilder;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontFamilyType;

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
        builder.title("Simple Document Example").writtenBy("Example Author").on(Calendar.getInstance()).about("Toucan-PDF").filename("document");

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
}
