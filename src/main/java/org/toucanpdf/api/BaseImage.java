package org.toucanpdf.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.toucanpdf.image.JPEG;
import org.toucanpdf.model.Alignment;
import org.toucanpdf.model.Compression;
import org.toucanpdf.model.DocumentPartType;
import org.toucanpdf.model.Image;
import org.toucanpdf.model.ImageParser;
import org.toucanpdf.model.ImageType;
import org.toucanpdf.model.PlaceableDocumentPart;
import org.toucanpdf.model.Position;
import org.toucanpdf.utility.PointsConverter;

/**
 * This is the base implementation for the image interface. This class can be added to an instance of DocumentBuilder in order to add 
 * images to the document.
 * @author Dylan de Wolff
 *
 */
public class BaseImage extends AbstractPlaceableFixedSizeDocumentPart implements Image {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseImage.class);
    private ImageParser image;
    private Compression compressionMethod = Compression.FLATE;
    private double scale = 1;
    private boolean invertColors = false;

    /**
     * Creates a new BaseImage instance.
     */
    public BaseImage() {
        super(DocumentPartType.IMAGE);
    }

    /**
     * Creates a new instance of BaseImage using the given inputstream and image filename.
     * @param imageStream InputStream for the image.
     * @param filename Filename of the image.
     */
    public BaseImage(InputStream imageStream, String filename) {
        this(imageStream, getTypeFromFilename(filename));
    }

    /**
     * Creates a new instance of BaseImage using the given image data and type.
     * @param image Image data in bytes.
     * @param type Image type.
     */
    public BaseImage(byte[] image, ImageType type) {
        this();
        this.parse(image, type);
        setInitialSize();
    }

    /**
     * Creates a new BaseImage instance.
     * @param imageStream InputStream with the image file to use.
     * @param type The format of the image.
     */
    public BaseImage(InputStream imageStream, ImageType type) {
        this();
        this.parse(imageStream, type);
        setInitialSize();
    }

    private void setInitialSize() {
        height = 0;
        width = 0;
        if (image != null) {
            this.height = PointsConverter.getPointsForPixels(image.getHeight());
            this.width = PointsConverter.getPointsForPixels(image.getWidth());
        }
        if (this.width > 0 && this.height > 0) {
            scale = this.width / this.height;
        }
    }

    /**
     * Creates a new instance of BaseImage.
     * @param height Height of the image. (You can fill in any custom height)
     * @param width Width of the image. (You can fill in any custom width)
     * @param imageStream InputStream with the image file to use.
     * @param type The format of the image.
     */
    public BaseImage(int height, int width, InputStream imageStream, ImageType type) {
        this();
        this.parse(imageStream, type);
        this.height = height;
        this.width = width;
    }

    private void parse(byte[] imageData, ImageType type) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        this.parse(bais, type);
    }

    private void parse(InputStream imageStream, ImageType type) {
        if (imageStream != null) {
            switch (type) {
            case JPEG:
                image = new JPEG(imageStream);
                break;
            default:
                LOGGER.warn("The given image format: " + type + " is currently not supported.");
                break;
            }
        }
    }

    /**
     * Creates a copy of the given Image object.
     * @param image Image to copy.
     */
    public BaseImage(Image image) {
        this();
        this.height = image.getHeight();
        this.width = image.getWidth();
        this.image = image.getImageParser();
        this.setWrappingAllowed(image.isWrappingAllowed());
        this.align(image.getAlignment());
        this.on(image.getPosition());
        this.marginBottom = image.getMarginBottom();
        this.marginLeft = image.getMarginLeft();
        this.marginTop = image.getMarginTop();
        this.marginRight = image.getMarginRight();
        this.invertColors = image.getInvertColors();
        this.compressionMethod = image.getCompressionMethod();
    }

    @Override
    public PlaceableDocumentPart copy() {
        return new BaseImage(this);
    }

    @Override
    public Image align(Alignment alignment) {
        super.align(alignment);
        return this;
    }

    @Override
    public Image on(Position position) {
        super.on(position);
        return this;
    }

    @Override
    public Image on(int x, int y) {
        return this.on(new Position(x, y));
    }

    @Override
    public Image height(int height) {
        return height(height, true);
    }

    @Override
    public Image height(int height, boolean scaleWidth) {
        this.height = Math.max(1, height);
        if (scaleWidth) {
            this.width = this.height * scale;
        }
        return this;
    }

    @Override
    public Image width(int width) {
        return this.width(width, true);
    }

    @Override
    public Image width(int width, boolean scaleHeight) {
        this.width = Math.max(1, width);
        if (scaleHeight) {
            this.height = this.width * scale;
        }
        return this;
    }

    @Override
    public ImageParser getImageParser() {
        return image;
    }

    @Override
    public Image compress(Compression method) {
        this.compressionMethod = method;
        return this;
    }

    @Override
    public Compression getCompressionMethod() {
        return this.compressionMethod;
    }

    @Override
    public Image allowWrapping(boolean isWrappable) {
        this.setWrappingAllowed(isWrappable);
        return this;
    }

    @Override
    public Image marginTop(int marginTop) {
        super.marginTop(marginTop);
        return this;
    }

    @Override
    public Image marginBottom(int marginBottom) {
        super.marginBottom(marginBottom);
        return this;
    }

    @Override
    public Image marginRight(int marginRight) {
        super.marginRight(marginRight);
        return this;
    }

    @Override
    public Image marginLeft(int marginLeft) {
        super.marginLeft(marginLeft);
        return this;
    }

    /**
     * Returns the imagetype corresponding to the given filename.
     * @param filename filename to check.
     * @return The type of the image, null if no corresponding image type could be discovered.
     */
    public static ImageType getTypeFromFilename(String filename) {
        String file = filename.toLowerCase();
        if (file.endsWith(".jpg") || file.endsWith(".jpeg")) {
            return ImageType.JPEG;
        } else if (file.endsWith(".png")) {
            return ImageType.PNG;
        } else if (file.endsWith(".gif")) {
            return ImageType.GIF;
        } else if (file.endsWith(".bmp")) {
            return ImageType.BMP;
        }
        return null;
    }

    @Override
    public Image invertColors(boolean invert) {
        this.invertColors = invert;
        return this;
    }

    @Override
    public boolean getInvertColors() {
        return this.invertColors;
    }
}
