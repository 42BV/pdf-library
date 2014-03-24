package nl.mad.pdflibrary.api;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.FontFamilyType;
import nl.mad.pdflibrary.model.FontStyle;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.structure.PdfDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Document is the class responsible for passing document parts on to the PdfDocument.
 * It also stores properties of the document such as page width/height and the author.
 * @author Dylan de Wolff
 */
public class Document {
    private String author;
    private String title;
    private String subject;
    private Calendar creationDate;
    private final Logger logger = LoggerFactory.getLogger(Document.class);
    /**
     * Default font. This is used when no font is specified.
     */
    public static final BaseFont DEFAULT_FONT = new BaseFont(FontFamilyType.TIMES_ROMAN, FontStyle.NORMAL);
    private static final String DEFAULT_FILE_NAME = "document.pdf";
    /**
     * A4 page width.
     */
    public static final int A4_WIDTH = 595;
    /**
     * A4 page height.
     */
    public static final int A4_HEIGHT = 842;
    private int defaultPageWidth = A4_WIDTH;
    private int defaultPageHeight = A4_HEIGHT;
    private List<Page> pages;
    private String filename;

    /**
     * Creates a new instance of Document.
     */
    public Document() {
        pages = new LinkedList<Page>();
        author = "";
        title = "";
        subject = "";
        filename = "";
        creationDate = Calendar.getInstance();
    }

    /**
     * Directly adds a new line to the document.
     */
    //    public void newLine() {
    //        //do something
    //    }

    /**
     * Directly adds new lines to the document.
     * @param amount Amount of new lines.
     */
    //    public void newLines(int amount) {
    //        for (int i = 0; i < amount; ++i) {
    //            //do something
    //        }
    //    }

    /**
     * Adds a new part to the document. Use this method to add text, fonts or paragraphs to the document.
     * This method will automatically add the content to the last page.
     * @param part New document part to be added to the document.
     * @return the document object.
     */
    public Document addPart(DocumentPart part) {
        if (pages.size() == 0 && part.getType() != DocumentPartType.PAGE) {
            this.addPage(new BasePage(this.defaultPageWidth, this.defaultPageHeight));
        }
        return this.addPart(part, pages.size());
    }

    /**
     * Adds a new part to the document. 
     * Use this method to add text, fonts or paragraphs to a specific page in the document.
     * 
     * @param part New document part to be added to the document.
     * @param pagenumber Number of the page to add this part to.
     * @see DocumentPart
     * @return the document object.
     */
    public Document addPart(DocumentPart part, int pagenumber) {
        if (part != null) {
            if (part.getType() != DocumentPartType.PAGE) {
                if (pages.size() == 0) {
                    this.addPage(new BasePage(this.defaultPageWidth, this.defaultPageHeight));
                }
                if (pages.size() >= pagenumber && pagenumber > 0) {
                    Page currentPage = pages.get(pagenumber - 1);
                    currentPage.add(part);
                }
            } else {
                this.addPage((Page) part);
            }
        }
        return this;
    }

    /**
     * Adds a list of parts to the document.
     *
     * @param parts List of new document parts to be added.
     * @see DocumentPart
     */
    public void addParts(List<DocumentPart> parts) {
        for (DocumentPart part : parts) {
            this.addPart(part);
        }
    }

    /**
     * Adds the given page to the end of the document.
     * @param page Page to add.
     */
    public void addPage(Page page) {
        if (page != null) {
            pages.add(page);
        }
    }

    /**
     * Adds the given page to the document on the given position.
     * @param page Page to add.
     * @param position The position to place the page on.
     */
    public void addPage(Page page, int position) {
        if (page != null && position > 0) {
            pages.add(position - 1, page);
        }
    }

    public int getPageAmount() {
        return this.pages.size();
    }

    public int getDefaultPageWidth() {
        return this.defaultPageWidth;
    }

    public int getDefaultPageHeight() {
        return this.defaultPageHeight;
    }

    /**
     * Prints the document to a PDF file.
     */
    public void finish() {
        checkFilename();
        try {
            this.finish(new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            logger.error("File not found exception ocurred during the creation of a file with the given filename.");
        }
    }

    /**
     * Checks the file name and changes it if needed.
     */
    private void checkFilename() {
        String file = filename;
        if (file.isEmpty()) {
            if (!title.isEmpty()) {
                file = title;
            } else {
                file = DEFAULT_FILE_NAME;
            }
        }

        if (!file.endsWith(".pdf")) {
            file += ".pdf";
        }
        filename = file;
    }

    /**
     * Prints the document to the given OutputStream and no longer allows editing of the document.
     * @param os The OutputStream to write to.
     */
    public void finish(OutputStream os) {
        try {
            PdfDocument pdfDoc = new PdfDocument();
            for (Page page : pages) {
                pdfDoc.add(page);
                pdfDoc.add(page.getContent());
            }
            pdfDoc.addDocumentInfo(author, title, subject, Calendar.getInstance());
            pdfDoc.write(os);
        } catch (IOException e) {
            logger.error("IOException ocurred during the writing process of the PDF file.");
        }
    }

    /**
     * Sets the creation date of the document.
     * @param calendar Calendar containing the date.
     * @return the document.
     */
    public Document on(Calendar calendar) {
        this.creationDate = calendar;
        return this;
    }

    /**
     * Returns the creation date for this document.
     * @return calendar containing the creation date.
     */
    public Calendar getCreationDate() {
        return this.creationDate;
    }

    public String getAuthor() {
        return author;
    }

    /**
     * Set the author of the document.
     * @param writer The author that wrote the document.
     * @return the document.
     */
    public Document writtenBy(String writer) {
        this.author = writer;
        return this;
    }

    /**
     * Returns the title of this document.
     * @return String containing title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the document.
     * @param documentTitle Title to use.
     * @return the document.
     */
    public Document title(String documentTitle) {
        this.title = documentTitle;
        return this;
    }

    /**
     * Returns the subject of this document.
     * @return String containing subject.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set the subject of the document.
     * @param documentSubject Subject to use.
     * @return the document.
     */
    public Document about(String documentSubject) {
        this.subject = documentSubject;
        return this;
    }

    /**
     * Returns the page with the given page number.
     * @param pageNumber Page number
     * @return page object for the given number.
     */
    public Page getPage(int pageNumber) {
        if (pageNumber > 0 && pages.size() >= pageNumber) {
            return this.pages.get(pageNumber - 1);
        }
        return null;
    }

    /**
     * Sets the filename of the document.
     * @param documentFilename Name to use.
     * @return the document.
     */
    public Document filename(String documentFilename) {
        this.filename = documentFilename;
        return this;
    }

    /**
     * Returns the filename for this document.
     * @return String containing filename.
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Returns the page number corresponding to the given page.
     * @param page Page to look for.
     * @return int containing page number, 0 if it could not be found
     */
    public int getPageNumberOf(Page page) {
        return pages.indexOf(page) + 1;
    }
}
