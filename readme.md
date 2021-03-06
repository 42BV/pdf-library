#Toucan-PDF
*A Java library for the creation of PDF files.*

##Features
The Toucan-PDF library is currently capable of the following features

- Creating a document.
- Adding text and (automatic) positioning of text, in- or outside a paragraph.
- Font selection (only the 14 default font types are available for now)
- Adding images
- Adding tables, with custom column/row spans
- Adding headers/footers
- And more!

##Introduction
Toucan-PDF offers an easy to use fluent API to start creating your documents with. In order to begin you simply create a new instance of DocumentBuilder.

    DocumentBuilder builder = new DocumentBuilder();

Congratulations, you've just created a document. Now, to start actually adding content we can just continue using this builder. The API offers method chaining allowing you to quickly create and fill new objects. Let's go ahead and fill in some metadata of the document.

    builder.title("The Hitchhiker's Guide to the Galaxy").writtenBy("Douglas Adams");

All it requires is calling a few methods and passing along our data. Adding actual content is not all that different from adding metadata. Here's an example of adding text to the document.
   
    builder.addText("Example text").on(10, 10).size(11);

In this example we've added the text "Example text" on a fixed position. You can also add text without specifying a position which will cause the library to automatically position the text for you.

You can also add multiple text objects to a paragraph. This is mostly useful for anchors (see below) and for setting margins on the combined text instead of individual objects. Note the use of builder.createText() instead of builder.addText() here, this makes sure the text object is only instantiated and not directly added to the document. Using addText and adding the text to a paragraph afterwards would result in showing the text twice!
   
    builder.addParagraph().on(10, 10).addText(builder.createText().text("This stays")).addText(builder.createText("together"));

The same goes here as for text. All document parts can be added without specifying a position. 
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

The state of the document is preserved after finishing up, so you can go back, make changes and create another PDF file if you wish. 

##Advanced Usage
Now that we have discussed the basics of the library, let's introduce some of the more advanced features. We'll handle them one by one, starting with some general features that are applicable to more than one document part. The term 'document part' is used more often in the library and refers to content in the document such as text or an image. For brevity's sake, the following examples will often assume that you have already created a DocumentBuilder instance or a document part instance.

###General features
Here we'll discuss a few of the options that are available on several, if not all, document parts.

#####- Fixed positioning
Almost all document parts can be given a fixed position. Parts with a fixed position can overlap each other and it is generally not recommended to make extensive use of this feature if you're trying to create a normal page with a lot of text or other document parts. However sometimes fixed positioning is required or simply easier, for example it is required if you want to make use of headers/footers and fixed positioning is really useful for the creation of title pages. The code below shows how to set a position for a document part.
 
    builder.addText("Text").on(100, 200);
    builder.addText("Text2").on(new Position(100, 200));
If you do not pass a Position object yourself it will be created automatically, so there is usually no reason to use the second method from this example. Something important to take into account is that text is positioned differently than the other document parts. Text is positioned according to the baseline of the text, so the text will be slightly higher than the given position. The other document parts are positioned according to their top left point and will therefore be exactly on the given position.

#####- Alignment
Most document parts will allow you to adjust their alignment. Here is an example of how to that.

    builder.addText("I'm at the left side!");
    builder.addText("I'm in the center!").align(Alignment.CENTERED);
    builder.addText("I'm at the right side!").align(Alignment.RIGHT);
    builder.addText("I had my reasons!").align(Alignment.JUSTIFIED);
These four are all the alignment options available. Keep in mind that justified alignment only works once your text occupies more than one line. Using justified alignment on document parts such as images or tables will have no effect.

##### - Margins
All document parts have the option to set the margin values. 
    
    builder.addTable().marginRight(10).marginLeft(10).marginTop(10).marginBottom(10);
You can also set the margins for pages, this works the same as in the example above. In order to streamline this process you can set a default margin value in DocumentBuilder. All document parts made with the builder will use these default margins. Adjusting the default margin works as follows:

    builder.setDefaultMarginLeft(20).setDefaultMarginRight(10).setDefaultMarginBottom(30).setDefaultMarginTop(5);

These default margins will not be applied to pages you create.

##### - Builder defaults
There are several other builder defaults you can set besides the margins mentioned above. You can also set a default font to use, a default text size to use and a default text color to use. Whenever you add or create text through the builder, these defaults will be applied to the resulting object.

    builder.setDefaultFont(myCoolFont).setDefaultTextSize(11).setDefaultColor(Color.RED);

##### - Compression
The library also allows you to set the compression to use for your document parts. Right now only flate compression is supported and flate will always be used by default. Here is an example of how to change the compression.

    builder.addText("Text").compression(Compression.FLATE);

##### - Wrapping
Unique to tables and images is the option to allow or disallow wrapping. When wrapping is allowed other document parts will be placed next to the image/table if there are available spaces. If not then the other document parts will simply be placed beneath the object. Changing this is very straight forward:

    builder.addTable().allowWrapping(false);

This value will be overridden when the image or table is within an anchor. Right and left anchors automatically allow wrapping, while anchors above or beneath a text will automatically disallow wrapping.

###Text
We've already discussed the basics of text addition, but there are a few more things we can do with text. For example we can set the shear and scale of text. Changing the shear values of a text allows you to skew the text. Use small values to adjust this (0.1 is a slight skew for example). The scale adjusts the size of the text. Both the shear and scale can be set on the X or Y axis separately or at the same time. In the example below we use the method that allows us to set both values at once.

    myTextObject.scale(10, 10).shear(0.1, 0.1);
    
Beware that the shear and scale are **not** taken into account by the library during automatic positioning. Therefore a text with a large scale can go out of the page bounds and is likely to overlap with other content on the page. If you want simple scaling on both axes or a slight skew simply adjust the text size and use an italic font instead of changing the scale and shearing values. If you do decide to use these options make sure to test the output to see if it is the result you wanted.

Furthermore we can also change the color of text. You can either use one of the default available colors or determine one yourself by passing RGB values. Note that these RGB values must be between zero and one, not between zero and 255. This means the color red, which is one of the default available colors, would have the RGB values of 1, 0 and 0.

    myFirstText.color(Color.GREEN);
    mySecondText.color(new Color(0.3, 0.7, 0.2));

###Pages
While you can let the library add pages automatically by simply adding content, you can also take matters into your own hands. 

	builder.addPage().size(400, 400).marginTop(20).marginLeft(30).marginRight(30).marginBottom(10);

In this example we've manually added a new page to the document. The page width and height are both 400 points. If we hadn't specified a size ourselves the page size would default to the size of an A4. We've also adjusted the margins of the page. Beware that content with a fixed position is capable of ignoring said margins. If we now add content through the document builder it'll automatically be placed on this newly added page.

However, what if we add so much content to the page that it will no longer fit? No problem, the library will make a new page for you automatically. This "overflow" page will contain all the content that did not fit on the last page and will copy all attributes of the previous page. So you don't have to worry about constantly adding pages manually. This also brings us to the next feature.

#####- Master pages 
The library also supports the use of master pages. This allows you to determine a layout for multiple pages at once. Master pages function exactly the same as normal pages. Things get interesting when you appoint a master page to one of your normal "content" pages. The normal page will take over all attributes from the given master page and will also copy the content of the master page. Here is an example.

    Page masterPage = builder.createPage().size(500, 500).marginTop(50);
    builder.addPage().master(masterPage);

The page added by calling the addPage() method will have the same size and margin top value as the master page. Keep in mind that content copied from the master page is considered to be fixed content! 

#####- Headers/footers
While master pages are fun, they start getting a lot more useful when combined with headers and footers. Luckily we have the option to do just that. Let's demonstrate it with a bog standard page.

    Text headerText = builder.createText("Hello!").on(10, 800);
    Text footerText = builder.createText("Bye!").on(10, 10);
    Page page = builder.addPage().addHeader().height(20).add(headerText);
    page.addFooter().height(20).add(footerText);

So, we've now added a new header and footer to a page. As you can see we've manually positioned the text for both the header and footer. All content within the header and footer is considered as fixed content and therefore should be given a position. Keep in mind that content in the header/footer **can** exceed the given height limit. There is nothing stopping you from adding text which is positioned at the dead center of the page. You can also add tables, paragraphs or images to the header.

This is not all we can do with headers. Let's take a look at this example.

    Text headerText = builder.createText("Title: %documentTitle").size(14).on(20, 800);
    builder.addPage().addHeader().addAttribute("documentTitle", "Toucan-PDF").addText(headerText);

This is an example of the attribute system that is in place. You can add attributes to headers and refer to them in your text. The attributes are stored with a key, in our example the key is "documentTitle" with the value "Toucan-PDF". Now we can refer to this  attribute in our text by simply prepending a % to the key value. These attributes are not global, you'll have to specify them for each header/footer separately. There are also a few attributes that are provided by the library itself. These are the "pageNumber" and "totalPages" attributes. All headers and footers will get these automatically, so no need to add these yourself. You could create a nice page number indicator by using the following text in a footer. 
> Page %pageNumber of %totalPages

###Images
Next up are the images. Adding an image works much the same as adding other document parts.

    InputStream imageDataStream = ...
    byte[] imageData = ...
    builder.addImage(imageDataStream, ImageType.JPEG).height(200).marginRight(10);
    builder.addImage(imageData, ImageType.JPEG).width(100).on(20, 500);

As you can see there are several ways to add or create images. You can either pass along an InputStream that contains the image data or pass a byte array containing the data. You also have to specify what kind of image format we're dealing with, currently only JPEG is supported. After creating the image you can adjust all kinds of attributes like the height, width and position. Just like with any other document parts you can also adjust the margins. The width and height method used above automatically incorporate scaling. If you wish to adjust the size of the image without scaling you can pass a boolean with the value false as a second parameter. 

##### - Anchors
You can also attach images to text within a paragraph by using anchors. The use of anchors is fairly straight forward.

    Text text = builder.createText("Text in paragraph.");
    Image image = builder.createImage(imageData, ImageType.JPEG);
    builder.addParagraph().addText(text).addAnchor(image).above(text);

There is also the option to place an anchor beneath a text or left/right of a text. Anchors also support margins allowing you to put some space between the text and the anchor object. As said before wrapping is determined automatically. You can also set the alignment of the anchor object, but it will only be taken into account if the anchor is placed above or below the text.

####Tables
The last document part is the table object. While creating tables works exactly the same as creating other document parts, filling them is slightly different though. Let's look at an example to demonstrate this.

    Table table = builder.addTable().columns(2);
    table.addCell("Text 1");
    table.addCell(myCoolImage);
    table.addCell("Text 2");
    table.addCell("Text 3");

The above code would result in a table two columns wide with on the first row a single column containing the text "Text 1", followed by an image in the second column. On the second row would be a column containing the text "Text 2" followed by a column with the text "Text 3". Keep in mind that you can only add text and images to tables. It's not possible to add paragraphs or other tables. Tables can also be added to anchors just like images. There is a lot more to learn about tables, but we'll discuss this separately in the sections below.

##### - Borders
You can determine the border size of a table yourself. This size can range from zero to six. On zero the table will simply have no visible borders. You can also set the border on a cell level instead of on a table level. The border set on the table level will automatically be applied to each cell you did not manually specify a border on. 

    myAmazingTable.border(2);
    myPerfectCell.border(0);

##### - Padding
You can also set a padding value on both a table and cell level. This works the same as for borders. If you do not specify padding on a cell, the table padding will be used by default. You can only set a single padding value that will be applied on each side.

    myAmazingTable.padding(2);
    myPerfectCell.padding(0);

##### - Column & row spans
Cells can take up multiple columns and rows if you want. It's fairly straightforward, simply adjust the value on a cell. The library will automatically limit the column span to the actual size of the table if the specified value exceeds it.

    myPerfectCell.columnSpan(2).rowSpan(2);
    
##### - Headers
If you want you can decide to repeat the header of the table in case the table has to be spread over multiple pages due to the table not fitting. The first row of a table will be automatically considered to be the header. 

    myAmazingTable.repeatHeader(true);

##### - Filler cells
Sometimes the cells that you add will not be able to fully fill a table row, like in the example below. Let's assume that myAmazingTable has a column count of four per row.
   
    myAmazingTable.addCell("t1").columnSpan(3);
    myAmazingTable.addCell("t2").columnSpan(2);

In this example the second cell would not be able to fit on the same row as the first cell, however the first cell only takes up three columns. This means there is a single empty column at the end of the first row and two empty columns at the end of the second row. You can decide to draw those open spaces as empty cells or to simply not draw them at all. By default they are drawn.

    myAmazingTable.drawFillerCells(false);
    
##### - Vertical alignment
You can activate vertical alignment on a table level. Keep in mind that vertical alignment only applies if there is a column in the row that is larger than the rest.

    myAmazingTable.verticalAlign(true);

#### - Preview retrieval
As stated before you do not have to specify positions for document parts, the library can handle the positioning for you. However the positioning is not processed immediately after adding the part through the DocumentBuilder. The library maintains two states. The first one is the state that is built by adding pages and content through the DocumentBuilder as we've been doing throughout these examples. The second state only comes into play when you decide to create the PDF file. The library executes the positioning and splitting of document parts when you call the finish method and the result is maintained in this second state. This ensures that the original state will always remain exactly as you made it allowing it to be reused or adjusted. However, there is an option that allows you to take a peek into this second state before finalizing the entire document. The DocumentBuilder offers a method that retrieves a preview allowing you to see what will happen to the document parts you've added so far. By requesting this preview the library will calculate and process the second state. This state will be exactly the same as the state created during the finish method. You can then use this preview to see how many pages there actually are due to overflow processing and how your document parts have been split/positioned. Enough talking for now, let's see how this actually works. In this example we're making the assumption that the text is too large to fit on the page and needs to be split into two text objects. 

    Page page = builder.addPage();
    Text text = builder.addText("This part of the text will fit. This part will not.");
    DocumentState preview = builder.getPreview(); 
    List<Text> actualTextObjects = preview.getPreviewFor(text);
    actualTextObjects.get(0).getText(); //would return "This part of the text will fit."
    actualTextObjects.get(1).getText(); //would return "This part will not."
    actualTextObjects.get(0).getPosition(); //returns the exact position of the text object. 
    preview.getPreviewFor(page).size() //would return two. Since the text object does not fit on the first page, the creation of a second page is required.

As you can see the preview state allows us to see exactly what the document will look like. There is a lot more information you can pull from the objects returned by the preview state. Making changes to the objects in the preview state will not do anything. The state is fully recalculated every time you request the preview and before an actual PDF file is created. Keep this in mind while working on very large documents since the calculations can become costly performance wise if you retrieve the preview often. The getPreviewFor method works for almost every document part. However, parts that are added to other parts can not be retrieved through the use of this method. So you can not, for example, retrieve the text within a paragraph through the original text object or retrieve an image that was added to a table. You can still retrieve these through other means by simply looking in the text collection of the paragraph or the content of a table. 

##Example

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
    .addText(builder.createText("Lorem ipsum dolor sit amet, consectetur adipiscing elit...."))
    .addText(builder.createText("Ut eget velit nec ipsum fermentum aliquet. Phasellus...."));				
		
    //Load in image and create new image for the paragraph anchors
    input = this.getClass().getClassLoader().getResourceAsStream("example.jpg");
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
		
    //finishing creates the actual file
    builder.finish();
