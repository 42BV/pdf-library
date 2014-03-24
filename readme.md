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

    builder.getDocument().title("The Hitchhiker's Guide to the Galaxy").writtenBy("Douglas Adams");

We simply retrieve the actual document from the builder and set the title and the author of the document. Adding actual content is not all that different from adding metadata. Here's an example of adding text to the document.
   
    builder.addText("Example text").on(10, 10).size(11);

Or adding it to a paragraph to make sure it stays together:
   
    builder.addParagraph().on(10, 10).addText(builder.createText().text("This stays")).addText(builder.createText("together"));

When you're finished with adding content, simply use the finish method to create the PDF file.

    builder.finish();

The complete code for creating a very simple document including a font would be:

    //create new document
    DocumentBuilder builder = new DocumentBuilder();

    //set metadata
    builder.getDocument().title("Simple Document Example").writtenBy("Example Author").on(Calendar.getInstance()).about("Toucan-PDF");

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

It's also possible to skip the DocumentBuilder entirely and work with the underlying classes. If you're new to the library it's not advisable to do this, but here is an example anyway:

    //create a new document
    Document document = new Document();
    
    //creates a new font instance, the Base* indicates this is the basic implementation of the corresponding interface.
    //In this case, the font interface.
    Font font = new BaseFont(FontFamilyType.HELVETICA, FontStyle.BOLD);
    
    //create a new text instance.
    Text text = new BaseText("Example text").on(10, 10).size(11).font(font);
    
    //add the text instance to the document
    document.addPart(text);
    
    //create a new paragraph instance
    Paragraph paragraph = new BaseParagraph();
    
    //add text to the paragraph
    paragraph.addText(new BaseText("Paragraph example text").size(11));
    
    //add the paragraph to the document
    document.addPart(paragraph);
    
    //finish the document
    document.finish();

    //or pass along your own OutputStream
    OutputStream baos = new ByteArrayOutputStream();
    document.finish(baos);
