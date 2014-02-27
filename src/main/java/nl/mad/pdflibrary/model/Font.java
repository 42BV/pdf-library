package nl.mad.pdflibrary.model;

public interface Font extends DocumentPart {

    public FontFamily getFamily();

    public void setFamily(FontFamily family);

    public FontStyle getStyle();

    public void setStyle(FontStyle style);

    public BaseFontFamily getBaseFont();

}
