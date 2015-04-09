package wf.doyle.blockbuster.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.CaseFormat;

import wf.doyle.blockbuster.App;
import wf.doyle.blockbuster.item.LibraryItem;
import wf.doyle.blockbuster.util.EnumLineType;

/**
 * Serializes all the items and writes them back into a data file.
 * 
 * @author Jordan Doyle
 */
public class FileWriter {
	/**
	 * Provides a versioning for our data files which allows backwards
	 * compatability with older data files.
	 */
	protected static final long serialVersionUID = 0L;

	/**
	 * Path to the file to write to
	 */
	private Path path;

	/**
	 * Lines to write to the data file
	 */
	private List<String> lines = new ArrayList<String>();

	/**
	 * Final string to write to file.
	 */
	public String joinedString;

	/**
	 * Instantiates a new FileWriter instance
	 * 
	 * @param path
	 *            path to file to write to
	 */
	public FileWriter(Path path)
	{
		this.lines.add("// SERIAL " + serialVersionUID);

		this.path = path;
	}

	/**
	 * Serializes all items from the main array (
	 * {@link wf.doyle.blockbuster.App#items}) and stores each line in the
	 * {@link #lines} array.
	 * 
	 * @return serialized classes
	 */
	public FileWriter serializeItems()
	{
		List<LibraryItem> items = App.items;

		// sort the items by class type, doesn't mess up our storage file when
		// we save.
		Collections.sort(items, new Comparator<LibraryItem>()
		{
			@Override
			public int compare(LibraryItem arg0, LibraryItem arg1)
			{
				return arg0.getType().toString().compareTo(arg1.getType().toString());
			}
		});

		EnumLineType header = null;

		for(LibraryItem item : items)
		{
			if(header != item.getType())
			{
				this.lines.add("");
				// add the declaration line in camel case
				this.lines.add("[" + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, item.getType().toString()) + " data]");
				// add back the "data is" line, helps us a lot with reflection
				this.lines.add("// data is " + StringUtils.join(FileReader.containing.get(item.getClass().getName()), ", ").trim());
				header = item.getType();
			}

			this.lines.add(item.toString());
		}

		this.joinedString = StringUtils.join(this.lines, "\r\n");

		return this;
	}

	/**
	 * Write and save to the file
	 */
	public void save()
	{
		File file = this.path.toFile();
		try
		{
			file.createNewFile();
		}
		catch(IOException e)
		{
			App.LOGGER.error("Could not create new file", e);
		}

		try(PrintWriter writer = new PrintWriter(file))
		{
			writer.write(this.joinedString);
		}
		catch(Exception e)
		{
			App.LOGGER.error("Could not write to file", e);
		}
	}
}
