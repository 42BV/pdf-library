package nl.mad.pdflibrary.pdf;

import java.awt.Font;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.mad.pdflibrary.document.AbstractDocumentPart;
import nl.mad.pdflibrary.document.Paragraph;
import nl.mad.pdflibrary.document.Text;
import nl.mad.pdflibrary.pdf.object.PdfDictionary;
import nl.mad.pdflibrary.pdf.object.PdfFont;
import nl.mad.pdflibrary.pdf.object.PdfIndirectObject;
import nl.mad.pdflibrary.pdf.object.PdfName;
import nl.mad.pdflibrary.pdf.object.PdfNameValue;
import nl.mad.pdflibrary.pdf.object.PdfObjectType;
import nl.mad.pdflibrary.pdf.object.PdfPage;
import nl.mad.pdflibrary.pdf.object.PdfStream;
import nl.mad.pdflibrary.pdf.object.PdfString;
import nl.mad.pdflibrary.pdf.object.PdfText;
import nl.mad.pdflibrary.pdf.utility.ByteEncoder;

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
    private static Map<Font, PdfIndirectObject> fontList = new HashMap<Font, PdfIndirectObject>();
    /**
     * The default line separator.
     */
    public static final byte[] LINE_SEPARATOR = ByteEncoder.getBytes(System.getProperty("line.separator"));
    private static final String CREATOR = "PDF-Library";

    /**
     * Creates a new instance of PdfDocument.
     * @throws UnsupportedEncodingException 
     */
    public PdfDocument() throws UnsupportedEncodingException {
        header = new PdfHeader();
        body = new PdfBody();
        xref = new PdfCrossReferenceTable();
        trailer = new PdfTrailer();
        writer = new PdfWriter();
    }

    /**
     * Creates a PdfObject from the given document part and adds it to the document.
     * @param part Document part that is to be added.
     * @see PdfObjectFactory
     */
    public void add(AbstractDocumentPart part) {
        switch (part.getType()) {
        case TEXT:
            this.addText((Text) part, false);
            break;
        case PARAGRAPH:
            Paragraph paragraph = (Paragraph) part;
            List<Text> textCollection = paragraph.getTextCollection();
            if (paragraph.getTextCollection().size() != 0) {
                //if we're using custom paragraph positioning, adjust the first text object
                if (paragraph.getCustomPositioning() && textCollection.get(0) != null) {
                    textCollection.get(0).setPositionX(paragraph.getPositionX());
                    textCollection.get(0).setPositionY(paragraph.getPositionY());
                }
                for (int i = 0; i < textCollection.size(); ++i) {
                    boolean ignorePosition = true;
                    //if we're not using custom paragraph positioning, try to use the first text object position
                    if (!paragraph.getCustomPositioning() && i == 0) {
                        ignorePosition = false;
                    }
                    this.addText(textCollection.get(i), ignorePosition);
                }
            }
            break;
        case FONT:
            //TODO: Implementation of direct font adding
            break;
        default:
            break;
        }
    }

    /**
     * Adds the given text object to the PdfTextStream of the current page.
     * @param text Text that needs to be added.
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
            //if we do not want to keep the nl position we avoid the position method
            PdfText pdfText = new PdfText();
            pdfText.addFont(PdfDocument.getPdfFont(text.getFont()), text.getFont().getSize());
            pdfText.addTextString(text.getText());
            currentPage.getCurrentStream().add(pdfText);
        }
    }

    /**
     * Creates an indirectObject for the given font and adds it to the font list.
     * @param font Font that needs to be added.
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
     * @param font Requested font.
     * @return PdfIndirectObject representing the font or null if the font is not in the list
     */
    public static PdfIndirectObject getPdfFont(Font font) {
        return fontList.get(font);
    }

    /**
     * Adds a new page and changes the current page.
     * @param width Width of page.
     * @param height Height of page.
     */
    public void addPage(int width, int height) {
        PdfPage page = new PdfPage(width, height);
        currentPage = (PdfPage) body.addPage(page).getObject();
    }

    /**
     * Adds the given document info to the PDF trailer.
     * @param author Writer of the document.
     * @param title Title of the document.
     * @param subject Subject of the document.
     * @param creationDate Creation date of the document.
     * @see PdfTrailer
     */
    public void addDocumentInfo(String author, String title, String subject, Calendar creationDate) {
        PdfDictionary trailerInfo = new PdfDictionary(PdfObjectType.DICTIONARY);
        PdfIndirectObject info = body.addObject(trailerInfo);

        if (author.length() > 0) trailerInfo.put(new PdfName(PdfNameValue.AUTHOR), new PdfString(author));
        if (title.length() > 0) trailerInfo.put(new PdfName(PdfNameValue.TITLE), new PdfString(title));
        if (subject.length() > 0) trailerInfo.put(new PdfName(PdfNameValue.SUBJECT), new PdfString(subject));
        PdfString dateString = new PdfString();
        dateString.setString(creationDate);
        if (creationDate != null) trailerInfo.put(new PdfName(PdfNameValue.CREATION_DATE), dateString);
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
