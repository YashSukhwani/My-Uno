/**
 * This class defines a Card in the game of Uno.
 */
public class Card {

	// First Word is Color/Wild. Rest is Value.
	String value;
	String color;
	boolean isSpecial;
	
	/**
	 * Constructor for the Card class.
	 */
	public Card (String color, String value, boolean isSpecial) {
		this.color = color;
		this.value = value;
		this.isSpecial = isSpecial;
		
	}
}
