package nl.mad.pdflibrary.model;

public interface PlaceableDocumentPart extends DocumentPart {

    public int getPositionX();

    public void setPositionX(int positionX);

    public int getPositionY();

    public void setPositionY(int positionY);

    boolean getCustomPositioning();

}
