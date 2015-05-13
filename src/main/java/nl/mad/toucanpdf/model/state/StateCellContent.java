package nl.mad.toucanpdf.model.state;

import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Position;

public interface StateCellContent extends StateDocumentPart, PlaceableDocumentPart, StateSpacing {
    double calculateContentHeight(double availableWidth, double leading, Position position, boolean processPositioning);

    double getRequiredWidth();

    double getSpecifiedWidth();

    double getTotalRequiredWidth();

}
