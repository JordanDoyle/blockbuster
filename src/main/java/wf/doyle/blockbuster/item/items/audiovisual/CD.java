package wf.doyle.blockbuster.item.items.audiovisual;

import wf.doyle.blockbuster.item.AudioVisualItem;
import wf.doyle.blockbuster.util.EnumLineType;

/**
 * This object represents a CD in a library.
 * 
 * @author Jordan Doyle
 */
public final class CD extends AudioVisualItem {
	/**
	 * Number of tracks on the album.
	 */
	private int noOfTracks;

	/**
	 * The artist of the album.
	 */
	private String artist;

	/**
	 * @return number of tracks on the album
	 */
	public int getNumberOfTracks()
	{
		return this.noOfTracks;
	}

	/**
	 * @return artist of the album
	 */
	public String getArtist()
	{
		return this.artist;
	}

	@Override
	public EnumLineType getType()
	{
		return EnumLineType.CD;
	}
}
