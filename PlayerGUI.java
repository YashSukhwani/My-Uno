import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PlayerGUI {

	JFrame frame;
	JPanel panel;
	JButton playCard;
	JButton drawCards;
	JButton dealButton;
	JButton joinButton;
	
	Player player;
	
	public PlayerGUI(Player player) {
		
		this.player = player;
		
		frame = new JFrame();
		panel = new JPanel();
		panel.setLayout(new GridLayout(10, 5));
		
		frame.add(panel, BorderLayout.CENTER);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(player.name);
		frame.setSize(600, 600);
		
		dealButton = new JButton("Deal Cards");
		dealButton.setVisible(false);
		
		joinButton = new JButton("Join Game");

		playCard = new JButton("Play Card");
		playCard.setVisible(false);
		
		drawCards = new JButton("Draw Cards");
		drawCards.setVisible(false);
		
		panel.add(joinButton);
		panel.add(dealButton);
		panel.add(playCard);
		panel.add(drawCards);
		
		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Dealing to " + player.name);
				
				dealButton.setVisible(true);
				joinButton.setVisible(false);
			}
		});
		
		dealButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Dealing to " + player.name);
				
				if (player.getStartHand()) {
					dealButton.setVisible(false);
					playCard.setVisible(true);
					drawCards.setVisible(true);
					
					fillCardsGUI(panel, player);
				}
			}
		});
		
		
		frame.setVisible(true);
	}
	
	public void fillCardsGUI(JPanel panel, Player player) {
		for (int i = 0; i < player.cards.size(); i++) {
			Card unoCard = player.cards.get(i);
			JButton newCard;
			
			if (unoCard.color != null) {
				newCard = new JButton(" - " + unoCard.color + " " + unoCard.value + " - ");
				panel.add(newCard);
			} else {
				newCard = new JButton(" - " + unoCard.value + " - ");
				panel.add(newCard);
			}
			
			newCard.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Playing Card");
					boolean cardAccepted = player.playCard(unoCard);
					
					if (cardAccepted) {
						panel.remove(newCard);
					}
					
				}
			});
		}
	}

	
}
