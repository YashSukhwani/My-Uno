import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI {
	
	JFrame frame;
	JPanel panel;
	JLabel gameState;
	JLabel topCard;
	JButton startButton;
	JButton addPlayer;
	
	GameMaster master;
	
	
	public GUI() {
		master = new GameMaster();
		
		frame = new JFrame();
		panel = new JPanel();
		panel.setLayout(new GridLayout(10, 5));
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Uno");
		frame.setSize(600, 600);
		
		gameState = new JLabel("Game State: Off");
		startButton = new JButton("Start Game");
		
		addPlayer = new JButton("Add Player");
		topCard = new JLabel();
		topCard.setVisible(false);
		
		panel.add(gameState);
		panel.add(addPlayer);	
		panel.add(startButton);
		panel.add(topCard);
		
		frame.setVisible(true);
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Game Started!");
				gameState.setText("Game State: On");
				startButton.setVisible(false);
				
				master.buildDeck();
				Card startCard = master.initializeGame();
				topCard.setText(startCard.getCardName());
				topCard.setVisible(true);
			}
		});
		
		addPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Adding Player");
				openNameModal();
				
			}
		});
	}
	
	public void openNameModal() {
		new NameModal(master, this);
	}
	
	public void openPlayerGUI (Player player) {
		new PlayerGUI(player);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GUI gui = new GUI();
	}

}
