package nl.mad.pdflibrary.api;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontFamily;
import nl.mad.pdflibrary.model.FontStyle;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Text;
import nl.mad.pdflibrary.structure.PdfDocument;

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
    public static final BaseFont DEFAULT_FONT = new BaseFont(FontFamily.TIMES_ROMAN, FontStyle.NORMAL);
    private static final String DEFAULT_FILE_NAME = "document.pdf";
    private int pageNumber = 0;
    /**
     * A4 page width.
     */
    public static final int A4_WIDTH = 595;
    /**
     * A4 page height.
     */
    public static final int A4_HEIGHT = 842;
    private boolean finished;

    /**
     * Creates a new instance of Document.
     * @throws UnsupportedEncodingException 
     */
    public Document() throws UnsupportedEncodingException {
        this(A4_WIDTH, A4_HEIGHT, "", "", "");
    }

    public Document(int width, int height) throws UnsupportedEncodingException {
        this(width, height, "", "", "");
    }

    /**
     * Creates a new instance of Document.
     * @param width Default page width for this document
     * @param height Default page height for this document
     * @param author Writer of this document
     * @param title Title of this document
     * @param subject Subject of this document
     * @throws UnsupportedEncodingException 
     */
    public Document(int width, int height, String author, String title, String subject) throws UnsupportedEncodingException {
        pdfDocument = new PdfDocument(width, height);
        finished = false;
        this.author = author;
        this.title = title;
        this.subject = subject;
        addNewPage();
    }

    public Text createText(String text, int textSize) {
        return new BaseText(text, textSize);
    }

    public Font createFont(FontFamily family, FontStyle style) {
        return new BaseFont(family, style);
    }

    public Paragraph createParagraph() {
        return new BaseParagraph();
    }

    public void addNewLine() {
        pdfDocument.addNewLine();
    }

    public void addNewLines(int amount) {
        for (int i = 0; i < amount; ++i) {
            pdfDocument.addNewLine();
        }
    }

    /**
     * Adds a new part to the document.
     * 
     * @param part New document part to be added to the document.
     * @see DocumentPart
     */
    public void addPart(DocumentPart part) {
        if (!finished && part != null) {
            pdfDocument.add(part);
        }
    }

    /**
     * Adds a list of new parts to the document.
     *
     * @param parts List of new document parts to be added.
     * @see DocumentPart
     */
    public void addParts(List<DocumentPart> parts) {
        if (!finished) {
            for (DocumentPart part : parts) {
                this.addPart(part);
            }
        }
    }

    /**
     * Adds a new page to the document with the default document width and height.
     */
    public void addNewPage() {
        if (!finished) {
            pdfDocument.addPage();
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
        if (!title.isEmpty()) {
            this.finish(title);
        }
        this.finish(new FileOutputStream(DEFAULT_FILE_NAME));
    }

    /** 
     * Prints the document to a PDF file with the given name and no longer allows editing of the document.
     * @param filename The filename
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void finish(String filename) throws FileNotFoundException, IOException {
        if (!filename.endsWith(".pdf")) {
            filename += ".pdf";
        }
        this.finish(new FileOutputStream(filename));
    }

    /**
     * Prints the document to the given OutputStream and no longer allows editing of the document.
     * @param OutputStream The OutputStream to write to.
     * @throws IOException 
     */
    public void finish(OutputStream os) throws IOException {
        if (!finished) {
            this.pdfDocument.addDocumentInfo(author, title, subject, Calendar.getInstance());
            this.pdfDocument.finish(os);
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
