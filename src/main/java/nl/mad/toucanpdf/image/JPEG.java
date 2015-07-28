package nl.mad.toucanpdf.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import nl.mad.toucanpdf.model.ColorSpace;
import nl.mad.toucanpdf.model.Compression;
import nl.mad.toucanpdf.model.ImageParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the default JPEG parsing implementation for the ImageParser interface.
 * This class uses the default Java classes to parse the image.
 * @author Dylan de Wolff
 * @see ImageParser
 */
public class JPEG implements ImageParser {
    private byte[] data;
    private ColorSpace colorSpace;
    private int bitsPerComponent;
    private Compression filter;
    private int width;
    private int height;
    private static final int GRAY_COMPONENT_AMOUNT = 1;
    private static final int RGB_COMPONENT_AMOUNT = 3;
    private static final int CMYK_COMPONENT_AMOUNT = 4;
    private static final Logger LOGGER = LoggerFactory.getLogger(JPEG.class);

    /**
     * Creates a new instance of the JPEG parser.
     * @param stream Stream to parse.
     */
    public JPEG(InputStream stream) {
        parseStream(stream);
    }

    /**
     * Creates a BufferedImage from the given stream to retrieve the required image data.
     * @param stream Stream to read from.
     */
    private void parseStream(InputStream stream) {
        try {
            BufferedImage image = ImageIO.read(stream);
            image = convertImage(image);
            if (image != null) {
                loadImageData(image);
            }
            filter = Compression.DCT;
            stream.close();
        } catch (IOException e) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    LOGGER.warn("Exception occurred on closing the image stream");
                }
            }
            LOGGER.warn("Exception occurred during parsing of image stream");
        }
    }

    private void loadImageData(BufferedImage image) {
        retrieveData(image);
        determineColorSpace(image);
        retrieveBitsPerComponent(image);
        retrieveImageSize(image);
    }

    /**
     * Retrieves the actual image data from the BufferedImage.
     * @param image Image to retrieve the data from.
     */
    private void retrieveData(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        data = baos.toByteArray();
    }

    /**
     * If possible converts the image to the default RGB/Gray colorspace. This is to avoid color spaces that have the same amount 
     * of components but specify them in a different order. 
     * @param image Image to convert.
     * @return The same image if no conversion was necessary, a converted image otherwise. 
     */
    private BufferedImage convertImage(BufferedImage image) {
        if (image != null) {
            if (image.getColorModel().getNumComponents() == RGB_COMPONENT_AMOUNT && image.getType() != BufferedImage.TYPE_INT_RGB) {
                image = convertImageToType(image, BufferedImage.TYPE_INT_RGB);
            } else if (image.getColorModel().getNumComponents() == GRAY_COMPONENT_AMOUNT && image.getType() != BufferedImage.TYPE_BYTE_GRAY) {
                image = convertImageToType(image, BufferedImage.TYPE_BYTE_GRAY);
            }
            return image;
        }
        return null;
    }

    /**
     * Determines the color space being used by this image based on the number of components.
     * @param image Image to determine the color space of.
     */
    private void determineColorSpace(BufferedImage image) {
        int components = image.getColorModel().getNumComponents();
        if (components == RGB_COMPONENT_AMOUNT) {
            colorSpace = ColorSpace.DEVICE_RGB;
        } else if (components == GRAY_COMPONENT_AMOUNT) {
            colorSpace = ColorSpace.DEVICE_GRAY;
        } else if (components == CMYK_COMPONENT_AMOUNT) {
            colorSpace = ColorSpace.DEVICE_CMYK;
        }
    }

    /**
     * Retrieves the amount of bits used to specify each component.
     * @param image The image to check.
     */
    private void retrieveBitsPerComponent(BufferedImage image) {
        ColorModel model = image.getColorModel();
        bitsPerComponent = model.getPixelSize() / model.getNumComponents();
    }

    /**
     * Retrieves the original size of the image.
     * @param image The image to check.
     */
    private void retrieveImageSize(BufferedImage image) {
        this.height = image.getHeight();
        this.width = image.getWidth();
    }

    /**
     * Converts the image to the given type. 
     * @param image Image to convert.
     * @param type Type to convert to.
     * @return Converted image.
     */
    private BufferedImage convertImageToType(BufferedImage image, int type) {
        //conversion is done by creating a new BufferedImage with the same size attributes as the old one and a different type.
        //Then the image of the old one is drawn onto the new BufferedImage.
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), type);
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return newImage;
    }

    @Override
    public byte[] getData() {
        return this.data.clone();
    }

    @Override
    public ColorSpace getColorSpace() {
        return this.colorSpace;
    }

    @Override
    public int getBitsPerComponent() {
        return this.bitsPerComponent;
    }

    @Override
    public Compression getFilter() {
        return this.filter;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getRequiredComponentsForColorSpace(ColorSpace color) {
        switch (color) {
        case DEVICE_GRAY:
        case CAL_GRAY:
            return GRAY_COMPONENT_AMOUNT;
        case DEVICE_RGB:
        case CAL_RGB:
            return RGB_COMPONENT_AMOUNT;
        case DEVICE_CMYK:
            return CMYK_COMPONENT_AMOUNT;
        default:
            return 0;
        }
    }
}
