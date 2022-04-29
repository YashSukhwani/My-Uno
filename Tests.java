import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * This class runs JUnit tests on the rest of the project.
 */
class Tests {

	/**
	 * Test whether the card deck was built properly.
	 */
	@Test
	void builtDeckTest() {
		GameMaster master = new GameMaster();
		master.buildDeck();
		if (master.drawPile.size() != 108) {
			fail("!= 108 in deck.");
		}
	}
	
	/**
	 * Checks whether the cards were dealt properly.
	 */
	@Test
	void dealCardsTest() {
		GameMaster master = new GameMaster();
		master.buildDeck();
		if (master.dealCards().size() != 7) {
			fail("!= 7 in starting hand.");
		}
	}
	
	/**
	 * Checks whether the cards were shuffled and re-stocked after the draw pile ran out. 
	 */
	@Test
	void checkShuffle() {
		GameMaster master = new GameMaster();
		Player player = new Player("Yash", master, false);
		
		
		player.joinGame();
		master.buildDeck();
		player.getStartHand();
		
		for (int i = 0; i < 120; i++) {
			player.pickupCard();
		}
	}

}
