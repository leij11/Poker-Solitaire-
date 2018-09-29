/** 
 * Plays a simple game of Poker Solitaire
 */
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.*;

public class PokerSolitaire extends JFrame implements ActionListener
{	
	private static final int TOP_OFFSET = 10;
	private static final int LEFT_OFFSET = 13;
	private static final int ROW_SPACING = Card.HEIGHT + 15;
	private static final int COL_SPACING = Card.WIDTH + 15;
	private static final Point POS_OF_NEXT_CARD = new Point(6 * COL_SPACING
			+ LEFT_OFFSET, 50 + TOP_OFFSET);
	private static final int ANIMATION_FRAMES = 6;
	private static final boolean ANIMATION_ON = true;

	private DrawingPanel cardArea;
	private JMenuItem newMenuItem, topScoresOption, quitMenuItem,
			aboutMenuItem;
	private Deck myDeck;
	private PokerHand[] rowHands;
	private PokerHand[] colHands;
	private Card nextCard;
	private Card currentCard;
	private Point lastPoint;
	private int originalRow, originalCol;
	private boolean[][] spotsTaken;
	private int score;
	private boolean gameOver;
	private ArrayList<Player> topPlayers;

	/**
	 * Creates a Simple Poker Solitaire Frame Application
	 */
	public PokerSolitaire()
	{
		super("Poker Solitaire");

		// Add in a Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');
		newMenuItem = new JMenuItem("New Game");
		newMenuItem.addActionListener(this);

		topScoresOption = new JMenuItem("Top Scores");
		topScoresOption.addActionListener(this);

		quitMenuItem = new JMenuItem("Exit");
		quitMenuItem.addActionListener(this);
		gameMenu.add(newMenuItem);
		gameMenu.add(topScoresOption);
		gameMenu.addSeparator();
		gameMenu.add(quitMenuItem);
		menuBar.add(gameMenu);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		aboutMenuItem = new JMenuItem("About...");
		aboutMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);

		// Set up the layout and add in a DrawingPanel for the cardArea
		// Centre the frame in the middle (almost) of the screen
		setLayout(new BorderLayout());
		cardArea = new DrawingPanel();
		add(cardArea, BorderLayout.CENTER);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - cardArea.WIDTH) / 2,
				(screen.height - cardArea.HEIGHT) / 2 - 52);

		// Set up the deck and hands
		myDeck = new Deck();

		// We need 10 hands in total
		rowHands = new PokerHand[5];
		colHands = new PokerHand[5];
		for (int hand = 0; hand < 5; hand++)
		{
			rowHands[hand] = new PokerHand();
			colHands[hand] = new PokerHand();
		}
		// Set up an array to keep track of the spots taken on the table
		spotsTaken = new boolean[5][5];
		newGame();
	}

	/**
	 * Method that deals with the menu options
	 * 
	 * @param event the event that triggered this method
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == newMenuItem)
		{
			newGame();
		}
		else if (event.getSource() == topScoresOption)
		{
			// show a list of top scores
			// You could add in this code
		}
		else if (event.getSource() == quitMenuItem)
		{
			System.exit(0);
		}
		else if (event.getSource() == aboutMenuItem)
		{
			JOptionPane
					.showMessageDialog(
							cardArea,
							"Poker Solitaire by Ridout\nand AddInYourName\n\u00a9 2013",
							"About Poker Solitaire",
							JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Starts a new game by shuffling the deck and re-initialising the hands and
	 * the spots taken on the table
	 */
	public void newGame()
	{
		myDeck.shuffle();

		// Clear the hands (the clear method just resets the size to 0)
		// Also reset the spotsTaken all to false
		for (int hand = 0; hand < 5; hand++)
		{
			rowHands[hand].clear();
			colHands[hand].clear();
			for (int column = 0; column < 5; column++)
				spotsTaken[hand][column] = false;
		}
		score = 0;
		gameOver = false;

		// Deal the first card
		nextCard = myDeck.deal();
		nextCard.setLocation(POS_OF_NEXT_CARD);
		currentCard = null;

		repaint();
	}

	/**
	 * Updates the score based on the score in all of the hands
	 */
	public void updateScore()
	{
		score = 0;
		for (int hand = 0; hand < 5; hand++)
		{
			score += rowHands[hand].getScore();
			score += colHands[hand].getScore();
		}
	}

	/**
	 * Refresh the drawing area immediately Immediate refresh is needed to show
	 * the animation
	 */
	private void rePaintDrawingAreaImmediately()
	{
		cardArea.paintImmediately(new Rectangle(0, 0, cardArea.getWidth(),
				cardArea.getHeight()));
	}

	/**
	 * Inner class to keep track of the card area
	 */
	private class DrawingPanel extends JPanel
	{
		final Color TABLE_COLOUR = new Color(140, 225, 140);
		final int WIDTH = 650;
		final int HEIGHT = 600;

		public DrawingPanel()
		{
			setPreferredSize(new Dimension(WIDTH, HEIGHT));
			setFont(new Font("Arial", Font.PLAIN, 18));
			this.addMouseListener(new PokerMouseHandler());
			this.addMouseMotionListener(new MouseMotionHandler());
			this.setBackground(TABLE_COLOUR);
		}

		/**
		 * Paints the drawing area
		 * 
		 * @param g the graphics context to paint
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			// Draw the spots for the cards to go
			// You may want to make this nicer
			g.setColor(Color.black);
			for (int row = 0; row < 5; row++)
				for (int column = 0; column < 5; column++)
				{
					int x = column * COL_SPACING;
					int y = row * ROW_SPACING;
					g.drawRoundRect(x + LEFT_OFFSET, y + TOP_OFFSET,
							Card.WIDTH - 1, Card.HEIGHT - 1, 7, 7);
				}

			// Draw the hands and their current scores
			// Only the row hands need to be drawn
			g.setColor(Color.blue);
			for (int hand = 0; hand < 5; hand++)
			{
				rowHands[hand].draw(g);
				int score = rowHands[hand].getScore();
				if (score > 0)
					g.drawString(String.valueOf(score), 5 * COL_SPACING
							+ LEFT_OFFSET + 10, hand * ROW_SPACING
							+ Card.HEIGHT / 2 + TOP_OFFSET + 5);
				score = colHands[hand].getScore();
				if (score > 0)
					g.drawString(String.valueOf(score), hand * COL_SPACING
							+ Card.WIDTH / 2 - 5 + LEFT_OFFSET, 5 * ROW_SPACING
							+ TOP_OFFSET + 10);
			}

			// Draw the total score
			g.setFont(new Font("Arial", Font.BOLD, 24));
			g.setColor(Color.blue);
			g.drawString("Score: " + score, 500, 310);

			// Draw the next card if not game over
			if (!gameOver)
				nextCard.draw(g);
			else
			{
				g.setColor(Color.blue);
				g.drawString("Game", POS_OF_NEXT_CARD.x - 7,
						POS_OF_NEXT_CARD.y + 25);
				g.drawString("Over", POS_OF_NEXT_CARD.x,
						POS_OF_NEXT_CARD.y + 55);
			}

			// Draw the moving card
			if (currentCard != null)
				currentCard.draw(g);
		}

		/**
		 * Places a card in the given row and column on the board and marks this
		 * spot as taken
		 * 
		 * @param card the Card to place
		 * @param row the row to place the card
		 * @param column the column to place the card 
		 * Precondition row and column are on the board
		 */
		private void placeACard(Card card, int row, int column)
		{
			spotsTaken[row][column] = true;
			Point newCardPos = new Point(column * COL_SPACING + LEFT_OFFSET,
					row * ROW_SPACING + TOP_OFFSET);

			if (ANIMATION_ON)
				moveACard(card, newCardPos);
			else
				card.setLocation(newCardPos);
		}

		/**
		 * Inner class to handle mouse events Extends MouseAdapter instead of
		 * implementing MouseListener since we only need to override
		 * mousePressed
		 */
		private class PokerMouseHandler extends MouseAdapter
		{
			/**
			 * Handles a mousePress when selecting a spot to place a card
			 * 
			 * @param event the event information
			 */
			public void mousePressed(MouseEvent event)
			{
				// If the game is over, we disable any mouse presses
				if (gameOver)
				{
					if (JOptionPane.showConfirmDialog(cardArea,
							"Do you want to Play Again?", "Game Over",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						newGame();
					return;
				}

				// If we are dragging a Card disregard any mouse pressed
				// May not be needed
				if (currentCard != null)
					return;

				Point clickPoint = event.getPoint();

				// Figure out the selected row and column on the board
				int row = (clickPoint.y - TOP_OFFSET) / ROW_SPACING;
				int column = (clickPoint.x - LEFT_OFFSET) / COL_SPACING;

				// Ignore any clicks off the board area
				if (row < 0 || row > 4 || column < 0 || column > 4)
					return;

				// Pick up card if there is a Card here
				if (spotsTaken[row][column])
				{
					lastPoint = new Point(event.getPoint());

					// Find out which Card was selected
					currentCard = rowHands[row].getCardAt(lastPoint);

					// Ignore clicks between Cards
					if (currentCard == null)
						return;

					// ... and remove it from both hands and the board
					rowHands[row].removeCard(currentCard);
					colHands[column].removeCard(currentCard);
					spotsTaken[row][column] = false;
					updateScore();

					// Keep track of original position if we need to return the
					// card
					originalRow = row;
					originalCol = column;
					// We can quit early
					return;
				}

				// Clicking on an empty spot
				// Place the next card in this spot
				placeACard(nextCard, row, column);

				// ... and add it to the corresponding row and column hand
				rowHands[row].addCard(nextCard);
				colHands[column].addCard(nextCard);
				updateScore();
				nextCard = null;

				// Deal the next card if not done
				if (myDeck.getNoOfCardsLeft() > 27)
				{
					nextCard = myDeck.deal();
					nextCard.setLocation(POS_OF_NEXT_CARD);
					rePaintDrawingAreaImmediately();
				}
				// Game is over - check for top scores
				else
				{
					gameOver = true;
					rePaintDrawingAreaImmediately();

					// Update top scores
					// Need to add this code in

					if (JOptionPane.showConfirmDialog(cardArea,
							"Do you want to Play Again?", "Game Over",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						newGame();
				}

			}

			public void mouseReleased(MouseEvent event)
			{
				// Only can release a Card we have
				if (currentCard != null)
				{
					// Figure out the selected row and column on the board
					Point clickPoint = event.getPoint();
					int row = (clickPoint.y - TOP_OFFSET) / ROW_SPACING;
					int column = (clickPoint.x - LEFT_OFFSET) / COL_SPACING;

					// If off the grid or in a taken spot return to original
					// spot
					if (row < 0 || row > 4 || column < 0 || column > 4
							|| spotsTaken[row][column])
					{
						rowHands[originalRow].addCard(currentCard);
						colHands[originalCol].addCard(currentCard);
						placeACard(currentCard, originalRow, originalCol);
					}
					// else add to new spot
					else
					{
						rowHands[row].addCard(currentCard);
						colHands[column].addCard(currentCard);
						placeACard(currentCard, row, column);
					}

					currentCard = null;
					updateScore();
					repaint();
				}
			}
		}

		// Inner Class to handle mouse movements
		private class MouseMotionHandler implements MouseMotionListener
		{
			public void mouseMoved(MouseEvent event)
			{
				// Figure out if we are on a card or not
				boolean onACard = false;
				Point clickPoint = event.getPoint();

				// Check all Cards on the table
				for (int row = 0; row < rowHands.length && !onACard; row++)
					if (rowHands[row].getCardAt(clickPoint) != null)
						onACard = true;

				// Show either a hand (on a card) or the default cursor
				if (onACard)
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				else
					setCursor(Cursor.getDefaultCursor());
			}

			public void mouseDragged(MouseEvent event)
			{
				Point currentPoint = event.getPoint();

				if (currentCard != null)
				{
					// We use the difference between the lastPoint and the
					// currentPoint to move the card so that the position of
					// the mouse on the card doesn't matter.
					// i.e. we can drag the card from any point on the card
					// image
					currentCard.translate(currentPoint.x - lastPoint.x,
							currentPoint.y - lastPoint.y);
					lastPoint = currentPoint;
					repaint();
				}
			}
		}

		/**
		 * Moves a card with a simple animation
		 * 
		 * @param cardToMove the card you want to move
		 * @param finalPos the final position of the card
		 */
		private void moveACard(Card cardToMove, Point finalPos)
		{
			int x = cardToMove.x;
			int y = cardToMove.y;
			int dx = (finalPos.x - x) / ANIMATION_FRAMES;
			int dy = (finalPos.y - y) / ANIMATION_FRAMES;
			for (int times = 1; times <= ANIMATION_FRAMES; times++)
			{
				x += dx;
				y += dy;
				cardToMove.setLocation(x, y);
				rePaintDrawingAreaImmediately();
				delay(50);
			}
			cardToMove.setLocation(finalPos);
			// Clear up whole card area
			rePaintDrawingAreaImmediately();
		}

		/**
		 * Delays the given number of milliseconds
		 * 
		 * @param msec the number of milliseconds to delay
		 */
		private void delay(int msec)
		{
			try
			{
				Thread.sleep(msec);
			}
			catch (Exception e)
			{
			}
		}
	}

	public static void main(String[] args)
	{
		PokerSolitaire game = new PokerSolitaire();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.pack();
		game.setVisible(true);
	}
}
