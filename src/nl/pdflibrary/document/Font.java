package nl.pdflibrary.document;

public class Font {
    private FontFamily family;
    private FontStyle style;
    //temp attribute!
    private String filename;

    public Font(FontFamily family, FontStyle style) {
        this.family = family;
        this.style = style;
    }

    public Font(String filename) {
        this.filename = filename;
    }
}
