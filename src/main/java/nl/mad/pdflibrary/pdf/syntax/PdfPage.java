package nl.mad.pdflibrary.pdf.syntax;

import nl.mad.pdflibrary.model.PdfNameValue;

/**
 * PdfPage stores all the data necessary to form a page. The class is responsible for holding it's own content and used resources.
 * 
 * @author Dylan de Wolff
 */
public class PdfPage extends PdfDictionary {
    private int width;
    private int height;
    private double filledHeight;
    private double filledWidth;

    /**
     * The amount of resources this page uses.
     */
    private int resourceCount;
    /**
     * The current content stream.
     */
    private PdfStream currentStream;
    private static final PdfName CONTENT = new PdfName(PdfNameValue.CONTENTS);
    private static final PdfName RESOURCES = new PdfName(PdfNameValue.RESOURCES);
    private static final String RESOURCE_REFERENCE_PREFIX = "R";

    /**
     * Creates a new instance of PdfPage with the given width and height.
     * @param width Width of page.
     * @param height Height of page.
     */
    public PdfPage(int width, int height) {
        super(PdfObjectType.PAGE);
        this.width = width;
        this.height = height;
        this.resourceCount = 0;
        this.initPage();
    }

    /**
     * Initializes the page by adding type, mediabox, resources and content.
     */
    private void initPage() {
        put(PdfNameValue.TYPE, PdfNameValue.PAGE);
        put(PdfNameValue.MEDIA_BOX, createMediabox());
        put(RESOURCES, new PdfDictionary(PdfObjectType.DICTIONARY));
        put(CONTENT, new PdfArray());
    }

    /**
     * Creates a new Mediabox PdfArray based on the height and width of the page.
     * The mediabox is responsible for specifying the size/visible area of the page
     * @return
     */
    private PdfArray createMediabox() {
        PdfArray mediabox = new PdfArray();
        mediabox.addValue(new PdfNumber(0));
        mediabox.addValue(new PdfNumber(0));
        mediabox.addValue(new PdfNumber(width));
        mediabox.addValue(new PdfNumber(height));
        return mediabox;
    }

    /**
     * Adds an indirect object to the resources or contents of this page.
     * @param indirectObject Object to be added.
     */
    public void add(PdfIndirectObject indirectObject) {
        switch (indirectObject.getObject().getType()) {
        case STREAM:
            this.addContent(indirectObject);
            break;
        case FONT:
            this.addResource(indirectObject);
            break;
        default:
            break;
        }
    }

    /**
     * Adds a reference to the given object to the contents array.
     * @param indirectObject IndirectObject to be added.
     */
    private void addContent(PdfIndirectObject indirectObject) {
        PdfArray currentContent = (PdfArray) this.get(CONTENT);
        currentContent.addValue(indirectObject.getReference());
        this.currentStream = (PdfStream) indirectObject.getObject();
    }

    /**
     * Adds a resource to the resource array.
     * @param indirectObject Resource to be added.
     */
    private void addResource(PdfIndirectObject indirectObject) {
        PdfDictionary currentResources = (PdfDictionary) this.get(RESOURCES);
        PdfName key = getKeyForType(indirectObject.getObject().getType());

        if (!objectInResources(indirectObject, currentResources, key)) {
            ++resourceCount;
            String resourceReference = RESOURCE_REFERENCE_PREFIX + this.resourceCount;
            indirectObject.getReference().setResourceReference(resourceReference);
            PdfName resourceKey = new PdfName(resourceReference);

            if (currentResources.get(key) != null) {
                PdfDictionary keyResourceDictionary = (PdfDictionary) currentResources.get(key);
                keyResourceDictionary.put(resourceKey, indirectObject.getReference());
            } else {
                PdfDictionary newResource = new PdfDictionary(PdfObjectType.DICTIONARY);
                newResource.put(resourceKey, indirectObject.getReference());
                currentResources.put(key, newResource);
            }
        }
    }

    private boolean objectInResources(PdfIndirectObject indirectObject, PdfDictionary currentResources, PdfName key) {
        if (currentResources.get(key) != null) {
            PdfDictionary keyResources = (PdfDictionary) currentResources.get(key);
            if (keyResources.containsValue(indirectObject.getReference())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns dictionary key corresponding to the given type.
     * @param type Type of object.
     * @return Key for the dictionary.
     */
    private PdfName getKeyForType(PdfObjectType type) {
        PdfName key = null;

        switch (type) {
        case FONT:
            key = new PdfName(PdfNameValue.FONT);
            break;
        default:
            break;
        }
        return key;
    }

    public void setCurrentStream(PdfStream stream) {
        this.currentStream = stream;
    }

    public PdfStream getCurrentStream() {
        return this.currentStream;
    }

    /**
     * Checks if the stream is empty.
     * @return true if the stream is empty, false otherwise
     */
    public boolean streamEmpty() {
        if (this.currentStream != null && this.currentStream.getContentSize() > 0) {
            return false;
        }
        return true;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    /**
     * @return the filledHeight
     */
    public double getFilledHeight() {
        return filledHeight;
    }

    /**
     * @param filledHeight the height to set
     */
    public void setFilledHeight(double filledHeight) {
        this.filledHeight = filledHeight;
    }

    /**
     * @return the filledWidth
     */
    public double getFilledWidth() {
        return filledWidth;
    }

    /**
     * @param filledWidth the width to set
     */
    public void setFilledWidth(double filledWidth) {
        this.filledWidth = filledWidth;
    }
}
