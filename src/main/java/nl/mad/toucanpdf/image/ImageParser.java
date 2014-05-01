package nl.mad.toucanpdf.image;

import nl.mad.toucanpdf.model.ColorSpace;
import nl.mad.toucanpdf.model.Filter;

public interface ImageParser {

    byte[] getData();

    ColorSpace getColorSpace();

    int getBitsPerComponent();

    Filter getFilter();

    int getWidth();

    int getHeight();
}
