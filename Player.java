import java.util.*;

/**
 * This class provides all the functionality required for a player to play the game of Uno with the GameMaster.
 */
public class Player {
	String name;
	ArrayList<Card> cards;
	GameHandler master;
	Scanner sc;
	boolean cardsDealt;
	boolean isAI;
	
	/**
	 * Constructor for the Player class.
	 */
	public Player(String name, GameHandler master, boolean isAI) {
		this.name = name;
		this.cards = null;
		this.master = master;
		this.isAI = isAI;
		
		sc = new Scanner(System.in);
	}
	
	/**
	 * This function is used to help a player join a game. It must be run before all other player functions.
	 */
	public void joinGame() {
		
		System.out.println("Please enter your name to join the game.\n");
		String name = sc.nextLine();
		
		master.addPlayer(name, this);
	}
	
	/**
	 * This function requests the Game Master to deal the player a starting hand.
	 */
	public boolean getStartHand() {
		if (!cardsDealt) {
			cards = master.dealCards();
			cardsDealt = true;
		} else {
			return false;
		}
		
		if (cards == null) {
			System.out.println("Error! Must build deck before + dealing!");
			return false;
		}
		
		return true;
	}
	
	/**
	 * This function implements the baseline AI logic.
	 */
	public void baselineAI() {
		for (Card card: cards) {
			if (master.checkMove(card, true)) {
				playCard(card);
				
			}
		}
		
		pickupCard();
		
	}
	
	
	/**
	 * Chooisng color when playing a wild card as AI.
	 * @return
	 */
	public String wildChoosing() {
		int red = 0;
		int blue = 0;
		int yellow = 0;
		int green = 0;
		String argMax = null;
		
		for (Card card: cards) {
			if (card.color == "Red") {
				red += 1;
			} else if (card.color == "Blue") {
				blue += 1;
			} else if (card.color == "Green") {
				green += 1;
			} else if (card.color == "Yellow" ) {
				yellow += 1;
			}
		}
		
		return argMax;
	}

	
	/**
	 * This function executes the actual playing of a card by the player.
	 */
	public boolean playCard(Card card) {
		boolean acceptFlag = master.handleMove(card, this.name, cards.size());
		
		if (acceptFlag) {
			this.cards.remove(card);
			
			if (this.cards.size() == 1) { // Uno Card Face Up Rule
				Card unoCard = cards.get(0);
				if (unoCard.color != null) {
					System.err.println(this.name + "has a " + unoCard.color + " " + unoCard.value + ".");
				} else {
					System.err.println(this.name + "has a " + unoCard.value + ".");
				}
			}
			return true;
		} else {
			return false;
		}	
		
	}
	
	/**
	 * This function executes the picking up of a single card from the drawing deck, along with the decision process of whether to play it.
	 */
	public void pickupCard() {
		Card newCard = master.pickupCard();
		Card topCard = master.lastDiscarded;
		
		if (newCard.color != null && topCard.color != null && newCard.color.equals(topCard.color)) {
			this.playCard(newCard);
		} else if (newCard.value.substring(0, 4).equals("Wild")) {
			this.playCard(newCard);
		} else if (newCard.value.equals(topCard.value)) {
			this.playCard(newCard);
		} else if (topCard.color == null && newCard.color.equals(master.changedColor)) {
			this.playCard(newCard);
		} else {
			cards.add(newCard);
			master.moveCounter += 1;
		}
			
	}
	
	/**
	 * This function executes picking up four cards (receiving end of a Wild Draw Four).
	 */
	private void drawFour() {
		for (int i = 0; i < 4; i++) {
			cards.add(master.pickupCard());
		}
		master.moveCounter += 1;
	}
	
	/**
	 * This function executes picking up two cards (receiving end of a Draw Two).
	 */
	private void drawTwo() {
		for (int i = 0; i < 2; i++) {
			cards.add(master.pickupCard());
		}
		master.moveCounter += 1;
	}
	
	
	
}
