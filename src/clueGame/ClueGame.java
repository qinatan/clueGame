
 

package clueGame;

import java.awt.BorderLayout;

/**
 * Clue Game
 * 
 * This is the main entry point for our game.
 * 
 * @author michaeleack
 * @author johnOmalley Date: 3/7/23 Collaborators: None Sources: None
 * @author Qina Tan
 */


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	private static final long serialVersionUID = 1L;
	Board board = Board.getInstance();
	CardsPanel cardsPanel;
	GameControlPanel controlPanel;

	// Default constructor
	public ClueGame() {
		
		// Create board panel and add it to frame
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		initialTurn();
		add(board, BorderLayout.CENTER);

		// Create game panel and add it to frame
		controlPanel = new GameControlPanel();
		add(controlPanel, BorderLayout.SOUTH);

		// Create cards panel and add it to frame
		cardsPanel = new CardsPanel();
		add(cardsPanel, BorderLayout.EAST);

		// Set default frame size, title etc.
		setSize(750, 630); // 630 was changed from 930 so that it fit on my screen
		setTitle("Clue");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close

		addMouseListener(new movePlayerClick());

		controlPanel.getNextButton().addActionListener(new NextButtonListener());
		controlPanel.getACCButton().addActionListener(new ACCButtonListener());

		setVisible(true);

		// This is the splash panel.
		JOptionPane.showMessageDialog(null,
				"You are " + board.getPlayer(0).getPlayerName() + ".\n Can you find the solution before the computers?",
				"Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);

	}

	public void initialTurn() {
		// human player roll a die
		board.getPlayersTurn().setRollNum();
		int currentRow = board.getPlayersTurn().getPlayerRow();
		int currentCol = board.getPlayersTurn().getPlayerCol();
		BoardCell currentCell = board.getCell(currentRow, currentCol);
		int rolledDice = board.getPlayersTurn().getRollNum();
		board.calcTargets(currentCell, rolledDice);
		repaint();
	}

	private class NextButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			nextButtonPressedLogic();
		}
	}

	private void clearTargetCells() {
		for (BoardCell targetCell : board.getTargets()) {
			targetCell.setIsTargetCell(false);
		}
		board.getTargets().clear();
		repaint();
	}

	private class movePlayerClick implements MouseListener {

		private void mouseClickedLogic(MouseEvent e) {
			if (board.getTargets() == null || board.getTargets().size() == 0) {
				board.getPlayersTurn().setHasPlayerMoved(true);
				return;
			}
			board.resetPlayersLocations();
			int cellWidth = board.getCellWidth();
			int cellHeight = board.getCellHeight();
			int col = (int) e.getPoint().getX() / cellWidth;
			int row = ((int) e.getPoint().getY() - 30) / cellHeight;

			if (!(row > board.getNumRows() - 1 || col > board.getNumColumns() - 1)) {
				BoardCell cell = board.getCell(row, col);

				// Check that the clicked cell is a target walkway and update player's location 
				if (board.getTargets().contains(cell) && cell.getCellSymbol() == 'W') {
					board.getPlayersTurn().setPlayerLocation(row, col);
					board.getPlayersTurn().setHasPlayerMoved(true);
					clearTargetCells();
					// Check if the target cell is a room
				} else if (board.getTargets().contains(cell) && cell.isRoom()) {
					BoardCell thisRoomCenter = board.getRoom(cell).getCenterCell();
					board.getPlayersTurn().setPlayerLocation(thisRoomCenter.getRowNum(), thisRoomCenter.getColumnNum());

					// Make suggestion and handle suggestion 
					ArrayList<Card> suggestionCards = board.getPlayersTurn().makeSuggestion();
					Card disapprovalCard = board.handleSuggestion(suggestionCards.get(0), suggestionCards.get(1), suggestionCards.get(2), board.getPlayersTurn());					
					
					//add dispprovalCard to player's seenMap
					if (disapprovalCard != null)
					{
						board.getPlayersTurn().addToSeenMap(disapprovalCard.getCardType(), disapprovalCard);
					}
					
					//update guess and guess result field
					controlPanel.updateGuessText(suggestionCards, disapprovalCard, board.getPlayersTurn());
					board.getPlayersTurn().setHasPlayerMoved(true);
					clearTargetCells();
					cardsPanel.updatePanels();
				}
				
				else {
					JOptionPane.showMessageDialog(null, "Please click on a vaild tile", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// mouseClickedLogic(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}// This should be left blank

		@Override
		public void mouseReleased(MouseEvent e) {
			mouseClickedLogic(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}// This should be left blank

		@Override
		public void mouseExited(MouseEvent e) {
		} // This should be left blank
	}

	/**
	 * This is where the logic of the next button pressed should be held
	 */
	public void nextButtonPressedLogic() {
		
		board.resetPlayersLocations(); // this is necessary for drawing player offsets in rooms
		
		if (board.getPlayersTurn().getIsHasPlayerACC() || board.getPlayersTurn().getIsHasPlayerMoved()) {
			
			// switch to get next player in the list and roll a dice 
			board.nextTurn();
			BoardCell currentLocation = board.getCell(board.getPlayersTurn().getPlayerRow(), board.getPlayersTurn().getPlayerCol());
			board.getPlayersTurn().setRollNum();
			int randomRoll = board.getPlayersTurn().getRollNum();
			
			//update text field after switching to new player and rolling dice 
			controlPanel.newTurnTextUpdate(board.getPlayersTurn(), String.valueOf(randomRoll)); 

			//calculate target list based on current board cell and dice number
			board.calcTargets(currentLocation, randomRoll);

			if (board.getTargets() == null || board.getTargets().size() == 0) {
				board.getPlayersTurn().setHasPlayerMoved(true);
				return;
			}
			
			// If Human Player: repaint to highlight cells in target list
			if (board.getPlayersTurn() instanceof Humanplayer) {
				repaint();
			}
			
			
			// Else it's a CPU Player
			else {

				// If the suggestion from the previous suggestion is upheld, make accusation with cards from the previous suggestion
				if (((Computerplayer) board.getPlayersTurn()).canMakeAccusation()) {

					ArrayList<Card>accusation = board.getPlayersTurn().makeAccusation();
					Map<CardType, Card> solutions = Board.getSolution().getSolutionMap(); 
					if(Boolean.TRUE.equals(board.checkAccusation(accusation))) {
						JOptionPane.showMessageDialog(null, board.getPlayersTurn().getPlayerName() + " Won!" + "\n The solution was " + solutions.get(CardType.ROOM) + " " + solutions.get(CardType.WEAPON) + " " + solutions.get(CardType.PERSON) + "", "Loser!", JOptionPane.INFORMATION_MESSAGE);
						dispose(); 
					} else {
						
					}
	
					
				} else {  
					
					// update player location based on target cell
					BoardCell targetCell = ((Computerplayer) board.getPlayersTurn()).targetSelection(board.getTargets());
					if (targetCell != null) {
						board.getPlayersTurn().setPlayerLocation(targetCell.getRowNum(), targetCell.getColumnNum());
						
						if (Boolean.TRUE.equals(targetCell.isRoom())) {
							//CPU player make suggestion and handle suggestion 
							ArrayList<Card> suggestedCards = board.getPlayersTurn().makeSuggestion();
							Card disprovenCard = board.handleSuggestion(suggestedCards.get(0), suggestedCards.get(1), suggestedCards.get(2), board.getPlayersTurn());
							
							if (disprovenCard != null) {
								board.getPlayersTurn().addToSeenMap(disprovenCard.getCardType(), disprovenCard);
							}
							
							// CPU made a suggestion and no-one disproved it
							else {
								// First check if any of the cards they suggested are in their hand
								Boolean readyToAccuse = true;
							    for (Card card: board.getPlayersTurn().getHand()) {
							    	if (suggestedCards.contains(card)) {
							    		readyToAccuse = false; 
							    	}
							    }
							    if (Boolean.TRUE.equals(readyToAccuse)) {
							    ((Computerplayer) board.getPlayersTurn()).setUpheldSuggestion();
							    }
								
							}
							//update guess and guess result text field 
							controlPanel.updateGuessText(suggestedCards, disprovenCard, board.getPlayersTurn());
						}
					}
					
					board.getPlayersTurn().setHasPlayerMoved(true);
					clearTargetCells(); //clear target cells and repaint board 
					
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "Please finish your turn", "Players turn", JOptionPane.ERROR_MESSAGE);
		}
		
		// Checks if the current player was moved for someone elses suggestion. If they were, allow them the opportunity
		// to stay in the room to make a suggestion. 
		if (Boolean.TRUE.equals(board.getPlayersTurn().getMovedForSuggestion())) {
			board.getTargets().add(board.getPlayersTurn().getCurrCell());
			board.getPlayersTurn().setMovedForSuggestion(false);
		}
		
	}

	private class ACCButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (board.getPlayersTurn() instanceof Computerplayer) {
				JOptionPane.showMessageDialog(null, "It's not your turn yet, make an accusation at the start of your turn.", "Accusation", JOptionPane.ERROR_MESSAGE);
			}

			if (board.getPlayersTurn().getIsHasPlayerMoved()) {
				JOptionPane.showMessageDialog(null,
						"You can't make an accusation because you have moved. \n Please finish your turn.",
						"Accusation", JOptionPane.ERROR_MESSAGE);
			} else {
				accButtonPressedLogic();
			}

		}
	}

	public void accButtonPressedLogic() {
		String[] players = new String[board.getPlayerList().size()];
		for (int i = 0; i < board.getPlayerList().size(); i++) {
			players[i] = board.getPlayerList().get(i).getPlayerName();
		}

		String[] rooms = new String[board.getRoomDeck().size()];
		for (int j = 0; j < board.getRoomDeck().size(); j++) {
			rooms[j] = board.getRoomDeck().get(j).getCardName();
		}

		String[] weapons = new String[board.getWeaponDeck().size()];
		for (int k = 0; k < board.getWeaponDeck().size(); k++) {
			weapons[k] = board.getWeaponDeck().get(k).getCardName(); // There is also a to string method
		}

		JComboBox <Card> playersBox = new JComboBox(players);
		JComboBox <Card> roomsBox = new JComboBox(rooms);
		JComboBox <Card> weaponsBox = new JComboBox(weapons);

		final JComponent[] inputs = new JComponent[] { new JLabel("Room"), roomsBox, new JLabel("Player"), playersBox,
				new JLabel("Weapon"), weaponsBox };
		int result = JOptionPane.showConfirmDialog(null, inputs, "Make Accusation", JOptionPane.PLAIN_MESSAGE);

		ArrayList<Card> accCards = new ArrayList<>();

		String selectedPlayer = (String) playersBox.getSelectedItem();
		String selectedWeapon = (String) weaponsBox.getSelectedItem();
		String selectedRoom = (String) roomsBox.getSelectedItem();
		// Getting the weapon Card
		for (int w = 0; w < board.getWeaponDeck().size(); w++) {
			if (board.getWeaponDeck().get(w).getCardName().equals(selectedWeapon)) {
				accCards.add(board.getWeaponDeck().get(w));
			}
		}

		// Getting the room card
		for (int w = 0; w < board.getRoomDeck().size(); w++) {
			if (board.getRoomDeck().get(w).getCardName().equals(selectedRoom)) {
				accCards.add(board.getRoomDeck().get(w));
			}
		}

		// Getting the player card
		for (int q = 0; q < board.getPeopleDeck().size(); q++) {
			if (board.getPeopleDeck().get(q).getCardName().equals(selectedPlayer)) {
				Card playerCard = board.getPeopleDeck().get(q);
				accCards.add(playerCard); // Adds that card to the suggested list
			}
		}
		
		if (result == JOptionPane.CLOSED_OPTION) {
			board.getPlayersTurn().setHasPlayerACC(false);
			
		}
		else if (result == JOptionPane.YES_OPTION) {
			// Clears painted color cells from board
			board.getPlayersTurn().setHasPlayerACC(true);
			for (BoardCell targetCell : board.getTargets()) {
				targetCell.setIsTargetCell(false);
			}
			board.getTargets().clear(); 
			repaint();
			
			if (Boolean.TRUE.equals(board.checkAccusation(accCards))) {
				JOptionPane.showMessageDialog(null, "You Won!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} else {
				String Solution = board.getSolution().toString();
				JOptionPane.showMessageDialog(null, "Sorry you lost, the solution was: \n " + Solution, "Loser", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		}
		
	}

	// Main entry point for game
	public static void main(String[] args) {
		ClueGame ThisClueGame = new ClueGame();
	}
}