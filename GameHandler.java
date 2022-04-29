import java.util.*;

/**
 * This class provides all the fields and methods required to handle the game of Uno among the different players.
 */
abstract class GameHandler {
	private ArrayList<String> playerOrder; // Reverse it when a reverse card is played.
	ArrayList<Card> drawPile;
	ArrayList<Card> discardPile;
	
	Card lastDiscarded;
	private String playerTurn;
	private int deckSize; // refers to drawPile size.
	private Random rand;
	private Scanner sc;
	private int stackCount;
	private String stackType;
	ArrayList<Player> players;
	
	private boolean isGameInitialized;
	int moveCounter;
	String changedColor; // used to set color right after a wild is played.
	
	public GameHandler() {
		this.playerOrder = new ArrayList<>();
		// this.drawPile is null until buildDeck() is called.
		
		// this.discardPile is null until initializeGame() is called.
		// Any one player must call initializeGame().
		
		deckSize = 0;
		moveCounter = 0;
		stackCount = 0;
		changedColor = null;
		lastDiscarded = null;
		
		players = new ArrayList<>();
		
		this.rand = new Random();
		sc = new Scanner(System.in);
	}
	
	/**
	 * Players use this function to enter the game. The order of turns follows the order of entry into the game.
	 */
	public void addPlayer(String name, Player player) {
		playerOrder.add(name);
		players.add(player);
	}
	
	/**
	 * This function builds the deck of Uno cards for the game.
	 */
	public void buildDeck() {
		String [] colors = {"Red", "Blue", "Green", "Yellow"};
		String [] twoKinds = {"Skip", "Reverse", "Draw Two"};
		String [] fourKinds = {"Wild", "Wild Draw Four"};
		
		ArrayList<Card> allCards = new ArrayList<>();
		
		for (String color: colors) { // Loop Level 1
			allCards.add(new Card(color, Integer.toString(0), false));
			
			for (int j = 0; j < 2; j++) { // Loop Level 2
				for (int i = 1; i < 10; i++) { // Building Numerics
					allCards.add(new Card(color, Integer.toString(i), false));
				}
				
				for (String value: twoKinds) { // Building Color Specials
					allCards.add(new Card(color, value, true));
				}
			}
		}
		
		for (int j = 0; j < 4; j++) { // Building Wilds
			for (String value: fourKinds) {
				allCards.add(new Card(null, value, true));
			}
		}

		drawPile = allCards;
		deckSize = allCards.size();
	}
	
	/**
	 * This function deals 7 Uno cards to a player when they request to be dealt. Each player can only be dealt once in a game.
	 */
	public ArrayList<Card> dealCards() { // Each player must request to be dealt cards.
		if (drawPile == null) {
			return null;
		} // Can't deal before building deck.
		
		ArrayList<Card> startHand = new ArrayList<>();
		
		for (int i = 0; i < 7; i++) {
			int cardIndex = rand.nextInt(deckSize);
			startHand.add(drawPile.remove(cardIndex));
			deckSize -= 1;
		}
		
		return startHand;
	}
	
	/**
	 * This function initializes the Uno game, with a draw pile and a discard pile and a starting card.
	 * The player turn order already exists by this point.
	 */
	public Card initializeGame() {
		this.discardPile = new ArrayList<>();
		
		String startFace = null;
		int loopControl = 0;
		Card startCard = null;
		
		while (loopControl == 0) {
			int cardIndex = rand.nextInt(deckSize);
			
			if (!drawPile.get(cardIndex).isSpecial) {
				startCard = drawPile.remove(cardIndex);
				discardPile.add(startCard);
				
				isGameInitialized = true;
				loopControl = 1;
			}
		}
		
		return startCard;
	}
	
	/**
	 * This function helps handle the case of when a Wild card is used to change the current color.
	 */
	private void handleWildColorChange(Card card) {
		String newValue = card.value;
		
		if (!newValue.subSequence(0, 4).equals("Wild")) {
			changedColor = null;
			
		} else {
			int colorChoice = -1;
			String [] colors = {"Red", "Blue", "Green", "Yellow"};
			
			while (!(colorChoice >= 0 && colorChoice <= 3)) {
				System.out.println("Please choose the new color:");
				System.out.println("Enter 0 for Red");
				System.out.println("Enter 1 for Blue");
				System.out.println("Enter 2 for Green");
				System.out.println("Enter 3 for Yellow\n");
				colorChoice = sc.nextInt();
			}
			changedColor = colors[colorChoice];
		}
	}
	
	/**
	 * This function handles the specific execution to be performed when a card is played.
	 */
	private void handleCard(Card card, String playerTurn) {
		if (card.value.equals("Skip")) {
			moveCounter += 1;
		} else if (card.value.equals("Reverse")) {
			
			ArrayList<String> newOrder = new ArrayList<>();
			while (!playerOrder.get(0).equals(playerTurn)) {
				playerOrder.add(playerOrder.remove(0));
			}
			for (int i = playerOrder.size() - 1; i >= 0; i--) {
				newOrder.add(playerOrder.get(i));
			}
			playerOrder = newOrder;
			moveCounter = -1;
			
		}
	}
	
	/**
	 * This function ends the game when a player has exhausted all their cards.
	 */
	private void endGame(String playerName) {
		System.out.println(playerName + " wins!");
		playerOrder = null;
	}
	
	/**
	 * This function returns whether the move was accepted or not (to the player that initiated it).
	 */
	public boolean handleMove(Card card, String playerName, int numCards) { 
		if (playerOrder == null) {
			System.out.println("Invalid! The game has ended.");
			return false;
		}
		
		playerTurn = playerOrder.get(moveCounter % playerOrder.size());
		
		if (!playerTurn.equals(playerName)) {
			System.out.println("It's not your turn! Wait for your turn. Penalty +1"); // Incorporated Penalty Law
			return false;
		}
		
		if (checkMove(card, false)) {
			handleWildColorChange(card);
			handleCard(card, playerTurn);
			
			moveCounter += 1;
			discardPile.add(0, card);
			lastDiscarded = discardPile.get(0);
			
			if (numCards == 0) {
				endGame(playerName);
			}
			
			return true;
			
		} else {
			System.out.println("Invalid Move! Penalty +1"); // Incorporated Penalty Law
			return false;
		}
	}
	
	/**
	 * This function re-shuffles the discarded cards to add them to the drawing pile so that the game can continue.
	 */
	private void refillDrawPile() {		
		for (int i = 0; i < discardPile.size(); i += 1) {
			drawPile.add(discardPile.remove(0));
			deckSize += 1;
		}
		
		Collections.shuffle(drawPile);
	}

	/**
	 * This function executes the picking up of a single card by a player.
	 */
	public Card pickupCard() {
		int cardIndex = rand.nextInt(deckSize);
		deckSize -= 1;
		
		if (deckSize < 1) {
			refillDrawPile();
		}
		
		return drawPile.remove(cardIndex);
	}
	
	/**
	 * This function validates whether a move played by a player is correct and should indeed be processed.
	 */
	public boolean checkMove(Card card, boolean aiTesting) {
		String currValue = discardPile.get(0).value;
		String currColor = discardPile.get(0).color;
		
		String newValue = card.value;
		String newColor = card.color;
		
		if (newValue.equals(currValue)) {
			if (stackType == null) {
				if (newValue.equals("Draw Two")) {
					stackType = "Draw Two";
					stackCount += 1;
				} else if (newValue.equals("Wild Draw Four")) {
					stackType = "Wild Draw Four";
					stackCount += 1;
				}
				
			} else if (stackType.equals("Wild Draw Four") && newValue.equals("Wild Draw Four")) {
				stackCount += 1;
			} else if (stackType.equals("Draw Two") && newValue.equals("Draw Two")) {
				stackCount += 1;
			}
				
			return true;
		} else {
			stackCount = 0;
			stackType = null;
		}
		
		if (newValue.subSequence(0, 4).equals("Wild")) {
			return true;
		}
		
		if (changedColor != null && changedColor.equals(newColor)) {
			return true;
		}
		
		if (changedColor == null && newColor.equals(currColor)) {
			return true;
		}
		
		
		return false;
	}
}
