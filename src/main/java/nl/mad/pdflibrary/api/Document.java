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

    /**
     * Creates a new instance of Document with the given width and height.
     * @param width Default page width.
     * @param height Default page height.
     * @throws UnsupportedEncodingException
     */
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

    /**
     * Returns a new Text instance with the given text and text size. This method does not add the created Text object
     * to the document automatically. Use the addPart method and pass the created Text object as attribute to do this.
     * @param text String the text instance should represent.
     * @param textSize Size of the text.
     * @return new Text instance containing the given text.
     * @see Text
     */
    public Text createText(String text, int textSize) {
        return new BaseText(text, textSize);
    }

    /**
     * Returns a new Font instance with the given family and style. This method does not add the created Font object
     * to the document automatically. You can add the font directly to the document by passing it as attribute to the
     * addPart method. The easier method is to attach the font to a Text object, this will add the font automatically 
     * when the Text object is added.
     * @param family FontFamily specifying the family of the font.
     * @param style FontStyle specifying the style of the font.
     * @return new Font instance.
     * @see Font
     */
    public Font createFont(FontFamily family, FontStyle style) {
        return new BaseFont(family, style);
    }

    /**
     * Creates a new, empty, paragraph object. This method does not add the created paragraph to the document automatically.
     * The paragraph can be added to the Document by passing it as parameter to the addPart method. The paragraph can be filled
     * up by adding Text objects.
     * @return new Paragraph instance.
     * @see Paragraph
     */
    public Paragraph createParagraph() {
        return new BaseParagraph();
    }

    /**
     * Directly adds a new line to the document.
     */
    public void addNewLine() {
        pdfDocument.addNewLine();
    }

    /**
     * Directly adds new lines to the document.
     * @param amount Amount of new lines.
     */
    public void addNewLines(int amount) {
        for (int i = 0; i < amount; ++i) {
            pdfDocument.addNewLine();
        }
    }

    /**
     * Adds a new part to the document. Use this method to add text, fonts or paragraphs to the document.
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
     * Adds a list of parts to the document.
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
            this.pdfDocument.write(os);
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
