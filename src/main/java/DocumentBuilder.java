import nl.mad.pdflibrary.api.BaseFont;
import nl.mad.pdflibrary.api.BasePage;
import nl.mad.pdflibrary.api.BaseParagraph;
import nl.mad.pdflibrary.api.BaseText;
import nl.mad.pdflibrary.api.Document;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Text;

/**
 * The DocumentBuilder is the access point for the library and allows the creation of documents. 
 * By instantiating a new DocumentBuilder you will also automatically create a new document. 
 * You can retrieve the document by using the getDocument function. This will allow you to edit the document metadata, such as the title and author.
 * DocumentBuilder also offers several methods for adding content to the document such as addText and addParagraph. 
 * All the methods for adding objects return the object made allowing you to edit them as you please. 
 * If you are done editing the document, use the finish function. If you wish to make adjustments afterwards, simply use the finish method again 
 * to print out a new Pdf document with the changes.
 * @author Dylan de Wolff
 *
 */
public class DocumentBuilder {
    private Document document;
    private int currentPageNumber;

    /**
     * Creates a new instance of DocumentBuilder, this also creates a document.
     * Use the getDocument method to retrieve the document and start setting metadata. 
     * You can add content to the document by using any of the add* methods.
     */
    public DocumentBuilder() {
        document = new Document();
        currentPageNumber = 1;
    }

    /**
     * Returns the actual document instance. This allows you to edit document metadata.
     * @return document object
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * Creates a new font and adds it to the document. Use the returned font object to specify the attributes of the font.
     * @return font object
     * @see Font
     */
    public Font addFont() {
        Font font = new BaseFont();
        document.addPart(font, currentPageNumber);
        return font;
    }

    /**
     * Creates a new page and adds it to the document. Use the returned page object to specify the attributes of the page.
     * @return page object
     * @see Page
     */
    public Page addPage() {
        Page page = new BasePage(document.getDefaultPageWidth(), document.getDefaultPageHeight());
        document.addPage(page);
        this.currentPageNumber = document.getPageAmount();
        return page;
    }

    /**
     * Returns the page at the given page number.
     * @param pageNumber number of the page.
     * @return page instance or null if there is no page for the given number.
     */
    public Page getPage(int pageNumber) {
        return document.getPage(pageNumber);
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
        document.addPart(text, currentPageNumber);
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
        return new BaseText(s);
    }

    /**
     * Creates a new paragraph object and adds it to the document. Use the returned paragraph object to position the paragraph or add text 
     * to the paragraph.
     * @return paragraph object
     * @see Paragraph
     */
    public Paragraph addParagraph() {
        Paragraph paragraph = new BaseParagraph();
        document.addPart(paragraph, currentPageNumber);
        return paragraph;
    }

    /**
     * Sets the page to add new content to.
     * @param page Page to add new content to.
     * @return the DocumentBuilder
     */
    public DocumentBuilder setCurrentPage(Page page) {
        return this.setCurrentPage(document.getPageNumberOf(page));
    }

    /**
     * Sets the page to add new content to.
     * @param pageNumber Page number of the page.
     * @return the DocumentBuilder
     */
    public DocumentBuilder setCurrentPage(int pageNumber) {
        if (pageNumber > 0 && pageNumber <= document.getPageAmount()) {
            this.currentPageNumber = pageNumber;
        }
        return this;
    }

    /**
     * Finishes up the document by converting it to a PDF. If no custom filename has been specified before calling this method the title of
     * the document will be used instead. If the title is not specified either, the document will simply be named "document".
     */
    public void finish() {
        document.finish();
    }
}
