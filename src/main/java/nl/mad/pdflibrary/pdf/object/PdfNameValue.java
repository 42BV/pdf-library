package nl.mad.pdflibrary.pdf.object;

/**
 * Contains a list of standard PdfName values
 * 
 * @author Dylan de Wolff 
 * @see PdfName
 */
public enum PdfNameValue {
    TYPE, KIDS, FONT, BASE_FONT, SUB_TYPE, TYPE1, CATALOG, PAGES, PAGE, CONTENTS, RESOURCES, MEDIA_BOX, LENGTH, ROOT, SIZE, INFO, AUTHOR, CREATOR, CREATION_DATE, SUBJECT, TITLE, FILTER;

    @Override
    public String toString() {
        String result = "";
        String allCapsValue = super.toString();
        String[] splitValue = allCapsValue.split("_");
        for (String value : splitValue) {
            result += (value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase());
        }
        return result;
    }
}
