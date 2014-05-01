package nl.mad.toucanpdf.model;

public interface StateImage extends Image, StatePlaceableDocumentPart {

    void processContentSize(StatePage page);

    void processContentSize(StatePage page, boolean wrapping);

}
