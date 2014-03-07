package nl.mad.pdflibrary.font.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class PfbParser {
    private byte[] pfbData;
    private static final int HEADER_LENGTH = 18;
    private static final int START_MARKER = 0x80;
    private static final int ASCII_MARKER = 0x01;
    private static final int BINARY_MARKER = 0x02;
    private static final int[] PFB_RECORD_TYPES = { ASCII_MARKER, BINARY_MARKER, ASCII_MARKER };
    private static final int BUFFER_SIZE = 0xffff;

    public PfbParser(InputStream file) {
        pfbData = new byte[0];
        parse(readInput(file));
    }

    private void parse(byte[] file) {
        if (file != null) {
            try {
                ByteArrayInputStream input = new ByteArrayInputStream(file);
                pfbData = new byte[(int) (file.length - HEADER_LENGTH)];
                int recordLength = PFB_RECORD_TYPES.length;

                int[] lengths = new int[recordLength];
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
                    size += input.read() << 8;
                    size += input.read() << 16;
                    size += input.read() << 24;
                    lengths[i] = size;
                    int got = input.read(pfbData, pointer, size);
                    if (got < 0) {
                        throw new EOFException();
                    }
                    pointer += got;
                }
            } catch (IOException e) {
                System.err.print("The file was found, but an IOException ocurred during parsing: " + e.toString());
            }
        }
    }

    private byte[] readInput(InputStream file) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] tempBuffer = new byte[BUFFER_SIZE];
        int amountRead = -1;
        try {
            while ((amountRead = file.read(tempBuffer)) != -1) {
                out.write(tempBuffer, 0, amountRead);
            }
        } catch (IOException e) {
            System.err.print("An IOException occurred while reading the file: " + e.toString());
        }
        return out.toByteArray();
    }

    public byte[] getPfbData() {
        return this.pfbData;
    }

}
