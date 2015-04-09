package wf.doyle.blockbuster.item.items.audiovisual;

import wf.doyle.blockbuster.item.AudioVisualItem;
import wf.doyle.blockbuster.util.EnumLineType;

/**
 * This object represents a DVD in a library.
 * 
 * @author Jordan Doyle
 */
public final class DVD extends AudioVisualItem {
	/**
	 * Director of the DVD
	 */
	private String director;

	/**
	 * @return gets the director of the DVD
	 */
	public String getDirector()
	{
		return this.director;
	}

	@Override
	public EnumLineType getType()
	{
		return EnumLineType.DVD;
	}
}
