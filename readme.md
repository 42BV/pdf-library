#Toucan-PDF
*A Java library for the creation of PDF files.*

##Features
The Toucan-PDF library is currently capable of the following features

- Creating a document.
- Adding text and (automatic) positioning of text, in- or outside a paragraph.
- Font selection (only the 14 default font types are available for now)

Upcoming features include, but are not limited to

- Adding images
- Adding tables
- Adding forms
- Support for more page customization (headers, footers, margins, etc.)

##Usage
Toucan-PDF offers an easy to use fluent API to start creating your documents with. To start creating documents simply create a new instance of DocumentBuilder.

    DocumentBuilder builder = new DocumentBuilder();

Congratulations, you've just created a document. Now, to start actually adding content we can just continue using this builder. The API offers method chaining allowing you to quickly create and fill new objects. Let's start with filling in some metadata of the document.

    builder.title("The Hitchhiker's Guide to the Galaxy").writtenBy("Douglas Adams");

All it requires is calling a few methods and passing along our data. Adding actual content is not all that different from adding metadata. Here's an example of adding text to the document.
   
    builder.addText("Example text").on(10, 10).size(11);

You can also add multiple text objects to a paragraph to make sure they stay together. Note the use of builder.createText() instead of builder.addText() here, this makes sure the text object is only instantiated and not directly added to the document. Using addText and adding the text to a paragraph afterwards would result in showing the text twice!
   
    builder.addParagraph().on(10, 10).addText(builder.createText().text("This stays")).addText(builder.createText("together"));

When you're finished with adding content, simply use the finish method to create the PDF file.

    builder.finish();

The complete code for creating a very simple document including a font would be:

    //create new document
    DocumentBuilder builder = new DocumentBuilder();

    //set metadata
    builder.title("Simple Document Example").writtenBy("Example Author").on(Calendar.getInstance()).about("Toucan-PDF");

    //add font to the document and store it for the upcoming text
    Font font = builder.addFont().family(FontFamilyType.HELVETICA).boldItalic();

    //add text to the document
    builder.addText("Example text").size(11).font(font);

    //create another font
    Font secondFont = builder.addFont().family(FontFamilyType.TIMES_ROMAN).bold();

    //add paragraph to the document and add text objects to the paragraph
    builder.addParagraph().on(10, 10).addText(builder.createText().text("This stays").font(secondFont)).addText(builder.createText("together"));

    //finish up the document and create a PDF file
    builder.finish();

The state of the document is preserved after finishing up, so you can go back, make changes and create another PDF file. 
