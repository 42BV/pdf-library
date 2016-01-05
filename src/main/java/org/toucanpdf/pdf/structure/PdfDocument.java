package org.toucanpdf.pdf.structure;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.toucanpdf.model.Compression;
import org.toucanpdf.model.DocumentPart;
import org.toucanpdf.model.Font;
import org.toucanpdf.model.FontMetrics;
import org.toucanpdf.model.Image;
import org.toucanpdf.model.Page;
import org.toucanpdf.model.PdfNameValue;
import org.toucanpdf.model.state.StateCell;
import org.toucanpdf.model.state.StateCellContent;
import org.toucanpdf.model.state.StateImage;
import org.toucanpdf.model.state.StatePage;
import org.toucanpdf.model.state.StateParagraph;
import org.toucanpdf.model.state.StateSplittableText;
import org.toucanpdf.model.state.StateTable;
import org.toucanpdf.model.state.StateText;
import org.toucanpdf.pdf.syntax.PdfDictionary;
import org.toucanpdf.pdf.syntax.PdfFile;
import org.toucanpdf.pdf.syntax.PdfFont;
import org.toucanpdf.pdf.syntax.PdfFontDescriptor;
import org.toucanpdf.pdf.syntax.PdfFontEncoding;
import org.toucanpdf.pdf.syntax.PdfFontProgram;
import org.toucanpdf.pdf.syntax.PdfImage;
import org.toucanpdf.pdf.syntax.PdfImageDictionary;
import org.toucanpdf.pdf.syntax.PdfIndirectObject;
import org.toucanpdf.pdf.syntax.PdfObjectType;
import org.toucanpdf.pdf.syntax.PdfPage;
import org.toucanpdf.pdf.syntax.PdfStream;
import org.toucanpdf.pdf.syntax.PdfString;
import org.toucanpdf.pdf.syntax.PdfTable;
import org.toucanpdf.pdf.syntax.PdfText;
import org.toucanpdf.utility.Constants;

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
    private Map<ByteBuffer, PdfIndirectObject> imageList = new HashMap<ByteBuffer, PdfIndirectObject>();
    private static final String CREATOR = "Toucan-PDF";
    private static final Compression DEFAULT_COMPRESSION_METHOD = Compression.FLATE;

    /**
     * Creates a new instance of PdfDocument.
     */
    public PdfDocument() {
        this.header = new PdfHeader();
        this.body = new PdfBody();
        this.xref = new PdfCrossReferenceTable();
        this.trailer = new PdfTrailer();
    }

    /**
     * Creates a PdfObject from the given document part and adds it to the document.
     * @param part Document part that is to be added.
     */
    public void add(DocumentPart part) {
        switch (part.getType()) {
        case TEXT:
            if (part instanceof StateText) {
                this.addText((StateText) part, false);
            }
            break;
        case PARAGRAPH:
            if (part instanceof StateParagraph) {
                this.addParagraph((StateParagraph) part);
            }
            break;
        case FONT:
            if (part instanceof Font) {
                this.addFont((Font) part);
            }
            break;
        case PAGE:
            if (part instanceof StatePage) {
                this.addPage((StatePage) part);
            }
            break;
        case IMAGE:
            if (part instanceof StateImage) {
                this.addImage((StateImage) part);
            }
            break;
        case TABLE:
            if (part instanceof StateTable) {
                this.addTable((StateTable) part);
            }
            break;
        default:
            break;
        }
    }

    /**
     * Creates PdfObjects for the given parts and adds them to the document.
     * @param parts Document parts to add.
     */
    public void add(List<DocumentPart> parts) {
        parts.forEach(this::add);
    }

    private void addTable(StateTable part) {
        PdfTable table = new PdfTable(part);
        PdfStream stream = this.getCurrentPageStream();
        stream.add(table);
        for (StateCell c : part.getStateCellCollection()) {
            StateCellContent content = c.getStateCellContent();
            if (content != null) {
                switch (content.getType()) {
                case IMAGE:
                    addImage((Image) content);
                    break;
                case TEXT:
                    this.addText((StateSplittableText) content, false);
                    break;
                default:
                    break;
                }
            }
        }
    }

    private void addImage(Image part) {
        ByteBuffer buffer = ByteBuffer.wrap(part.getImageParser().getData());
        PdfIndirectObject imageRef = imageList.get(buffer);
        if (imageRef == null) {
            PdfImageDictionary imageDic = new PdfImageDictionary(part);
            imageRef = body.addObject(imageDic);
            imageList.put(buffer, imageRef);
        }
        this.getCurrentPage().addResource(imageRef);
        PdfStream stream = this.getCurrentPageStream();
        stream.add(new PdfImage(imageRef.getReference().getResourceReference(), part));
        stream.addFilter(part.getCompressionMethod());
    }

    /**
     * Adds a paragraph object to the document.
     * @param paragraph Paragraph to be added.
     */
    private void addParagraph(StateParagraph paragraph) {
        List<StateText> textCollection = paragraph.getStateTextCollection();
        for (int i = 0; i < textCollection.size(); ++i) {
            boolean ignoreMatrix = true;
            if (i == 0) {
                ignoreMatrix = false;
            }
            this.addText(textCollection.get(i), ignoreMatrix);
        }

    }

    /**
     * Adds the given text object to the PdfTextStream of the current page.
     * @param text StateText that needs to be added.
     * @param overrideMatrix if true the matrix of the text will be disregarded. This should be true when the new text object has the same
     * matrix as the text object before it.
     */
    private void addText(StateSplittableText text, boolean overrideMatrix) {
        PdfIndirectObject font = this.addFont(text.getFont());
        PdfFont fontObj = (PdfFont) font.getObject();
        if (fontObj.getEncoding() != null) {
            fontObj.getEncoding().updateDifferences(text.getText());
        }
        currentPage.add(font);
        PdfText pdfText = new PdfText(fontObj);
        PdfStream ts = getCurrentPageStream();

        AddTextPropertiesToPdfText(text, overrideMatrix, pdfText);

        ts.add(pdfText);
        ts.addFilter(text.getCompressionMethod());
    }

    private void AddTextPropertiesToPdfText(StateSplittableText text, boolean overrideMatrix, PdfText pdfText) {
        if (overrideMatrix) {
            pdfText.addFont(getPdfFont(text.getFont()), text.getTextSize());
            pdfText.addTextString(text, currentPage.getLeading());
        } else {
            pdfText.addText(text, getPdfFont(text.getFont()), currentPage.getLeading());
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
            FontMetrics metrics = font.getFontFamily().getMetricsForStyle(font.getStyle());
            AddFontProgram(font, newFontDescriptor, metrics);

            PdfIndirectObject enc = body.addObject(new PdfFontEncoding(font));
            PdfFont newFont = new PdfFont(font, enc);
            PdfIndirectObject indirectFont = body.addObject(newFont);
            PdfIndirectObject indirectFontDictionary = body.addObject(newFontDescriptor);
            newFont.setFontDescriptorReference(indirectFontDictionary.getReference());

            fontList.put(font, indirectFont);
            return indirectFont;
        } else {
            return fontList.get(font);
        }
    }

    private void AddFontProgram(Font font, PdfFontDescriptor newFontDescriptor, FontMetrics metrics) {
        byte[] fontProgramFile = metrics.getFontFile();
        if (fontProgramFile != null) {
            PdfFontProgram fontProgram = CreateFontProgram(metrics, fontProgramFile);
            PdfIndirectObject indirectFontFile = body.addObject(fontProgram);
            newFontDescriptor.setFontFileReference(indirectFontFile.getReference(), font.getFontFamily().getSubType());
        }
    }

    private PdfFontProgram CreateFontProgram(FontMetrics metrics, byte[] fontProgramFile) {
        PdfFontProgram fontProgram = new PdfFontProgram();
        fontProgram.addFilter(DEFAULT_COMPRESSION_METHOD);
        fontProgram.setFontProgram(new PdfFile(fontProgramFile));
        fontProgram.setLengths(metrics.getFontProgramLengths());
        return fontProgram;
    }

    /**
     * Adds a new page and changes the current page.
     * @param page Page to add.
     */
    public void addPage(Page page) {
        PdfPage pdfPage = new PdfPage(page.getWidth(), page.getHeight(), page.getLeading(), page.getRotation());
        pdfPage.setMargins(page.getMarginLeft(), page.getMarginRight(), page.getMarginBottom(), page.getMarginTop());
        currentPage = (PdfPage) body.addPage(pdfPage).getObject();
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
        dos.write(Constants.LINE_SEPARATOR);
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
}
