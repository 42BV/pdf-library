package nl.mad.toucanpdf.model;

import java.util.List;
import java.util.Map;

/**
 * Interface for page areas. Page areas are used to specify headers/footers on pages.
 * Page areas contain a list of attributes allowing for the addition of specific data that 
 * might not be fully known before completing the document.
 * @author Dylan
 *
 */
public interface PageArea {
	/**
	 * Returns the height of the area.
	 * @return int containing the height.
	 */
	int getHeight();
	
	/**
	 * Sets the height of the area.
	 * @param height int containing the height.
	 * @return the PageArea instance.
	 */
	PageArea height(int height);
	
	/**
	 * Adds an attribute to the attribute list. If you wish to use the value of one of these attributes in your text, simply 
	 * add the following text to your text object "%keyOfAttribute" without the quotation marks. 
	 * The following attributes are supplied automatically during the creation of a preview or upon the creation of the document.
	 * - pageNumber, refer to as "%pageNumber"
	 * - totalPages, refer to as "%totalPages"
	 * @param key The key of the attribute.
	 * @param value The value of the attribute.
	 * @return the PageArea instance.
	 */
	public PageArea addAttribute(String key, String value);
	
	/**
	 * Returns the attribute value of the given key.
	 * @param key Key to check.
	 * @return String containing the value or null if the attribute could not be found.
	 */
	public String getAttribute(String key);
	
	/**
	 * Adds content to the page area. All the content of a page area is considered to be fixed content.
	 * @param part Part to add.
	 * @return the PageArea instance.
	 */
	public PageArea add(DocumentPart part);
	
	/**
	 * Returns the list of content from this PageArea.
	 * @return List containing DocumentParts.
	 */
	public List<DocumentPart> getContent();

	/**
	 * Returns the map of attributes for this page area.
	 * @return Map containing the attributes.
	 */
	Map<String, String> getAttributes();
}
