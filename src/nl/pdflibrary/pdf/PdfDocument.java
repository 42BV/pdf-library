package nl.pdflibrary.pdf;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import nl.pdflibrary.document.AbstractDocumentPart;
import nl.pdflibrary.document.Paragraph;
import nl.pdflibrary.document.Text;
import nl.pdflibrary.pdf.factory.PdfObjectFactory;
import nl.pdflibrary.pdf.object.PdfFont;
import nl.pdflibrary.pdf.object.PdfIndirectObject;
import nl.pdflibrary.pdf.object.PdfPage;
import nl.pdflibrary.pdf.object.PdfTextStream;


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
    public static final byte[] LINE_SEPARATOR = System.lineSeparator().getBytes();

    /**
     * Creates a new instance of PdfDocument
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
     * Passes the given document part over to the PdfObjectFactory and adds the resulting PdfObject to the document
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
            for (int i = 0; i < textCollection.size(); ++i) {
                boolean ignorePosition = true;
                if (i == 0) {
                    ignorePosition = false;
                }
                this.addText(textCollection.get(i), ignorePosition);
            }
            break;
        default:
            break;
        }
    }

    private void addText(Text text, boolean overridePosition) {
        //TODO: DIT KAN NETTER, OOK WERKT JE FONT LIMIET DINGES TOEVOEGEN HIERO NIET
        //OOK MOET PAGE ANDERE FONTS DENK IK AAN DEZELFDE RESOURCES DICT GEVEN, NIET APARTE /Font DICS in DE RESOURCES DICT
        PdfIndirectObject font = this.addFont(text.getFont());
        currentPage.add(font);
        if (currentPage.streamEmpty()) {
            PdfTextStream ts = PdfObjectFactory.createTextStream(text);
            currentPage.add(body.addObject(ts));
        } else if (!overridePosition) {
            currentPage.getCurrentStream().addCommands(text);
        } else {
            PdfTextStream ts = (PdfTextStream) currentPage.getCurrentStream();
            ts.addCommand(PdfDocument.getPdfFont(text.getFont()), text.getFont().getSize());
            ts.addCommand(text.getText());
        }
    }

    /**
     * Creates an indirectObject for the given font and adds it to the font list
     * @param font
     * @return indirect object for the given font
     */
    public PdfIndirectObject addFont(Font font) {
        if (!fontList.containsKey(font) && font != null) {
            PdfFont newFont = PdfObjectFactory.createPdfFont(font);
            PdfIndirectObject indirectFont = body.addObject(newFont);
            fontList.put(font, indirectFont);
            return indirectFont;
        } else {
            return fontList.get(font);
        }
    }

    /**
     * Returns the indirect object representing the given font
     * @param font
     * @return PdfIndirectObject representing the font or null if the font is not in the list
     */
    public static PdfIndirectObject getPdfFont(Font font) {
        return fontList.get(font);
    }

    /**
     * Adds a new page and changes the current page
     * @param width
     * @param height
     */
    public void addPage(int width, int height) {
        PdfPage page = PdfObjectFactory.createPdfPage(width, height);
        currentPage = (PdfPage) body.addPage(page).getObject();
    }

    /**
     * Make the writer start writing the document
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
