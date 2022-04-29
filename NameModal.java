import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NameModal extends JDialog {
	
	JFrame frame;
	JPanel panel;


	public NameModal(GameMaster master, GUI gui) {
		setModal(true);
		
		frame = new JFrame();
		panel = new JPanel();
		panel.setLayout(new GridLayout(10, 5));
		
		frame.add(panel, BorderLayout.CENTER);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextField text = new JTextField();
		JLabel name = new JLabel("Please Enter Your Name Here");
		JButton submit = new JButton("Add Player");
		JCheckBoxMenuItem isAI = new JCheckBoxMenuItem("AI");
		
		panel.add(name);
		panel.add(text);
		panel.add(isAI);
		panel.add(submit);

		
		frame.setTitle("Add Player");
		frame.setSize(500, 500);
		
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Added " + text.getText());
				
				Player newPlayer = new Player(text.getText(), master, isAI.getState());
				
				if (!isAI.getState()) gui.openPlayerGUI(newPlayer);
				
			}
		});
		
		frame.setVisible(true);
	
	}
	
	private void showModal() {

	    JDialog dialog = new JDialog(this, Dialog.ModalityType.APPLICATION_MODAL);

	    dialog.setBounds(500, 500, 500, 500);
	    dialog.setVisible(true);
	}
	
}
