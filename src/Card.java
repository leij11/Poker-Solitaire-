import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Comparator;

import javax.swing.ImageIcon;


public class Card extends Rectangle implements Comparable<Card>
{
	private static final Image background=new ImageIcon("images\\blackback.png").getImage();
	public static final int WIDTH=background.getWidth(null);
	public static final int HEIGHT=background.getHeight(null);		
	private Image image;
        private int suit;
        private int rank;
        private boolean isFaceUp;
        private static String SUIT=" DCHS";
        private static String RANK=" A23456789TJQK";
        public static final Comparator<Card>RANK_ORDER= new rankOrder();
        public Card(int rank,int suit)
        {
        	   super(0, 0, 0, 0);
                this.rank=rank;
                this.suit=suit;
                String imageFileName=""+" dchs".charAt(suit)+rank+".png";
                imageFileName="images\\"+imageFileName;
                image= new ImageIcon(imageFileName).getImage();
                isFaceUp=true;
             // Set the size of the card based on the image size
                setSize(image.getWidth(null), image.getHeight(null));
        }
        public Card(String cardStr)
        {
                rank=RANK.indexOf(cardStr.charAt(0));
                suit=SUIT.indexOf(cardStr.charAt(1));
        }
        /* 
         * 
         * 
         */
        public String toString()
        {
                StringBuffer string= new StringBuffer();
                string.append(RANK.charAt(rank));
                string.append(SUIT.charAt(suit));
                return string.toString();

        }
        /* Get the rankings for the card
         * @return  the ranking of the card
         */
        public int getRank()
        {
                return rank;
        }
        /* Get the suit of the card
         * @return the suit of the card
         */
        public int getSuit()
        {
                return suit;
        }
        /* Get the value of the card.If the rank of the card is 
         * greater or equal to 10, the value is one
         * If the rank of the card is 1, the value is 11
         * Other card's value has the same value as the rank 
         * @return the value of the card
         */
        public int getValue()
        {
                if(rank>=10)
                {
                     return 10;
                }
                else if(rank==1)
                {
                     return 11;
                }
                else
                {
                       return rank;
                }
        }
        public boolean isAce()
        {
                if(rank==1)
                {
                        return true;
                }
                return false;
        }
        public void draw(Graphics g)
        {
        	if(isFaceUp)
        	{
        		g.drawImage(image,x,y,null);
        	}
        	else
        	{
        		g.drawImage(background,x,y,null);
        	}
        }
        
        public void move (Point initialPos, Point finalPos)
        {
        translate (finalPos.x - initialPos.x, finalPos.y - initialPos.y);
        }
        
        public int compareTo(Card cardToCompare)
        {
        	if(this.suit!=cardToCompare.suit)
        	{
        		return this.suit-cardToCompare.suit;
        	}
        	return this.rank-cardToCompare.rank;              
        
        }
        private static class rankOrder implements Comparator<Card>
        {
        	public int compare(Card first, Card second)
        	{
        		if(first.rank<second.rank)
        		{
        			return -1;
        		}
        		else if(first.rank>second.rank)
        		{
        			return 1;
        		}
				else
				{
					return first.suit-second.suit;
				}
        		
        	}
        }
}

