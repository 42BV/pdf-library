package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.mad.pdflibrary.model.Alignment;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontMetrics;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.PlaceableDocumentPart;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.Text;
import nl.mad.pdflibrary.utility.Constants;
import nl.mad.pdflibrary.utility.FloatEqualityTester;

/**
 * BaseText is the default Text document part. Adding an instance of this class to the Document class will 
 * add the text in this instance to the actual document. This class stores all the data necessary to show the text.
 * 
 * @author Dylan de Wolff
 * @see AbstractDocumentPart
 */
public class BaseText extends AbstractPlaceableDocumentPart implements Text {
    private String textString;
    private int textSize;
    private static final int DEFAULT_TEXT_SIZE = 12;
    private double scaleX = 1;
    private double scaleY = 1;
    private double shearX = 0;
    private double shearY = 0;
    private Alignment alignment = Alignment.LEFT;
    private Font font;
    private Map<Position, String> textSplit;
    private Map<Position, Double> justificationOffsets;

    /**
     * Creates a new text instance with the given text. Will use default text size, default font and 
     * will automatically position the text in the document.
     */
    public BaseText() {
        this("");
    }

    /**
     * Creates a new text instance. Will use default text size, default font and
     * will automatically position the text in the document.
     * @param text The text to use.
     */
    public BaseText(String text) {
        super(DocumentPartType.TEXT);
        textString = text;
        textSplit = new LinkedHashMap<>();
        justificationOffsets = new HashMap<>();
        textSize = DEFAULT_TEXT_SIZE;
        font = Constants.DEFAULT_FONT;
        this.setPosition(new Position());
    }

    /**
     * Creates a copy of the given Text object.
     * @param copyFrom Text object to copy from.
     */
    public BaseText(Text copyFrom) {
        super(DocumentPartType.TEXT);
        this.textString = copyFrom.getText();
        this.font = copyFrom.getFont();
        this.setPosition(copyFrom.getPosition());
        this.textSize = copyFrom.getTextSize();
        this.scaleX = copyFrom.getScaleX();
        this.scaleY = copyFrom.getScaleY();
        this.shearX = copyFrom.getShearX();
        this.shearY = copyFrom.getShearY();
        this.textSplit = copyFrom.getTextSplit();
        this.justificationOffsets = copyFrom.getJustificationOffset();
        this.alignment = copyFrom.getAlignment();
    }

    @Override
    public double getScaleX() {
        return scaleX;
    }

    @Override
    public Text scale(double newScaleX, double newScaleY) {
        this.scaleX = newScaleX;
        this.scaleY = newScaleY;
        return this;
    }

    @Override
    public Text scaleX(double newScaleX) {
        this.scaleX = newScaleX;
        return this;
    }

    @Override
    public double getScaleY() {
        return scaleY;
    }

    @Override
    public Text scaleY(double newScaleY) {
        this.scaleY = newScaleY;
        return this;
    }

    @Override
    public Text shear(double newShearX, double newShearY) {
        this.shearX = newShearX;
        this.shearY = newShearY;
        return this;
    }

    @Override
    public double getShearX() {
        return shearX;
    }

    @Override
    public Text shearX(double newShearX) {
        this.shearX = newShearX;
        return this;
    }

    @Override
    public double getShearY() {
        return shearY;
    }

    @Override
    public Text shearY(double newShearY) {
        this.shearY = newShearY;
        return this;
    }

    @Override
    public String getText() {
        return textString;
    }

    @Override
    public Text text(String text) {
        this.textString = text;
        return this;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public Text font(Font newFont) {
        this.font = newFont;
        return this;
    }

    @Override
    public int getTextSize() {
        return textSize;
    }

    @Override
    public Text size(int newTextSize) {
        if (newTextSize >= 0) {
            this.textSize = newTextSize;
        } else {
            this.textSize = 0;
        }
        return this;
    }

    @Override
    public boolean textMatrixEquals(Text text) {
        if (FloatEqualityTester.equals(getPosition().getX(), text.getPosition().getX())
                && FloatEqualityTester.equals(getPosition().getY(), text.getPosition().getY()) && FloatEqualityTester.equals(scaleX, text.getScaleX())
                && FloatEqualityTester.equals(scaleY, text.getScaleY()) && FloatEqualityTester.equals(shearX, text.getShearX())
                && FloatEqualityTester.equals(shearY, text.getShearY())) {
            return true;
        }
        return false;
    }

    @Override
    public Text on(int x, int y) {
        this.setPosition(new Position(x, y));
        return this;
    }

    @Override
    public Text on(Position position) {
        this.setPosition(position);
        return this;
    }

    @Override
    public Text processContentSize(Page page, double positionX, boolean fixedPosition) {
        System.out.println("PositionX : " + positionX);
        System.out.println("PositionY : " + this.getPosition().getY());
        System.out.println("Page filled height : " + page.getFilledHeight());
        System.out.println("Processing content size of: " + textString);
        ArrayList<String> strings = new ArrayList<String>(Arrays.asList(textString.split(" ")));
        double leading = page.getLeading() + getRequiredSpaceBelow();
        System.out.println("My leading is: " + leading);
        this.textSplit = new LinkedHashMap<Position, String>();
        int i = 0;
        boolean stringsProcessed = false;
        Position pos = new Position(this.getPosition());
        Text overflowText = null;
        System.out.println("String size = " + strings.size());
        while (!stringsProcessed && i < strings.size()) {
            List<int[]> openSpaces = getOpenSpaces(pos, page, fixedPosition);
            for (int[] openSpace : openSpaces) {
                System.out.println("Open space: " + openSpace[0] + "-" + openSpace[1]);
            }
            System.out.println("i value before: " + i);
            i += splitText(openSpaces, strings.subList(i, strings.size()), pos, page);
            System.out.println("Resulting i from splitText = " + i);
            pos = handleTextAddition(page, leading, pos, positionX, fixedPosition);
            //this might cause trouble if the final text has been added and there is no more room on the page
            if (pos == null) {
                System.out.println("We've encountered overflow");
                overflowText = handleOverflow(i, strings);
                stringsProcessed = true;
            }
        }

        if (!fixedPosition) {
            //page.setFilledHeight(page.getFilledHeight() + this.getRequiredSpaceBelow());
        }
        return overflowText;
    }

    private List<int[]> getOpenSpaces(Position pos, Page page, boolean fixedPosition) {
        List<int[]> openSpaces;
        if (!fixedPosition) {
            openSpaces = page.getOpenSpacesOn(pos, true, getRequiredSpaceAbove(), getRequiredSpaceBelow());
        } else {
            openSpaces = new ArrayList<int[]>();
            openSpaces.add(new int[] { (int) pos.getX(), page.getWidth() - page.getMarginRight() });
        }
        return openSpaces;
    }

    private int splitText(List<int[]> openSpaces, List<String> strings, Position pos, Page page) {
        List<String> stringsCopy = new LinkedList<String>(strings);
        stringsCopy.add("");
        StringBuilder currentLine = new StringBuilder();
        int[] openSpace = openSpaces.get(0);
        int i = 0;
        int lastAdditionIndex = 0;
        int additions = 0;
        int cutOffAdditions = 0;
        double width = 0;
        boolean openSpacesFilled = false;
        FontMetrics metrics = font.getMetrics();
        int openSpaceIndex = 0;
        while (!openSpacesFilled && i < stringsCopy.size()) {
            String s = stringsCopy.get(i);
            double oldWidth = width;
            //TODO: fix use of "space"
            if (i != (stringsCopy.size() - 1)) {
                width += metrics.getWidthPointOfString(s, textSize, true) + (metrics.getWidthPoint("space") * textSize);
            }
            System.out.println("CurrentLine: " + currentLine.toString() + ", String: " + s);
            //System.out.println("Width of string: " + width + ", availableWidth: " + (openSpace[1] - openSpace[0]));
            if ((width > openSpace[1] - openSpace[0] || i == (stringsCopy.size() - 1)) && oldWidth < openSpace[1] - openSpace[0]) {
                System.out.println("Adding line through second check: " + (i == (stringsCopy.size() - 1)));
                System.out.println("Width on adding: " + width + ", available width in this openspace: " + (openSpace[1] - openSpace[0]));
                String cutOffLine = processCutOff(openSpace[0] + oldWidth, openSpace[1], width - oldWidth, currentLine.toString(), stringsCopy, i, page);
                if (!cutOffLine.equals(currentLine.toString())) {
                    strings.add(i + 1, stringsCopy.get(i + 1));
                    ++cutOffAdditions;
                    for (int b = 0; b < strings.size(); ++b) {
                        System.out.println("Ze string iz: " + strings.get(b) + ", on index: " + b);
                    }
                    System.out.println("And for the copy:");
                    for (int c = 0; c < strings.size(); ++c) {
                        System.out.println("Ze string iz: " + strings.get(c) + ", on index: " + c);
                    }
                    currentLine = new StringBuilder(cutOffLine);
                }
                if (!currentLine.toString().isEmpty()) {
                    lastAdditionIndex = i;
                    ++additions;
                    Position position = new Position(openSpace[0], pos.getY());
                    width = metrics.getWidthPointOfString(currentLine.toString(), textSize, true) + (metrics.getWidthPoint("space") * textSize);
                    position = processAlignment(currentLine.toString(), position, width, openSpace[1] - openSpace[0]);
                    addTextSplitEntry(position, currentLine.toString());
                    currentLine = new StringBuilder();
                }
                if (openSpaceIndex != (openSpaces.size() - 1)) {
                    openSpace = openSpaces.get(openSpaceIndex + 1);
                    width = metrics.getWidthPointOfString(stringsCopy.get(i), textSize, true) - (metrics.getWidthPoint("space") * textSize);
                    ++openSpaceIndex;
                } else {
                    openSpacesFilled = true;
                }
            }
            System.out.println("what is stringcopy.geti: " + stringsCopy.get(i) + ", What is s, baby dont hurt me: " + s);
            currentLine.append(stringsCopy.get(i));
            currentLine.append(' ');
            ++i;
        }
        if (cutOffAdditions > 0) {
            --cutOffAdditions;
        }
        //if we only do a single loop we should still skip the current text if it was added.
        if (lastAdditionIndex == 0 && additions > 0) {
            ++lastAdditionIndex;
        }
        System.out.println("Returning the i value: " + (lastAdditionIndex + cutOffAdditions));
        return lastAdditionIndex + cutOffAdditions;
    }

    private Position processAlignment(String line, Position position, double width, int openSpaceSize) {
        Position newPos = new Position(position);
        double remainingWidth = openSpaceSize - width;
        //do alignment
        switch (alignment) {
        case RIGHT:
            newPos.setX(position.getX() + remainingWidth);
            break;
        case CENTERED:
            newPos.setX(position.getX() + (remainingWidth / 2));
            break;
        case JUSTIFIED:
            int wordAmount = line.split(" ").length;
            double justificationOffset = remainingWidth / wordAmount;
            justificationOffsets.put(newPos, justificationOffset);
            break;
        default:
            break;
        }
        return newPos;
    }

    private void addTextSplitEntry(Position position, String string) {
        if (textSplit.isEmpty()) {
            this.setPosition(new Position(position.getX(), position.getY()));
        }
        textSplit.put(position, string);
    }

    private String processCutOff(double width, int widthLimit, double widthOfCurrentString, String currentLine, List<String> text, int currentTextIndex,
            Page page) {
        if (FloatEqualityTester.greaterThan((widthLimit - width), ((page.getWidth() - page.getMarginRight()) * (1.0 - BasePage.CUT_OFF_POINT_PERCENTAGE)))) {
            double currentWidth = width;
            StringBuilder currentString = new StringBuilder(currentLine);
            FontMetrics metrics = font.getMetrics();
            char[] charArray = text.get(currentTextIndex).toCharArray();
            int i = 0;
            double dashWidth = metrics.getWidthPoint("endash") * textSize;
            while (currentWidth < (widthLimit - dashWidth) && i != charArray.length) {
                char c = charArray[i];
                double characterSize = 0.0;
                if (i + 1 != charArray.length) {
                    characterSize = (((metrics.getWidth(c) - metrics.getKerning(c, charArray[i + 1])) * textSize) * metrics.getConversionToPointsValue());
                } else {
                    characterSize = metrics.getWidthPoint(c) * textSize;
                }
                currentWidth += characterSize;
                if (currentWidth < widthLimit - dashWidth) {
                    currentString.append(c);
                }
                ++i;
            }
            if (i != 0) {
                currentString.append('-');
                text.set(currentTextIndex, "");
                text.add(currentTextIndex + 1, String.valueOf(charArray).substring(i - 1));
            }
            return currentString.toString();
        }
        return currentLine;
    }

    private Position handleTextAddition(Page page, double leading, Position pos, double positionX, boolean fixedPosition) {
        Position newPos;
        if (!fixedPosition) {
            newPos = page.getOpenPosition(positionX, pos.getY() - leading - this.getRequiredSpaceAbove(), this.getRequiredSpaceAbove(),
                    this.getRequiredSpaceBelow(), 0);
            double heightDifference = (page.getHeight() - page.getFilledHeight()) - pos.getY();
            page.setFilledHeight(page.getFilledHeight() + heightDifference + page.getLeading() + this.getRequiredSpaceBelow());

        } else {
            newPos = new Position(positionX, pos.getY() - leading - this.getRequiredSpaceAbove());
        }
        return newPos;
    }

    private Text handleOverflow(int overflowStart, List<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : textSplit.values()) {
            if (!"\n".equals(s)) {
                sb.append(s);
                sb.append(" ");
            }
        }
        this.textString = sb.toString();

        sb = new StringBuilder();
        for (int i = overflowStart; i < strings.size(); ++i) {
            sb.append(strings.get(i));
            sb.append(" ");
        }
        Text overflowText = new BaseText(this).text(sb.toString());
        overflowText.on(new Position());
        return overflowText;
    }

    @Override
    public Map<Position, String> getTextSplit() {
        return this.textSplit;
    }

    @Override
    public double getContentHeight(Page page) {
        double lowestHeight = this.getPosition().getY();
        double highestHeight = 0;
        for (Entry<Position, String> entry : textSplit.entrySet()) {
            Double posY = entry.getKey().getY();
            highestHeight = Math.max(highestHeight, posY);
            lowestHeight = Math.min(lowestHeight, posY);
        }
        return highestHeight - lowestHeight + this.getRequiredSpaceAbove() + this.getRequiredSpaceBelow();
    }

    @Override
    public double getContentHeightUnderBaseLine(Page page) {
        return this.getContentHeight(page) - getRequiredSpaceAbove();
    }

    @Override
    public int getContentWidth(Page page, Position position) {
        FontMetrics metrics = font.getMetrics();
        List<Entry<Position, String>> entries = this.getEntriesAtHeight(position.getY());
        int width = 0;
        for (Entry<Position, String> entry : entries) {
            width += (int) (metrics.getWidthPointOfString(entry.getValue(), textSize, true) + (metrics.getWidthPoint("space") * textSize));
        }
        return width;
    }

    @Override
    public int[] getPositionAt(double height) {
        List<Entry<Position, String>> entries = this.getEntriesAtHeight(height);
        int[] positions = new int[0];
        if (!entries.isEmpty()) {
            positions = new int[entries.size()];
            for (int i = 0; i < entries.size(); ++i) {
                positions[i] = (int) entries.get(i).getKey().getX();
            }
        }
        return positions;
    }

    private List<Entry<Position, String>> getEntriesAtHeight(double height) {
        List<Entry<Position, String>> entries = new LinkedList<>();
        for (Entry<Position, String> entry : textSplit.entrySet()) {
            Position linePos = entry.getKey();
            if (FloatEqualityTester.lessThanOrEqualTo(height, linePos.getY() + this.getRequiredSpaceAbove())
                    && FloatEqualityTester.greaterThanOrEqualTo(height, linePos.getY() - this.getRequiredSpaceBelow())) {
                entries.add(entry);
            }
        }
        return entries;
    }

    @Override
    public List<int[]> getUsedSpaces(double height) {
        System.out.println("  Getting used spaces for: " + this.textString);
        List<int[]> spaces = new LinkedList<>();
        List<Entry<Position, String>> entries = getEntriesAtHeight(height);
        FontMetrics metrics = font.getMetrics();
        for (int i = 0; i < entries.size(); ++i) {
            Entry<Position, String> entry = entries.get(i);
            double stringWidth = metrics.getWidthPointOfString(entry.getValue(), textSize, true);
            spaces.add(new int[] { (int) entry.getKey().getX(), (int) (entry.getKey().getX() + stringWidth) });
        }
        return spaces;
    }

    @Override
    public double getRequiredSpaceAbove() {
        return font.getMetrics().getAscentPoint() * textSize;
    }

    @Override
    public double getRequiredSpaceBelow() {
        return Math.abs(font.getMetrics().getDescentPoint() * textSize);
    }

    @Override
    public Text align(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @Override
    public Alignment getAlignment() {
        return this.alignment;
    }

    @Override
    public Map<Position, Double> getJustificationOffset() {
        return this.justificationOffsets;
    }

    @Override
    public PlaceableDocumentPart copy() {
        return new BaseText(this);
    }
}
