package clueGame;

public class Card {
	private String cardName; 
	private CardType cardType; 
	//constructor 
	
	public Card(String cardName, String type)
	{
		this.cardName = cardName;
		switch(type) {
		case "Room": this.cardType = CardType.ROOM; 
					break; 
		case "Weapon": this.cardType = CardType.WEAPON; 
					break; 
		case "Player": this.cardType = CardType.PERSON;
					break; 
		}
					
	}


	
	//setters 
	public void setCardName(String cardName)
	{
		this.cardName = cardName;
	}
	public void setCardType(CardType cardType)
	{
		this.cardType = cardType; 
	}
	
	//getters
	public String getCardName()
	{
		return cardName;
	}
	public CardType getCardType()
	{
		return cardType; 
	}

	
	public boolean equals(Card target){
		return false; 
	}



	@Override
	public String toString() {
		return "Card [cardName=" + cardName + ", cardType=" + cardType + "]";
	}

}
