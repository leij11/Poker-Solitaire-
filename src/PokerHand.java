/**
 * Keeps track of a Poker Hand.  Hand size could be 1 to 7 cards.
 * Includes a getType() method that finds the type (e.g. two pair,
 * flush, straight) of this hand.  Note: In determining a hand's type
 * you should consider up to the best 5 cards in the hand.
 * 
 * @author Jessica Lei
 * @version October 2013
 */

public class PokerHand extends Hand
{
	// Poker Hand types/categories
	// Use these constants in your getType method
	// e.g. return FULL_HOUSE;
	static final int[] SCORES = { 0, 1, 3, 6, 12, 5, 10, 16, 30, 50 };
	public final static int ROYAL_FLUSH = 9;
	public final static int STRAIGHT_FLUSH = 8;
	public final static int FOUR_OF_A_KIND = 7;
	public final static int FULL_HOUSE = 6;
	public final static int FLUSH = 5;
	public final static int STRAIGHT = 4;
	public final static int THREE_OF_A_KIND = 3;
	public final static int TWO_PAIR = 2;
	public final static int PAIR = 1;
	public final static int NOTHING = 0;

	public final static String[] TYPES = { "Nothing", "Pair", "Two Pair",
		"Three of a Kind ", "Straight", "Flush", "Full House",
		"Four of a Kind", "Straight Flush", "Royal Flush" };

	/** Constructs an empty PokerHand
	 */        
	public PokerHand()
	{
		super();
	}

    public int getScore()
    {
    return SCORES[getType()];
    }
	/** Returns the type of this hand e.g. Flush, Straight, Two Pair
	 * @return the poker hand type 0 - NOTHING to 9 - ROYAL_FLUSH
	 */
	public int getType()
	{
        int ranks[] = new int [15];
        int suits[] = new int [5];
        //Get the value and the suit for each card
        for (Card nextCard: hand)
        {
            ranks [nextCard.getRank ()]++;
            suits [nextCard.getSuit ()]++;
        }
        ranks[14]=ranks[1];
        //check for suit
        int inARow = 0;
        boolean isFlush = false;
        int isStraight = -1;
        for (int suit=1;suit<suits.length;suit++)
        {
            if (suits [suit] >= 5)
            {
                isFlush = true;
            }
        }
        //Check for rank
        inARow=0;
        for (int rank = 1 ; rank <ranks.length ; rank++)
        {
            if (ranks [rank] >= 1)
            {
                inARow++;
                if (inARow >= 5)
                {
                    isStraight = rank;
                }
            }
            else
            {
            inARow=0;
            }
        }
        //Check for royal flush
        if(isStraight>0&&isFlush)
        {
        	if(isStraight==14)
        	{
        		return ROYAL_FLUSH;
        	}
        	return STRAIGHT_FLUSH;
        }
        int noOfThree=0;
        int pair=0;
        for(int rank=1;rank<ranks.length-1;rank++)
        {
        	if(ranks[rank]>=4)
        	{
        		return FOUR_OF_A_KIND;
        	}
        	if(ranks[rank]==3)
        	{
        		noOfThree++;
        	}
        	else if(ranks[rank]==2)
        	{
        		pair++;
        	}
        }
        if(noOfThree>=1&&pair>=1||noOfThree>=2)
        {
        	return FULL_HOUSE;
        }
        if(isFlush)
        {
        	return FLUSH;
        }
        if(isStraight>0)
        {
        	return STRAIGHT;
        }
        if(noOfThree==1)
        {
        	return THREE_OF_A_KIND;
        }
        if(pair>=2)
        {
        	return TWO_PAIR;
        }
        if(pair==1)
        {
        	return PAIR;
        }
        return NOTHING;
        

    }
}
