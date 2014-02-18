package nl.pdflibrary.document;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;

import nl.pdflibrary.pdf.PdfDocument;


/**
 * Document is the class responsible for collecting all the document parts and passing them on to the PdfDocument.
 * It also stores properties of the document such as page width/height and the author.
 * @author Dylan de Wolff
 */
public class Document {
    private String author;
    private String title;
    private PdfDocument pdfDocument;
    public static final Font DEFAULT_FONT = new Font(Font.SERIF, Font.PLAIN, 12);
    private int pageNumber = 0;
    private final static int A4_WIDTH = 795;
    private final static int A4_HEIGHT = 842;
    private int width = A4_WIDTH;
    private int height = A4_HEIGHT;
    private ArrayList<AbstractDocumentPart> content;
    private ArrayList<Font> fonts;
    private boolean finished;

    /**
     * Creates a new document object.
     */
    public Document() {
        pdfDocument = new PdfDocument();
        content = new ArrayList<AbstractDocumentPart>();
        fonts = new ArrayList<Font>();
        finished = false;
        this.width = A4_WIDTH;
        this.height = A4_HEIGHT;
        addFont(DEFAULT_FONT);
        addNewPage();
    }

    public Document(int width, int height) {
        pdfDocument = new PdfDocument();
        content = new ArrayList<AbstractDocumentPart>();
        fonts = new ArrayList<Font>();
        finished = false;
        addFont(DEFAULT_FONT);
        this.width = width;
        this.height = height;
        addNewPage();
    }

    public void addFont(Font font) {
        if (!finished && !this.fonts.contains(font)) {
            fonts.add(font);
        }
    }

    /**
     * Adds a new part to the document.
     * 
     * @param part New document part to be added to the document
     * @see AbstractDocumentPart
     */
    public void addPart(AbstractDocumentPart part) {
        if (!finished) {
            if (part.getType().equals(DocumentPartType.TEXT)) {
                Text text = (Text) part;
                this.addFont(text.getFont());
            }
            if (part.getType().equals(DocumentPartType.PARAGRAPH)) {
                for (Text text : ((Paragraph) (part)).getTextCollection()) {
                    this.addFont(text.getFont());
                }
            }
            this.content.add(part);
            pdfDocument.add(part);
        }
    }

    /**
     * Adds a new page to the document with the default document width and height.
     */
    public void addNewPage() {
        if (!finished) {
            pdfDocument.addPage(width, height);
            ++this.pageNumber;
        }
    }

    /**
     * Adds a new page to the document with the given width and height
     * @param width
     * @param height
     */
    public void addNewPage(int width, int height) {
        if (!finished) {
            pdfDocument.addPage(width, height);
            ++this.pageNumber;
        }
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public ArrayList<AbstractDocumentPart> getDocumentParts() {
        return this.content;
    }

    public ArrayList<Font> getFonts() {
        return this.fonts;
    }

    /**
     * Prints the document to a pdf file and no longer allows editing of the document.
     * 
     * @throws IOException 
     */
    public void finish() throws IOException {
        if (!finished) {
            this.pdfDocument.finish();
            finished = true;
        }
    }

    public boolean getFinished() {
        return this.finished;
    }

}
