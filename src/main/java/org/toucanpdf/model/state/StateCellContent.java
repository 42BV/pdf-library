package org.toucanpdf.model.state;

import org.toucanpdf.model.PlaceableDocumentPart;
import org.toucanpdf.model.Position;

public interface StateCellContent extends StateDocumentPart, PlaceableDocumentPart, StateSpacing {
    double calculateContentHeight(double availableWidth, double leading, Position position, boolean processPositioning);

    double getRequiredWidth();

    double getSpecifiedWidth();

    double getTotalRequiredWidth();

    double getMinimumWidth();

    void processVerticalAlignment(double height);
}
