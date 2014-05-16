package nl.mad.toucanpdf.model.state;

import java.util.List;

import nl.mad.toucanpdf.model.Table;

public interface StateTable extends Table, StatePlaceableFixedSizeDocumentPart {
    List<StateCell> getStateCellCollection();
}
