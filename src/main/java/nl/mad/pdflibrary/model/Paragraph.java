package nl.mad.pdflibrary.model;

import java.util.List;

public interface Paragraph extends PlaceableDocumentPart {

    public void addText(Text text);

    public List<Text> getTextCollection();

}
