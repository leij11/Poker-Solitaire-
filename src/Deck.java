
public class Deck 
{
	protected Card[] deck;
	private int topCard;
	// A deck of 52 cards
	public Deck()
	{
		deck=new Card[52];
		topCard=51;
		for(int suit=1;suit<=4;suit++)
		{
			for(int rank=1;rank<=13;rank++)
			{
				Card other= new Card(rank,suit);
				this.deck[topCard]=other;
				topCard--;
			}
		}
	}
	  public void shuffle()
      {
            for ( int pos = deck.length-1; pos>=0; pos-- ) 
            {
                int rand = (int)(Math.random()*deck.length);
                Card temp = deck[pos];
                deck[pos] = deck[rand];
                deck[rand] = temp;
             }
            topCard=0;
      }
      public Card deal()
      {       
              topCard++;
              return deck[topCard-1];

      }
      public int getNoOfCardsLeft()
      {
              return 52-topCard;
      }
}
