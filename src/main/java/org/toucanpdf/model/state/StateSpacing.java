package org.toucanpdf.model.state;

public interface StateSpacing {

    /**
     * @return double containing the clear space required above the object.
     */
    double getRequiredSpaceAbove();

    /**
     * @return double containing the clear space required below the object.
     */
    double getRequiredSpaceBelow();

    /**
     * @return double containing the clear space required to the left of the object.
     */
    double getRequiredSpaceLeft();

    /**
     * @return double containing the clear space required to the right of the object.
     */
    double getRequiredSpaceRight();
}
