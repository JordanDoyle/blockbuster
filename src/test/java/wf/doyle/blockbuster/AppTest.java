package wf.doyle.blockbuster;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;

import wf.doyle.blockbuster.file.FileReader;
import wf.doyle.blockbuster.item.LibraryItem;
import wf.doyle.blockbuster.item.items.audiovisual.DVD;

/**
 * Unit test for blockbuster
 */
public class AppTest {
	/**
	 * 
	 */
	@Test
	public void assertReflection() {
		LibraryItem item = new DVD();
		
		Field f = FileReader.getField("title", item.getClass());
		
		Assert.assertNotNull(f);
		
		FileReader.setField(f, item, "testCase");
		
		try {
			Assert.assertEquals(f.get(item), "testCase");
		} catch(Exception e) {
			Assert.fail("Getting value of String using reflection failed");
		}
	}
}
