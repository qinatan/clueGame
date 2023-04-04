package clueGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class computerPlayer extends Player {

	public computerPlayer(String playerName, String playerColor, String row, String col) {
		super(playerName, playerColor, row, col);
	}

	@Override
	public void updateHand(Card card) {
		hand.add(card);
	}

	public ArrayList<Card> makeSuggestion() {

		ArrayList<Card> finalSuggestion = new ArrayList<Card>();
		HashMap<CardType, ArrayList<Card>> possibleSuggestions = new HashMap<CardType, ArrayList<Card>>();

		// pick this players current room
		possibleSuggestions.computeIfAbsent(CardType.ROOM, k -> new ArrayList<>()).add(this.currRoom);
		// adds a the room were into the possible suggestions


		// TODO: Code Smell
		Board board = Board.getInstance();

		// TODO: create a get weapon deck method and set weapon deck to private
		for (Card weaponCard : board.weaponDeck) { // Loop through all the weapons we have
			ArrayList<Card> seenWeapons = seenMap.get(CardType.WEAPON);

			if (!seenWeapons.contains(weaponCard) && !hand.contains(weaponCard)) {
				possibleSuggestions.computeIfAbsent(CardType.WEAPON, k -> new ArrayList<>()).add(weaponCard);

			}
	
		}

		
		for (Card personCard : board.peopleDeck) { // Loop through all the weapons we have
			ArrayList<Card> seenPeople = seenMap.get(CardType.PERSON);

			if (!seenPeople.contains(personCard) && !hand.contains(personCard)) {
				possibleSuggestions.computeIfAbsent(CardType.PERSON, k -> new ArrayList<>()).add(personCard);

			}
	
		}


		// Check if more than 3 total elements, if yes, pick randomly amongst items in
		// the arrayList
		// populate finalSuggestion and return it
		Random rand = new Random();
		for (ArrayList<Card> cardList : possibleSuggestions.values()) {
			if (cardList.size() > 1) {
				Card randomCard = cardList.get(rand.nextInt(cardList.size()));
				finalSuggestion.add(randomCard);

			}
			// Else if only one possible card, add the card to finalSuggestion
			else if (cardList.size() == 1) {
				finalSuggestion.add(cardList.get(0));
			}
		}

		return finalSuggestion;	
	}
	
	//AI player select one target from targetList to move toward 
	//return the first room found in targetList if room is not in seenMap, or return random target location if no room exit or room is already in seenMap 
	public BoardCell targetSelection(Set<BoardCell> targetList)
	{
		Board board = Board.getInstance(); // TODO: Code smell
		ArrayList<BoardCell> targets = new ArrayList<BoardCell>(targetList); 
		//System.out.println("targets = ");
		//System.out.println(targets.toString());
		ArrayList<String> cardNames = new ArrayList<String>(); 
		BoardCell targetLocation = null; 
		ArrayList<Card> roomCards = seenMap.get(CardType.ROOM);
		if (roomCards !=null)
		{
			for (int j = 0; j < roomCards.size(); j++)
			{
				cardNames.add(roomCards.get(j).getCardName()); 
			}
		}
		//loop through every target location to check if a room exist
		/*
		for (int i = 0; i < targets.size(); i++)
		{
			targetLocation = targets.get(i); 
			if (targetLocation.isRoom())
			{
				//get the cellSymbol in order to get the matching room from roomMap 
				Character cellSymbol = targetLocation.getCellSymbol(); 
				String roomName = board.getRoomMap().get(cellSymbol).getName(); 
				
				//check if the room is in the seenMap 
				//No Room Card was seen: we can return this room
				if (!cardNames.contains(roomName))
				{
					return targetLocation; 

				}

			}
		}*/
		for (BoardCell target: board.getTargetList()) {
			if (target.isRoom()) {
				//get the cellSymbol in order to get the matching room from roomMap 
				Character cellSymbol = target.getCellSymbol(); 
				String roomName = board.getRoomMap().get(cellSymbol).getName(); 
				
				//check if the room is in the seenMap 
				//No Room Card was seen: we can return this room
				if (!cardNames.contains(roomName))
				{
					return target; 

				}
			}
		}
		//System.out.println("Generating random result");
		// return random target location is no room in the target list
		Random randomTarget = new Random();
		int randomNumber = randomTarget.nextInt(board.getTargetList().size());
		return targets.get(randomNumber); 
		

	}
}
