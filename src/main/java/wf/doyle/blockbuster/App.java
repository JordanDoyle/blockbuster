package wf.doyle.blockbuster;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import wf.doyle.blockbuster.gui.GUI;
import wf.doyle.blockbuster.item.LibraryItem;
import wf.doyle.blockbuster.util.Library;
import wf.doyle.blockbuster.util.User;

/**
 * Entry class of BlockBuster
 * 
 * @author Jordan Doyle
 */
public class App {
	/**
	 * Log4j logger instance
	 */
	public static final Logger LOGGER = LogManager.getLogger("BlockBuster");

	/**
	 * List of items available in the library
	 */
	public static List<LibraryItem> items = new ArrayList<LibraryItem>();

	/**
	 * Local user
	 */
	public static final User USER = new User();
	
	/**
	 * Entry point of BlockBuster
	 * 
	 * @param args
	 *            arguments passed to program
	 */
	public static void main(String[] args)
	{
		
		LOGGER.info("BlockBuster starting");

		Library l = null;
		try
		{
			l = new Library();
		}
		catch(URISyntaxException e1)
		{
			e1.printStackTrace();
		}

		if(args.length > 0 && args[0] == "-debug") {
			l.printAllDetails();
			l.printAllDetailsSortedOnTitle();
			return;
		}
		
		GUI n = new GUI();
		n.setVisible(true);
		
		// write to file:
		// FileWriter a = new
		// FileWriter(Paths.get(App.class.getResource("/serialized-data.txt").toURI())).serializeItems();
	}
}
