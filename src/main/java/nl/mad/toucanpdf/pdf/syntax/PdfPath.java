package nl.mad.toucanpdf.pdf.syntax;

import nl.mad.toucanpdf.utility.Constants;

/**
 * PdfPath represents the Path object from the PDF specification.
 * This class can be used to draw all kinds of shapes. 
 * @author Dylan de Wolff
 *
 */
public class PdfPath extends AbstractPdfObject {
    private static final String MOVE = " m ";
    private static final String DRAW_LINE = " l ";
    private static final String STROKE_PATH = " S" + Constants.LINE_SEPARATOR_STRING;
    private static final String DRAW_RECTANGLE = " re ";
    private static final String CLOSE_SUBPATH = " h ";
    private static final String CLOSE_SUBPATH_AND_STROKE_LINE = "s" + Constants.LINE_SEPARATOR_STRING;
    private static final String FILL_PATH = " f" + Constants.LINE_SEPARATOR_STRING;
    private static final String LINE_WIDTH = " w ";

    /**
     * Creates a new instance of PdfPath.
     */
    public PdfPath() {
        super(PdfObjectType.PATH);
    }

    /**
     * Creates a new instance of PdfPath with the given type.
     * @param type Object type.
     */
    public PdfPath(PdfObjectType type) {
        super(type);
    }

    /**
     * Begins a new subpath and draws a rectangle at the given location of the given width and height.
     * @param x The lower left x-value of the rectangle.
     * @param y The lower left y-value of the rectangle.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public void drawRectangle(double x, double y, double width, double height) {
        this.addToByteRepresentation(x + " " + y + " " + width + " " + height + DRAW_RECTANGLE);
    }

    /**
     * Draws a line from the current position towards the given position.
     * @param x X-value of the position to draw towards.
     * @param y Y-value of the position to draw towards.
     */
    public void drawLine(double x, double y) {
        this.addToByteRepresentation(x + " " + y + DRAW_LINE);
    }

    /**
     * Draws the path and concludes the current path definition.
     */
    public void strokePath() {
        this.addToByteRepresentation(STROKE_PATH);
    }

    /**
     * Sets the line thickness.
     * @param lineWidth Width to use.
     */
    public void setLineWidth(double lineWidth) {
        this.addToByteRepresentation(lineWidth + LINE_WIDTH);
    }

    /**
     * Draws and fills the path. This concludes the current path definition.
     */
    public void fillPath() {
        this.addToByteRepresentation(FILL_PATH);
    }

    /**
     * Begins a new subpath on the given position.
     * @param x The x-value to start on.
     * @param y The y-value to start on.
     */
    public void moveTo(double x, double y) {
        this.addToByteRepresentation(x + " " + y + MOVE);
    }

    /**
     * Closes off the current subpath by drawing a line from the current position towards the starting point of the current subpath.
     * If the subpath is already closed this method will do nothing.
     */
    public void closeSubpath() {
        this.addToByteRepresentation(CLOSE_SUBPATH);
    }

    /**
     * Closes off the current subpath by drawing a line from the current position towards the starting point of the current subpath. 
     * Strokes the path afterwards.
     * This method achieves the same result as using the closeSubpath() method followed by the strokePath() method, it 
     * uses a different operator though and is mostly here for the sake of completion. 
     * When both these methods are used right after each other it is better to use this method simply because it requires a single operator less.
     */
    public void closeSubpathAndStrokeLine() {
        this.addToByteRepresentation(CLOSE_SUBPATH_AND_STROKE_LINE);
    }
}
