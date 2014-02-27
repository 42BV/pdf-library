package nl.mad.pdflibrary.structure;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.PdfNameValue;
import nl.mad.pdflibrary.model.Text;
import nl.mad.pdflibrary.syntax.PdfDictionary;
import nl.mad.pdflibrary.syntax.PdfFont;
import nl.mad.pdflibrary.syntax.PdfIndirectObject;
import nl.mad.pdflibrary.syntax.PdfName;
import nl.mad.pdflibrary.syntax.PdfObjectType;
import nl.mad.pdflibrary.syntax.PdfPage;
import nl.mad.pdflibrary.syntax.PdfStream;
import nl.mad.pdflibrary.syntax.PdfString;
import nl.mad.pdflibrary.syntax.PdfText;
import nl.mad.pdflibrary.utility.ByteEncoder;

/**
 * Represents the PDF api itself, containing the four different sections of a PDF api.
 * This class is responsible for the creation of the four sections (header, body, cross reference table and trailer),
 * passing along new api parts and storing the used fonts.
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
    private Map<Font, PdfIndirectObject> fontList = new HashMap<>();

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
     * Creates a PdfObject from the given api part and adds it to the api.
     * @param part Document part that is to be added.
     */
    public void add(DocumentPart part) {
        switch (part.getType()) {
        case TEXT:
            this.addText((Text) part, false, false);
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
                    boolean ignoreMatrix = true;
                    boolean ignorePosition = true;
                    //if we're not using custom paragraph positioning, try to use the first text object position
                    //TODO: We should have some kind of position calculation!
                    if (i == 0) {
                        ignoreMatrix = false;
                        ignorePosition = false;
                    } else if (!textCollection.get(i).textMatrixEquals(textCollection.get(i - 1))) {
                        ignoreMatrix = false;
                    }
                    this.addText(textCollection.get(i), ignoreMatrix, ignorePosition);
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
     * @param overrideMatrix if true the matrix of the text will be disregarded. This should be true when the new text object has the same
     * matrix as the text object before it.
     * @param ignorePosition if true the position of the text will be disregarded. This should be true when using a paragraph for every text
     * object besides the first one. This ensures that all text is placed together.
     */
    private void addText(Text text, boolean overrideMatrix, boolean ignorePosition) {
        PdfIndirectObject font = this.addFont(text.getFont());
        currentPage.add(font);
        //if the page has no content yet
        if (currentPage.streamEmpty()) {
            //create new stream object and add the text
            PdfStream ts = new PdfStream();
            ts.add(new PdfText(text, getPdfFont(text.getFont())));
            currentPage.add(body.addObject(ts));
        } else {
            PdfText pdfText = new PdfText();
            if (!overrideMatrix) {
                pdfText.addMatrix(text);
            }
            pdfText.addFont(getPdfFont(text.getFont()), text.getTextSize());
            pdfText.addTextString(text.getText());
            currentPage.getCurrentStream().add(pdfText);
        }

        //        else if (!overrideMatrix) {
        //            //if we want to use the matrix from the new text object
        //            currentPage.getCurrentStream().add(new PdfText(text));
        //        } else {
        //            //if we want to reuse the matrix from the last text object, we simply convert the new text object without setting it
        //            PdfText pdfText = new PdfText();
        //            pdfText.addFont(PdfDocument.getPdfFont(text.getFont()), text.getTextSize());
        //            pdfText.addTextString(text.getText());
        //            currentPage.getCurrentStream().add(pdfText);
        //        }
    }

    /**
     * Returns the indirect object representing the given font.
     * @param font Requested font.
     * @return PdfIndirectObject representing the font or null if the font is not in the list
     */
    public PdfIndirectObject getPdfFont(Font font) {
        return fontList.get(font);
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
     * Make the writer start writing the document to the given OutputStream.
     * 
     * @add OutputStream OutputStream to write to.
     * @throws IOException 
     */
    public void finish(OutputStream os) throws IOException {
        this.writer.write(os, header, body, xref, trailer);
    }

    public PdfPage getCurrentPage() {
        return this.currentPage;
    }

    public void addNewLine() {
        //No implementation yet
        if (currentPage.streamEmpty()) {

        } else {

        }
    }
}
