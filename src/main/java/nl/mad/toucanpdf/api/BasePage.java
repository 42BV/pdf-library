package nl.mad.toucanpdf.api;

import java.util.ArrayList;
import java.util.List;

import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;

/**
 * Base implementation of the page interface. BasePage stores a collection of DocumentParts and stores page specific
 * attributes such as page width/height and margins. 
 * @author Dylan de Wolff
 *
 */
public class BasePage extends AbstractDocumentPart implements Page {
    private int width;
    private int height;
    private int marginTop = 0;
    private int marginBottom = 0;
    private int marginLeft = 0;
    private int marginRight = 0;
    private Page masterPage;
    private List<DocumentPart> content;
    private int leading = DEFAULT_NEW_LINE_SIZE;

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

    /**
     * Creates a new page based on the attributes of the given page. This does not copy the content of the given page!
     * @param page Page to base attributes on.
     */
    public BasePage(Page page) {
        super(DocumentPartType.PAGE);
        content = new ArrayList<DocumentPart>();
        this.width = page.getWidth();
        this.height = page.getHeight();
        this.marginBottom = page.getMarginBottom();
        this.marginLeft = page.getMarginLeft();
        this.marginTop = page.getMarginTop();
        this.marginRight = page.getMarginRight();
        this.leading = page.getLeading();
        this.masterPage = page.getMasterPage();
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

    @Override
    public int getHeightWithoutMargins() {
        return this.height - marginTop - marginBottom;
    }

    @Override
    public int getWidthWithoutMargins() {
        return this.width - marginRight - marginLeft;
    }

    @Override
    public int getMarginTop() {
        return marginTop;
    }

    @Override
    public Page marginTop(int margin) {
        this.marginTop = margin;
        return this;
    }

    @Override
    public int getMarginBottom() {
        return marginBottom;
    }

    @Override
    public Page marginBottom(int margin) {
        this.marginBottom = margin;
        return this;
    }

    @Override
    public int getMarginLeft() {
        return marginLeft;
    }

    @Override
    public Page marginLeft(int margin) {
        this.marginLeft = margin;
        return this;
    }

    @Override
    public int getMarginRight() {
        return marginRight;
    }

    @Override
    public Page marginRight(int margin) {
        this.marginRight = margin;
        return this;
    }

    @Override
    public List<DocumentPart> getFixedPositionContent() {
        List<DocumentPart> fixedPositionList = new ArrayList<DocumentPart>();
        for (DocumentPart p : content) {
            if (p instanceof PlaceableDocumentPart) {
                if (((PlaceableDocumentPart) p).getPosition().hasCustomPosition()) {
                    fixedPositionList.add((PlaceableDocumentPart) p);
                }
            }
        }
        return fixedPositionList;
    }

    @Override
    public List<DocumentPart> getPositionlessContent() {
        List<DocumentPart> positionlessContent = new ArrayList<DocumentPart>();
        for (DocumentPart p : content) {
            if (p instanceof PlaceableDocumentPart) {
                if (!((PlaceableDocumentPart) p).getPosition().hasCustomPosition()) {
                    positionlessContent.add((PlaceableDocumentPart) p);
                }
            }
        }
        return positionlessContent;
    }

    @Override
    public Page addAll(List<DocumentPart> parts) {
        this.content.addAll(parts);
        return this;
    }

    @Override
    public int getLeading() {
        return leading;
    }

    @Override
    public Page leading(int leadingSize) {
        if (leadingSize >= 0) {
            this.leading = leadingSize;
        }
        return this;
    }

    @Override
    public Page master(Page master) {
        this.masterPage = master;
        this.marginBottom = master.getMarginBottom();
        this.marginLeft = master.getMarginLeft();
        this.marginRight = master.getMarginRight();
        this.marginTop = master.getMarginTop();
        this.width = master.getWidth();
        this.height = master.getHeight();
        return this;
    }

    @Override
    public Page getMasterPage() {
        return this.masterPage;
    }
}
