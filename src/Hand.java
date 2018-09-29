import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
public class Hand
{
        protected ArrayList<Card> hand;
        public Hand()
        {
                hand=new ArrayList<Card>();
        }
        public Hand(String handStr)
        {
                hand=new ArrayList<Card>();
                Scanner str=new Scanner(handStr);
                while(str.hasNext())
                {
                        Card c=new Card(str.next());
                        hand.add(c);
                }
                str.close();
        }
        public String toString()
        {
                StringBuffer str=new StringBuffer(3);
                for(int index=0;index<hand.size();index++)
                {
                        Card card=hand.get(index);
                        str.append(card+" ");
                }
                return str.toString();
        }
        public void addCard(Card cardToAdd)
        {
                hand.add(cardToAdd);
        }
        public int getValue()
        {
                int total=0;
                int numAces=0;
                for(Card card: hand)
                {                 
                        if(card.isAce())
                        {
                                numAces++;
                        }
                       total+=card.getValue();
                }
                while(total>21&&numAces>0)
                {
                        total-=10;
                        numAces--;
                }
                return total;
        }
        public void clear()
        {
                hand.clear();
        }
        
        public void sortBySuit()
        {
        	Collections.sort(hand);
        }

        public void sortByRank()
        {
        	for(Card card: hand)
        	{
        		Collections.sort(hand,card.RANK_ORDER);
        	}
        }
        /**
         * Displays the Cards in this Hand
         *@param g Graphics context to display the deck
         */ public void draw(Graphics g)
         {
        	 for (Card next : hand)
        		 next.draw(g);
         }
         
         public void removeCard(Card card)
         {
        	 hand.remove(card);
         }
         public Card getCardAt(Point point)
         {
        	 for (Card next : hand)
        		 if (next.contains(point))
        			 return next;
        	 return null;
         }

}
