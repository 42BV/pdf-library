package nl.mad.pdflibrary.structure;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.mad.pdflibrary.api.BaseText;
import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.PdfNameValue;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.Text;
import nl.mad.pdflibrary.syntax.PdfDictionary;
import nl.mad.pdflibrary.syntax.PdfFile;
import nl.mad.pdflibrary.syntax.PdfFont;
import nl.mad.pdflibrary.syntax.PdfFontDescriptor;
import nl.mad.pdflibrary.syntax.PdfIndirectObject;
import nl.mad.pdflibrary.syntax.PdfObjectType;
import nl.mad.pdflibrary.syntax.PdfPage;
import nl.mad.pdflibrary.syntax.PdfStream;
import nl.mad.pdflibrary.syntax.PdfString;
import nl.mad.pdflibrary.syntax.PdfText;
import nl.mad.pdflibrary.utility.ByteEncoder;
import nl.mad.pdflibrary.utility.PdfConstants;

/**
 * Represents the PDF document itself, containing the four different sections of a PDF document.
 * This class is responsible for the creation of the four sections (header, body, cross reference table and trailer),
 * passing along new document parts and storing the used fonts.
 * 
 * @author Dylan de Wolff
 */
public class PdfDocument {
    private PdfHeader header;
    private PdfBody body;
    private PdfCrossReferenceTable xref;
    private PdfTrailer trailer;
    private PdfPage currentPage;
    private Map<Font, PdfIndirectObject> fontList = new HashMap<Font, PdfIndirectObject>();
    private int defaultPageHeight;
    private int defaultPageWidth;

    /**
     * The default line separator.
     */
    public static final byte[] LINE_SEPARATOR = ByteEncoder.getBytes(System.getProperty("line.separator"));
    private static final String CREATOR = "PDF-Library";

    /**
     * Creates a new instance of PdfDocument.
     * @throws UnsupportedEncodingException 
     */
    public PdfDocument(int width, int height) throws UnsupportedEncodingException {
        this.header = new PdfHeader();
        this.body = new PdfBody();
        this.xref = new PdfCrossReferenceTable();
        this.trailer = new PdfTrailer();
        this.defaultPageHeight = height;
        this.defaultPageWidth = width;
    }

    /**
     * Creates a PdfObject from the given document part and adds it to the document.
     * @param part Document part that is to be added.
     */
    public void add(DocumentPart part) {
        switch (part.getType()) {
        case TEXT:
            this.addText((Text) part, new PdfText(), false, false, false);
            break;
        case PARAGRAPH:
            this.addParagraph((Paragraph) part);
            break;
        case FONT:
            this.addFont((Font) part);
            break;
        default:
            break;
        }
    }

    /**
     * Adds a paragraph object to the document.
     * @param paragraph Paragraph to be added.
     */
    private void addParagraph(Paragraph paragraph) {
        List<Text> textCollection = paragraph.getTextCollection();
        if (paragraph.getTextCollection().size() != 0) {
            Position pos = paragraph.getPosition();
            //if we're using custom paragraph positioning, adjust the first text object
            if (pos.hasCustomPosition() && textCollection.get(0) != null) {
                textCollection.get(0).setPosition(pos);
            } else if (!textCollection.get(0).getPosition().hasCustomPosition()) {
                calculatePosition(textCollection.get(0), false);
            }
            int posX = textCollection.get(0).getPosition().getX();
            for (int i = 0; i < textCollection.size(); ++i) {
                boolean ignoreMatrix = true;
                boolean ignorePosition = true;

                if (i == 0) {
                    ignoreMatrix = false;
                    ignorePosition = false;
                }
                //NO SUPPORT YET FOR MORE THAN ONE MATRIX IN A PARAGRAPH
                //                    else if (!textCollection.get(i).textMatrixEquals(textCollection.get(i - 1))) {
                //                        ignoreMatrix = false;
                //                    }
                this.addText(textCollection.get(i), new PdfText(posX), ignoreMatrix, ignorePosition, true);
            }
            currentPage.setFilledWidth(0);
        }
    }

    /**
     * Calculates a position to place the given text.
     * @param text Text to be positioned
     * @param inParagraph Whether or not the given text object is part of a paragraph. 
     */
    private void calculatePosition(Text text, boolean inParagraph) {
        Font font = text.getFont();
        double spaceWidth = font.getBaseFontFamily().getMetricsForStyle(font.getStyle()).getWidthPoint((int) ' ');
        if (inParagraph) {
            text.getPosition().setX((int) (Math.ceil(currentPage.getFilledWidth() + spaceWidth)));
            text.getPosition().setY((int) (currentPage.getHeight() - currentPage.getFilledHeight()));
        } else {
            //TODO: take into account margins and such
            text.getPosition().setX(0);
            text.getPosition().setY((int) (Math.ceil(currentPage.getHeight() - (currentPage.getFilledHeight() + calculateLeading(font, text.getTextSize())))));
        }

    }

    /**
     * Calculates leading based on font metrics and size of the text.
     * @param font Font of the text.
     * @param textSize Size of the text.
     * @return int containing the calculated leading. 
     */
    private int calculateLeading(Font font, int textSize) {
        return font.getBaseFontFamily().getMetricsForStyle(font.getStyle()).getLeadingForSize(textSize);
    }

    /**
     * Adds the given text object to the PdfTextStream of the current page.
     * @param text Text that needs to be added.
     * @param PdfText the pdfText object to be added.
     * @param overrideMatrix if true the matrix of the text will be disregarded. This should be true when the new text object has the same
     * matrix as the text object before it.
     * @param ignorePosition if true the position of the text will be disregarded. This 
     * should be true when the text object is following another text object in a paragraph.
     * @param isParagraph specifies if the given text object is inside a paragraph. 
     */
    private void addText(Text text, PdfText pdfText, boolean overrideMatrix, boolean ignorePosition, boolean isParagraph) {
        PdfIndirectObject font = this.addFont(text.getFont());
        currentPage.add(font);
        PdfStream ts = getCurrentPageStream();

        if (!text.getPosition().hasCustomPosition() && !isParagraph) {
            calculatePosition(text, false);
        }

        String overflow = "";
        if (overrideMatrix) {
            pdfText.addFont(getPdfFont(text.getFont()), text.getTextSize());
            overflow = pdfText.addTextString(text, currentPage, calculateLeading(text.getFont(), text.getTextSize()), ignorePosition);
        } else {
            if (ignorePosition) {
                calculatePosition(text, true);
            }
            overflow = pdfText.addText(text, getPdfFont(text.getFont()), currentPage, calculateLeading(text.getFont(), text.getTextSize()), ignorePosition);
        }
        ts.add(pdfText);
        handleOverflow(text, overflow, isParagraph, pdfText.getPositionX());
    }

    /**
     * This method handles any overflow that comes forth from adding a text object to the document.
     * @param text Text object that created the overflow.
     * @param overflow String containing the overflow.
     * @param isParagraph Whether or not the text object is part of a paragraph.
     * @param posX The X position of the paragraph. 
     */
    private void handleOverflow(Text text, String overflow, boolean isParagraph, int posX) {
        //if a part of the text doesn't fit on the current page we create a new page and add the text to the next one.
        if (overflow != "") {
            this.addPage();
            Text overflowText = new BaseText(text);
            overflowText.setText(overflow);
            calculatePosition(overflowText, false);
            PdfText pdfText;
            if (isParagraph) {
                pdfText = new PdfText(posX);
            } else {
                pdfText = new PdfText();
            }
            this.addText(overflowText, pdfText, false, false, isParagraph);
        }
    }

    /**
     * Returns the stream used by the current page. Creates a new stream if the page does not have one.
     * @return Stream of the current page.
     */
    private PdfStream getCurrentPageStream() {
        PdfStream ts;
        //if the page has no content yet
        if (currentPage.streamEmpty()) {
            //create new stream object and add the text
            ts = new PdfStream();
            currentPage.add(body.addObject(ts));
        } else {
            ts = currentPage.getCurrentStream();
        }
        return ts;
    }

    /**
     * Returns the indirect object representing the given font.
     * @param font Requested font.
     * @return PdfIndirectObject representing the font or null if the font is not in the list
     */
    public PdfIndirectObject getPdfFont(Font font) {
        return fontList.get(font);
    }

    /**
     * Creates an indirectObject for the given font and adds it to the font list.
     * @param font Font that needs to be added.
     * @return indirect object for the given font
     */
    public PdfIndirectObject addFont(Font font) {
        if (!fontList.containsKey(font) && font != null) {
            PdfFontDescriptor newFontDescriptor = new PdfFontDescriptor(font);
            byte[] fontProgramFile = font.getBaseFontFamily().getMetricsForStyle(font.getStyle()).getFontFile();
            if (fontProgramFile != null) {
                PdfStream stream = new PdfStream();
                stream.add(new PdfFile(fontProgramFile));
                PdfIndirectObject indirectFontFile = body.addObject(stream);
                newFontDescriptor.setFontFileReference(indirectFontFile.getReference(), font.getBaseFontFamily().getSubType());
            }

            PdfFont newFont = new PdfFont(font);
            PdfIndirectObject indirectFont = body.addObject(newFont);
            PdfIndirectObject indirectFontDictionary = body.addObject(newFontDescriptor);
            newFont.setFontDescriptorReference(indirectFontDictionary.getReference());

            fontList.put(font, indirectFont);
            return indirectFont;
        } else {
            return fontList.get(font);
        }
    }

    /**
     * Adds a new page with the default size and changes the current page.
     */
    public void addPage() {
        this.addPage(defaultPageWidth, defaultPageHeight);
    }

    /**
     * Adds a new page and changes the current page.
     * @param width Width of page.
     * @param height Height of page.
     */
    public void addPage(int width, int height) {
        PdfPage page = new PdfPage(width, height);
        currentPage = (PdfPage) body.addPage(page).getObject();
    }

    /**
     * Adds the given document info to the PDF trailer.
     * @param author Writer of the document.
     * @param title Title of the document.
     * @param subject Subject of the document.
     * @param creationDate Creation date of the document.
     * @see PdfTrailer
     */
    public void addDocumentInfo(String author, String title, String subject, Calendar creationDate) {
        PdfDictionary trailerInfo = new PdfDictionary(PdfObjectType.DICTIONARY);
        PdfIndirectObject info = body.addObject(trailerInfo);

        if (author.length() > 0) trailerInfo.put(PdfNameValue.AUTHOR, new PdfString(author));
        if (title.length() > 0) trailerInfo.put(PdfNameValue.TITLE, new PdfString(title));
        if (subject.length() > 0) trailerInfo.put(PdfNameValue.SUBJECT, new PdfString(subject));
        PdfString dateString = new PdfString();
        dateString.setString(creationDate);
        if (creationDate != null) trailerInfo.put(PdfNameValue.CREATION_DATE, dateString);
        trailerInfo.put(PdfNameValue.CREATOR, new PdfString(CREATOR));
        trailer.setInfo(info);
    }

    /**
     * Writes the document to the given OutputStream.
     * 
     * @param os OutputStream to write to.
     * @throws IOException 
     */
    public void write(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        header.writeToFile(dos);
        dos.write(PdfConstants.LINE_SEPARATOR);
        body.writeToFile(dos);
        xref.fillTableWithIndirectObjects(body.getAllIndirectObjects());
        xref.writeToFile(dos);
        trailer.setObjectAmount(body.getTotalIndirectObjectsAmount() + 1);
        trailer.setCrossReferenceStartByte(xref.getStartByte());
        trailer.fillObjectSpecification(body.getCatalogReference());
        trailer.writeToFile(dos);
        dos.flush();
        dos.close();
    }

    public PdfPage getCurrentPage() {
        return this.currentPage;
    }

    /**
     * Adds a new line to the current page stream.
     */
    public void addNewLine() {
        //No implementation yet
        if (currentPage.streamEmpty()) {

        } else {

        }
    }
}
