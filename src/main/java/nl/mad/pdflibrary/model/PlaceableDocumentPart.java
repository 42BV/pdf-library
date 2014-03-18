package nl.mad.pdflibrary.model;

/**
 * Interface for placeable document parts. This extends DocumentPart and specifies methods for positioning the DocumentPart.
 * @author Dylan de Wolff
 *
 */
public interface PlaceableDocumentPart extends DocumentPart {

    void setPosition(Position position);

    Position getPosition();
}
