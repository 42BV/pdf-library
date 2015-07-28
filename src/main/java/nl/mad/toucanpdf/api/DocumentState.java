package nl.mad.toucanpdf.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.PageArea;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.PlaceableFixedSizeDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Table;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.model.state.StateDocumentPart;
import nl.mad.toucanpdf.model.state.StateImage;
import nl.mad.toucanpdf.model.state.StatePage;
import nl.mad.toucanpdf.model.state.StateParagraph;
import nl.mad.toucanpdf.model.state.StatePlaceableDocumentPart;
import nl.mad.toucanpdf.model.state.StateTable;
import nl.mad.toucanpdf.model.state.StateText;
import nl.mad.toucanpdf.state.BaseStateImage;
import nl.mad.toucanpdf.state.BaseStatePage;
import nl.mad.toucanpdf.state.BaseStateParagraph;
import nl.mad.toucanpdf.state.BaseStateTable;
import nl.mad.toucanpdf.state.BaseStateText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DocumentState processes the builders state and builds up the actual representative state of the document.
 * @author Dylan de Wolff
 *
 */
public class DocumentState {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentState.class);
    private List<Page> state = new LinkedList<Page>();
    private Map<DocumentPart, List<DocumentPart>> stateLink = new HashMap<DocumentPart, List<DocumentPart>>();

    /**
     * Creates a new instance of DocumentState.
     */
    public DocumentState() {
    }

    /**
     * Updates the state with the given builder state. This will clear the currently saved state.
     * @param builderState Builder state to process.
     */
    public void updateState(List<Page> builderState) {
        List<Page> builderStateCopy = new LinkedList<Page>(builderState);
        state = new LinkedList<Page>();
        stateLink = new HashMap<DocumentPart, List<DocumentPart>>();
        for (int i = 0; i < builderStateCopy.size(); ++i) {
            Page page = builderStateCopy.get(i);
            StatePage newPage = CreateStateInstanceForPage(page);
            state.add(newPage);
            addToStateLink(page, newPage);
            Page overflowPage = processPageContent(page, newPage);
            if (overflowPage != null) {
                builderStateCopy.add(i + 1, overflowPage);
            }
        }
        processPageAreas();
    }

    private StatePage CreateStateInstanceForPage(Page page) {
        StatePage newPage = new BaseStatePage(page);
        newPage.setOriginalObject(page);
        AddMasterPageToNewStatePage(page, newPage);
        return newPage;
    }

    private void AddMasterPageToNewStatePage(Page page, StatePage newPage) {
        Page masterPage = page.getMasterPage();
        if (masterPage != null) {
            StatePage masterCopy = new BaseStatePage(page.getMasterPage());
            processPageContent(masterPage, masterCopy);
            newPage.master(masterCopy);
            newPage.addAll(masterCopy.getContent());
        }
    }

    private void processPageAreas() {
        String totalPageNumbers = String.valueOf(state.size() - 1);
        //we skip the title page, so pagenumber starts at 1
        for (int pageNumber = 1; pageNumber < state.size(); ++pageNumber) {
            StatePage page = (StatePage) state.get(pageNumber);
            ProcessPageAreaContent(totalPageNumbers, pageNumber, page, page.getHeader());
            ProcessPageAreaContent(totalPageNumbers, pageNumber, page, page.getFooter());
        }
    }

    private void ProcessPageAreaContent(String totalPageNumbers, int i, StatePage sp, PageArea header) {
        if (header != null) {
            addPageNumbersToPageArea(totalPageNumbers, i, header);
            processPageAreaContent(header, sp);
        }
    }

    private void addPageNumbersToPageArea(String totalPageNumbers, int number, PageArea area) {
        area.addAttribute("pageNumber", String.valueOf(number));
        area.addAttribute("totalPages", totalPageNumbers);
    }

    private void processPageAreaContent(PageArea area, StatePage sp) {
        List<DocumentPart> content = new ArrayList<DocumentPart>();
        CopyPageAreaContent(area, content);

        Map<String, String> attributes = area.getAttributes();
        for (DocumentPart part : content) {
            if (part.getType().equals(DocumentPartType.TEXT)) {
                processAttributes((Text) part, attributes);
            } else if (part.getType().equals(DocumentPartType.TABLE)) {
                processPageAreaAttributeForTextInTable(attributes, (Table) part);
            }
        }
        processFixedPositionContent(content, sp);
    }

    private void processPageAreaAttributeForTextInTable(Map<String, String> attributes, Table table) {
        List<Cell> cells = table.getContent();
        for (Cell c : cells) {
            if (c.getContent().getType().equals(DocumentPartType.TEXT)) {
                c.content(processAttributes((Text) c.getContent(), attributes));
            }
        }
    }

    private void CopyPageAreaContent(PageArea area, List<DocumentPart> content) {
        content.addAll(area.getContent().stream().map(this::createCopyOf).collect(Collectors.toList()));
    }

    private DocumentPart createCopyOf(DocumentPart part) {
        if (part != null) {
            switch (part.getType()) {
            case PARAGRAPH:
                return new BaseParagraph((Paragraph) part, true);
            case TABLE:
                return createCopyOfTable((Table) part);
            default:
                if (part instanceof PlaceableDocumentPart) {
                    return ((PlaceableDocumentPart) part).copy();
                }
            }
        }
        return null;
    }

    private DocumentPart createCopyOfTable(Table table) {
        Table newTable = new BaseTable(table);
        newTable.removeContent();
        for (int i = 0; i < table.getContent().size(); ++i) {
            Cell c = table.getContent().get(i);
            newTable.addCell(new BaseCell(c).content((PlaceableDocumentPart) this.createCopyOf(c.getContent())));
        }
        return newTable;
    }

    private Text processAttributes(Text text, Map<String, String> attributes) {
        String textString = text.getText();
        for (Entry<String, String> entry : attributes.entrySet()) {
            textString = textString.replace('%' + entry.getKey(), entry.getValue());
        }
        return text.text(textString);
    }

    private Page processPageContent(Page oldPage, StatePage newPage) {
        processFixedPositionContent(oldPage.getFixedPositionContent(), newPage);
        return processPositioning(oldPage.getPositionlessContent(), newPage);
    }

    private void addToStateLink(DocumentPart old, DocumentPart newPart) {
        List<DocumentPart> results = this.stateLink.get(old);
        if (results != null) {
            results.add(newPart);
        } else {
            List<DocumentPart> newList = new LinkedList<DocumentPart>();
            newList.add(newPart);
            stateLink.put(old, newList);
        }
    }

    /**
     * Adds fixed position content to the given page.
     * @param content Content to add.
     * @param page Page to add content to.
     */
    private void processFixedPositionContent(List<DocumentPart> content, StatePage page) {
        for (DocumentPart part : content) {
            switch (part.getType()) {
            case TEXT:
                StateText text = new BaseStateText((Text) part);
                text.setOriginalObject(part);
                text.processContentSize(page, text.getPosition().getX(), true);
                AddPartToStatePage(page, part, text);
                break;
            case PARAGRAPH:
                StateParagraph paragraph = new BaseStateParagraph((Paragraph) part, true);
                paragraph.setOriginalObject(part);
                paragraph.processContentSize(page, true);
                AddPartToStatePage(page, part, paragraph);
                break;
            case IMAGE:
                StateImage image = new BaseStateImage((Image) part);
                image.setOriginalObject(part);
                image.processContentSize(page, true, false, true);
                AddPartToStatePage(page, part, image);
                break;
            case TABLE:
                StateTable table = new BaseStateTable((Table) part);
                ((Table) part).getContent().forEach(table::addCell);
                table.setOriginalObject(part);
                table.processContentSize(page, true, false, true);
                AddPartToStatePage(page, part, table);
                break;
            default:
                LOGGER.warn("The given document type: " + part.getType() + " is unsupported.");
                break;
            }
        }
    }

    private void AddPartToStatePage(StatePage page, DocumentPart original, StateDocumentPart part) {
        page.add(part);
        addToStateLink(original, part);
    }

    /**
     * Adds non-fixed content to the given page. 
     * @param content Content to add.
     * @param page Page to add content to.
     * @return Instance of page if there is overflow, null otherwise.
     */
    private Page processPositioning(List<DocumentPart> content, StatePage page) {
        for (int i = 0; i < content.size(); ++i) {
            DocumentPart p = content.get(i);
            Page overflow = null;
            switch (p.getType()) {
            case TEXT:
                overflow = addPositionlessText(content, page, i, p);
                break;
            case PARAGRAPH:
                overflow = addPositionlessParagraph(content, page, i, p);
                break;
            case IMAGE:
                overflow = addPositionlessImage(content, page, i, p);
                break;
            case TABLE:
                overflow = addPositionlessTable(content, page, i, p);
                break;
            default:
                page.add(p);
                break;
            }
            if (overflow != null) {
                return overflow;
            }
        }
        return null;
    }

    private Page addPositionlessTable(List<DocumentPart> content, StatePage page, int i, DocumentPart p) {
        Position position;
        StateTable table = new BaseStateTable((Table) p);
        StateTable overflow;
        ((Table) p).getContent().forEach(table::addCell);
        table.setOriginalObject(p);
        table.updateHeight(page);

        position = getPositionForPart(page, table);
        if (position == null) {
            return handleOverflow(page, i, null, content);
        }

        table.on(position);
        overflow = table.processContentSize(page);
        page.add(table);
        addToStateLink(p, table);

        if (overflow != null) {
            return handleOverflow(page, i + 1, overflow, content);
        }

        return null;
    }

    /**
     * Adds the given part to the page.
     * @param content Content to be added.
     * @param page Page to add the part to.
     * @param i current content index.
     * @param p part to add.
     */
    private Page addPositionlessImage(List<DocumentPart> content, StatePage page, int i, DocumentPart p) {
        Position position;
        BaseStateImage image = new BaseStateImage((Image) p);
        if (checkContentSize(image, page)) {
            image.setOriginalObject(p);
            position = getPositionForPart(page, image);
            if (position == null) {
                return handleOverflow(page, i, null, content);
            }
            image.on(position);
            if (!image.processContentSize(page)) {
                page.add(image);
                addToStateLink(p, image);

            } else {
                return handleOverflow(page, i, null, content);
            }
        }
        return null;
    }

    /**
     * Adds the given part to the page.
     * @param content Content to be added.
     * @param page Page to add the part to.
     * @param i current content index.
     * @param p part to add.
     */
    private Page addPositionlessParagraph(List<DocumentPart> content, StatePage page, int i, DocumentPart p) {
        Position position;
        StateParagraph paragraph = new BaseStateParagraph((Paragraph) p, true);
        paragraph.setOriginalObject(p);
        position = getPositionForPart(page, paragraph);
        if (position == null) {
            return handleOverflow(page, i, null, content);
        }
        paragraph.on(position);
        page.add(paragraph);
        addToStateLink(p, paragraph);
        Paragraph overflowParagraph = paragraph.processContentSize(page, false);
        if (overflowParagraph != null) {
            return handleOverflow(page, i + 1, overflowParagraph, content);
        }
        return null;
    }

    /**
     * Adds the given part to the page.
     * @param content Content to be added.
     * @param page Page to add the part to.
     * @param i current content index.
     * @param p part to add.
     */
    private Page addPositionlessText(List<DocumentPart> content, StatePage page, int i, DocumentPart p) {
        Position position;
        StateText text = new BaseStateText((Text) p);
        text.setOriginalObject(p);
        position = getPositionForPart(page, text);
        if (position == null) {
            return handleOverflow(page, i, null, content);
        }
        text.on(position);

        StateText overflowText = text.processContentSize(page, 0, false);
        AddPartToStatePage(page, p, text);
        if (overflowText != null) {
            return handleOverflow(page, i + 1, overflowText, content);
        }
        return null;
    }

    /**
     * @param page Page to place the part on.
     * @param p The part to add.
     * @return Position for the part.
     */
    private Position getPositionForPart(StatePage page, StatePlaceableDocumentPart part) {
        return page.getOpenPosition(part.getRequiredSpaceAbove(), part.getRequiredSpaceBelow(), part);
    }

    /**
     * Processes the overflow by creating a new page and adding any remaining content to that page.
     * @param page Page with overflow.
     * @param overflowIndex Index of the object causing overflow.
     * @param overflowContent Object that contains the non-fitting content of the overflow causing object.
     * @param content The content being processed.
     * @return the new page with the overflowing content.
     */
    private Page handleOverflow(Page page, int overflowIndex, DocumentPart overflowContent, List<DocumentPart> content) {
        BaseStatePage overflowPage = new BaseStatePage(page);
        if (page instanceof StateDocumentPart) {
            overflowPage.setOriginalObject(((StateDocumentPart) page).getOriginalObject());
        } else {
            overflowPage.setOriginalObject(page);
        }
        List<DocumentPart> newContent = new LinkedList<>();
        if (overflowContent != null) {
            newContent.add(overflowContent);
        }
        newContent.addAll(content.subList(overflowIndex, content.size()));
        page.getContent().removeAll(newContent);
        newContent.stream()
                .filter(p -> p instanceof PlaceableDocumentPart)
                .forEach(p -> ((PlaceableDocumentPart) p).setPosition(new Position()));
        overflowPage.addAll(newContent);
        return overflowPage;
    }

    /**
     * Returns the pages in the state.
     * @return list of pages.
     */
    public List<Page> getPages() {
        return state;
    }

    /**
     * Returns the page the given part is located on.
     * @param part Part to find.
     * @return instance of Page or null if the given part couldn't be found.
     */
    public Page getPageFor(PlaceableDocumentPart part) {
        DocumentPart objectToFind = part;
        //if this is an object from the preview state
        if (!(part instanceof StatePlaceableDocumentPart)) {
            List<DocumentPart> results = this.stateLink.get(part);
            if (results != null) {
                objectToFind = results.get(0);
            }
        }
        //if this is an object from the developer state
        if (objectToFind != null) {
            for (Page page : this.state) {
                if (page.getContent().contains(part)) {
                    return page;
                }
            }
        }
        return null;
    }

    private boolean checkContentSize(PlaceableFixedSizeDocumentPart part, Page page) {
        if (part.getWidth() + part.getMarginLeft() + part.getMarginRight() > page.getWidthWithoutMargins()
                || part.getHeight() + part.getMarginTop() + part.getMarginBottom() > page.getHeightWithoutMargins()) {
            LOGGER.warn("The given document part of type: " + part.getType()
                    + " is too large to fit on the page and has therefore been removed from the document.");
            return false;
        }
        return true;
    }

    /**
     * Returns the preview objects corresponding to the given table object.
     * @param table Table object to get the corresponding preview objects for.
     * @return List of table objects. The list will be empty if the given table object is not represented in the preview.
     */
    public List<Table> getPreviewFor(Table table) {
        return getLinkedObjects(table);
    }

    /**
     * Returns the preview objects corresponding to the given text object.
     * @param text Text object to get the corresponding preview objects for.
     * @return List of text objects. The list will be empty if the given text object is not represented in the preview.
     */
    public List<Text> getPreviewFor(Text text) {
        return getLinkedObjects(text);
    }

    /**
     * Returns the preview objects corresponding to the given paragraph object.
     * @param paragraph Paragraph object to get the corresponding preview objects for.
     * @return List of paragraph objects. The list will be empty if the given paragraph object is not represented in the preview.
     */
    public List<Paragraph> getPreviewFor(Paragraph paragraph) {
        return getLinkedObjects(paragraph);
    }

    /**
     * Returns the preview objects corresponding to the given image object.
     * @param image Image object to get the corresponding preview objects for.
     * @return List of image objects. The list will be empty if the given image object is not represented in the preview.
     */
    public List<Image> getPreviewFor(Image image) {
        return getLinkedObjects(image);
    }

    /**
     * Returns the preview pages corresponding to the given page object.
     * @param page Page object to get the corresponding preview pages for.
     * @return List of page objects. The list will be empty if the given page object is not represented in the preview.
     */
    public List<Page> getPreviewFor(Page page) {
        return getLinkedObjects(page);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getLinkedObjects(T part) {
        List<DocumentPart> results = stateLink.get(part);
        List<T> partList = new LinkedList<T>();
        if (results != null) {
            for (DocumentPart linkedObj : results) {
                try {
                    partList.add((T) linkedObj);
                } catch (ClassCastException e) {
                    LOGGER.debug("StateLink contains an object of the wrong type.");
                }
            }
        }
        return partList;
    }
}
