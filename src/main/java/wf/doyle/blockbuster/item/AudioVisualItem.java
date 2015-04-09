package wf.doyle.blockbuster.item;

/**
 * @see wf.doyle.blockbuster.item.LibraryItem
 * @author Jordan Doyle
 */
public abstract class AudioVisualItem extends LibraryItem {
	/**
	 * The time this item runs for
	 */
	private int playingTime;

	/**
	 * Gets the running time for this audio visual item.
	 * 
	 * @return length of item
	 */
	public int getPlayingTime()
	{
		return this.playingTime;
	}
}
