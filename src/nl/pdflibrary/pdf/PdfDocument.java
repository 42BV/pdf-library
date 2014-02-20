package nl.pdflibrary.pdf;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import nl.pdflibrary.document.AbstractDocumentPart;
import nl.pdflibrary.document.Paragraph;
import nl.pdflibrary.document.Text;
import nl.pdflibrary.pdf.object.PdfDictionary;
import nl.pdflibrary.pdf.object.PdfFont;
import nl.pdflibrary.pdf.object.PdfIndirectObject;
import nl.pdflibrary.pdf.object.PdfName;
import nl.pdflibrary.pdf.object.PdfNameValue;
import nl.pdflibrary.pdf.object.PdfObjectType;
import nl.pdflibrary.pdf.object.PdfPage;
import nl.pdflibrary.pdf.object.PdfStream;
import nl.pdflibrary.pdf.object.PdfString;
import nl.pdflibrary.pdf.object.PdfText;

/**
 * Represents the PDF document itself, containing the four different sections of a PDF document.
 * This class is responsible for the creation of the four sections (header, body, cross reference table and trailer),
 * passing along new document parts and storing the used fonts.
 * 
 * @author Dylan de Wolff
 */
public class PdfDocument {
    private PdfHeader header;
    private PdfBody body;
    private PdfCrossReferenceTable xref;
    private PdfTrailer trailer;
    private PdfWriter writer;
    private PdfPage currentPage;
    private static HashMap<Font, PdfIndirectObject> fontList;
    /**
     * The default line separator.
     */
    public static final byte[] LINE_SEPARATOR = System.lineSeparator().getBytes();
    private static final String CREATOR = "PDF-Library";

    /**
     * Creates a new instance of PdfDocument.
     */
    public PdfDocument() {
        header = new PdfHeader();
        body = new PdfBody();
        xref = new PdfCrossReferenceTable();
        trailer = new PdfTrailer();
        writer = new PdfWriter();
        fontList = new HashMap<Font, PdfIndirectObject>();
    }

    /**
     * Passes the given document part over to the PdfObjectFactory and adds the resulting PdfObject to the document.
     * @param part Document part that is to be added
     * @see PdfObjectFactory
     */
    public void add(AbstractDocumentPart part) {
        switch (part.getType()) {
        case TEXT:
            this.addText((Text) part, false);
            break;
        case PARAGRAPH:
            Paragraph paragraph = (Paragraph) part;
            ArrayList<Text> textCollection = paragraph.getTextCollection();
            if (paragraph.getTextCollection().size() != 0) {
                //if we're using custom paragraph positioning, adjust the first text object
                if (paragraph.getCustomPositioning() && textCollection.get(0) != null) {
                    textCollection.get(0).setPositionX(paragraph.getPositionX());
                    textCollection.get(0).setPositionY(paragraph.getPositionY());
                }
                for (int i = 0; i < textCollection.size(); ++i) {
                    boolean ignorePosition = true;
                    //if we're not using custom paragraph positioning, try to use the first text object position
                    if (!paragraph.getCustomPositioning()) {
                        if (i == 0) {
                            ignorePosition = false;
                        }
                    }
                    this.addText(textCollection.get(i), ignorePosition);
                }
            }
            break;
        default:
            break;
        }
    }

    /**
     * Adds the given text object to the PdfTextStream of the current page.
     * @param text
     * @param overridePosition if true the position of the text will be disregarded. When using a paragraph, this should be true 
     * for every text object besides the first one.
     */
    private void addText(Text text, boolean overridePosition) {
        PdfIndirectObject font = this.addFont(text.getFont());
        currentPage.add(font);
        //if the page has no content yet
        if (currentPage.streamEmpty()) {
            //create new stream object and add the text
            PdfStream ts = new PdfStream();
            ts.add(new PdfText(text));
            currentPage.add(body.addObject(ts));
        } else if (!overridePosition) {
            //if we want to keep the text position, we simply convert the text object
            currentPage.getCurrentStream().add(new PdfText(text));
        } else {
            //if we do not want to keep the test position we avoid the position method
            PdfText pdfText = new PdfText();
            pdfText.addFont(PdfDocument.getPdfFont(text.getFont()), text.getFont().getSize());
            pdfText.addText(text.getText());
            currentPage.getCurrentStream().add(pdfText);
        }
    }

    /**
     * Creates an indirectObject for the given font and adds it to the font list.
     * @param font 
     * @return indirect object for the given font
     */
    public PdfIndirectObject addFont(Font font) {
        if (!fontList.containsKey(font) && font != null) {
            PdfFont newFont = new PdfFont(font);
            PdfIndirectObject indirectFont = body.addObject(newFont);
            fontList.put(font, indirectFont);
            return indirectFont;
        } else {
            return fontList.get(font);
        }
    }

    /**
     * Returns the indirect object representing the given font.
     * @param font 
     * @return PdfIndirectObject representing the font or null if the font is not in the list
     */
    public static PdfIndirectObject getPdfFont(Font font) {
        return fontList.get(font);
    }

    /**
     * Adds a new page and changes the current page.
     * @param width 
     * @param height 
     */
    public void addPage(int width, int height) {
        PdfPage page = new PdfPage(width, height);
        currentPage = (PdfPage) body.addPage(page).getObject();
    }

    /**
     * Adds the given document info to the PDF trailer.
     * @param author Writer of the document
     * @param title Title of the document
     * @param subject Subject of the document
     * @param creationDate Creation date of the document
     * @see PdfTrailer
     */
    public void addDocumentInfo(String author, String title, String subject, Calendar creationDate) {
        PdfDictionary trailerInfo = new PdfDictionary(PdfObjectType.DICTIONARY);
        PdfIndirectObject info = body.addObject(trailerInfo);

        if (author.length() > 0) trailerInfo.put(new PdfName(PdfNameValue.AUTHOR), new PdfString(author));
        if (title.length() > 0) trailerInfo.put(new PdfName(PdfNameValue.TITLE), new PdfString(title));
        if (subject.length() > 0) trailerInfo.put(new PdfName(PdfNameValue.SUBJECT), new PdfString(subject));
        if (creationDate != null) trailerInfo.put(new PdfName(PdfNameValue.CREATION_DATE), new PdfString(creationDate));
        trailerInfo.put(new PdfName(PdfNameValue.CREATOR), new PdfString(CREATOR));
        trailer.setInfo(info);
    }

    /**
     * Make the writer start writing the document.
     * 
     * @throws IOException 
     */
    public void finish() throws IOException {
        this.writer.write(header, body, xref, trailer);
    }

    public PdfPage getCurrentPage() {
        return this.currentPage;
    }
}
