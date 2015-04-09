package wf.doyle.blockbuster.item.items.printed;

import wf.doyle.blockbuster.item.PrintedItem;
import wf.doyle.blockbuster.util.EnumLineType;

/**
 * This object represents a Book in a library.
 * 
 * @author Jordan Doyle
 */
public class Book extends PrintedItem {
	/**
	 * Author of the book.
	 */
	private String author;

	/**
	 * ISBN of the book.
	 */
	private String isbn;

	/**
	 * @return author of the book
	 */
	public String getAuthor()
	{
		return this.author;
	}

	/**
	 * @return ISBN of the book
	 */
	public String getISBN()
	{
		return this.isbn;
	}

	@Override
	public EnumLineType getType()
	{
		return EnumLineType.BOOK;
	}
}
