package org.toucanpdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.toucanpdf.api.BaseFont;
import org.toucanpdf.api.BaseImage;
import org.toucanpdf.api.BasePage;
import org.toucanpdf.api.BaseParagraph;
import org.toucanpdf.api.BaseTable;
import org.toucanpdf.api.BaseText;
import org.toucanpdf.api.DocumentState;
import org.toucanpdf.model.Anchor;
import org.toucanpdf.model.Color;
import org.toucanpdf.model.DocumentPart;
import org.toucanpdf.model.DocumentPartType;
import org.toucanpdf.model.Font;
import org.toucanpdf.model.Image;
import org.toucanpdf.model.ImageType;
import org.toucanpdf.model.Page;
import org.toucanpdf.model.Paragraph;
import org.toucanpdf.model.PlaceableDocumentPart;
import org.toucanpdf.model.Table;
import org.toucanpdf.model.Text;
import org.toucanpdf.pdf.structure.PdfDocument;
import org.toucanpdf.utility.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The DocumentBuilder is the access point for the library and allows the creation of documents. 
 * This class allows you to edit the document metadata, such as the title and author.
 * DocumentBuilder also offers several methods for adding content to the document such as addText and addParagraph. 
 * All the methods for adding objects return the object made allowing you to edit them as you please. 
 * If you are done editing the document, use the finish function. If you wish to make adjustments afterwards, simply use the finish method again 
 * to print out a new Pdf document with the changes.
 * @author Dylan de Wolff
 *
 */
public class DocumentBuilder {
    private int currentPageNumber;
    private String author;
    private String title;
    private String subject;
    private Calendar creationDate;
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentBuilder.class);
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
    private DocumentState state;
    private int defaultMarginTop = 0;
    private int defaultMarginLeft = 0;
    private int defaultMarginRight = 0;
    private int defaultMarginBottom = 0;
    private Font defaultFont = Constants.DEFAULT_FONT;
    private Integer defaultTextSize = Constants.DEFAULT_TEXT_SIZE;
    private Color defaultColor = Color.BLACK;

    /**
     * Creates a new instance of DocumentBuilder, this also creates a document.
     * Use the getDocument method to retrieve the document and start setting metadata. 
     * You can add content to the document by using any of the add* methods.
     */
    public DocumentBuilder() {
        pages = new LinkedList<Page>();
        author = "";
        title = "";
        subject = "";
        filename = "";
        creationDate = Calendar.getInstance();
        currentPageNumber = 1;
        state = new DocumentState();
    }

    /**
     * Adds a new part to the document. 
     * Use this method to add text, fonts or paragraphs to a specific page in the document.
     * 
     * @param part New document part to be added to the document.
     * @see DocumentPart
     * @return the builder object.
     */
    public DocumentBuilder addPart(DocumentPart part) {
        if (part != null && part.getType() != DocumentPartType.PAGE) {
            if (pages.size() == 0) {
                this.addPage();
            }
            Page currentPage = pages.get(currentPageNumber - 1);
            currentPage.add(part);
        }
        return this;
    }

    private void setDefaultMargins(PlaceableDocumentPart part) {
        if (part != null) {
            part.setMarginBottom(defaultMarginBottom);
            part.setMarginLeft(defaultMarginLeft);
            part.setMarginRight(defaultMarginRight);
            part.setMarginTop(defaultMarginTop);
        }
    }

    /**
     * Creates a new font and adds it to the document. Use the returned font object to specify the attributes of the font.
     * @return font object
     * @see Font
     */
    public Font addFont() {
        Font font = this.createFont();
        this.addPart(font);
        return font;
    }

    /**
     * Creates a new font and returns it.
     * @return new font object.
     * @see Font
     */
    public Font createFont() {
        return new BaseFont();
    }

    /**
     * Creates a new page and returns it.
     * @return new page object.
     * @see Page
     */
    public Page createPage() {
        return new BasePage(defaultPageWidth, defaultPageHeight);
    }

    /**
     * Creates a new page and adds it to the document. Use the returned page object to specify the attributes of the page.
     * @return page object.
     * @see Page
     */
    public Page addPage() {
        Page page = this.createPage();
        pages.add(page);
        this.currentPageNumber = this.getPageAmount();
        return page;
    }

    /**
     * Creates a new page and adds it to the document on the given position. Use the returned page object to specify the attributes of the page.
     * @param pageNumber The position to place the page on.
     * @return page object, null if the specified page number is invalid
     */
    public Page addPage(int pageNumber) {
        Page page = null;
        if (pageNumber > 0) {
            page = new BasePage(defaultPageWidth, defaultPageHeight);
            pages.add(pageNumber - 1, page);
            currentPageNumber = pageNumber;
        } else {
            LOGGER.warn("Invalid page number specified, returning null.");
        }
        return page;
    }

    /**
     * Returns the amount of pages in the document.
     * @return int containing amount of pages.
     */
    public int getPageAmount() {
        return this.pages.size();
    }

    /**
     * Returns the page at the given page number.
     * @param pageNumber number of the page.
     * @return page instance or null if there is no page for the given number.
     */
    public Page getPage(int pageNumber) {
        if (pageNumber > 0 && pages.size() >= pageNumber) {
            return this.pages.get(pageNumber - 1);
        }
        LOGGER.warn("Could not find page on the given page number, returned null");
        return null;
    }

    /**
     * Prints the document to a PDF file.
     */
    public void finish() {
        checkFilename();
        try {
            this.finish(new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found exception ocurred during the creation of a file with the given filename.");
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
            state.updateState(pages);
            PdfDocument pdfDoc = new PdfDocument();
            for (Page page : state.getPages()) {
                pdfDoc.add(page);
                pdfDoc.add(page.getContent());
            }
            pdfDoc.addDocumentInfo(author, title, subject, Calendar.getInstance());
            pdfDoc.write(os);
        } catch (IOException e) {
            LOGGER.error("IOException ocurred during the writing process of the PDF file.");
        }
    }

    /**
     * Sets the creation date of the document.
     * @param calendar Calendar containing the date.
     * @return the document.
     */
    public DocumentBuilder on(Calendar calendar) {
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

    /**
     * Set the author of the document.
     * @param writer The author that wrote the document.
     * @return the document.
     */
    public DocumentBuilder writtenBy(String writer) {
        this.author = writer;
        return this;
    }

    /**
     * Returns the author of the document.
     * @return String containing authors name.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the title of the document.
     * @param documentTitle Title to use.
     * @return the document.
     */
    public DocumentBuilder title(String documentTitle) {
        this.title = documentTitle;
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
     * Set the subject of the document.
     * @param documentSubject Subject to use.
     * @return the document.
     */
    public DocumentBuilder about(String documentSubject) {
        this.subject = documentSubject;
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
     * Creates a new text object and adds it to the document. Use the returned text object to specify the attributes of the text.
     * If you wish to use this text inside a paragraph, use the createText method instead.
     * @return text object
     * @see Text
     */
    public Text addText() {
        return this.addText("");
    }

    /**
     * Creates a new text object with the given string and adds it to the document. Use the returned text object to specify the attributes of the
     * text. If you wish to use this text inside a paragraph, use the createText method instead.
     * @param s String representing the text.
     * @return text object
     * @see Text
     */
    public Text addText(String s) {
        Text text = createText(s);
        this.addPart(text);
        return text;
    }

    /**
     * Creates a new text object and does not add it to the document. This allows you to add it to a paragraph instead of directly adding it to 
     * the document. Use the returned text object to specify the attributes of the text.
     * @return text object
     * @see Text
     */
    public Text createText() {
        return this.createText("");
    }

    /**
     * Creates a new text object with the given string and does not add it to the document. This allows you to add it to a paragraph 
     * instead of directly adding it to the document. Use the returned text object to specify the attributes of the text.
     * @param s String representing the text.
     * @return text object
     * @see Text
     */
    public Text createText(String s) {
        Text text = new BaseText(s).font(defaultFont).size(defaultTextSize).color(defaultColor);
        setDefaultMargins(text);
        return text;
    }

    /**
     * Creates a new paragraph object and adds it to the document. Use the returned paragraph object to position the paragraph or add text 
     * to the paragraph.
     * @return paragraph object
     * @see Paragraph
     */
    public Paragraph addParagraph() {
        Paragraph paragraph = createParagraph();
        this.addPart(paragraph);
        return paragraph;
    }

    /**
     * Creates a new paragraph object. Use the returned paragraph object to position the paragraph or add text.
     * @return paragraph object.
     * @see Paragraph
     */
    public Paragraph createParagraph() {
        Paragraph paragraph = new BaseParagraph();
        setDefaultMargins(paragraph);
        return paragraph;
    }

    /**
     * Sets the page to add new content to.
     * @param page Page to add new content to.
     * @return the DocumentBuilder
     */
    public DocumentBuilder setCurrentPage(Page page) {
        return this.setCurrentPage(this.getPageNumberOf(page));
    }

    /**
     * Sets the page to add new content to.
     * @param pageNumber Page number of the page.
     * @return the DocumentBuilder
     */
    public DocumentBuilder setCurrentPage(int pageNumber) {
        if (pageNumber > 0 && pageNumber <= this.getPageAmount()) {
            this.currentPageNumber = pageNumber;
        }
        return this;
    }

    /**
     * Sets the filename of the document.
     * @param documentFilename Name to use.
     * @return the documentBuilder.
     */
    public DocumentBuilder filename(String documentFilename) {
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

    /**
     * Sets the default page size to use.
     * @param width Default page width to use.
     * @param height Default page height to use.
     * @return the documentBuilder.
     */
    public DocumentBuilder setDefaultPageSize(int width, int height) {
        if (width > 0 && height > 0) {
            this.defaultPageHeight = height;
            this.defaultPageWidth = width;
        }
        return this;
    }

    /**
     * Returns a state that contains what the actual document will look like once it's converted.
     * Calling this method will completely reset the state which can cost a lot of performance on large documents.
     * @return DocumentState containing the state of the document as is.
     */
    public DocumentState getPreview() {
        state.updateState(pages);
        return state;
    }

    /**
     * Adds an image to the document. Use the returned image instance to adjust attributes of the image.
     * @param imageFile InputStream containing the image file to parse.
     * @param format The format of the image.
     * @return image object
     */
    public Image addImage(InputStream imageFile, ImageType format) {
        Image image = createImage(imageFile, format);
        this.addPart(image);
        return image;
    }

    /**
     * Adds an image to the document. Use the returned image instance to adjust attributes of the image.
     * @param imageData image data to use.
     * @param format The format of the image.
     * @return image object
     */
    public Image addImage(byte[] imageData, ImageType format) {
        Image image = createImage(imageData, format);
        this.addPart(image);
        return image;
    }

    /**
     * Creates a new image instance. Use the returned instance to adjust attributes of the image. 
     * You can use this method to create images and add them to anchors without directly adding the image to the document as well.
     * @param imageData Byte[] containing the image data.
     * @param format The format of the image.
     * @return image object
     * @see Anchor
     */
    public Image createImage(byte[] imageData, ImageType format) {
        Image image = new BaseImage(imageData, format);
        setDefaultMargins(image);
        return image;
    }

    /**
     * Creates a new image instance. Use the returned instance to adjust attributes of the image. 
     * You can use this method to create images and add them to anchors without directly adding the image to the document as well.
     * @param imageFile InputStream containing the image file to parse.
     * @param format The format of the image.
     * @return image object
     * @see Anchor
     */
    public Image createImage(InputStream imageFile, ImageType format) {
        Image image = new BaseImage(imageFile, format);
        setDefaultMargins(image);
        return image;
    }

    /**
    * Creates a new image instance. Use the returned instance to adjust attributes of the image. 
    * You can use this method to create images and add them to anchors without directly adding the image to the document as well.
    * @param imageFile InputStream containing the image file to parse.
    * @param imageFilename The name of the image file.
    * @return image object
    * @see Anchor
    */
    public Image createImage(InputStream imageFile, String imageFilename) {
        Image image = new BaseImage(imageFile, imageFilename);
        setDefaultMargins(image);
        return image;
    }

    /**
     * Creates a new table and adds it to the document. Use the returned table object to specify the attributes of the table.
     * @return table object.
     * @see Table
     */
    public Table addTable() {
        Table table = this.createTable();
        this.addPart(table);
        return table;
    }

    /**
     * Creates a new table and returns it. 
     * @return table object.
     * @see Table
     */
    public Table createTable() {
        int width = defaultPageWidth;
        if (currentPageNumber != 0 && pages.size() >= currentPageNumber) {
            width = pages.get(currentPageNumber - 1).getWidthWithoutMargins();
        }
        Table table = new BaseTable(width);
        setDefaultMargins(table);
        return table;
    }

    /**
     * @return the defaultMarginTop
     */
    public int getDefaultMarginTop() {
        return defaultMarginTop;
    }

    /**
     * Sets the default top margin to use for each object added to the document. 
     * @param marginTop the defaultMarginTop to set
     * @return the builder.
     */
    public DocumentBuilder setDefaultMarginTop(int marginTop) {
        this.defaultMarginTop = marginTop;
        return this;
    }

    /**
     * @return the defaultMarginLeft
     */
    public int getDefaultMarginLeft() {
        return defaultMarginLeft;
    }

    /**
     * Sets the default left margin to use for each object added to the document. 
     * @param marginLeft the defaultMarginLeft to set
     * @return the builder.
     */
    public DocumentBuilder setDefaultMarginLeft(int marginLeft) {
        this.defaultMarginLeft = marginLeft;
        return this;
    }

    /**
     * @return the defaultMarginRight
     */
    public int getDefaultMarginRight() {
        return defaultMarginRight;
    }

    /**
     * Sets the default right margin to use for each object added to the document. 
     * @param marginRight the defaultMarginRight to set
     * @return this builder.
     */
    public DocumentBuilder setDefaultMarginRight(int marginRight) {
        this.defaultMarginRight = marginRight;
        return this;
    }

    /**
     * @return the defaultMarginBottom
     */
    public int getDefaultMarginBottom() {
        return defaultMarginBottom;
    }

    /**
     * Sets the default bottom margin to use for each object added to the document.
     * @param marginBottom the defaultMarginBottom to set
     * @return this builder.
     */
    public DocumentBuilder setDefaultMarginBottom(int marginBottom) {
        this.defaultMarginBottom = marginBottom;
        return this;
    }

    /**
     * Sets the default font to be used for each text object created through this builder.
     * @param defaultFont font to be used by default
     * @return this builder.
     */
    public DocumentBuilder setDefaultFont(Font defaultFont) {
        this.defaultFont = defaultFont;
        return this;
    }

    /**
     * Returns the default font 
     * @return default font
     */
    public Font getDefaultFont() {
        return this.defaultFont;
    }

    /**
     * Sets the default text size to be used for each text object created through this builder.
     * @param defaultTextSize text size to use by default.
     * @return this builder.
     */
    public DocumentBuilder setDefaultTextSize(Integer defaultTextSize) {
        this.defaultTextSize = defaultTextSize;
        return this;
    }

    /**
     * Returns the default font size
     * @return default font size
     */
    public Integer getDefaultTextSize() {
        return this.defaultTextSize;
    }

    /**
     * Returns the default text color
     * @return default color
     */
    public Color getDefaultColor() {
        return defaultColor;
    }

    /**
     * Sets the default color to be used for each text object created through this builder.
     * @param defaultColor color to use by default.
     * @return this builder
     */
    public DocumentBuilder setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
        return this;
    }
}
