package nl.mad.pdflibrary.pdf.object;

/**
 * PdfPage stores all the data necessary to form a page. The class is responsible for holding it's own content and used resources.
 * 
 * @author Dylan de Wolff
 */
public class PdfPage extends PdfDictionary {
    private int width;
    private int height;
    /**
     * The amount of resources this page uses
     */
    private int resourceCount;
    /**
     * The current content stream
     */
    private PdfStream currentStream;
    private static final PdfName content = new PdfName(PdfNameValue.CONTENTS);
    private static final PdfName resources = new PdfName(PdfNameValue.RESOURCES);
    private static final String RESOURCE_REFERENCE_PREFIX = "R";

    /**
     * Creates a new instance of PdfPage with the given width and height
     * @param width
     * @param height
     */
    public PdfPage(int width, int height) {
        super(PdfObjectType.PAGE);
        this.width = width;
        this.height = height;
        this.resourceCount = 0;
        this.initPage();
    }

    /**
     * Initializes the page by adding type, mediabox, resources and content
     */
    private void initPage() {
        put(new PdfName(PdfNameValue.TYPE), new PdfName(PdfNameValue.PAGE));
        put(new PdfName(PdfNameValue.MEDIA_BOX), createMediabox());
        put(resources, new PdfDictionary(PdfObjectType.DICTIONARY));
        put(content, new PdfArray());
    }

    /**
     * Creates a new Mediabox PdfArray based on the height and width of the page
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
     * Adds an indirect object to the resources or contents of this page
     * @param indirectObject Object to be added
     */
    public void add(PdfIndirectObject indirectObject) {
        AbstractPdfObject obj = indirectObject.getObject();
        if (obj instanceof PdfDictionary) {
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
    }

    /**
     * Adds a reference to the given object to the contents array
     * @param indirectObject
     */
    private void addContent(PdfIndirectObject indirectObject) {
        PdfArray currentContent = (PdfArray) this.get(content);
        currentContent.addValue(indirectObject.getReference());
        this.currentStream = (PdfStream) indirectObject.getObject();
    }

    /**
     * Adds a resource to the resource array
     * @param indirectObject
     */
    private void addResource(PdfIndirectObject indirectObject) {
        ++resourceCount;
        PdfDictionary currentResources = (PdfDictionary) this.get(resources);
        PdfName key = getKeyForType(indirectObject.getObject().getType());
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

    /**
     * Returns dictionary key corresponding to the given type 
     * @param type
     * @return
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

    public boolean streamEmpty() {
        if (this.currentStream != null && this.currentStream.getContentSize() > 0) {
            return false;
        }
        return true;
    }
}