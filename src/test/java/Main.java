import nl.mad.toucanpdf.DocumentBuilder;

import java.io.IOException;

public class Main {

    /**
     * This is only for testing purposes.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        DocumentBuilder builder = new DocumentBuilder();
        builder.addText("Jantje").scale(10, 10);
        builder.addText("Pietje").shear(0.1, 0.1);

        builder.addParagraph().addText(builder.createText("Jantje")).addText(builder.createText("Pietje"));
        builder.finish();
    }
}
