package nl.mad.toucanpdf.model.state;

public interface StateSpacing {

    /**
     * @return double containing the clear space required above the object.
     */
    double getRequiredSpaceAbove();

    /**
     * @return double containing the clear space required below the object.
     */
    double getRequiredSpaceBelow();
}
