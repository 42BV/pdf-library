package nl.mad.toucanpdf.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

import nl.mad.toucanpdf.model.Compression;

/**
 * This utility class allows for the compression of data.
 * @author Dylan de Wolff
 *
 */
public final class Compressor {
    private static final int DEFAULT_BUFFER_SIZE = 1024;

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
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            return data;
        }
    }

}
