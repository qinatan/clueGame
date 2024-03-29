/**
 * Card Class contains attributes describing a card, includes card type, card name
 * @author: Mike EacK 
 * @author: John Omalley 
 * @author: Qina Tan 
 * @start Date: 4/24/2023
 * @collaborator: none 
 * @resources: none 
 * 
 * 
 **/


package clueGame;

import java.awt.Color;

public class Card implements Comparable<Card> {
	private String cardName;
	private CardType cardType;
	private Color cardColor; 

	// constructors
	public Card(CardType type, String cardName) {
		this.cardType = type;
		this.cardName = cardName;
	}

	public Card(String cardName, String type) {
		this.cardName = cardName;
		switch (type) {
		case "Room":
			this.cardType = CardType.ROOM;
			break;
		case "Weapon":
			this.cardType = CardType.WEAPON;
			break;
		case "Player":
			this.cardType = CardType.PERSON;
			break;
		default:
			break;
		}

	}

	// setters
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	// getters
	public String getCardName() {
		return cardName;
	}

	public CardType getCardType() {
		return cardType;
	}
	
	public void setCardColor(Color cardColor) {
		this.cardColor = cardColor; 
	}
	
	public Color getcardColor() {
		return this.cardColor; 
	}

	@Override
	public boolean equals(Object o ) {
		 if (o == null || getClass() != o.getClass()) return false;
		Card that = (Card) o ; 
		return cardType.equals(that.cardType) && cardName.equals(that.cardName);
	}



	@Override
	public String toString() {
		return cardName + " ";
	}

	public void setDealCount() {
	}

	@Override
	public int compareTo(Card o) {
		return 0;
	}

}
