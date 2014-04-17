package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.PlaceableDocumentPart;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.utility.FloatEqualityTester;

/**
 * Base implementation of the page interface. BasePage stores a collection of DocumentParts and stores page specific
 * attributes such as page width/height and margins. 
 * @author Dylan de Wolff
 *
 */
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
        this.filledWidth = 0;
        this.marginBottom = page.getMarginBottom();
        this.marginLeft = page.getMarginLeft();
        this.marginTop = page.getMarginTop();
        this.filledHeight = marginTop;
        this.marginRight = page.getMarginRight();
        this.leading = page.getLeading();
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
        if (filledHeight == this.marginTop) {
            filledHeight = marginTop;
        }
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
        return this.getOpenPosition(0, 0);
    }

    @Override
    public Position getOpenPosition(double requiredSpaceAbove, double requiredSpaceBelow) {
        double posHeight = height - filledHeight - requiredSpaceAbove;
        System.out.println(posHeight);
        return this.getOpenPosition(0, posHeight, requiredSpaceAbove, requiredSpaceBelow);
    }

    @Override
    public Position getOpenPosition(double positionWidth, double positionHeight, double requiredSpaceAbove, double requiredSpaceBelow) {
        boolean openPositionFound = false;
        System.out.println("GetOpenPosition @ " + positionWidth + ":" + positionHeight);
        double potentialHeight = positionHeight;
        if (positionHeight > marginBottom) {
            double potentialWidth = positionWidth + marginLeft;
            Position position = new Position(potentialWidth, potentialHeight);
            while (!openPositionFound) {
                if (checkAvailableWidth(position, requiredSpaceAbove, requiredSpaceBelow) > (MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING)) {
                    return position;
                }
                if (potentialHeight <= marginBottom) {
                    return null;
                }
                potentialHeight -= leading;
                position = new Position(potentialWidth, potentialHeight);
            }
        }
        return null;
    }

    @Override
    public int checkAvailableWidth(Position position, double requiredSpaceAbove, double requiredSpaceBelow) {
        int availableWidth = 0;
        for (int[] openSpace : this.getOpenSpacesOn(position, true, requiredSpaceAbove, requiredSpaceBelow)) {
            availableWidth += openSpace[1] - openSpace[0];
        }
        return availableWidth;
    }

    private List<PlaceableDocumentPart> getPartsOnLine(Position pos, double requiredSpaceAbove, double requiredSpaceBelow) {
        List<PlaceableDocumentPart> contentOnSameLine = new ArrayList<PlaceableDocumentPart>();
        List<DocumentPart> pageContent = new LinkedList<DocumentPart>(content);
        for (int i = 0; i < pageContent.size(); ++i) {
            DocumentPart p = pageContent.get(i);
            if (p instanceof PlaceableDocumentPart) {
                if (DocumentPartType.PARAGRAPH.equals(p.getType())) {
                    Paragraph paragraph = (Paragraph) p;
                    pageContent.addAll(i + 1, paragraph.getTextCollection());
                } else {
                    PlaceableDocumentPart part = (PlaceableDocumentPart) p;
                    if (onSameLine(pos, requiredSpaceAbove, requiredSpaceBelow, part)) {
                        contentOnSameLine.add(part);
                    }
                }
            }
        }
        return contentOnSameLine;
    }

    private boolean onSameLine(Position position, double requiredSpaceAbove, double requiredSpaceBelow, PlaceableDocumentPart part) {
        if (part.getPosition().hasCustomPosition()) {
            double topLimit = part.getPosition().getY() + part.getRequiredSpaceAbove();
            double bottomLimit = part.getPosition().getY() - part.getContentHeight(this) + part.getRequiredSpaceAbove();
            double y = position.getY();
            double positionTopLimit = y + requiredSpaceAbove;
            double positionBottomLimit = y - requiredSpaceBelow;
            System.out.println("OnSameLine check for pos: " + position.getX() + ":" + position.getY());
            System.out.println("    Part pos: " + part.getPosition().getX() + ", " + part.getPosition().getY());
            System.out.println("    First check result: " + FloatEqualityTester.lessThanOrEqualTo(y, topLimit));
            System.out.println("    Second check result: " + FloatEqualityTester.greaterThanOrEqualTo(y, bottomLimit));
            System.out.println("    Third check result: "
                    + (FloatEqualityTester.greaterThan(positionTopLimit, bottomLimit) && FloatEqualityTester.lessThan(positionTopLimit, topLimit)));
            System.out.println("    Fourth check result: "
                    + (FloatEqualityTester.lessThan(positionBottomLimit, topLimit) && FloatEqualityTester.greaterThan(positionBottomLimit, bottomLimit)));
            System.out.println("    topLimit: " + topLimit);
            System.out.println("    bottomLimit: " + bottomLimit);
            System.out.println("    PosTopLimit: " + positionTopLimit);
            System.out.println("    PosBottomLimit: " + positionBottomLimit);

            if ((FloatEqualityTester.lessThanOrEqualTo(y, topLimit) && FloatEqualityTester.greaterThanOrEqualTo(y, bottomLimit))
                    || (FloatEqualityTester.greaterThan(positionTopLimit, bottomLimit) && FloatEqualityTester.lessThan(positionTopLimit, topLimit))
                    || (FloatEqualityTester.lessThan(positionBottomLimit, topLimit) && FloatEqualityTester.greaterThan(positionBottomLimit, bottomLimit))) {
                return true;
            }
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
    public List<int[]> getOpenSpacesOn(Position pos, boolean ignoreSpacesBeforePositionWidth, double requiredSpaceAbove, double requiredSpaceBelow) {
        List<PlaceableDocumentPart> parts = this.getPartsOnLine(pos, requiredSpaceAbove, requiredSpaceBelow);
        List<int[]> openSpaces = new ArrayList<int[]>();

        //this might cause trouble with fixed position stuff beyond the margins
        int startingPoint = marginLeft;
        if (ignoreSpacesBeforePositionWidth) {
            startingPoint = (int) pos.getX();
        }
        openSpaces.add(new int[] { startingPoint, (width - marginRight) });
        for (PlaceableDocumentPart part : parts) {
            for (int[] usedSpace : getUsedSpacesFrom(part, pos, requiredSpaceAbove, requiredSpaceBelow)) {
                System.out.println("Adjusting open spaces for part (pos): " + part.getPosition().getX() + ":" + part.getPosition().getY() + ", "
                        + part.getType());
                System.out.println("   " + usedSpace[0] + "-" + usedSpace[1]);
                openSpaces = adjustOpenSpaces(openSpaces, usedSpace);
            }
        }
        return openSpaces;
    }

    private List<int[]> getUsedSpacesFrom(PlaceableDocumentPart part, Position pos, double requiredSpaceAbove, double requiredSpaceBelow) {
        List<int[]> usedSpaces = new ArrayList<>();
        double[] heights = new double[] { pos.getY(), pos.getY() + requiredSpaceAbove, pos.getY() - requiredSpaceBelow };
        for (double y : heights) {
            usedSpaces.addAll(part.getUsedSpaces(y));
        }
        return usedSpaces;
    }

    private List<int[]> adjustOpenSpaces(List<int[]> openSpaces, int[] usedSpace) {
        List<int[]> newOpenSpaces = new ArrayList<int[]>();
        for (int[] openSpace : openSpaces) {
            if (usedSpace[0] >= openSpace[0] && usedSpace[1] <= openSpace[1]) {
                if (openSpace[0] != usedSpace[0]) {
                    addOpenSpaceToList(newOpenSpaces, new int[] { openSpace[0], usedSpace[0] });
                }
                if (usedSpace[1] != openSpace[1]) {
                    addOpenSpaceToList(newOpenSpaces, new int[] { usedSpace[1], openSpace[1] });
                }
            } else if (usedSpace[0] < openSpace[0] && usedSpace[1] > openSpace[0] && !(usedSpace[1] > openSpace[1])) {
                addOpenSpaceToList(newOpenSpaces, new int[] { usedSpace[1], openSpace[1] });
            } else if (usedSpace[0] < openSpace[1] && usedSpace[1] > openSpace[1] && !(usedSpace[0] < openSpace[0])) {
                addOpenSpaceToList(newOpenSpaces, new int[] { openSpace[0], usedSpace[0] });
            } else if (!(usedSpace[0] < openSpace[0] && usedSpace[1] > openSpace[1])) {
                addOpenSpaceToList(newOpenSpaces, openSpace);
            }
        }
        return newOpenSpaces;
    }

    private List<int[]> addOpenSpaceToList(List<int[]> openSpaces, int[] newOpenSpace) {
        if (newOpenSpace[0] != newOpenSpace[1]) {
            openSpaces.add(newOpenSpace);
        }
        return openSpaces;
    }

    @Override
    public int getLeading() {
        return leading;
    }

    @Override
    public Page leading(int leading) {
        if (leading >= 0) {
            this.leading = leading;
        }
        return this;
    }
}
