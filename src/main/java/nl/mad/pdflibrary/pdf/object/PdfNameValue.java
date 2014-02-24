package nl.mad.pdflibrary.pdf.object;

/**
 * Contains a list of standard PdfName values.
 * 
 * @author Dylan de Wolff 
 * @see PdfName
 */
public enum PdfNameValue {
    TYPE("Type"), KIDS("Kids"), FONT("Font"), BASE_FONT("BaseFont"), SUB_TYPE("SubType"), TYPE1("Type1"), CATALOG("Catalog"), PAGES("Pages"), PAGE("Page"), CONTENTS(
            "Contents"), RESOURCES("Resources"), MEDIA_BOX("MediaBox"), LENGTH("Length"), ROOT("Root"), SIZE("Size"), INFO("Info"), AUTHOR("Author"), CREATOR(
            "Creator"), CREATION_DATE("CreationDate"), SUBJECT("Subject"), TITLE("Title"), FILTER("Filter"), TYPE0("Type0"), MMTYPE1("MMType1"), TYPE3("Type3"), TRUE_TYPE(
            "TrueType");

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
