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
import nl.mad.pdflibrary.model.PlaceableDocumentPart;
import nl.mad.pdflibrary.model.Position;
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
    public static final double CUT_OFF_POINT_PERCENTAGE = 0.9;
    private static final int MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING = 100;
    private static final int DEFAULT_NEW_LINE_SIZE = 10;

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

    /**
     * Creates a new page based on the attributes of the given page. This does not copy the content of the given page!
     * @param page Page to base attributes on.
     */
    public BasePage(Page page) {
        super(DocumentPartType.PAGE);
        content = new ArrayList<DocumentPart>();
        this.width = page.getWidth();
        this.height = page.getHeight();
        this.filledHeight = 0;
        this.filledWidth = 0;
        this.marginBottom = page.getMarginBottom();
        this.marginLeft = page.getMarginLeft();
        this.marginTop = page.getMarginTop();
        this.marginRight = page.getMarginRight();
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
            break;
        default:
            break;
        }
    }

    private void removeSenderIfDisposable(Observable sender) {
        if (sender instanceof Text) {
            Text text = (Text) sender;
            if (text.getText().isEmpty()) {
                this.content.remove(sender);
            }
        } else if (sender instanceof Paragraph) {
            Paragraph paragraph = (Paragraph) sender;
            if (paragraph.getTextCollection().size() == 0) {
                this.content.remove(sender);
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

    @Override
    public Position getOpenPosition() {
        return this.getOpenPositionOn((int) (height - marginTop));
    }

    @Override
    public Position getOpenPositionOn(int positionHeight) {
        boolean openPositionFound = false;
        int potentialHeight = positionHeight;
        Position position = new Position(0 + marginLeft, potentialHeight);
        while (!openPositionFound) {
            if (checkAvailableWidth(position) > (MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING)) {
                return position;
            }
            if (potentialHeight <= marginBottom) {
                return null;
            }
            potentialHeight -= DEFAULT_NEW_LINE_SIZE;
            position = new Position(marginLeft, potentialHeight);
        }
        return null;
    }

    private int checkAvailableWidth(Position position) {
        int availableWidth = this.getWidth() - marginRight - position.getX();
        int startOccupied = availableWidth;
        int endOccupied = 0;
        for (PlaceableDocumentPart part : getPartsOnLine(position)) {
            System.out.println("SOMETHINGS ON MAH LINE");
            Position pos = part.getPosition();
            if (!positionOutsideRightBoundary(pos)) {
                int x = pos.getX();
                int partWidth = part.getContentWidth(this, pos);
                if (positionOutsideLeftBoundary(pos)) {
                    x += this.marginLeft - x;
                }
                if (x < startOccupied) {
                    startOccupied = x;
                }
                int end = partWidth + x;
                if (end > endOccupied) {
                    endOccupied = width;
                }
            }
        }

        if (endOccupied != 0 && startOccupied != availableWidth) {
            availableWidth = availableWidth - (startOccupied - endOccupied);
        }
        return availableWidth;
    }

    private List<PlaceableDocumentPart> getPartsOnLine(Position pos) {
        List<PlaceableDocumentPart> contentOnSameLine = new ArrayList<PlaceableDocumentPart>();
        for (DocumentPart p : content) {
            if (p instanceof PlaceableDocumentPart) {
                PlaceableDocumentPart part = (PlaceableDocumentPart) p;
                if (onSameLine(pos, part)) {
                    System.out.println("ONLINE");
                    contentOnSameLine.add(part);
                }
            }
        }
        return contentOnSameLine;
    }

    private boolean positionOutsideLeftBoundary(Position pos) {
        if (pos.getX() > marginLeft) {
            return false;
        }
        return true;
    }

    private boolean positionOutsideRightBoundary(Position pos) {
        if (pos.getX() < (width - marginRight)) {
            return false;
        }
        return true;
    }

    private boolean onSameLine(Position position, PlaceableDocumentPart part) {
        //        System.out.println("Given position: " + position.getX() + ", " + position.getY());
        //        System.out.println("Part position: " + part.getPosition().getX() + ", " + part.getPosition().getY());
        //        System.out.println("Part height: " + part.getContentHeight(this));
        //        System.out.println("Check 1: " + (position.getY() <= part.getPosition().getY()) + ", Check 2: " + (position.getY() >= part.getContentHeight(this)));
        System.out.println("Pos check: " + position.getY());
        System.out.println("part pos: " + part.getPosition().getY());
        System.out.println("Part height: " + part.getContentHeight(this));
        System.out.println("If statement: " + (position.getY() <= part.getPosition().getY()) + " "
                + (position.getY() >= (part.getPosition().getY() - part.getContentHeight(this))));
        //increase check size 
        if (position.getY() <= part.getPosition().getY() && position.getY() >= (part.getPosition().getY() - part.getContentHeight(this))) {
            return true;
        }
        return false;
    }

    private int checkAvailableHeight(Position position) {
        int heightAvailable = this.getHeight() - marginBottom - position.getY();
        return heightAvailable;
    }

    @Override
    public List<DocumentPart> getFixedPositionContent() {
        List<DocumentPart> fixedPositionList = new ArrayList<>();
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
        List<DocumentPart> positionlessContent = new ArrayList<>();
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
    public List<int[]> getOpenSpacesOn(Position pos) {
        List<PlaceableDocumentPart> parts = this.getPartsOnLine(pos);
        List<int[]> openSpaces = new ArrayList<int[]>();

        //this might cause trouble with fixed position stuff beyond the margins
        openSpaces.add(new int[] { marginLeft, (width - marginRight) });
        for (PlaceableDocumentPart part : parts) {
            openSpaces = adjustOpenSpaces(openSpaces, part);
        }
        return openSpaces;
    }

    private List<int[]> adjustOpenSpaces(List<int[]> openSpaces, PlaceableDocumentPart part) {
        List<int[]> newOpenSpaces = new ArrayList<int[]>();
        for (int[] openSpace : openSpaces) {
            int contentWidth = part.getContentWidth(this, part.getPosition());
            if (part.getPosition().getX() >= openSpace[0] && (part.getPosition().getX() + contentWidth) < openSpace[1]) {
                if (openSpace[0] != part.getPosition().getX()) {
                    newOpenSpaces.add(new int[] { openSpace[0], part.getPosition().getX() });
                }

                if (part.getPosition().getX() + contentWidth != openSpace[1]) {
                    newOpenSpaces.add(new int[] { part.getPosition().getX() + contentWidth, openSpace[1] });
                }
            } else {
                newOpenSpaces.add(openSpace);
            }
        }
        return newOpenSpaces;
    }
}
