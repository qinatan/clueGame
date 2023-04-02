package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.junit.Assert.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import clueGame.Room;
import clueGame.Solution;

public class AccusationSuggestionTests {
	private static Board board;

	@BeforeAll
	public static void setUp() {

		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initializeForTest();
	}

	@Test
	public void testSolution() {
		Solution solution = Board.getSolution();

		Card correctRoom = solution.getRoom();
		Card correctPerson = solution.getPerson();
		Card correctWeapon = solution.getWeapon();

		Card wrongRoom = board.getPlayerList().get(0).getHand().get(0);
		Card wrongPerson = board.getPlayerList().get(0).getHand().get(0);
		Card wrongWeapon = board.getPlayerList().get(0).getHand().get(0);

		// Correct solution
		Assert.assertTrue(board.checkAccusation(correctRoom, correctPerson, correctWeapon));
		Assert.assertFalse(board.checkAccusation(correctRoom, wrongPerson, correctWeapon)); // Solution with wrong person
		Assert.assertFalse(board.checkAccusation(correctRoom, correctPerson, wrongWeapon)); // solution with wrong weapon
		Assert.assertFalse(board.checkAccusation(wrongRoom, correctPerson, correctWeapon)); // solution with wrong weapon
	}

	// Test player Disproves suggestion
	@Test
	public void disprovesSuggestion() {
		// TODO: make sure that these are using the cards we think that they are using
		// TODO: add all of our tests into documentation on the google drive
		// TODO: try to refactor to remove the magic ints in the tests
		Solution solution = Board.getSolution();

		Card correctRoom = solution.getRoom(); // Expected: PlantRoom
		Card correctPerson = solution.getPerson(); // Expected: Dog Bone
		Card correctWeapon = solution.getWeapon(); // Expected: Chihiro Ogino
		Card suggestedRoom = board.getPlayerList().get(0).getHand().get(0); // Expected: Exercise Room
		Card suggestedPerson = board.getPlayerList().get(1).getHand().get(2); // Expected: Yubaba
		Card suggestedWeapon = board.getPlayerList().get(2).getHand().get(1); // Expected: Broken DVD

		// Section for testing that we can get one players matching card
		// Expected: ExersciseRoom
		Assert.assertEquals(suggestedRoom,
				board.getPlayerList().get(0).disproveSuggestion(suggestedRoom, correctPerson, correctWeapon));

		// Expected: Yubaba
		Assert.assertEquals(suggestedPerson,
				board.getPlayerList().get(1).disproveSuggestion(correctRoom, suggestedPerson, correctWeapon));

		// Expected: Broken DVD
		Assert.assertEquals(suggestedWeapon,
				board.getPlayerList().get(2).disproveSuggestion(correctRoom, correctPerson, suggestedWeapon));

		// Section for testing if we can get both of players matching card
		suggestedRoom = board.getPlayerList().get(0).getHand().get(0); // Expected: Exercise Room
		suggestedPerson = board.getPlayerList().get(0).getHand().get(2); //Expected: Extension cord
		
		boolean foundFirstCard = false;
		boolean foundSecondCard = false;

		int numLoops = 0;

		while (foundFirstCard == false || foundSecondCard == false) {
			Card foundCard = board.getPlayerList().get(0).disproveSuggestion(suggestedRoom, suggestedPerson,
					correctWeapon);
			if (foundCard == suggestedRoom) {
				foundFirstCard = true;
			}

			if (foundCard == suggestedPerson) {
				foundSecondCard = true;
			}
			numLoops++;
			if (numLoops > 10) {
				break;
			}
		}

		Assert.assertTrue(foundFirstCard);
		Assert.assertTrue(foundSecondCard);

		// Section for testing if a player has no matching cards

		Assert.assertEquals(null,
				board.getPlayerList().get(0).disproveSuggestion(correctRoom, correctPerson, correctWeapon));

	}

//Test handleSuggestion() for both CPU and human
	@Test
	public void handleSuggestionTest() {
		Solution solution = Board.getSolution();

		Card solutionRoom = solution.getRoom();
		Card solutionPerson = solution.getPerson();
		Card solutionWeapon = solution.getWeapon();
		
		Player testingPlayer1 = board.getPlayer(0);  // Chihiro Ogino == human player
		Player testingPlayer2 = board.getPlayer(1);
		Player testingPlayer3 = board.getPlayer(2); 
		ArrayList<Card> testingHand1 = testingPlayer1.getHand(); 
		ArrayList<Card> testingHand2 = testingPlayer2.getHand(); 
		 
		
		for (int i = 0; i < testingHand1.size(); i++)
		{
			
			System.out.println(testingHand1.get(i)); 
		}

		Card suggestedRoom1 = testingHand1.get(0); //Exercise Room 
		Card suggestedPerson1 = null; //player1 has another room: Garage 
		Card suggestedWeapon1 = testingHand1.get(2); //Extension Cord 
		
		for (int i = 0; i < testingHand2.size(); i++)
		{
			System.out.println(testingHand2.get(i)); 
		}
		Card suggestedRoom2 = testingHand2.get(0); // Dog House 
		Card suggestedPerson2 = testingHand2.get(1); //Yubaba 
		Card suggestedWeapon2 = null; // player2 has another room: Basement
		
		

		// Tests that no one can disprove the solution
		Card disprovedCard = board.handleSuggestion(solutionRoom, solutionPerson, solutionWeapon, testingPlayer1); 
		Assert.assertEquals(null, disprovedCard); 
		
		//Suggestion only suggesting player can disprove returns null
		disprovedCard = board.handleSuggestion(solutionRoom, solutionPerson, suggestedWeapon1, testingPlayer1);
		Assert.assertEquals(null, disprovedCard); 
		
		//Query in order. No other players can show other disproval cards once a disproval card is shown from one player 
		disprovedCard = board.handleSuggestion(suggestedRoom1, suggestedPerson2, solutionWeapon, testingPlayer3);
		Assert.assertEquals(suggestedRoom1, disprovedCard); 
				
	}

	@SuppressWarnings("null")
	//@Test
	public void CPUSuggestion() {
		// Room matches current location
		Player YubabaCpuPlayer = board.getPlayerList().get(1); 
		// Set players location to plant room 
		YubabaCpuPlayer.setPlayerLocation(24, 10); // row 24, col 10 = plant room center
		Card currRoom = new Card(CardType.ROOM, "Plant Room");
		YubabaCpuPlayer.currRoom = currRoom;  // sets currRoom to plant room
		YubabaCpuPlayer.addToSeenMap(CardType.WEAPON, new Card(CardType.WEAPON, "Dog Bone"));
		YubabaCpuPlayer.addToSeenMap(CardType.WEAPON, new Card(CardType.WEAPON, "Broken DVD"));
		YubabaCpuPlayer.addToSeenMap(CardType.WEAPON, new Card(CardType.WEAPON, "Wrench"));
		YubabaCpuPlayer.addToSeenMap(CardType.WEAPON, new Card(CardType.WEAPON, "Venus Fly Trap"));
		YubabaCpuPlayer.addToSeenMap(CardType.WEAPON, new Card(CardType.WEAPON, "Dumbbell"));
		YubabaCpuPlayer.addToSeenMap(CardType.PERSON, new Card(CardType.PERSON, "Yubaba"));
		YubabaCpuPlayer.addToSeenMap(CardType.PERSON, new Card(CardType.PERSON, "Zeniba"));
		YubabaCpuPlayer.addToSeenMap(CardType.PERSON, new Card(CardType.PERSON, "No-Face"));
		YubabaCpuPlayer.addToSeenMap(CardType.PERSON, new Card(CardType.PERSON, "Boh"));
		YubabaCpuPlayer.addToSeenMap(CardType.PERSON, new Card(CardType.PERSON, "River Spirit"));
		
		// Only Player Yubaba, and Weapon Extension Cord not in seen list
		// Expect Yubaba to guess: Person: Yubaba, Weapon: Extension Cord, Room: Plant Room
		ArrayList<Card> suggestion = YubabaCpuPlayer.makeSuggestion();
		Assert.assertTrue(suggestion.size() == 3);
		
		for (Card card: suggestion) {
			System.out.println(card.toString());
		}
		Assert.assertTrue(suggestion.contains(new Card(CardType.PERSON, "Yubaba")));
		Assert.assertTrue(suggestion.contains(new Card(CardType.WEAPON, "Extension Cord")));
		Assert.assertTrue(suggestion.contains(new Card(CardType.ROOM, "Plant Room")));
		
		
		// TODO: IDK how to do this if multiple weapons not seen, one of them is randomly selected
		// TODO: IDK how to do this if multiple persons not seen, one of them is randomly selected
	}

	//@Test
	public void CPUSelectTarget() {
		// targetList is a list of rooms not in seen List 
		// TODO: If no rooms in seenList, select randomly
		// TODO: If only one room not in seenList, select it 
		// TODO: If room in list that has been seen, each target (including room) selected randomly
		
		Assert.fail();
	}

}
