package wf.doyle.blockbuster.util;

import wf.doyle.blockbuster.item.LibraryItem;

/**
 * Storage class holding data about a user.
 * 
 * @author Jordan Doyle
 */
public class User {
	/**
	 * Holds the reference to the item that the user currently has
	 */
	private LibraryItem hasItem = null;

	/**
	 * Checks if the user currently has an item
	 * 
	 * @return true if the user has an item
	 */
	public boolean hasItem()
	{
		return this.hasItem != null;
	}

	/**
	 * Checks if a user has a certain item.
	 * 
	 * @param item
	 *            item to compare against
	 * @return true, if the user has the specified item
	 */
	public boolean hasItem(LibraryItem item)
	{
		if(!hasItem()) return false;
		
		return (hasItem.equals(item));
	}

	/**
	 * Takes the item off of the user
	 */
	public void removeItem()
	{
		this.hasItem = null;
	}

	/**
	 * Gives the user an item
	 * 
	 * @param l
	 *            item instance
	 */
	public void setItem(LibraryItem l)
	{
		this.hasItem = l;
	}
}
