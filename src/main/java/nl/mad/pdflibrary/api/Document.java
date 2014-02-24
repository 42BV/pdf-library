package nl.mad.pdflibrary.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import nl.mad.pdflibrary.model.FontFamily;
import nl.mad.pdflibrary.model.FontStyle;
import nl.mad.pdflibrary.structure.PdfDocument;

/**
 * Document is the class responsible for passing api parts on to the PdfDocument.
 * It also stores properties of the api such as page width/height and the author.
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
    public static final AbstractFont DEFAULT_FONT = new AbstractFont(FontFamily.TIMES_ROMAN, FontStyle.NORMAL);
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
     * @throws UnsupportedEncodingException 
     */
    public Document() throws UnsupportedEncodingException {
        this(A4_WIDTH, A4_HEIGHT, "", "", "");
    }

    /**
     * Creates a new instance of Document.
     * @param width Default page width for this api
     * @param height Default page height for this api
     * @param author Writer of this api
     * @param title Title of this api
     * @param subject Subject of this api
     * @throws UnsupportedEncodingException 
     */
    public Document(int width, int height, String author, String title, String subject) throws UnsupportedEncodingException {
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
     * Adds a new part to the api.
     * 
     * @param part New api part to be added to the api.
     * @see AbstractDocumentPart Part to add.
     */
    public void addPart(AbstractDocumentPart part) {
        if (!finished) {
            pdfDocument.add(part);
        }
    }

    /**
     * Adds a new page to the api with the default api width and height.
     */
    public void addNewPage() {
        if (!finished) {
            pdfDocument.addPage(width, height);
            ++this.pageNumber;
        }
    }

    /**
     * Adds a new page to the api with the given width and height.
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
     * Prints the api to a PDF file and no longer allows editing of the api.
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
