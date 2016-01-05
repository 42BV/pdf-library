package org.toucanpdf.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.toucanpdf.model.DocumentPart;
import org.toucanpdf.model.DocumentPartType;
import org.toucanpdf.model.Page;
import org.toucanpdf.model.PageArea;
import org.toucanpdf.model.PlaceableDocumentPart;

/**
 * Base implementation of the page interface. BasePage stores a collection of DocumentParts and stores page specific
 * attributes such as page width/height and margins. 
 * @author Dylan de Wolff
 *
 */
public class BasePage extends AbstractDocumentPart implements Page {
    private static final int ROTATION_LIMIT = 90;
    private int width;
    private int height;
    private int marginTop = 20;
    private int marginBottom = 20;
    private int marginLeft = 20;
    private int marginRight = 20;
    private Page masterPage;
    private List<DocumentPart> content;
    private int leading = DEFAULT_NEW_LINE_SIZE;
    private int rotation;
    private PageArea header = null;
    private PageArea footer = null;

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
        this.rotation = page.getRotation();
        this.header = page.getHeader();
        this.footer = page.getFooter();
        if (this.header != null) {
            this.marginTop(header.getHeight());
        }
        if (this.footer != null) {
            this.marginBottom(footer.getHeight());
        }
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
        this.marginTop = limitGivenMarginForPageArea(margin, header);
        return this;
    }

    private int limitGivenMarginForPageArea(int margin, PageArea relevantArea) {
        if (relevantArea != null) {
            return Math.max(margin, relevantArea.getHeight());
        } else {
            return Math.max(0, margin);
        }
    }

    @Override
    public int getMarginBottom() {
        return marginBottom;
    }

    @Override
    public Page marginBottom(int margin) {
        this.marginBottom = limitGivenMarginForPageArea(margin, footer);
        return this;
    }

    @Override
    public int getMarginLeft() {
        return marginLeft;
    }

    @Override
    public Page marginLeft(int margin) {
        this.marginLeft = Math.max(0, margin);
        return this;
    }

    @Override
    public int getMarginRight() {
        return marginRight;
    }

    @Override
    public Page marginRight(int margin) {
        this.marginRight = Math.max(0, margin);
        return this;
    }

    @Override
    public List<DocumentPart> getFixedPositionContent() {
        return content.stream()
                .filter(p -> partIsPlaceable(p) && partHasCustomPosition(p))
                .map(p -> (PlaceableDocumentPart) p).collect(Collectors.toList());
    }

    @Override
    public List<DocumentPart> getPositionlessContent() {
        return content.stream()
                .filter(p -> partIsPlaceable(p) && !partHasCustomPosition(p))
                .map(p -> (PlaceableDocumentPart) p).collect(Collectors.toList());
    }

    private boolean partHasCustomPosition(DocumentPart part) {
        return ((PlaceableDocumentPart) part).getPosition().hasCustomPosition();
    }

    private boolean partIsPlaceable(DocumentPart p) {
        return p instanceof PlaceableDocumentPart;
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
        this.leading = master.getLeading();
        this.rotation = master.getRotation();
        this.header = master.getHeader();
        this.footer = master.getFooter();
        return this;
    }

    @Override
    public Page getMasterPage() {
        return this.masterPage;
    }

    @Override
    public Page rotate(int rotationDegrees) {
        rotationDegrees = Math.max(0, rotationDegrees);
        int remainder = rotationDegrees % ROTATION_LIMIT;
        if (remainder < ROTATION_LIMIT / 2) {
            rotationDegrees -= remainder;
        } else {
            rotationDegrees += (ROTATION_LIMIT - remainder);
        }
        this.rotation = rotationDegrees;
        return this;
    }

    @Override
    public int getRotation() {
        return this.rotation;
    }

    @Override
    public Page header(PageArea newHeader) {
        this.header = newHeader;
        if (this.marginTop < newHeader.getHeight()) {
            this.marginTop(newHeader.getHeight());
        }
        return this;
    }

    @Override
    public PageArea addHeader() {
        PageArea h = new BasePageArea(this.marginTop);
        header(h);
        return h;
    }

    @Override
    public PageArea addFooter() {
        PageArea f = new BasePageArea(this.marginBottom);
        footer(f);
        return f;
    }

    @Override
    public Page footer(PageArea newFooter) {
        this.footer = newFooter;
        if (this.marginBottom < newFooter.getHeight()) {
            this.marginTop(newFooter.getHeight());
        }
        return this;
    }

    @Override
    public PageArea getHeader() {
        return this.header;
    }

    @Override
    public PageArea getFooter() {
        return this.footer;
    }
}
