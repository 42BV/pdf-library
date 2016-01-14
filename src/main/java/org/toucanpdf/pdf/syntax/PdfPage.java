package org.toucanpdf.pdf.syntax;

import org.toucanpdf.model.PdfNameValue;

/**
 * PdfPage stores all the data necessary to form a page. The class is responsible for holding it's own content and used resources.
 * 
 * @author Dylan de Wolff
 */
public class PdfPage extends PdfDictionary {
    private int width;
    private int height;
    private int marginLeft;
    private int marginRight;
    private int marginTop;
    private int marginBottom;
    private int leading;

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
     * @param leading the space between lines.
     * @param rotation the rotation of the page.
     */
    public PdfPage(int width, int height, int leading, int rotation) {
        super(PdfObjectType.PAGE);
        this.width = width;
        this.height = height;
        this.leading = leading;
        this.resourceCount = 0;
        this.initPage(rotation);
    }

    /**
     * Initializes the page by adding type, mediabox, resources and content.
     * @param rotation the rotation of the page.
     */
    private void initPage(int rotation) {
        put(PdfNameValue.TYPE, PdfNameValue.PAGE);
        put(PdfNameValue.MEDIA_BOX, createMediabox());
        put(RESOURCES, new PdfDictionary(PdfObjectType.DICTIONARY));
        put(CONTENT, new PdfArray());
        put(PdfNameValue.ROTATION, new PdfNumber(rotation));
    }

    /**
     * Creates a new Mediabox PdfArray based on the height and width of the page.
     * The mediabox is responsible for specifying the size/visible area of the page
     * @return PdfArray containing the mediabox
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
        case XOBJECT:
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
    public void addContent(PdfIndirectObject indirectObject) {
        PdfArray currentContent = (PdfArray) this.get(CONTENT);
        currentContent.addValue(indirectObject.getReference());
        this.currentStream = (PdfStream) indirectObject.getObject();
    }

    /**
     * Adds a resource to the resource array.
     * @param indirectObject Resource to be added.
     */
    public void addResource(PdfIndirectObject indirectObject) {
        PdfDictionary currentResources = (PdfDictionary) this.get(RESOURCES);
        PdfName key = getKeyForType(indirectObject.getObject().getType());

        if (!objectInResources(indirectObject, currentResources, key)) {
            AddNewResource(indirectObject, currentResources, key);
        }
    }

    private void AddNewResource(PdfIndirectObject indirectObject, PdfDictionary currentResources, PdfName key) {
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
        case XOBJECT:
            key = new PdfName(PdfNameValue.XOBJECT);
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
        return !(this.currentStream != null && this.currentStream.getContentSize() > 0);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    /**
     * @return the marginLeft
     */
    public int getMarginLeft() {
        return marginLeft;
    }

    /**
     * @param marginLeft the marginLeft to set
     */
    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    /**
     * @return the marginRight
     */
    public int getMarginRight() {
        return marginRight;
    }

    /**
     * @param marginRight the marginRight to set
     */
    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    /**
     * @return the marginTop
     */
    public int getMarginTop() {
        return marginTop;
    }

    /**
     * @param marginTop the marginTop to set
     */
    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    /**
     * @return the marginBottom
     */
    public int getMarginBottom() {
        return marginBottom;
    }

    /**
     * @param marginBottom the marginBottom to set
     */
    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    /**
     * Sets the values for all margins.
     * @param leftMargin Left margin to use.
     * @param rightMargin Right margin to use.
     * @param bottomMargin Bottom margin to use.
     * @param topMargin Top margin to use.
     */
    public void setMargins(int leftMargin, int rightMargin, int bottomMargin, int topMargin) {
        this.marginLeft = leftMargin;
        this.marginRight = rightMargin;
        this.marginBottom = bottomMargin;
        this.marginTop = topMargin;
    }

    public int getLeading() {
        return leading;
    }

    public void setLeading(int leading) {
        this.leading = leading;
    }
}
