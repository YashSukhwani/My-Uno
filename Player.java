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
	
	/**
	 * Constructor for the Player class.
	 */
	public Player(String name, GameHandler master) {
		this.name = name;
		this.cards = null;
		this.master = master;
		sc = new Scanner(System.in);
	}
	
	/**
	 * This function is used to help a player join a game. It must be run before all other player functions.
	 */
	public void joinGame() {
		
		System.out.println("Please enter your name to join the game.\n");
		String name = sc.nextLine();
		
		master.addPlayer(name);
	}
	
	/**
	 * This function requests the Game Master to deal the player a starting hand.
	 */
	public void getStartHand() {
		if (!cardsDealt) {
			cards = master.dealCards();
			cardsDealt = true;
		} else {
			return;
		}
		
		if (cards == null) {
			System.out.println("Error! Must build deck before + dealing!");
		}
	}
	
	/**
	 * This function enables the user to decide and choose which card from their hand to play.
	 */
	private void decideMove() {
		do {
			System.out.println("What move would you like to play?");
			Card topCard = master.lastDiscarded;
			System.out.println("TopCard has color & value: " + topCard.color + ", " + topCard.value + " respectively.");
			System.out.println("Your cards are:");
			for (int i = 0; i < cards.size(); i++) {
				System.out.println("Position in hand: " + i + ", Color: " + cards.get(i).color + ", Value: " + cards.get(i).value);
			}
			System.out.println("Enter the Position in hand of the card you wish to play. Else, enter -1 to pick up a card.");
			
			int answer = sc.nextInt();
			if (answer == -1) {
				this.pickupCard();
				break;
			} else if (answer >= 0 && answer < cards.size()) {
				playCard(cards.get(answer));
				break;
			} else {
				System.out.println("Enter a valid decision please!");
			}
		} while (true);
	}
	
	/**
	 * This function executes the actual playing of a card by the player.
	 */
	private void playCard(Card card) {
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
