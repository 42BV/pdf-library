

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import nl.mad.toucanpdf.DocumentBuilder;
import nl.mad.toucanpdf.api.BaseCell;
import nl.mad.toucanpdf.api.DocumentState;
import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.ImageType;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Table;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

import org.junit.Test;


public class documentCreationTest {
	
	
	@Test
	public void testDocumentCreation() {
		//create new document
		DocumentBuilder builder = new DocumentBuilder();
		//setting general document data
		builder.about("Test subject").title("Testing a document").writtenBy("Someone");
		//creating a masterpage and setting margins for it
		Page masterPage = builder.createPage().marginBottom(20).marginLeft(20).marginRight(20).marginTop(20);
		//creating document parts for the header
		Text headerText = builder.createText("Document: %documentTitle").on(20, masterPage.getHeight() - 15);	
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("logo_placeholder.jpg");	
		Image i = builder.createImage(input, ImageType.JPEG).height(40).on(masterPage.getWidth() - 50, masterPage.getHeight() - 5);
		//the height for text and image is slightly different because text is positioned according to the base line of text, while images are positioned
		//according to the top left point of the image.
		
		//adding the header and adding the parts made above to the header
		masterPage.addHeader().height(20).add(headerText).add(i).addAttribute("documentTitle", builder.getTitle());	
		
		//same for footer
		Text footerText = builder.createText("Page %pageNumber of %totalPages").on(20, 5);
		masterPage.addFooter().height(20).add(footerText);		
		
		//manually adding a new page and setting the master page, this page will contain our actual content
		builder.addPage().master(masterPage);
		builder.setDefaultMarginBottom(10);
		
		//Adding a new paragraph with three text objects
		Paragraph p = builder.addParagraph().addText(builder.createText("This is the title of paragraph #1").size(14).font(builder.createFont().bold()))
				.addText(builder.createText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse a sem sodales, laoreet nulla id, tempor nibh. Maecenas lectus mauris, malesuada vitae ultricies consectetur, scelerisque et sem. Vestibulum posuere turpis a lorem cursus, eget ornare tortor auctor. Nullam condimentum ipsum nec dignissim volutpat. Phasellus suscipit varius mauris, sit amet ultrices est mattis quis. Vivamus mattis nisi vel purus viverra lacinia. Sed eget justo sapien. Etiam nec velit quis metus sagittis pellentesque nec non orci. Proin volutpat, urna in aliquam laoreet, nibh diam elementum mauris, placerat tempus justo metus quis arcu."))
				.addText(builder.createText("Ut eget velit nec ipsum fermentum aliquet. Phasellus sollicitudin justo ac libero tincidunt ornare ut ut nunc. Mauris vestibulum cursus mauris ac elementum. Phasellus fringilla mi ac molestie tristique. Nunc ut turpis lorem. Suspendisse ac congue mauris. Nunc auctor dapibus facilisis. Nulla justo nulla, venenatis et porta nec, dapibus et lectus. Proin sit amet accumsan elit, eget hendrerit ipsum. Etiam ac tellus leo. Proin fermentum ante id rutrum tincidunt."));				
		
		//Load in image and create new image for the paragraph anchors
		input = this.getClass().getClassLoader().getResourceAsStream("hammock.jpg");
		i = builder.createImage(input, ImageType.JPEG).align(Alignment.CENTERED);
		
		//adding four anchors to the paragraph
		p.addAnchor(i).above(p.getTextCollection().get(1));
		p.addAnchor(((Image) i.copy()).marginBottom(10).marginRight(5)).leftOf(p.getTextCollection().get(1));
		p.addAnchor((Image) i.copy()).rightOf(p.getTextCollection().get(1));
		p.addAnchor((Image) i.copy()).beneath(p.getTextCollection().get(1));		
		
		//adding a table to the document
		Table table = builder.addTable().columns(5);
		table.addCell("Table header - row 1").columnSpan(5);		
		table.addCell("Text 1");
		table.addCell("Text 2");
		table.addCell(((Image) i.copy()).marginBottom(0));
		
		//adding another table, this time without filling empty remaining columns and with an empty second row
		//this table will not fit on the current page, so a second page will be added automatically
		Table table2 = builder.addTable().columns(3).drawFillerCells(false);
		table2.addCell("Row 1 - header");
		table2.addCell("Row 2&3 - header").columnSpan(2);
		table2.addCell(new BaseCell().columnSpan(3).height(10));
		table2.addCell("Text 1").height(80);
		table2.marginTop(30);
		
		//
		
		//retrieving the preview to check for positioning
		DocumentState state = builder.getPreview();
		assertEquals(2, state.getPages().size());
		Paragraph previewParagraph = state.getPreviewFor(p).get(0);
		assertEquals(new Position(20, 812.438), previewParagraph.getPosition());
		assertEquals(new Position(20, 812.438), previewParagraph.getTextCollection().get(0).getPosition());
		Position expectedPos = previewParagraph.getTextCollection().get(1).getPosition();
		assertTrue(FloatEqualityTester.equals(152, expectedPos.getX()) && FloatEqualityTester.equals(634.508, expectedPos.getY()));
		expectedPos = previewParagraph.getTextCollection().get(2).getPosition();
		assertTrue(FloatEqualityTester.equals(20, expectedPos.getX()) && FloatEqualityTester.equals(331.012, expectedPos.getY()));
		expectedPos = state.getPreviewFor(table).get(0).getPosition();
		assertTrue(FloatEqualityTester.equals(20, expectedPos.getX()) && FloatEqualityTester.equals(252.208, expectedPos.getY()));
		expectedPos = state.getPreviewFor(table2).get(0).getPosition();
		assertTrue(FloatEqualityTester.equals(20, expectedPos.getX()) && FloatEqualityTester.equals(792, expectedPos.getY()));
		
		//finishing creates the actual file and is here simply for developing purposes, the final product can be seen in "Testing a document.pdf".
		builder.finish();
	}
}
