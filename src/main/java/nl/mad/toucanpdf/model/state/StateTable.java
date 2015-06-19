package nl.mad.toucanpdf.model.state;

import java.util.List;

import nl.mad.toucanpdf.model.Table;
import nl.mad.toucanpdf.state.BaseStateTable;

public interface StateTable extends Table, StatePlaceableFixedSizeDocumentPart {
    List<StateCell> getStateCellCollection();

    boolean updateHeight(StatePage page);
}
