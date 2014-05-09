package nl.mad.toucanpdf.model;

/**
 * This interface specifies the methods all images in the preview state should implement.
 * @author Dylan de Wolff
 *
 */
public interface StateImage extends Image, StatePlaceableDocumentPart {

    /**
     * Processes the size of the image and positions it accordingly.
     * @param page The page to add the image to.
     */
    void processContentSize(StatePage page);

    /**
     * Processes the size of the image and positions it accordingly.
     * @param page The page to add the image to.
     * @param wrapping Whether wrapping around the image should be allowed.
     * @param processAlignment Whether alignment should be applied.
     */
    void processContentSize(StatePage page, boolean wrapping, boolean processAlignment);

}
