package nl.mad.pdflibrary.api.state;

import java.util.LinkedList;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.PlaceableDocumentPart;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.StatePage;
import nl.mad.pdflibrary.model.StateParagraph;
import nl.mad.pdflibrary.model.StatePlaceableDocumentPart;
import nl.mad.pdflibrary.model.StateText;
import nl.mad.pdflibrary.model.Text;

/**
 * DocumentState processes the builders state and builds up the actual representative state of the document.
 * @author Dylan de Wolff
 *
 */
public class DocumentState {
    private List<Page> state;

    /**
     * Creates a new instance of DocumentState.
     */
    public DocumentState() {
        state = new LinkedList<Page>();
    }

    /**
     * Updates the state with the given builder state. This will clear the currently saved state.
     * @param builderState Builder state to process.
     */
    public void updateState(List<Page> builderState) {
        List<Page> builderStateCopy = new LinkedList<Page>(builderState);
        state = new LinkedList<Page>();
        for (int i = 0; i < builderStateCopy.size(); ++i) {
            Page page = builderStateCopy.get(i);
            StatePage newPage = new BaseStatePage(page);
            Page masterPage = page.getMasterPage();
            if (masterPage != null) {
                StatePage masterCopy = new BaseStatePage(page.getMasterPage());
                processPageContent(masterPage, masterCopy);
                newPage.master(masterCopy);
                newPage.addAll(masterCopy.getContent());
            }
            state.add(newPage);
            Page overflowPage = processPageContent(page, newPage);
            if (overflowPage != null) {
                builderStateCopy.add(i + 1, overflowPage);
            }
        }
    }

    private Page processPageContent(Page oldPage, StatePage newPage) {
        processFixedPositionContent(oldPage.getFixedPositionContent(), newPage);
        return processPositioning(oldPage.getPositionlessContent(), newPage);
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
                text.processContentSize(page, text.getPosition().getX(), true);
                page.add(text);
                break;
            case PARAGRAPH:
                StateParagraph paragraph = new BaseStateParagraph((Paragraph) part, true);
                paragraph.processContentSize(page, true);
                page.add(paragraph);
                break;
            default:
                break;
            }
        }
    }

    /**
     * Adds non-fixed content to the given page. 
     * @param content Content to add.
     * @param page Page to add content to.
     * @return Instance of page if there is overflow, null otherwise.
     */
    private Page processPositioning(List<DocumentPart> content, StatePage page) {
        int i = 0;
        boolean overflowFound = false;
        while (!overflowFound && i < content.size()) {
            DocumentPart p = content.get(i);
            Position position = null;
            switch (p.getType()) {
            case TEXT:
                StateText text = new BaseStateText((Text) p);
                position = getPositionForPart(page, text);
                if (position == null) {
                    return handleOverflow(page, i, null, content);
                }
                text.on(position);
                page.add(text);
                StateText overflowText = text.processContentSize(page, 0, false);
                if (overflowText != null) {
                    return handleOverflow(page, i + 1, overflowText, content);
                }
                break;
            case PARAGRAPH:
                StateParagraph paragraph = new BaseStateParagraph((Paragraph) p, true);
                position = getPositionForPart(page, paragraph);
                if (position == null) {
                    return handleOverflow(page, i, null, content);
                }
                paragraph.on(position);
                page.add(paragraph);
                Paragraph overflowParagraph = paragraph.processContentSize(page, false);
                if (overflowParagraph != null) {
                    return handleOverflow(page, i + 1, overflowParagraph, content);
                }
                break;
            case IMAGE:
                //                BaseImage imageP = (BaseImage) p;
                //                BaseImage image = new BaseImage((int) imageP.getContentHeight(null), imageP.getContentWidth(null, null));
                //                image.on(position);
                //                page.add(image);
                break;
            default:
                page.add(p);
                break;
            }
            ++i;
        }
        return null;
    }

    /**
     * @param page Page to place the part on.
     * @param p The part to add.
     * @return
     */
    private Position getPositionForPart(StatePage page, StatePlaceableDocumentPart part) {
        Position position = null;
        position = page.getOpenPosition(part.getRequiredSpaceAbove(), part.getRequiredSpaceBelow());
        return position;
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
        Page overflowPage = new BaseStatePage(page);
        List<DocumentPart> newContent = new LinkedList<DocumentPart>();
        newContent.add(overflowContent);
        newContent.addAll(content.subList(overflowIndex, content.size()));
        page.getContent().removeAll(newContent);
        for (DocumentPart p : newContent) {
            if (p instanceof PlaceableDocumentPart) {
                ((PlaceableDocumentPart) p).setPosition(new Position());
            }
        }
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
}
