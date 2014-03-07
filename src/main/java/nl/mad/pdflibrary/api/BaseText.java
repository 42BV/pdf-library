package nl.mad.pdflibrary.api;

import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.Text;

/**
 * This is the text document part, storing all the data necessary for showing text in a document.
 * 
 * @author Dylan de Wolff
 * @see AbstractDocumentPart
 */
public class BaseText extends AbstractPlaceableDocumentPart implements Text {
    private String textString;
    private int textSize;
    private static final int defaultTextSize = 12;
    private double scaleX;
    private double scaleY;
    private double shearX;
    private double shearY;
    private Font font;

    /**
     * Creates a new text instance.
     */
    public BaseText() {
        this("", defaultTextSize, Document.DEFAULT_FONT, 0, 0, 1, 1, 0, 0);
        this.setCustomPositioning(false);
    }

    /**
     * Creates a new text instance. 
     * 
     * @param text The text that needs to be shown.
     * @param textSize The size of the text.
     */
    public BaseText(String text, int textSize) {
        this(text, textSize, Document.DEFAULT_FONT, 0, 0, 1, 1, 0, 0);
        this.setCustomPositioning(false);
    }

    /**
     * Creates a new text instance.
     * 
     * @param text The text that needs to be shown.
     * @param textSize The size of the text.
     * @param font The font that's used for the text.
     */
    public BaseText(String text, int textSize, Font font) {
        this(text, textSize, font, 0, 0, 1, 1, 0, 0);
        this.setCustomPositioning(false);
    }

    /**
     * Creates a new text instance.
     * @param text The text that needs to be shown.
     * @param textSize The size of the text.
     * @param posX The x position of the text from the lower left corner.
     * @param posY The y position of the text from the lower left corner.
     */
    public BaseText(String text, int textSize, int posX, int posY) {
        this(text, textSize, Document.DEFAULT_FONT, posX, posY, 1, 1, 0, 0);
    }

    /**
     * Creates a new text instance.
     * @param text The text that needs to be shown.
     * @param textSize The size of the text.
     * @param font The font of the text.
     * @param posX The x position of the text from the lower left corner.
     * @param posY The y position of the text from the lower left corner.
     */
    public BaseText(String text, int textSize, Font font, int posX, int posY) {
        this(text, textSize, font, posX, posY, 1, 1, 0, 0);
    }

    /**
     * Creates a new text instance.
     * 
     * @param text The text that needs to be shown.
     * @param textSize The size of the text.
     * @param font The font that's used for the text.
     * @param posX The x position of the text from the lower left corner.
     * @param posY The y position of the text from the lower left corner.
     * @param scaleX The scale of the text on the X-axis.
     * @param scaleY The scale of the text on the Y-axis.
     * @param shearX The shear (tilt) of the text on the X-axis.
     * @param shearY The shear (tilt) of the text on the Y-axis.
     */
    public BaseText(String text, int textSize, Font font, int posX, int posY, double scaleX, double scaleY, double shearX, double shearY) {
        super(DocumentPartType.TEXT);
        this.textString = text;
        this.font = font;
        this.setPositionX(posX);
        this.setPositionY(posY);
        this.setTextSize(textSize);
        this.setScaleX(scaleX);
        this.setScaleY(scaleY);
        this.setShearX(shearX);
        this.setShearY(shearY);
    }

    /**
     * Creates a copy of the given BaseText.
     * @param copyFrom BaseText to copy from.
     */
    public BaseText(Text copyFrom) {
        super(DocumentPartType.TEXT);
        this.textString = copyFrom.getText();
        this.font = copyFrom.getFont();
        if (copyFrom.getCustomPositioning()) {
            this.setPositionX(copyFrom.getPositionX());
            this.setPositionY(copyFrom.getPositionY());
            this.setCustomPositioning(true);
        } else {
            this.setPositionX(0);
            this.setPositionY(0);
            this.setCustomPositioning(false);
        }
        this.setTextSize(copyFrom.getTextSize());
        this.setScaleX(copyFrom.getScaleX());
        this.setScaleY(copyFrom.getScaleY());
        this.setShearX(copyFrom.getShearX());
        this.setShearY(copyFrom.getShearY());
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

    public void setTextSize(int textSize) {
        if (textSize >= 0) {
            this.textSize = textSize;
        } else {
            this.textSize = 0;
        }
    }

    public boolean textMatrixEquals(Text text) {
        if (getPositionX() == text.getPositionX() && getPositionY() == text.getPositionY() && scaleX == text.getScaleX() && scaleY == text.getScaleY()
                && shearX == text.getShearX() && shearY == text.getShearY()) {
            return true;
        }
        return false;
    }
}
