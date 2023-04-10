package clueGame;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	Board board = Board.getInstance();
	CardsPanel cardsPanel ;
	GameControlPanel controlPanel ;
	
	
	// Default constructor
	public ClueGame() {
		
		// Create board panel and add it to frame 
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		add(board, BorderLayout.CENTER);
		
		// Create game panel and add it to frame 
		controlPanel = new GameControlPanel(); 
		add(controlPanel, BorderLayout.SOUTH);
		
		// Create cards panel and add it to frame 
		cardsPanel = new CardsPanel(); 
		add(cardsPanel, BorderLayout.EAST);
		
		// Set default frame size, title etc. 
		setSize(750, 630); //630 was changed from 930 so that it fit on my screen
		setTitle("Clue");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		
		addMouseListener(new movePlayerClick());
		
		//This is the splash panel.
		//TODO: should this open after the game board?
		JOptionPane.showMessageDialog(null, "You are " + board.getPlayer(0).getPlayerName() + ".\n Can you find the solution before the computers?", "Welcome to Clue",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	// This method will drive display updates. 
	//@SuppressWarnings("unused")
	private void updateDisplay() {
		
	}
	
	private class movePlayerClick implements MouseListener {
		humanPlayer player = (humanPlayer) board.getPlayer(0);

		@Override
		public void mouseClicked(MouseEvent e) {

			
			if (board.clickContainsTarget(e.getX(), e.getY())) {
				//TODO:include here where we move player
				player.setHasPlayerMoved(true);
			} else {
				JOptionPane.showMessageDialog(null, "Please click on a vaild tile", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		}

		@Override
		public void mousePressed(MouseEvent e) {
		}// This should be left blank
		@Override
		public void mouseReleased(MouseEvent e) {
		}// This should be left blank
		@Override
		public void mouseEntered(MouseEvent e) {
		}// This should be left blank
		@Override
		public void mouseExited(MouseEvent e) {
		} // This should be left blank
	}
	
	// Main entry point for game
	public static void main(String[] args) {
		ClueGame clueGame = new ClueGame();
		clueGame.setVisible(true);
	}
}

