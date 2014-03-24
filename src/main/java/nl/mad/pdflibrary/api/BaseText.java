package nl.mad.pdflibrary.api;

import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.Text;
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
        textSize = DEFAULT_TEXT_SIZE;
        font = Document.DEFAULT_FONT;
        this.setPosition(new Position());
        scaleX = 1;
        scaleY = 1;
        shearX = 0;
        shearY = 0;
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
}
