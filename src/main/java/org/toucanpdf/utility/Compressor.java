package org.toucanpdf.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

import org.toucanpdf.model.Compression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This utility class allows for the compression of data.
 * @author Dylan de Wolff
 *
 */
public final class Compressor {
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private static final Logger LOGGER = LoggerFactory.getLogger(Compressor.class);

    private Compressor() {
    }

    /**
     * Compresses the given data using the flate method.
     * @param data Data to compress.
     * @return the compressed data.
     */
    public static byte[] flateCompress(byte[] data) {
        byte[] output = new byte[0];
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
            Deflater compressor = new Deflater();
            byte[] outputBuffer = new byte[DEFAULT_BUFFER_SIZE];
            compressor.setInput(data);
            compressor.finish();
            while (!compressor.finished()) {
                int count = compressor.deflate(outputBuffer);
                baos.write(outputBuffer, 0, count);
            }
            baos.close();
            output = baos.toByteArray();
        } catch (IOException e) {
            LOGGER.error("IOException occurred on flate compression");
        }
        return output;
    }

    /**
     * Compresses the data based on the given method.
     * @param data Data to compress.
     * @param compressionMethod Method to use.
     * @return compressed data.
     */
    public static byte[] compress(byte[] data, Compression compressionMethod) {
        switch (compressionMethod) {
        case FLATE:
            return Compressor.flateCompress(data);
        case ASCII_85:
        case ASCII_HEX:
        case CCITT:
        case DCT:
        case JBIG2:
        case JPX:
        case LZW:
        case RUN_LENGTH:
        default:
            LOGGER.warn("The given compression: " + compressionMethod
                    + " is unsupported. The compression should be removed from the object to prevent problems.");
            return data;
        }
    }

}
