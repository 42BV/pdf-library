package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.PlaceableDocumentPart;
import nl.mad.pdflibrary.model.Position;

public class BasePage extends AbstractDocumentPart implements Page {
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
    private static final int MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING = 100;

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
    public Position getOpenPosition(int elementHeight) {
        return this.getOpenPosition((int) (height - marginTop - filledHeight - elementHeight), elementHeight);
    }

    @Override
    public Position getOpenPosition(int positionHeight, int elementHeight) {
        boolean openPositionFound = false;
        int potentialHeight = positionHeight;
        Position position = new Position(0 + marginLeft, potentialHeight);
        while (!openPositionFound) {
            System.out.println("Available width: " + checkAvailableWidth(position));
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

    @Override
    public int checkAvailableWidth(Position position) {
        int availableWidth = 0;
        for (int[] openSpace : this.getOpenSpacesOn(position, false)) {
            availableWidth += openSpace[1] - openSpace[0];
        }
        return availableWidth;
    }

    private List<PlaceableDocumentPart> getPartsOnLine(Position pos) {
        List<PlaceableDocumentPart> contentOnSameLine = new ArrayList<PlaceableDocumentPart>();
        for (DocumentPart p : content) {
            if (p instanceof PlaceableDocumentPart) {
                PlaceableDocumentPart part = (PlaceableDocumentPart) p;
                if (onSameLine(pos, part)) {
                    contentOnSameLine.add(part);
                }
            }
        }
        return contentOnSameLine;
    }

    private boolean onSameLine(Position position, PlaceableDocumentPart part) {
        //        System.out.println("Part pos: " + part.getPosition().getX() + ", " + part.getPosition().getY());
        //        System.out.println("Part check values, equal/smaller than: " + (part.getPosition().getY() + part.getLeading()) + ", larger/equal than "
        //                + (part.getPosition().getY() - (part.getContentHeight(this) - part.getLeading()) - (DEFAULT_NEW_LINE_SIZE / 2)));
        //        System.out.println("Result: " + (position.getY() <= part.getPosition().getY() + part.getLeading()) + ", "
        //                + (position.getY() >= part.getPosition().getY() - (part.getContentHeight(this) - part.getLeading()) - (DEFAULT_NEW_LINE_SIZE / 2)));

        if (position.getY() <= part.getPosition().getY() + part.getLeading()
                && position.getY() >= part.getPosition().getY() - (part.getContentHeight(this) - part.getLeading()) - (DEFAULT_NEW_LINE_SIZE / 2)) {
            return true;
        }
        return false;
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
    public List<int[]> getOpenSpacesOn(Position pos, boolean ignoreSpacesBeforePositionWidth) {
        List<PlaceableDocumentPart> parts = this.getPartsOnLine(pos);
        System.out.println("Part amount: " + parts.size());
        List<int[]> openSpaces = new ArrayList<int[]>();

        //this might cause trouble with fixed position stuff beyond the margins
        int startingPoint = marginLeft;
        if (ignoreSpacesBeforePositionWidth) {
            startingPoint = pos.getX();
        }
        openSpaces.add(new int[] { startingPoint, (width - marginRight) });
        for (PlaceableDocumentPart part : parts) {
            for (int[] usedSpace : part.getUsedSpaces(pos.getY())) {
                openSpaces = adjustOpenSpaces(openSpaces, usedSpace);
            }
        }
        return openSpaces;
    }

    private List<int[]> adjustOpenSpaces(List<int[]> openSpaces, int[] usedSpace) {
        List<int[]> newOpenSpaces = new ArrayList<int[]>();
        System.out.println("UsedSpace!: " + usedSpace[0] + ", " + usedSpace[1]);
        for (int[] openSpace : openSpaces) {
            if (usedSpace[0] >= openSpace[0] && usedSpace[1] < openSpace[1]) {
                if (openSpace[0] != usedSpace[0]) {
                    newOpenSpaces.add(new int[] { openSpace[0], usedSpace[0] });
                }

                if (usedSpace[1] != openSpace[1]) {
                    newOpenSpaces.add(new int[] { usedSpace[1], openSpace[1] });
                }
            } else {
                newOpenSpaces.add(openSpace);
            }
        }
        return newOpenSpaces;
    }
}
