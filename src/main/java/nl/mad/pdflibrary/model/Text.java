package nl.mad.pdflibrary.model;

public interface Text extends PlaceableDocumentPart {

    public double getScaleX();

    public void setScaleX(double scaleX);

    public double getScaleY();

    public void setScaleY(double scaleY);

    public double getShearX();

    public void setShearX(double shearX);

    public double getShearY();

    public void setShearY(double shearY);

    public String getText();

    public void setText(String text);

    public Font getFont();

    public void setFont(Font font);

    public int getTextSize();

    public void setTextSize(int textSize);

    public boolean textMatrixEquals(Text text);

}
