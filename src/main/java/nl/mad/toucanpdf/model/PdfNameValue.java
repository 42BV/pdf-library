package nl.mad.toucanpdf.model;

/**
 * Contains a list of standard PdfName values.
 * 
 * @author Dylan de Wolff 
 * @see nl.mad.toucanpdf.pdf.syntax.PdfName
 */
public enum PdfNameValue {
    TYPE("Type"), KIDS("Kids"), FONT("Font"), BASE_FONT("BaseFont"), SUB_TYPE("Subtype"), TYPE1("Type1"), CATALOG("Catalog"), PAGES("Pages"), PAGE("Page"), CONTENTS(
            "Contents"), RESOURCES("Resources"), MEDIA_BOX("MediaBox"), LENGTH("Length"), ROOT("Root"), SIZE("Size"), INFO("Info"), AUTHOR("Author"), CREATOR(
            "Creator"), CREATION_DATE("CreationDate"), SUBJECT("Subject"), TITLE("Title"), FILTER("Filter"), TYPE0("Type0"), MMTYPE1("MMType1"), TYPE3("Type3"), TRUE_TYPE(
            "TrueType"), FONT_DESCRIPTOR("FontDescriptor"), ENCODING("Encoding"), FONT_NAME("FontName"), FONT_FAMILY("FontFamily"), FLAGS("Flags"), FONT_BOUNDING_BOX(
            "FontBBox"), ITALIC_ANGLE("ItalicAngle"), ASCENT("Ascent"), DESCENT("Descent"), LEADING("Leading"), CAP_HEIGHT("CapHeight"), XHEIGHT("XHeight"), STEMV(
            "StemV"), STEMH("StemH"), AVG_WIDTH("AvgWidth"), MAX_WIDTH("MaxWidth"), MISSING_WIDTH("MissingWidth"), FIRST_CHAR("FirstChar"), LAST_CHAR(
            "LastChar"), WIDTHS("Widths"), WIN_ANSI_ENCODING("WinAnsiEncoding"), PARENT("Parent"), FONT_FILE("FontFile"), FONT_FILE2("FontFile2"), FONT_FILE3(
            "FontFile3"), ASCII_HEX_DECODE("ASCIIHexDecode"), ASCII_85_DECODE("ASCII85Decode"), FLATE_DECODE("FlateDecode"), LZW_DECODE("LZWDecode"), RUN_LENGTH_DECODE(
            "RunLengthDecode"), DCT_DECODE("DCTDecode"), CCITT_FAX_DECODE("CCITTFaxDecode"), JBIG2_DECODE("JBIG2Decode"), JPX_DECODE("JPXDecode"), COUNT(
            "Count"), LENGTH1("Length1"), LENGTH2("Length2"), LENGTH3("Length3"), XOBJECT("XObject"), WIDTH("Width"), HEIGHT("Height"), COLOR_SPACE(
            "ColorSpace"), BITS_PER_COMPONENT("BitsPerComponent"), IMAGE("Image"), DEVICE_GRAY("DeviceGray"), DEVICE_RGB("DeviceRGB"), DEVICE_CMYK("DeviceCMYK"), CAL_GRAY(
            "CalGray"), CAL_RGB("CalRGB"), LAB("Lab"), ICC_BASED("ICCBased"), INDEXED("Indexed"), PATTERN("Pattern"), SEPARATION("Separation"), DEVICE_N(
            "DeviceN");

    private String string;

    /**
     * Creates a new instance of PdfNameValue.
     * @param string How the value should be written to the PDF file.
     */
    private PdfNameValue(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
