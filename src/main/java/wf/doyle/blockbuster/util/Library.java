package wf.doyle.blockbuster.util;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wf.doyle.blockbuster.App;
import wf.doyle.blockbuster.file.FileReader;
import wf.doyle.blockbuster.item.LibraryItem;

/**
 * Contains various functions for the Library.
 * 
 * @author Jordan Doyle
 */
public final class Library {
	/**
	 * FileReader instance to read from the data files
	 */
	private FileReader reader;

	/**
	 * Instantiates a new Library class.
	 * 
	 * @throws URISyntaxException
	 *             resource could not be parsed
	 */
	public Library() throws URISyntaxException
	{
		Path path = Paths.get(App.class.getResource("/data/items_all.txt").toURI());

		this.reader = new FileReader(path);
		this.reader.parseFile();
	}

	/**
	 * Prints all the data about each item
	 */
	public void printAllDetails()
	{
		App.LOGGER.debug("UNSORTED DETAILS TEST");
		App.LOGGER.debug("=====================================================================");
		
		for(LibraryItem item : App.items)
		{
			for(String containing : FileReader.containing.get(item.getClass().getName()))
			{
				try
				{
					Field f = FileReader.getField(containing, item.getClass());
					f.setAccessible(true);
					App.LOGGER.debug(containing.trim() + ": " + f.get(item));
				}
				catch(IllegalArgumentException | IllegalAccessException e)
				{
					App.LOGGER.error("Couldn't print out details", e);
				}
			}

			App.LOGGER.debug("----------------------------------------");
		}
		
		App.LOGGER.debug("=====================================================================");
	}

	/**
	 * Prints all the data about each item sorted on title
	 */
	public void printAllDetailsSortedOnTitle() // who picks the method names for
	{                                          // these tasks..?
		List<LibraryItem> items = App.items;
		
		Collections.sort(items, (arg0, arg1) -> {
			return arg0.getName().compareTo(arg1.getName());
		});

		App.LOGGER.debug("SORTED DETAILS TEST");
		App.LOGGER.debug("=====================================================================");
		
		for(LibraryItem item : items)
		{
			for(String containing : FileReader.containing.get(item.getClass().getName()))
			{
				try
				{
					Field f = FileReader.getField(containing, item.getClass());
					f.setAccessible(true);
					App.LOGGER.debug(containing.trim() + ": " + f.get(item));
				}
				catch(IllegalArgumentException | IllegalAccessException e)
				{
					App.LOGGER.error("Couldn't print out details", e);
				}
			}
			
			App.LOGGER.debug("----------------------------------------");
		}
		
		App.LOGGER.debug("=====================================================================");
	}
}
