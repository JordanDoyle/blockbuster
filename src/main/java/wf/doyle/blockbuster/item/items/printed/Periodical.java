package wf.doyle.blockbuster.item.items.printed;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import wf.doyle.blockbuster.App;
import wf.doyle.blockbuster.item.PrintedItem;
import wf.doyle.blockbuster.util.EnumLineType;

/**
 * This object represents a periodic publication in a library.
 * 
 * @author Jordan Doyle
 */
public class Periodical extends PrintedItem {
	/**
	 * Time of publication
	 */
	private String publicationDate;

	/**
	 * @return time of publication
	 */
	public Date getPublicationDate()
	{
		try
		{
			return new SimpleDateFormat("dd-MM-yy").parse(this.publicationDate);
			// return a Date object from our publicationDate string. Dear
			// Oracle, please implement PHP's strtotime somehow
		}
		catch(ParseException e)
		{
			App.LOGGER.error("Error parsing publication date", e);
			return new Date();
		}
	}

	@Override
	public EnumLineType getType()
	{
		return EnumLineType.PERIODICAL;
	}
}
