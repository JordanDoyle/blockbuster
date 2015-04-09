package wf.doyle.blockbuster.util;

/**
 * Enum containing all the different types of lines.
 * 
 * @author Jordan Doyle
 */
public enum EnumLineType {
	/**
	 * The current lines contain information about CDs.
	 */
	CD,
	/**
	 * The current lines contain information about DVDs.
	 */
	DVD,
	/**
	 * The current lines contain information about books.
	 */
	BOOK,
	/**
	 * The current lines contain information about periodicals.
	 */
	PERIODICAL,
	/**
	 * This line contains an unknown data type.
	 */
	UNKNOWN,
	/**
	 * It is not yet determined what these lines contain
	 */
	NONE;
}
