package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontMetrics;
import nl.mad.pdflibrary.model.Page;
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
    private double scaleX;
    private double scaleY;
    private double shearX;
    private double shearY;
    private Font font;
    private Map<Position, String> textSplit;

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
     * @param text
     */
    public BaseText(String text) {
        super(DocumentPartType.TEXT);
        textString = text;
        textSplit = new LinkedHashMap<>();
        textSize = DEFAULT_TEXT_SIZE;
        font = Constants.DEFAULT_FONT;
        scaleX = 1;
        scaleY = 1;
        shearX = 0;
        shearY = 0;
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
    public void processContentSize(Page page, boolean inParagraph, int positionX, boolean wrappable) {
        ArrayList<String> strings = new ArrayList<String>(Arrays.asList(textString.split(" ")));
        int leading = this.getLeading();
        this.textSplit = new LinkedHashMap<Position, String>();
        int i = 0;
        boolean stringsProcessed = false;
        Position pos = new Position(this.getPosition());

        while (!stringsProcessed && i < strings.size()) {
            System.out.println("i: " + i + ", currentString: " + strings.get(i) + ", total size: " + strings.size());
            System.out.println("Pos: " + pos.getX() + "." + pos.getY());
            List<int[]> openSpaces = page.getOpenSpacesOn(pos, true);
            for (int[] openSpace : openSpaces) {
                System.out.println("Open space: " + openSpace[0] + "-" + openSpace[1]);
            }
            i += splitText(openSpaces, strings.subList(i, strings.size()), pos, page);
            if (i < (strings.size() - 1)) {
                page.setFilledHeight(page.getFilledHeight() + leading);
                pos = page.getOpenPosition(pos.getY() - leading, leading);
                if (pos == null) {
                    System.out.println("WE HAVE A LEAK");
                    //overflow
                }
            } else if (!inParagraph && !wrappable) {
                page.setFilledHeight(page.getFilledHeight() + leading);
            }
        }
    }

    private int splitText(List<int[]> openSpaces, List<String> strings, Position pos, Page page) {
        StringBuilder currentLine = new StringBuilder();
        int[] openSpace = openSpaces.get(0);
        int i = 0;
        double width = 0;
        boolean openSpacesFilled = false;
        FontMetrics metrics = font.getMetrics();
        int openSpaceIndex = 0;
        int iReturnOffset = 0;
        while (!openSpacesFilled && i < strings.size()) {
            String s = strings.get(i);
            double oldWidth = width;
            //TODO: fix use of "space"
            width += metrics.getWidthPointOfString(s, textSize, true) + (metrics.getWidthPoint("space") * textSize);
            System.out.println("CurrentLine: " + currentLine.toString() + ", String: " + s);
            System.out.println("Width of string: " + width + ", availableWidth: " + (openSpace[1] - openSpace[0]));
            if (width > openSpace[1] - openSpace[0]) {
                //currentLine = new StringBuilder(processCutOff(openSpace[0] + oldWidth, openSpace[1], currentLine.toString(), strings, i, page, pos));
                if (!currentLine.toString().isEmpty()) {
                    Position position = new Position(openSpace[0], pos.getY());
                    addTextSplitEntry(position, currentLine.toString());
                    currentLine = new StringBuilder();
                }
                if (openSpaceIndex != (openSpaces.size() - 1)) {
                    openSpace = openSpaces.get(openSpaceIndex + 1);
                    width = 0;
                    ++openSpaceIndex;
                } else {
                    openSpacesFilled = true;
                }
            }
            currentLine.append(s);
            currentLine.append(' ');

            if (i == (strings.size() - 1)) {
                if (!openSpacesFilled && width < openSpace[1] - openSpace[0]) {
                    Position position = new Position((int) (openSpace[0] + width), pos.getY());
                    //currentLine = new StringBuilder(processCutOff(oldWidth, (openSpace[1] - openSpace[0]), currentLine.toString(), strings, i, page));
                    addTextSplitEntry(position, currentLine.toString());
                } else {
                    ++iReturnOffset;
                }
            }
            ++i;
        }
        return i - iReturnOffset;
    }

    private void addTextSplitEntry(Position position, String string) {
        if (textSplit.isEmpty()) {
            this.setPosition(new Position(position.getX(), position.getY()));
        }
        textSplit.put(position, string);
    }

    private String processCutOff(double width, int widthLimit, String currentLine, List<String> text, int currentTextIndex, Page page, Position pos) {
        if (widthLimit == (page.getWidth() - page.getMarginRight())
                && page.checkAvailableWidth(pos) > ((page.getWidth() - page.getMarginRight()) * BasePage.CUT_OFF_POINT_PERCENTAGE) || width == 0) {
            double currentWidth = width;
            StringBuilder currentString = new StringBuilder(currentLine);
            FontMetrics metrics = font.getMetrics();
            char[] charArray = text.get(currentTextIndex).toCharArray();
            int i = 0;

            while (currentWidth < widthLimit && i != charArray.length) {
                char c = charArray[i];
                double characterSize = 0.0;
                if (i + 1 != charArray.length) {
                    characterSize = (((metrics.getWidth(c) - metrics.getKerning(c, charArray[i + 1])) * textSize) * metrics.getConversionToPointsValue());
                } else {
                    characterSize = metrics.getWidthPoint(c) * textSize;
                }
                currentWidth += characterSize;
                if (currentWidth < widthLimit) {
                    currentString.append(c);
                }
                ++i;
            }
            System.out.println(currentString.toString());
            text.set(currentTextIndex, "");
            text.add(currentTextIndex + 1, String.valueOf(charArray).substring(i - 1));
            return currentString.toString();
        }
        System.out.println("Over 90% already dude");
        return currentLine;
    }

    private void handleOverflow(int overflowStart, List<String> strings) {
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
    }

    @Override
    public Map<Position, String> getTextSplit() {
        return this.textSplit;
    }

    @Override
    public int getContentHeight(Page page) {
        return font.getLeading(textSize) * textSplit.entrySet().size();
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
    public int getLeading() {
        return font.getLeading(textSize);
    }

    @Override
    public int[] getPositionAt(int height) {
        List<Entry<Position, String>> entries = this.getEntriesAtHeight(height);
        int[] positions = new int[0];
        if (!entries.isEmpty()) {
            positions = new int[entries.size()];
            for (int i = 0; i < entries.size(); ++i) {
                positions[i] = entries.get(i).getKey().getX();
            }
        }
        return positions;
    }

    private List<Entry<Position, String>> getEntriesAtHeight(int height) {
        List<Entry<Position, String>> entries = new LinkedList<>();
        for (Entry<Position, String> entry : textSplit.entrySet()) {
            Position linePos = entry.getKey();
            if (height <= linePos.getY() + (Page.DEFAULT_NEW_LINE_SIZE / 2) && height >= linePos.getY() - font.getLeading(textSize)) {
                entries.add(entry);
            }
        }
        return entries;
    }

    @Override
    public List<int[]> getUsedSpaces(int height) {
        List<int[]> spaces = new LinkedList<>();
        List<Entry<Position, String>> entries = getEntriesAtHeight(height);
        FontMetrics metrics = font.getMetrics();
        for (Entry<Position, String> entry : entries) {
            double stringWidth = metrics.getWidthPointOfString(entry.getValue(), textSize, true) + (metrics.getWidthPoint("space") * textSize);
            spaces.add(new int[] { entry.getKey().getX(), (int) (entry.getKey().getX() + stringWidth) });
        }
        return spaces;
    }
}
