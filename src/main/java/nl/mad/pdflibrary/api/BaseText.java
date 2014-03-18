package nl.mad.pdflibrary.api;

import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.Text;

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
     * Creates a new text instance. Will use default text size, default font and 
     * will automatically position the text in the document.
     */
    public BaseText() {
        this("", DEFAULT_TEXT_SIZE, Document.DEFAULT_FONT, new Position(), 1, 1, 0, 0);
    }

    /**
     * Creates a new text instance. Will use default font and 
     * will automatically position the text in the document.
     * 
     * @param text The text that needs to be shown.
     * @param textSize The size of the text.
     */
    public BaseText(String text, int textSize) {
        this(text, textSize, Document.DEFAULT_FONT, new Position(), 1, 1, 0, 0);
    }

    /**
     * Creates a new text instance. Will automatically position the given text in the document.
     * 
     * @param text The text that needs to be shown.
     * @param textSize The size of the text.
     * @param font The font that's used for the text.
     */
    public BaseText(String text, int textSize, Font font) {
        this(text, textSize, font, new Position(), 1, 1, 0, 0);
    }

    /**
     * Creates a new text instance with the default font. 
     * @param text The text that needs to be shown.
     * @param textSize The size of the text.
     * @param position The position of the text.
     */
    public BaseText(String text, int textSize, Position position) {
        this(text, textSize, Document.DEFAULT_FONT, position, 1, 1, 0, 0);
    }

    /**
     * Creates a new text instance.
     * @param text The text that needs to be shown.
     * @param textSize The size of the text.
     * @param font The font of the text.
     * @param posX The position of the text.
     */
    public BaseText(String text, int textSize, Font font, Position position) {
        this(text, textSize, font, position, 1, 1, 0, 0);
    }

    /**
     * Creates a new text instance.
     * 
     * @param text The text that needs to be shown.
     * @param textSize The size of the text.
     * @param font The font that's used for the text.
     * @param position The position used for the text.
     * @param scaleX The scale of the text on the X-axis.
     * @param scaleY The scale of the text on the Y-axis.
     * @param shearX The shear (tilt) of the text on the X-axis.
     * @param shearY The shear (tilt) of the text on the Y-axis.
     */
    public BaseText(String text, int textSize, Font font, Position position, double scaleX, double scaleY, double shearX, double shearY) {
        super(DocumentPartType.TEXT);
        this.textString = text;
        this.font = font;
        this.setPosition(position);
        this.textSize = textSize;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.shearX = shearX;
        this.shearY = shearY;
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

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public double getShearX() {
        return shearX;
    }

    public void setShearX(double shearX) {
        this.shearX = shearX;
    }

    public double getShearY() {
        return shearY;
    }

    public void setShearY(double shearY) {
        this.shearY = shearY;
    }

    public String getText() {
        return textString;
    }

    public void setText(String text) {
        this.textString = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getTextSize() {
        return textSize;
    }

    @Override
    public void setTextSize(int textSize) {
        if (textSize >= 0) {
            this.textSize = textSize;
        } else {
            this.textSize = 0;
        }
    }

    @Override
    public boolean textMatrixEquals(Text text) {
        if (getPosition().getX() == text.getPosition().getX() && getPosition().getY() == text.getPosition().getY() && scaleX == text.getScaleX()
                && scaleY == text.getScaleY() && shearX == text.getShearX() && shearY == text.getShearY()) {
            return true;
        }
        return false;
    }
}
