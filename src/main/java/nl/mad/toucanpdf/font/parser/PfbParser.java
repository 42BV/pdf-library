package nl.mad.toucanpdf.font.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing pfb (Type 1 font program) and storing the data found. This is required to fully embed a Type 1 font.
 * @author Dylan de Wolff
 *
 */
public class PfbParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PfbParser.class);
    private byte[] pfbData;
    private int[] lengths;
    private static final int HEADER_LENGTH = 18;
    private static final int START_MARKER = 0x80;
    private static final int ASCII_MARKER = 0x01;
    private static final int BINARY_MARKER = 0x02;
    private static final int[] PFB_RECORD_TYPES = { ASCII_MARKER, BINARY_MARKER, ASCII_MARKER };
    private static final int BUFFER_SIZE = 0xffff;
    private static final int[] INPUT_ZERO_PADDING = { 8, 16, 24 };

    /**
     * Creates a new instance of PfbParser and parses the given file.
     * @param file File to be parsed.
     */
    public PfbParser(InputStream file) {
        pfbData = new byte[0];
        lengths = new int[PFB_RECORD_TYPES.length];
        parse(readInput(file));
    }

    /**
     * Parses the given array of bytes.
     * @param file Array of bytes containing the data in the file.
     */
    private void parse(byte[] file) {
        if (file.length > 0) {
            try {
                ByteArrayInputStream input = new ByteArrayInputStream(file);
                pfbData = new byte[(int) (file.length - HEADER_LENGTH)];
                int recordLength = PFB_RECORD_TYPES.length;
                int pointer = 0;
                for (int i = 0; i < recordLength; ++i) {
                    int read = input.read();
                    if (read != START_MARKER) {
                        throw new IOException("The start marker was not found");
                    }

                    if (input.read() != PFB_RECORD_TYPES[i]) {
                        throw new IOException("Record type was incorrect");
                    }

                    int size = input.read();
                    size += input.read() << INPUT_ZERO_PADDING[0];
                    size += input.read() << INPUT_ZERO_PADDING[1];
                    size += input.read() << INPUT_ZERO_PADDING[2];
                    lengths[i] = size;

                    int got = input.read(pfbData, pointer, size);
                    if (got < 0) {
                        throw new EOFException();
                    }
                    pointer += got;
                }
            } catch (IOException e) {
                LOGGER.error("An IOException occured during the parsing of a Pfb file.");
            }
        } else {
            LOGGER.warn("Received empty inputstream for .pfb file.");
        }
    }

    /**
     * Reads the given file and processes it into an array of bytes.
     * @param file File to process.
     * @return array of bytes containing file data.
     */
    private byte[] readInput(InputStream file) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (file != null) {
            byte[] tempBuffer = new byte[BUFFER_SIZE];
            int amountRead = -1;
            try {
                amountRead = file.read(tempBuffer);
                while (amountRead != -1) {
                    out.write(tempBuffer, 0, amountRead);
                    amountRead = file.read(tempBuffer);
                }
            } catch (IOException e) {
                LOGGER.error("An IOException occured while reading the Pfb file.");
            }
        } else {
            LOGGER.warn("Given inputstream in PFB parser is null");
        }
        return out.toByteArray();
    }

    public byte[] getPfbData() {
        return this.pfbData.clone();
    }

    public int[] getLengths() {
        return this.lengths.clone();
    }
}
