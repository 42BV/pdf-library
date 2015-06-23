package nl.mad.toucanpdf.model.state;

import java.util.List;

import nl.mad.toucanpdf.model.PlaceableFixedSizeDocumentPart;
import nl.mad.toucanpdf.model.Table;
import nl.mad.toucanpdf.state.BaseStateTable;

public interface StateTable extends Table, StatePlaceableDocumentPart, PlaceableFixedSizeDocumentPart {
    List<StateCell> getStateCellCollection();

    void updateHeight(StatePage page);

    /**
     * Processes the size of the part and positions it accordingly.
     * @param page The page to add the part to.
     * @return returns a table object containing the content that does not fit, null if everything fits
     */
    StateTable processContentSize(StatePage page);

    /**
     * Processes the size of the part and positions it accordingly.
     * @param page The page to add the part to.
     * @param wrapping Whether wrapping around the part should be allowed.
     * @param processAlignment Whether alignment should be applied.
     * @param fixed Whether the object has a fixed position.
     * @return returns a table object containing the content that does not fit, null if everything fits
     */
    StateTable processContentSize(StatePage page, boolean wrapping, boolean processAlignment, boolean fixed);

    void setOriginal(boolean original);

    boolean isOriginal();

    double[] getOriginalWidths();

    void setHeader(BaseStateTable.TableRow header);

    BaseStateTable.TableRow getHeader();
}
