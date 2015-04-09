package wf.doyle.blockbuster.file;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.google.common.base.CaseFormat;

import wf.doyle.blockbuster.App;
import wf.doyle.blockbuster.item.LibraryItem;
import wf.doyle.blockbuster.util.EnumLineType;

/**
 * Provides functions to read and parse provided files.
 * 
 * @author Jordan Doyle
 */
public class FileReader {
	/**
	 * Path to the file to read
	 */
	private Path path;

	/**
	 * Enum containing the current line type.
	 */
	private EnumLineType currentLine = EnumLineType.NONE;

	/**
	 * Pattern to find data types
	 */
	private Pattern dataTypes = Pattern.compile("\\[(.*) data\\]");

	/**
	 * Line number we're currently dealing with.
	 */
	private int lineNumber = 0;

	/**
	 * A list of what the current line type gives us, allows easy setting with
	 * reflection
	 */
	private String[] lineContains;

	/**
	 * Contains a list of what each line type should contain which lets us write
	 * data back to the file easily.
	 */
	public static Map<String, String[]> containing = new HashMap<String, String[]>();

	/**
	 * Contains a mapping of EnumLineTypes to the respective classes for use in
	 * {@link #wf.doyle.blockbuster.item.LibraryItem.toString()}
	 */
	public static Map<EnumLineType, Class<? extends LibraryItem>> classes = new HashMap<EnumLineType, Class<? extends LibraryItem>>();

	/**
	 * Instantiates a new instance of FileReader
	 * 
	 * @param path
	 *            path of the file to read
	 */
	public FileReader(Path path)
	{
		this.path = path;
	}

	/**
	 * Parses the current file
	 */
	public void parseFile()
	{
		// Open the file using UTF-8 charset
		try(Stream<String> lines = Files.lines(this.path, StandardCharsets.UTF_8))
		{
			Iterator<String> it = lines.iterator();

			while(it.hasNext())
			{
				this.lineNumber++;

				String line = it.next();

				// if we don't know what we're currently dealing with, lets just
				// leave it.
				if(this.currentLine == EnumLineType.UNKNOWN) continue;

				parseLine(line.trim());
			}
		}
		catch(IOException e)
		{
			App.LOGGER.error("Error whilst reading provided file", e);
		}
	}

	/**
	 * @return storage class from current line type
	 */
	private Class<? extends LibraryItem> getClassType()
	{
		Class<? extends LibraryItem> classType = null;

		// this is so horrible but there's no ways around it if I want to test
		// multiple packages for the class we want.
		try
		{
			// check if it's in wf.doyle.blockbuster.item.items.audiovisual in
			// its current case
			classType = (Class<? extends LibraryItem>) Class.forName("wf.doyle.blockbuster.item.items.audiovisual." + this.currentLine.toString());
		}
		catch(ClassNotFoundException e)
		{
			try
			{
				// check if it's in wf.doyle.blockbuster.item.items.printed in
				// camel case
				classType = (Class<? extends LibraryItem>) Class.forName("wf.doyle.blockbuster.item.items.printed." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.currentLine.toString()));
			}
			catch(ClassNotFoundException ex)
			{
				try
				{
					// check if it's in
					// wf.doyle.blockbuster.item.items.audiovisual in camel case
					classType = (Class<? extends LibraryItem>) Class.forName("wf.doyle.blockbuster.item.items.audiovisual." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.currentLine.toString()));
				}
				catch(ClassNotFoundException ex2)
				{
					App.LOGGER.error("Couldn't find storage class for this line type.", ex2);
				}
			}
		}

		return classType;
	}

	/**
	 * Find a private field in a class (and its parents)
	 * 
	 * @param nameRaw
	 *            name of field to find
	 * @param classType
	 *            class to find the field in
	 * @return the found private field
	 */
	public static Field getField(String nameRaw, Class<? extends LibraryItem> classType)
	{
		// for some reason the data files have extra spaces, why would you do
		// this :(
		String name = nameRaw.trim();

		Field field = null;

		// again, horrible code but it's the only thing we can do when we're
		// looking in multiple classes for a private field.
		try
		{
			// check LibraryItem for the field
			field = classType.getSuperclass().getSuperclass().getDeclaredField(name);
		}
		catch(NoSuchFieldException | SecurityException e)
		{
			try
			{
				// check the parent class for the field
				// (AudioVisualItem/PrintedItem)
				field = classType.getSuperclass().getDeclaredField(name);
			}
			catch(NoSuchFieldException | SecurityException ex)
			{
				try
				{
					// check the current class for the field
					field = classType.getDeclaredField(name);
				}
				catch(NoSuchFieldException | SecurityException ex2)
				{
					App.LOGGER.error("Couldn't find field " + name + " in " + classType.getName(), ex2);
				}
			}
		}

		return field;
	}

	/**
	 * Set the value of a private field
	 * 
	 * @param f
	 *            field to set the value of
	 * @param instance
	 *            instance to set the value in
	 * @param data
	 *            data to set the value as
	 * @return instance of LibraryItem
	 */
	public static LibraryItem setField(Field f, LibraryItem instance, String data)
	{
		try
		{
			try
			{
				// check if the field type is int
				if(f.getType().isAssignableFrom(int.class))
				{
					int val = (int) Double.parseDouble(data);
					f.setAccessible(true);
					f.set(instance, val);

					return instance;
				}
			}
			catch(NumberFormatException nfe)
			{
			}

			// check if the data is a boolean
			if(data.equalsIgnoreCase("true") || data.equalsIgnoreCase("false"))
			{
				boolean b = Boolean.parseBoolean(data);
				f.setAccessible(true);
				f.set(instance, b);

				return instance;
			}

			f.setAccessible(true);
			f.set(instance, data);
		}
		catch(IllegalArgumentException | IllegalAccessException e)
		{
			App.LOGGER.error("Couldn't set value of " + f.getName() + " in " + instance.getClass().getName(), e);
		}

		return instance;
	}

	/**
	 * Parses the current line and decides what to do with it.
	 * 
	 * @param line
	 *            string to parse
	 */
	private void parseLine(String line)
	{
		if(line.isEmpty() || line.startsWith("//"))
		{
			// get the data types from the "// data is" line, afaik it's not
			// what it's intended for, but ah well let's be lazy and use it for
			// reflection
			if(line.startsWith("// data is")) this.lineContains = line.substring(line.indexOf("// data is") + "// data is".length()).split(", ");

			return;
		}

		// if this line is a declaration block ([CD data]) then we wont continue
		// from here, we've already parsed it.
		if(determineLineType(line)) return;

		Class<? extends LibraryItem> itemClass = this.getClassType();

		// just some fields to help me with reflection. "classes" maps a class
		// object to an EnumLineType and "containing" just parses the "data is"
		// line for us for the reflection
		if(!containing.containsKey(itemClass.getName())) containing.put(itemClass.getName(), this.lineContains);
		if(!classes.containsKey(this.currentLine)) classes.put(this.currentLine, itemClass);

		try
		{
			LibraryItem item = itemClass.newInstance();

			// split the data on the line by comma
			ListIterator<String> iterator = Arrays.asList(line.split(",")).listIterator();

			while(iterator.hasNext())
			{
				int key = iterator.nextIndex();
				String value = iterator.next().trim(); // again with the
													   // unnecessary
													   // whitespaces

				Field field = getField(this.lineContains[key].trim(), itemClass);

				item = setField(field, item, value);
			}

			// add this storage class to our main array
			App.items.add(item);
		}
		catch(IllegalAccessException | InstantiationException e)
		{
			App.LOGGER.error("Couldn't instantiate storage class.", e);
		}
	}

	/**
	 * Parses the current line and determines the type of data it is providing
	 * 
	 * @param line
	 *            string to parse to type
	 * @return true if found a line type
	 */
	private boolean determineLineType(String line)
	{
		Matcher m = this.dataTypes.matcher(line);

		if(!m.find()) return false;

		switch(m.group(1).toLowerCase())
		{
			case "dvd":
				this.currentLine = EnumLineType.DVD;
				return true;

			case "cd":
				this.currentLine = EnumLineType.CD;
				return true;

			case "book":
				this.currentLine = EnumLineType.BOOK;
				return true;

			case "periodical":
				this.currentLine = EnumLineType.PERIODICAL;
				return true;

			default:
				this.currentLine = EnumLineType.UNKNOWN;
				App.LOGGER.error("Couldn't recognise data type on line " + this.lineNumber + " in file " + this.path.getFileName() + ", skipping this block.");
				return true;
		}
	}
}
