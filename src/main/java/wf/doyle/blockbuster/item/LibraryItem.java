package wf.doyle.blockbuster.item;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import wf.doyle.blockbuster.App;
import wf.doyle.blockbuster.file.FileReader;
import wf.doyle.blockbuster.util.EnumLineType;

/**
 * Provides a base class for all items to extend.
 * 
 * @author Jordan Doyle
 */
public abstract class LibraryItem {
	/**
	 * Checks if the item currently on loan.
	 */
	private boolean onLoan = false;

	/**
	 * Amount of times the item has been taken.
	 */
	private int timesBorrowed = 0;

	/**
	 * URN of the item, as specified by the providing data file.
	 */
	private String itemCode;

	/**
	 * Cost of the item, as specified by the providing data file.
	 */
	private int cost;

	/**
	 * Name of the library item
	 */
	private String title;

	/**
	 * Gets the unique reference number of the item.
	 * 
	 * @return URN of item
	 */
	public String getItemCode()
	{
		return this.itemCode;
	}

	/**
	 * Gets the storage class type for this class.
	 * 
	 * @return type of storage class
	 */
	public abstract EnumLineType getType();

	/**
	 * Gets the name of the item provided by the data file.
	 * 
	 * @return name of item
	 */
	public String getName()
	{
		return this.title;
	}

	/**
	 * Gets the cost of the item.
	 * 
	 * @return cost of the item in pounds
	 */
	public String getCost()
	{
		return new DecimalFormat("'£'0.00").format((float)this.cost / 100);
	}

	/**
	 * Checks if the item is currently being loaned
	 * 
	 * @return true if the item is loaned out
	 */
	public boolean getLoan()
	{
		return this.onLoan;
	}

	/**
	 * Checks how many times this item has been borrowed from the library.
	 * Increments every time {@link #takeItem()} is called.
	 * 
	 * @return amount of times item has been borrowed
	 */
	public int getTimesBorrowed()
	{
		return this.timesBorrowed;
	}

	/**
	 * Takes the item from the library and increments the taken counter by 1.
	 */
	public void takeItem()
	{
		if(this.getLoan()) return;

		this.onLoan = true;
		this.timesBorrowed++;
	}

	/**
	 * Returns the item back to the library and let it be available again.
	 */
	public void returnItem()
	{
		if(!this.getLoan()) return;

		this.onLoan = false;
	}

	@Override
	public String toString()
	{
		String[] contains = FileReader.containing.get(this.getClass().getName());

		String string = "";

		for(String key : contains)
		{
			Class<? extends LibraryItem> child = FileReader.classes.get(this.getType());

			Field f = FileReader.getField(key.trim(), child);
			f.setAccessible(true);
			try
			{
				string = string + f.get(this) + ", ";
			}
			catch(IllegalArgumentException | IllegalAccessException e)
			{
				App.LOGGER.error("Could not get value of reflection field", e);
			}
		}

		return string.substring(0, string.lastIndexOf(","));
	}

	/**
	 * Toggles between taking the item and putting it back.
	 * 
	 * @return true if we now have the item
	 */
	public boolean toggleItem()
    {
	    if(this.getLoan()) {
	    	this.returnItem();
	    	return false;
	    } else {
	    	this.takeItem();
	    	return true;
	    }
    }
}
