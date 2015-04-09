package wf.doyle.blockbuster.item;

/**
 * @see wf.doyle.blockbuster.item.LibraryItem
 * @author Jordan Doyle
 */
public abstract class PrintedItem extends LibraryItem {
	/**
	 * Number of pages in the item
	 */
	private int noOfPages;

	/**
	 * Publisher of the item
	 */
	private String publisher;

	/**
	 * @return number of pages in the item
	 */
	public int getNumberOfPages()
	{
		return this.noOfPages;
	}

	/**
	 * @return publisher of the item
	 */
	public String getPublisher()
	{
		return this.publisher;
	}
}
