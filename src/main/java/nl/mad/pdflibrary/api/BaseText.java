package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontMetrics;
import nl.mad.pdflibrary.model.ObserverEvent;
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
    private List<String> textArray;

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
        textArray = Arrays.asList(textString.split(" "));
        textSize = DEFAULT_TEXT_SIZE;
        font = Constants.DEFAULT_FONT;
        scaleX = 1;
        scaleY = 1;
        shearX = 0;
        shearY = 0;
        this.setPosition(new Position());
        this.notifyObserversOfChange();
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
        this.textArray = copyFrom.getTextArray();
    }

    @Override
    public double getScaleX() {
        return scaleX;
    }

    @Override
    public Text scale(double newScaleX, double newScaleY) {
        this.scaleX = newScaleX;
        this.scaleY = newScaleY;
        this.notifyObserversOfChange();
        return this;
    }

    @Override
    public Text scaleX(double newScaleX) {
        this.scaleX = newScaleX;
        this.notifyObserversOfChange();
        return this;
    }

    @Override
    public double getScaleY() {
        return scaleY;
    }

    @Override
    public Text scaleY(double newScaleY) {
        this.scaleY = newScaleY;
        this.notifyObserversOfChange();
        return this;
    }

    @Override
    public Text shear(double newShearX, double newShearY) {
        this.shearX = newShearX;
        this.shearY = newShearY;
        this.notifyObserversOfChange();
        return this;
    }

    @Override
    public double getShearX() {
        return shearX;
    }

    @Override
    public Text shearX(double newShearX) {
        this.shearX = newShearX;
        this.notifyObserversOfChange();
        return this;
    }

    @Override
    public double getShearY() {
        return shearY;
    }

    @Override
    public Text shearY(double newShearY) {
        this.shearY = newShearY;
        this.notifyObserversOfChange();
        return this;
    }

    @Override
    public String getText() {
        return textString;
    }

    @Override
    public Text text(String text) {
        this.textString = text;
        this.notifyObserversOfChange();
        return this;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public Text font(Font newFont) {
        this.font = newFont;
        this.notifyObserversOfChange();
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
        this.notifyObserversOfChange();
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
        this.notifyObserversOfChange();
        return this;
    }

    @Override
    public Text on(Position position) {
        this.setPosition(position);
        this.notifyObserversOfChange();
        return this;
    }

    private double getEffectiveHeight(Page page) {
        double height = page.getRemainingHeight();
        if (this.getPosition().hasCustomYValue()) {
            height = this.getPosition().getY();
        }
        return height;
    }

    @Override
    public void processContentSize(Page page, boolean inParagraph, int positionX) {
        ArrayList<String> strings = new ArrayList<String>(Arrays.asList(textString.split(" ")));
        int leading = font.getLeading(textSize);
        this.textArray = new ArrayList<String>();
        FontMetrics metrics = font.getFontFamily().getMetricsForStyle(font.getStyle());
        this.calculatePosition(page, inParagraph);
        StringBuilder currentLine = new StringBuilder();
        double height = getEffectiveHeight(page);
        double width = this.getPosition().getX() + page.getMarginRight();
        int i = 0;
        boolean overflowFound = false;
        while (!overflowFound && i < strings.size()) {
            double oldWidth = width;
            width += metrics.getWidthPointOfString(strings.get(i), textSize, true) + (metrics.getWidthPoint("space") * textSize);
            //exceeded width limit
            if (width > page.getWidthWithoutMargins()) {
                currentLine = new StringBuilder(processCutOff(oldWidth, page, currentLine.toString(), strings, i));
                height -= leading;
                if (height > 0) {
                    textArray.add(currentLine.toString());
                    textArray.add("\n");
                    width = processLineAdditionWidth(width, positionX, inParagraph);
                    currentLine = new StringBuilder();
                }
            }
            if (height <= 0) {
                height += leading;
                overflowFound = true;
                handleOverflow(i, strings);
            }
            currentLine.append(strings.get(i));
            currentLine.append(" ");
            ++i;
            //is this problematic for overflow?
            if (i == strings.size() && !overflowFound) {
                textArray.add(currentLine.toString());
                if (!inParagraph) {
                    height -= leading;
                }
            }
        }

        System.out.println(textString);
        System.out.println("new filled height: " + (page.getHeight() - height));
        System.out.println();
        page.setFilledHeight(page.getHeightWithoutMargins() - height);
        if (inParagraph) {
            page.setFilledWidth(width);
        } else {
            page.setFilledWidth(0);
        }
    }

    private double processLineAdditionWidth(double width, int positionX, boolean inParagraph) {
        if (inParagraph) {
            width = positionX;
        } else {
            width = this.getPosition().getX();
        }
        return width;
    }

    private String processCutOff(double width, Page page, String currentLine, List<String> text, int currentTextIndex) {
        //0 check should probably be originalWidth(?) check
        if (width < (page.getWidth() * BasePage.CUT_OFF_POINT_PERCENTAGE) || width == 0) {
            double currentWidth = width;
            StringBuilder currentString = new StringBuilder(currentLine);
            FontMetrics metrics = font.getFontFamily().getMetricsForStyle(font.getStyle());
            char[] charArray = text.get(currentTextIndex).toCharArray();
            int i = 0;

            while (currentWidth < page.getWidthWithoutMargins() && i != charArray.length) {
                char c = charArray[i];
                double characterSize = 0.0;
                if (i + 1 != charArray.length) {
                    characterSize = (((metrics.getWidth(c) - metrics.getKerning(c, charArray[i + 1])) * textSize) * metrics.getConversionToPointsValue());
                } else {
                    characterSize = metrics.getWidthPoint(c) * textSize;
                }
                currentWidth += characterSize;
                if (currentWidth < page.getWidth()) {
                    currentString.append(c);
                }
                ++i;
            }
            text.set(currentTextIndex, "");
            text.add(currentTextIndex + 1, String.valueOf(charArray).substring(i - 1));
            return currentString.toString();
        }
        return currentLine;
    }

    private void handleOverflow(int overflowStart, List<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : textArray) {
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

        this.notify(ObserverEvent.OVERFLOW, overflowText);
    }

    private void notifyObserversOfChange() {
        this.notifyObserversOfChange(ObserverEvent.RECALCULATE, null);
    }

    private void notifyObserversOfChange(ObserverEvent event, DocumentPart arg) {
        this.notify(event, arg);
    }

    @Override
    public List<String> getTextArray() {
        return this.textArray;
    }

    private void calculatePosition(Page page, boolean inParagraph) {
        Position position = getPosition();
        if (!position.hasCustomPosition()) {
            double spaceWidth = font.getFontFamily().getMetricsForStyle(font.getStyle()).getWidthPoint((int) ' ');
            if (inParagraph) {
                position.setX((int) (Math.ceil(page.getFilledWidth() + spaceWidth)));
                position.setY((int) (page.getRemainingHeight()));
            } else {
                position.setX(0);
                position.setY((int) (Math.ceil(page.getRemainingHeight() - font.getLeading(textSize))));
            }
        }
    }
}
