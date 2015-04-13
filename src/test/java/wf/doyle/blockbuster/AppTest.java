package wf.doyle.blockbuster;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import wf.doyle.blockbuster.file.FileReader;
import wf.doyle.blockbuster.item.LibraryItem;
import wf.doyle.blockbuster.item.items.audiovisual.CD;
import wf.doyle.blockbuster.item.items.audiovisual.DVD;

/**
 * Unit test for blockbuster
 */
public class AppTest {
	/**
	 * Test reflection methods
	 */
	@Test
	public void assertReflection()
	{
		LibraryItem item = new DVD();

		Field f = FileReader.getField("title", item.getClass());

		Assert.assertNotNull(f);

		FileReader.setField(f, item, "testCase");

		try
		{
			Assert.assertEquals(f.get(item), "testCase");
		}
		catch(Exception e)
		{
			Assert.fail("Getting value of String using reflection failed");
		}

		FileReader.setField(FileReader.getField("itemCode", item.getClass()), item, "abc123");
		Assert.assertEquals(item.getItemCode(), "abc123");
	}

	/**
	 * Test if the parser is correctly parsing things
	 */
	@Test
	public void assertParsing()
	{
		FileReader f = new FileReader(null);

		try
		{
			Method m = FileReader.class.getDeclaredMethod("parseLine", String.class);
			m.setAccessible(true);

			m.invoke(f, "[cD data]");
			m.invoke(f, "// test line");
			m.invoke(f, "// data is artist, noOfTracks, playingTime, title, itemCode, cost, timesBorrowed, onLoan");
			m.invoke(f, "Robert Plant and Alison Krauss,13,72, Raising Sand, LM003750,898,89,tRUE");

			CD cd = (CD) App.items.get(0);

			Assert.assertEquals(cd.getArtist(), "Robert Plant and Alison Krauss");
			Assert.assertEquals(cd.getNumberOfTracks(), 13);
			Assert.assertEquals(cd.getName(), "Raising Sand");
			Assert.assertTrue(cd.getLoan());

			// switch things about to test the "data is" parser
			m.invoke(f, "// data is noOfTracks, artist, playingTime, itemCode, title, cost, onLoan, timesBorrowed");
			m.invoke(f, "18,Robert Plant and Alison Krauss, 72, LM003750, Raising Sand,898,fAlSe,89");

			CD cd2 = (CD) App.items.get(1);

			Assert.assertEquals(cd2.getNumberOfTracks(), 18);
			Assert.assertEquals(cd2.getName(), "Raising Sand");
			Assert.assertEquals(cd2.getItemCode(), "LM003750");
			Assert.assertFalse(cd2.getLoan());
		}
		catch(Exception e)
		{
			Assert.fail("Couldn't find parseLine method");
		}
	}
}
