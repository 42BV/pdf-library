package nl.mad.toucanpdf.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import nl.mad.toucanpdf.model.ColorSpace;
import nl.mad.toucanpdf.model.Filter;

public class JPEG implements ImageParser {
    private byte[] data;
    private ColorSpace colorSpace;
    private int bitsPerComponent;
    private Filter filter;
    private int width;
    private int height;
    private static final int GRAY_COMPONENT_AMOUNT = 1;
    private static final int RGB_COMPONENT_AMOUNT = 3;
    private static final int CMYK_COMPONENT_AMOUNT = 4;

    public JPEG(InputStream stream) {
        parseStream(stream);
    }

    private void parseStream(InputStream stream) {
        try {
            BufferedImage image = ImageIO.read(stream);
            image = convertImage(image);
            retrieveData(image);
            determineColorSpace(image);
            retrieveBitsPerComponent(image);
            filter = Filter.DCT_DECODE;
            retrieveImageSize(image);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private BufferedImage convertImage(BufferedImage image) {
        if (image.getColorModel().getNumComponents() == RGB_COMPONENT_AMOUNT && image.getType() != BufferedImage.TYPE_INT_RGB) {
            image = convertImageToType(image, BufferedImage.TYPE_INT_RGB);
        } else if (image.getColorModel().getNumComponents() == GRAY_COMPONENT_AMOUNT && image.getType() != BufferedImage.TYPE_BYTE_GRAY) {
            image = convertImageToType(image, BufferedImage.TYPE_BYTE_GRAY);
        }
        return image;
    }

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

    private void retrieveBitsPerComponent(BufferedImage image) {
        ColorModel model = image.getColorModel();
        bitsPerComponent = model.getPixelSize() / model.getNumComponents();
    }

    private void retrieveImageSize(BufferedImage image) {
        this.height = image.getHeight();
        this.width = image.getWidth();
    }

    private BufferedImage convertImageToType(BufferedImage image, int type) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), type);
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return newImage;
    }

    @Override
    public byte[] getData() {
        return this.data;
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
    public Filter getFilter() {
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
}
