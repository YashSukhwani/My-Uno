/**
 * This class allows the instantiation of a Game Master using all the information and functionality from the Game Handler.
 */
public class GameMaster extends GameHandler {
	
	public static void main(String [] args) {
		GameMaster master = new GameMaster();
		master.buildDeck();
		master.initializeGame();
	}

}
