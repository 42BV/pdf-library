package nl.pdflibrary.document;

import java.awt.Font;
import java.io.IOException;
import java.util.Calendar;

import nl.pdflibrary.pdf.PdfDocument;

/**
 * Document is the class responsible for passing document parts on to the PdfDocument.
 * It also stores properties of the document such as page width/height and the author.
 * @author Dylan de Wolff
 */
public class Document {
    private String author;
    private String title;
    private String subject;
    private PdfDocument pdfDocument;
    /**
     * Default font. This is used when no font is specified.
     */
    public static final Font DEFAULT_FONT = new Font(Font.SERIF, Font.PLAIN, 12);
    private int pageNumber = 0;
    /**
     * A4 page width.
     */
    public static final int A4_WIDTH = 795;
    /**
     * A4 page height.
     */
    public static final int A4_HEIGHT = 842;
    private int width = A4_WIDTH;
    private int height = A4_HEIGHT;
    private boolean finished;

    /**
     * Creates a new instance of Document.
     */
    public Document() {
        this(A4_WIDTH, A4_HEIGHT, "", "", "");
    }

    /**
     * Creates a new instance of Document.
     * @param width Default page width for this document
     * @param height Default page height for this document
     * @param author Writer of this document
     * @param title Title of this document
     * @param subject Subject of this document
     */
    public Document(int width, int height, String author, String title, String subject) {
        pdfDocument = new PdfDocument();
        finished = false;
        this.width = width;
        this.height = height;
        this.author = author;
        this.title = title;
        this.subject = subject;
        addNewPage();
    }

    /**
     * Adds a new part to the document.
     * 
     * @param part New document part to be added to the document
     * @see AbstractDocumentPart
     */
    public void addPart(AbstractDocumentPart part) {
        if (!finished) {
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
     * Adds a new page to the document with the given width and height.
     * @param pageWidth 
     * @param pageHeight 
     */
    public void addNewPage(int pageWidth, int pageHeight) {
        if (!finished) {
            pdfDocument.addPage(pageWidth, pageHeight);
            ++this.pageNumber;
        }
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    /**
     * Prints the document to a PDF file and no longer allows editing of the document. 
     * @throws IOException 
     */
    public void finish() throws IOException {
        if (!finished) {
            this.pdfDocument.addDocumentInfo(author, title, subject, Calendar.getInstance());
            this.pdfDocument.finish();
            finished = true;
        }
    }

    public boolean getFinished() {
        return this.finished;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
