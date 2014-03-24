package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Page;

public class BasePage extends AbstractDocumentPart implements Page {
    private int width;
    private int height;
    private List<DocumentPart> content;

    /**
     * Creates a new instance of BasePage with the given width and height.
     * @param width Width of the page.
     * @param height Height of the page.
     */
    public BasePage(int width, int height) {
        super(DocumentPartType.PAGE);
        content = new ArrayList<DocumentPart>();
        this.width = width;
        this.height = height;
    }

    @Override
    public Page add(DocumentPart part) {
        if (part != null) {
            content.add(part);
        }
        return this;
    }

    @Override
    public Page size(int pageWidth, int pageHeight) {
        this.width = pageWidth;
        this.height = pageHeight;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public List<DocumentPart> getContent() {
        return content;
    }

}
