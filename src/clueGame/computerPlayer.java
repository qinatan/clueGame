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
		Boolean handContains = false;
		Boolean seenWeaponsContains = false;
		Boolean seenPersonContains = false;

		// Debug Printing all possible suggestions
		//System.out.println("Printing seenMap");
		//seenMap.forEach((key, value) -> System.out.println(key + ":" + value));

//		System.out.println("Printing hand");
//		for (int i = 0; i < hand.size(); i++) {
//			System.out.println(hand.get(i).toString());
//		}

		// == vs .equals()

		ArrayList<Card> finalSuggestion = new ArrayList<Card>();
		HashMap<CardType, ArrayList<Card>> possibleSuggestions = new HashMap<CardType, ArrayList<Card>>();

		// pick this players current room
		possibleSuggestions.computeIfAbsent(CardType.ROOM, k -> new ArrayList<>()).add(this.currRoom);
		// adds a the room were into the possible suggestions

		// suggestion.put(CardType.ROOM, this.currRoom);

		// TODO: Code Smell
		Board board = Board.getInstance();

		// pick weapon that has not been seen and is not in their hand
		// TODO: create a get weapon deck method and set weapon deck to private
		for (Card weaponCard : board.weaponDeck) { // Loop through all the weapons we have
			ArrayList<Card> seenWeapons = seenMap.get(CardType.WEAPON);

			if (!seenWeapons.contains(weaponCard) && !hand.contains(weaponCard)) {
				possibleSuggestions.computeIfAbsent(CardType.WEAPON, k -> new ArrayList<>()).add(weaponCard);
//				System.out.println("here " + weaponCard);
//				// System.out.println("test: " + seenWeapons + " " +
//				// seenMap.get(CardType.WEAPON));
//				System.out.println("test: " + seenWeapons);
			}
	
		}

		
		for (Card personCard : board.peopleDeck) { // Loop through all the weapons we have
			ArrayList<Card> seenPeople = seenMap.get(CardType.PERSON);

			if (!seenPeople.contains(personCard) && !hand.contains(personCard)) {
				possibleSuggestions.computeIfAbsent(CardType.PERSON, k -> new ArrayList<>()).add(personCard);
//				System.out.println("here " + personCard);
//				// System.out.println("test: " + seenWeapons + " " +
//				// seenMap.get(CardType.WEAPON));
//				System.out.println("test: " + seenPeople);
			}
	
		}

//			for (int j =0 ; j < hand.size(); j++) {
//				if (weaponCard.equals(hand.get(j))) {
//					handContains = true;
//				}
//			}

//		if (!handContains && !seenWeaponsContains) {
//			possibleSuggestions.computeIfAbsent(CardType.WEAPON, k -> new ArrayList<>()).add(weaponCard);
//		}

		// TODO: Fix bugs with this part
		// pick person that hasnt been seen and isnt in their hand
//		for (Card personCard : board.peopleDeck) {
//			ArrayList<Card> seenPeople = seenMap.get(CardType.PERSON);
//				if(seenPeople.contains(personCard)) {
//					continue;
//				}
//				else if (hand.contains(personCard)) {
//					continue;
//				}
//				else {
//					// oOoO fancy lambda expression
//					possibleSuggestions.computeIfAbsent(CardType.PERSON, k -> new ArrayList<>()).add(personCard);
//				}
//			}

		// Debug Printing all possible suggestions
//		System.out.println("Printing possibleSuggestions");
//		possibleSuggestions.forEach((key, value) -> System.out.println(key + ":" + value));

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
		Board board = Board.getInstance(); 
		ArrayList<BoardCell> targets = new ArrayList<BoardCell>(targetList); 
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
		
		for (int i = 0; i < targets.size(); i++)
		{
			targetLocation = targets.get(i); 
			if (targetLocation.isRoom())
			{
				//get the cellSymbol in order to get the matching room from roadMap 
				Character cellSymbol = targetLocation.getCellSymbol(); 
				String roomName = board.getRoomMap().get(cellSymbol).getName(); 
				
				//check if the room is in the seenMap 
				//No Room Card has seen: we can return this room
				if (roomCards == null || !cardNames.contains(roomName))
				{
					return targetLocation; 

				}

			}
		}

		// return random target location is no room in the target list
		Random randomTarget = new Random();
		int randomNumber = randomTarget.nextInt(targets.size());

		return targets.get(randomNumber); 
		

	}
}
