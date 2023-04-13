/**
 * 
 * This class is the main driver for the GUI elements and the game
 * @author: Mike Eact 
 * @author: John Omalley 
 * @author: Qina Tan 
 * @start Date: 4/9/2023
 * @collaborator: none 
 * @resources: none  
 */

package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

//import clueGame.java ; 

public class GameControlPanel extends JPanel {
	private JTextField guess = new JTextField();
	private JTextField guessResult = new JTextField();
	private JTextField playerNameText = new JTextField();
	private JTextField rollText = new JTextField();

	private JButton nextButton;
	private JButton ACCButton; 

	// constructor
	public GameControlPanel() {
		setLayout(new GridLayout(2, 0));
		JPanel topPanel = createTopPanel();
		JPanel bottomPanel = createBottomPanel();
		add(topPanel);
		add(bottomPanel);
		rollText.setText(String.valueOf(ClueGame.currPlayer.getRollNum()));
		repaint();
	}

	public JButton getNextButton() {
		return nextButton;
	}
	
	public JButton getACCButton() {
		return ACCButton ; 
	}

	private JPanel whoseTurn() {
		JPanel whoseTurn = new JPanel();
		JLabel label = new JLabel("Who's Turn:");
		// TODO: refactor into whats below
		// currPlay.getName
		String playersName = ClueGame.currPlayer.getPlayerName();
		Color playersColor = ClueGame.currPlayer.getPlayerColor();
		playerNameText.setText(playersName);
		playerNameText.setBackground(playersColor);
		whoseTurn.add(label);
		whoseTurn.add(playerNameText);
		return whoseTurn;
	}

	private JPanel rollPanel() {
		JPanel roll = new JPanel();
		JLabel rollLabel = new JLabel("Roll:");
		rollText.setText(String.valueOf(ClueGame.currPlayer.getRollNum()));
		roll.add(rollLabel);
		roll.add(rollText);
		return roll;
	}

	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 4));
		JPanel whoseTurn = whoseTurn();
		topPanel.add(whoseTurn);
		JPanel roll = rollPanel();
		topPanel.add(roll);
		ButtonGroup group = new ButtonGroup();
		JButton next = new JButton("NEXT!");
		nextButton = next; // This is here so that we can keep a lot of our old code

		JButton makeAccusation = new JButton("Make Accusation");
		ACCButton = makeAccusation ;  //This is needed for code continuity
		//makeAccusation.addActionListener(new makeAccusationButtonListener());
		group.add(makeAccusation);
		group.add(next);
		topPanel.add(next);
		topPanel.add(makeAccusation);
		return topPanel;
	}

	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 2));
		JPanel leftPanel = bottomLeftPanel();
		bottomPanel.add(leftPanel);
		JPanel rightPanel = bottomRightPanel();
		bottomPanel.add(rightPanel);
		return bottomPanel;
	}

	private JPanel bottomRightPanel() {
		JPanel bottomRightPanel = new JPanel();
		bottomRightPanel.setLayout(new GridLayout(1, 0));
		// JTextField someText = new JTextField("I have no guess");
		bottomRightPanel.add(guess);
		bottomRightPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result")); // Only using this for testing
		return bottomRightPanel;
	}

	private JPanel bottomLeftPanel() {
		JPanel bottomLeftPanel = new JPanel();
		bottomLeftPanel.setLayout(new GridLayout(1, 0));
		// JTextField someText = new JTextField("You have question?");
		bottomLeftPanel.add(guessResult);
		bottomLeftPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess")); // Only using this for testing
		return bottomLeftPanel;
	}

	public void setGuess(String guess) {
		this.guess.setText(guess);
	}

	public void setGuessResult(String guessResult) {
		this.guessResult.setText(guessResult);
	}

	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initializeForTest();
		board.setPlayersTurn(board.getPlayerList().get(0));

		JFrame frame = new JFrame(); // create the frame
		GameControlPanel panel = new GameControlPanel();
		frame.add(panel, BorderLayout.CENTER);
		frame.setSize(750, 180); // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true);
		// test filling the data
		// panel.setTurn(new ComputerPlayer("Col. Mustard", 0, 0, "orange"), 5);
		panel.setGuess("I have no guess!");
		panel.setGuessResult("So you have nothing?");

	}
}
