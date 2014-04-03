package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Observable;
import nl.mad.pdflibrary.model.Observer;
import nl.mad.pdflibrary.model.ObserverEvent;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Text;

public class BasePage extends AbstractDocumentPart implements Page, Observer {
    private int width;
    private int height;
    private double filledWidth;
    private double filledHeight;
    private int marginTop;
    private int marginBottom;
    private int marginLeft;
    private int marginRight;
    private List<DocumentPart> content;
    private Page overflowPage;
    public static final double CUT_OFF_POINT_PERCENTAGE = 0.7;

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
        this.filledHeight = 0;
        this.filledWidth = 0;
    }

    @Override
    public Page add(DocumentPart part) {
        if (part != null) {
            part.addChangeObserver(this);
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
    public void processContentSize() {
        this.setFilledHeight(0);
        this.setFilledWidth(0);
        for (int i = 0; i < content.size(); ++i) {
            DocumentPart part = content.get(i);
            switch (part.getType()) {
            case TEXT:
                ((Text) part).processContentSize(this, false, 0);
                break;
            case PARAGRAPH:
                ((Paragraph) part).processContentSize(this);
                break;
            default:
                break;
            }
        }
    }

    @Override
    public double getFilledWidth() {
        return this.filledWidth;
    }

    @Override
    public double getFilledHeight() {
        return this.filledHeight;
    }

    @Override
    public void setFilledWidth(double filledWidth) {
        this.filledWidth = filledWidth;
    }

    @Override
    public void setFilledHeight(double filledHeight) {
        this.filledHeight = filledHeight;
    }

    @Override
    public void overflowPage(Page page) {
        this.overflowPage = page;
    }

    @Override
    public void update(Observable sender, ObserverEvent event, DocumentPart arg) {
        switch (event) {
        case RECALCULATE:
            this.processContentSize();
            break;
        case OVERFLOW:
            this.removeSenderIfDisposable(sender);
            this.notify(event, arg);
            break;
        default:
            break;
        }
    }

    private void removeSenderIfDisposable(Observable sender) {
        if (sender instanceof Text) {
            Text text = (Text) sender;
            if (text.getText().isEmpty()) {
                text.removeChangeObserver(this);
                this.content.remove(sender);
            }
        } else if (sender instanceof Paragraph) {
            Paragraph paragraph = (Paragraph) sender;
            if (paragraph.getTextCollection().size() == 0) {
                this.content.remove(sender);
                paragraph.removeChangeObserver(this);
            }
        }
    }

    @Override
    public double getRemainingHeight() {
        return this.getHeightWithoutMargins() - filledHeight;
    }

    @Override
    public double getRemainingWidth() {
        return this.getWidthWithoutMargins() - filledWidth;
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
    public Page getOverflowPage() {
        return this.overflowPage;
    }

    @Override
    public int getMarginTop() {
        return marginTop;
    }

    @Override
    public Page marginTop(int marginTop) {
        this.marginTop = marginTop;
        return this;
    }

    @Override
    public int getMarginBottom() {
        return marginBottom;
    }

    @Override
    public Page marginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
        return this;
    }

    @Override
    public int getMarginLeft() {
        return marginLeft;
    }

    @Override
    public Page marginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
        return this;
    }

    @Override
    public int getMarginRight() {
        return marginRight;
    }

    @Override
    public Page marginRight(int marginRight) {
        this.marginRight = marginRight;
        return this;
    }

}
