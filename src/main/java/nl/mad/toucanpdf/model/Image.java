package nl.mad.toucanpdf.model;

import nl.mad.toucanpdf.image.ImageParser;

public interface Image extends PlaceableFixedSizeDocumentPart {

    Image align(Alignment alignment);

    Image height(int height);

    Image height(int height, boolean scaleWidth);

    Image width(int width);

    Image width(int width, boolean scaleHeight);

    Image on(int x, int y);

    Image on(Position position);

    ImageParser getImageParser();
}
