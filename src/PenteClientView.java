import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;



public class PenteClientView extends JFrame {
	
	//Button commands:
	public static final String UNDO_COMMAND = "Undo";
	public static final String END_TURN_COMMAND = "End Turn";
	public static final String QUIT_COMMAND = "Quit!";
	public static final String INSTRUCTIONS_COMMAND = "Instructions";
	public static final String NEW_GAME_COMMAND = "New Game";
	public static final String CONNECTING_BACK_COMMAND = "Back";
	public static final String EXIT_COMMAND = "Exit";
	public static final String RULES_BACK_COMMAND = "Back";
	public static final String NEXT_COMMAND = "Next";
	public static final String PREVIOUS_COMMAND = "Previous";
	public static final String DONE_COMMAND = "Done";
	
	private JPanel panels;
	private CardLayout cLayout;
	private PentePanel pentePanel;
	
	private JLabel yourCapturesLabel;
	private JLabel opponentCapturesLabel;
	
	//Buttons:
	private JButton undoButton;
	private JButton endTurnButton;
	private JButton resignButton;
	private JButton instructionsButton;
	private JButton newGameButton;
	private JButton connectingBackButton;
	private JButton exitButton;
	private JButton rulesBackButton;
	private JButton nextButton;
	private JButton previousButton;
	private JButton doneButton;
	
	//This is the grid responsible for containing the data about the player tiles:
	int[][] grid = new int[19][19];
	
	public PenteClientView() {
		
		//Basic initialization:
		
		super("Pente");
		setSize(800, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		//Initializing the cardLayout:
		
		cLayout = new CardLayout();
		panels = new JPanel(cLayout);
		add(panels);
		
		//Create Game Panel:
		
		JPanel gamePanel = new JPanel(new BorderLayout());
		gamePanel.setBackground(new Color(255, 204, 153));
		pentePanel = new PentePanel();
		gamePanel.add(pentePanel, BorderLayout.CENTER);
		
		JPanel upperPanel = new JPanel();
		upperPanel.setBackground(new Color(255, 204, 153));
		yourCapturesLabel = new JLabel("Your Captured Pairs: 0");
		upperPanel.add(yourCapturesLabel);
		resignButton = new JButton(QUIT_COMMAND);
		upperPanel.add(resignButton);
		opponentCapturesLabel = new JLabel("Opponent's Captured Pairs: 0");
		upperPanel.add(opponentCapturesLabel);
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setBackground(new Color(255, 204, 153));
		undoButton = new JButton(UNDO_COMMAND);
		lowerPanel.add(undoButton);
		endTurnButton = new JButton(END_TURN_COMMAND);
		lowerPanel.add(endTurnButton);
		gamePanel.add(lowerPanel, BorderLayout.SOUTH);
		
		gamePanel.add(upperPanel, BorderLayout.NORTH);
		panels.add(gamePanel, "gamePanel");
		
		// Creating the welcome panel.
		
		JPanel welcomePanel = new JPanel();
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		welcomePanel.setBackground(new Color(135, 21, 21));
		welcomePanel.add(Box.createRigidArea(new Dimension(0, 25)));
		JLabel welcomeLabel = new JLabel("Welcome to Pente!");
		welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD, 50));
		welcomeLabel.setForeground(new Color(247, 238, 198));
		welcomeLabel.setAlignmentX(CENTER_ALIGNMENT);
		welcomePanel.add(welcomeLabel);
		welcomePanel.add(Box.createRigidArea(new Dimension(0, 255)));
		instructionsButton = new JButton(INSTRUCTIONS_COMMAND);
		instructionsButton.setAlignmentX(CENTER_ALIGNMENT);
		welcomePanel.add(instructionsButton);
		welcomePanel.add(Box.createRigidArea(new Dimension(0, 50)));
		newGameButton = new JButton(NEW_GAME_COMMAND);
		newGameButton.setAlignmentX(CENTER_ALIGNMENT);
		welcomePanel.add(newGameButton);
		welcomePanel.add(Box.createRigidArea(new Dimension(0, 50)));
		exitButton = new JButton(EXIT_COMMAND);
		exitButton.setAlignmentX(CENTER_ALIGNMENT);
		welcomePanel.add(exitButton);
		panels.add(welcomePanel, "welcomePanel");
		cLayout.show(panels, "welcomePanel");
		
		// Creating waiting panel:
		
		JPanel waitingPanel = new JPanel();
		waitingPanel.setLayout(new BoxLayout(waitingPanel, BoxLayout.Y_AXIS));
		waitingPanel.setBackground(new Color(135, 21, 21));
		waitingPanel.add(Box.createRigidArea(new Dimension(0, 25)));
		JLabel connectingLabel = new JLabel("Waiting for Opponent...");
		connectingLabel.setAlignmentX(CENTER_ALIGNMENT);
		connectingLabel.setFont(new Font("Times New Roman", Font.BOLD, 61));
		connectingLabel.setForeground(new Color(247, 238, 198));
		waitingPanel.add(connectingLabel);
		waitingPanel.add(Box.createRigidArea(new Dimension(0, 200)));
		connectingBackButton = new JButton(CONNECTING_BACK_COMMAND);
		connectingBackButton.setAlignmentX(CENTER_ALIGNMENT);
		waitingPanel.add(connectingBackButton);
		panels.add(waitingPanel, "waitingPanel");
		
		// Creating Rules Instruction Panel:
		
		JPanel rulesInstructionPanel = new JPanel();
		rulesInstructionPanel.setBackground(new Color(135, 21, 21));
		rulesInstructionPanel.setLayout(new BoxLayout(rulesInstructionPanel, BoxLayout.Y_AXIS));
		rulesInstructionPanel.add(Box.createRigidArea(new Dimension(0, 25)));
		JLabel instructionsLabel = new JLabel("Rules and Instructions:");
		instructionsLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		instructionsLabel.setForeground(new Color(247, 238, 198));
		instructionsLabel.setAlignmentX(CENTER_ALIGNMENT);
		rulesInstructionPanel.add(instructionsLabel);
		rulesInstructionPanel.add(Box.createRigidArea(new Dimension(0, 25)));
		JTextArea instructions = new JTextArea("\n\n\n\n     Pente is played on a 19 by 19 grid of lines, and requires" 
				+ " each player to take turns placing their tiles at the intersection of the lines."
				+ "\n\n\n     The first tile must be played in the center spot. Additionally, in order to ensure the first" 
				+ " player does not have an unfair advantage, their second move must be placed outside the inner ring "
				+ "(specified on the game board)."
				+ "\n\n\n There are two ways to win. You can get a five in a row (horizontally, vertically, or diagonally) or you can 'capture'" 
				+ " five PAIRS of your opponent's tiles. There are more details on capturing" 
				+ " pairs on the next page. Note:    Once you capture a pair of tiles, they are removed from the board.");
		instructions.setWrapStyleWord(true);
		instructions.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		instructions.setForeground(new Color(247, 238, 198));
		instructions.setBackground(new Color(135, 21, 21));
		instructions.setLineWrap(true);
		instructions.setMaximumSize(new Dimension(700, 500));
		instructions.setAlignmentX(CENTER_ALIGNMENT);
		rulesInstructionPanel.add(instructions);
		rulesInstructionPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		rulesBackButton = new JButton(RULES_BACK_COMMAND);
		rulesBackButton.setAlignmentX(CENTER_ALIGNMENT);
		rulesInstructionPanel.add(rulesBackButton);
		nextButton = new JButton(NEXT_COMMAND);
		nextButton.setAlignmentX(CENTER_ALIGNMENT);
		rulesInstructionPanel.add(nextButton);
		panels.add(rulesInstructionPanel, "rulesInstructionPanel");
		
		// Creating Capture Examples Panel:
		
		JPanel exampleCapturesPanel = new JPanel(new BorderLayout());
		exampleCapturesPanel.setBackground(new Color(135, 21, 21));
		
		CapturesPanel capturesPanel = new CapturesPanel();
		exampleCapturesPanel.add(capturesPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(135, 21, 21));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		previousButton = new JButton(PREVIOUS_COMMAND);
		previousButton.setAlignmentX(CENTER_ALIGNMENT);
		buttonPanel.add(previousButton);
		doneButton = new JButton(DONE_COMMAND);
		doneButton.setAlignmentX(CENTER_ALIGNMENT);
		buttonPanel.add(doneButton);
		exampleCapturesPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		JPanel labelPanel = new JPanel();
		labelPanel.setBackground(new Color(135, 21, 21));
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		labelPanel.add(Box.createRigidArea(new Dimension(0, 25)));
		JLabel textLabel = new JLabel("The following are three different examples of the green player 'capturing' a pair of the yellow player's tiles:");
		textLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
		textLabel.setAlignmentX(CENTER_ALIGNMENT);
		textLabel.setForeground(new Color(247, 238, 198));
		labelPanel.add(textLabel);
		labelPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		JLabel textLabel2 = new JLabel("As you can see, it is legal to 'capture' tiles horizontally, vertically, or diagonally.");
		textLabel2.setFont(new Font("Times New Roman", Font.BOLD, 16));
		textLabel2.setAlignmentX(CENTER_ALIGNMENT);
		textLabel2.setForeground(new Color(247, 238, 198));
		labelPanel.add(textLabel2);
		labelPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		JLabel textLabel3 = new JLabel("Note: You cannot 'capture' yourself by placing two of your tiles in between an opponent's.");
		textLabel3.setFont(new Font("Times New Roman", Font.BOLD, 16));
		textLabel3.setAlignmentX(CENTER_ALIGNMENT);
		textLabel3.setForeground(new Color(247, 238, 198));
		labelPanel.add(textLabel3);
		exampleCapturesPanel.add(labelPanel, BorderLayout.NORTH);
		
		panels.add(exampleCapturesPanel, "exampleCapturesPanel");
	}
	
//------------------------------ The JPanel responsible for drawing the board and pieces ------------------------------------------------
	
	//The following class is responsible for drawing the board and player's pieces:
	
	private class PentePanel extends JPanel {
		
		int squareSize = 27;
		int offSetX = (800 - ((squareSize * 18) + 19)) / 2;
		int offSetY = 32;
		int tileRadius = 12;
		
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(new Color(247, 238, 198));
			g.fillRect(offSetX - 25, offSetY - 25
					, (squareSize * 18) + 19 + 50, (squareSize * 18) + 19 + 50);
			g.setColor(Color.BLACK);
			g.fillRect(offSetX, offSetY, (squareSize * 18) + 19, (squareSize * 18) + 19);
			g.setColor(new Color(247, 238, 198));
			for (int i = 1; i <= 18; i++) {
				for (int j = 1; j <= 18; j++) {
					g.fillRect(offSetX + i + ((i - 1) * squareSize)
							, offSetY + j + ((j - 1) * squareSize), squareSize, squareSize);
				}
			}
			g.setColor(Color.BLACK);
			g.fillOval(offSetX + (9 * 27) + 10 - 5, offSetY + (9 * 27) + 10 - 5, 8, 8);
			g.drawRect(offSetX + (6 * 27) + 7, offSetY + (6 * 27) + 7, (6 * 27) + 5, (6 * 27) + 5);
			g.drawRect(offSetX + (6 * 27) + 6, offSetY + (6 * 27) + 6, (6 * 27) + 7, (6 * 27) + 7);
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					if(grid[i][j] == 0) {
						continue;
					}
					else if (grid[i][j] == 1) {
						g.setColor(Color.black);
						g.fillOval(offSetX + (i * 27) + (i + 1) - tileRadius - 1
								, offSetY + (j * 27) + (j + 1) - tileRadius - 1
								, (tileRadius + 1)*2, (tileRadius + 1)*2);
						g.setColor(Color.YELLOW);
						g.fillOval(offSetX + (i * 27) + (i + 1) - tileRadius
								, offSetY + (j * 27) + (j + 1) - tileRadius
								, tileRadius*2, tileRadius*2);
					}
					else {
						g.setColor(Color.black);
						g.fillOval(offSetX + (i * 27) + (i + 1) - tileRadius - 1
								, offSetY + (j * 27) + (j + 1) - tileRadius - 1
								, (tileRadius + 1)*2, (tileRadius + 1)*2);
						g.setColor(Color.GREEN);
						g.fillOval(offSetX + (i * 27) + (i + 1) - tileRadius
								, offSetY + (j * 27) + (j + 1) - tileRadius
								, tileRadius*2, tileRadius*2);
					}
					
				}
			}
		}
	}
//------------------------------------------------------------------------------------------------------------------------------
	
	
	
//--------------------------------------------- Example Panel Drawings Below ---------------------------------------------------
	
	//This class is responsible for drawing the the example pair captures in the instructions manual:
	
	private class CapturesPanel extends JPanel {
		
		int squareSize = 27;
		int offSetX1 = 50 + 135;
		int offSetX2 = 213 + 135;
		int offSetX3 = 376 + 135;
		int offSetY = 175;
		
		@Override
		public void paintComponent(Graphics g) {
			
			// Making the grids:
			g.setColor(Color.BLACK);
			g.fillRect(offSetX1 - 28, offSetY - 28, (27 * 5) + 6, (27 * 5) + 6);
			g.fillRect(offSetX2 - 28, offSetY - 28, (27 * 5) + 6, (27 * 5) + 6);
			g.fillRect(offSetX3 - 28, offSetY - 28, (27 * 5) + 6, (27 * 5) + 6);
			g.setColor(new Color(247, 238, 198));
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					g.fillRect(offSetX1 + i + ((i - 1) * squareSize)
							, offSetY + j + ((j - 1) * squareSize), squareSize, squareSize);
					g.fillRect(offSetX2 + i + ((i - 1) * squareSize)
							, offSetY + j + ((j - 1) * squareSize), squareSize, squareSize);
					g.fillRect(offSetX3 + i + ((i - 1) * squareSize)
							, offSetY + j + ((j - 1) * squareSize), squareSize, squareSize);
				}
			}
			
			//Making the black outlines for the peices:
			//Third example:
			g.setColor(Color.BLACK);
			g.fillOval(offSetX1 + (2 * 27) + (2 + 1) - 12 - 1
					, offSetY + (0 * 27) + (0 + 1) - 12 - 1
					, (12 + 1)*2, (12 + 1)*2);
			g.fillOval(offSetX1 + (2 * 27) + (2 + 1) - 12 - 1
					, offSetY + (1 * 27) + (1 + 1) - 12 - 1
					, (12 + 1)*2, (12 + 1)*2);
			g.fillOval(offSetX1 + (2 * 27) + (2 + 1) - 12 - 1
					, offSetY + (2 * 27) + (2 + 1) - 12 - 1
					, (12 + 1)*2, (12 + 1)*2);
			g.fillOval(offSetX1 + (2 * 27) + (2 + 1) - 12 - 1
					, offSetY + (3 * 27) + (3 + 1) - 12 - 1
					, (12 + 1)*2, (12 + 1)*2);
			
			//Second example:
			g.fillOval(offSetX2 + (0 * 27) + (0 + 1) - 12 - 1
					, offSetY + (1 * 27) + (1 + 1) - 12 - 1
					, (12 + 1)*2, (12 + 1)*2);
			g.fillOval(offSetX2 + (1 * 27) + (1 + 1) - 12 - 1
					, offSetY + (1 * 27) + (1 + 1) - 12 - 1
					, (12 + 1)*2, (12 + 1)*2);
			g.fillOval(offSetX2 + (2 * 27) + (2 + 1) - 12 - 1
					, offSetY + (1 * 27) + (1 + 1) - 12 - 1
					, (12 + 1)*2, (12 + 1)*2);
			g.fillOval(offSetX2 + (3 * 27) + (3 + 1) - 12 - 1
					, offSetY + (1 * 27) + (1 + 1) - 12 - 1
					, (12 + 1)*2, (12 + 1)*2);
			
			//First example:
			g.fillOval(offSetX3 + (0 * 27) + (0 + 1) - 12 - 1
					, offSetY + (0 * 27) + (0 + 1) - 12 - 1
					, (12 + 1)*2, (12 + 1)*2);
			g.fillOval(offSetX3 + (1 * 27) + (1 + 1) - 12 - 1
					, offSetY + (1 * 27) + (1 + 1) - 12 - 1
					, (12 + 1)*2, (12 + 1)*2);
			g.fillOval(offSetX3 + (2 * 27) + (2 + 1) - 12 - 1
					, offSetY + (2 * 27) + (2 + 1) - 12 - 1
					, (12 + 1)*2, (12 + 1)*2);
			g.fillOval(offSetX3 + (3 * 27) + (3 + 1) - 12 - 1
					, offSetY + (3 * 27) + (3 + 1) - 12 - 1
					, (12 + 1)*2, (12 + 1)*2);
			
			//Making the yellow pieces:
			g.setColor(Color.YELLOW);
			g.fillOval(offSetX1 + (2 * 27) + (2 + 1) - 12
					, offSetY + (1 * 27) + (1 + 1) - 12, 12*2, 12*2);
			g.fillOval(offSetX1 + (2 * 27) + (2 + 1) - 12
					, offSetY + (2 * 27) + (2 + 1) - 12, 12*2, 12*2);
			g.fillOval(offSetX2 + (1 * 27) + (1 + 1) - 12
					, offSetY + (1 * 27) + (1 + 1) - 12, 12*2, 12*2);
			g.fillOval(offSetX2 + (2 * 27) + (2 + 1) - 12
					, offSetY + (1 * 27) + (1 + 1) - 12, 12*2, 12*2);
			g.fillOval(offSetX3 + (1 * 27) + (1 + 1) - 12
					, offSetY + (1 * 27) + (1 + 1) - 12, 12*2, 12*2);
			g.fillOval(offSetX3 + (2 * 27) + (2 + 1) - 12
					, offSetY + (2 * 27) + (2 + 1) - 12, 12*2, 12*2);
			
			//Making the green pieces:
			g.setColor(Color.GREEN);
			g.fillOval(offSetX1 + (2 * 27) + (2 + 1) - 12
					, offSetY + (0 * 27) + (0 + 1) - 12, 12*2, 12*2);
			g.fillOval(offSetX1 + (2 * 27) + (2 + 1) - 12
					, offSetY + (3 * 27) + (3 + 1) - 12, 12*2, 12*2);
			g.fillOval(offSetX2 + (0 * 27) + (0 + 1) - 12
					, offSetY + (1 * 27) + (1 + 1) - 12, 12*2, 12*2);
			g.fillOval(offSetX2 + (3 * 27) + (3 + 1) - 12
					, offSetY + (1 * 27) + (1 + 1) - 12, 12*2, 12*2);
			g.fillOval(offSetX3 + (0 * 27) + (0 + 1) - 12
					, offSetY + (0 * 27) + (0 + 1) - 12, 12*2, 12*2);
			g.fillOval(offSetX3 + (3 * 27) + (3 + 1) - 12
					, offSetY + (3 * 27) + (3+ 1) - 12, 12*2, 12*2);
		}
		
	}	
//------------------------------------------------------ End of Example Panel drawings -------------------------------------------

	
	
//------------------------------------------- Methods to be accessed outside of the class ----------------------------------------
	
	//The following methods are called mostly by the client controller and update different aspects of the view.
	
	public void addActionListener(ActionListener listener) {
		undoButton.addActionListener(listener);
		endTurnButton.addActionListener(listener);
		resignButton.addActionListener(listener);
		instructionsButton.addActionListener(listener);
		newGameButton.addActionListener(listener);
		connectingBackButton.addActionListener(listener);
		exitButton.addActionListener(listener);
		rulesBackButton.addActionListener(listener);
		nextButton.addActionListener(listener);
		previousButton.addActionListener(listener);
		doneButton.addActionListener(listener);
	}
	
	public void addMouseListener(MouseListener listener) {
		pentePanel.addMouseListener(listener);
	}
	
	public void showPanel(String panelName) {
		cLayout.show(panels, panelName);
	}
	
	public void setGrid(int[][] grid) {
		this.grid = grid;
	}
	
	public int getOffSetX()	 {
		return pentePanel.offSetX;
	}
	
	public int getOffSetY() {
		return pentePanel.offSetY;
	}
	
	public void setYourCapturesText(int numCaptures) {
		yourCapturesLabel.setText("Your Captured Pairs: " + numCaptures);
	}
	
	public void setOpponentCapturesText(int numCaptures) {
		opponentCapturesLabel.setText("Opponent's Captured Pairs: " + numCaptures);
	}
	
	public void hideButtons() {
		endTurnButton.setEnabled(false);
		undoButton.setEnabled(false);
	}
	
	public void showButtons() {
		endTurnButton.setEnabled(true);
		undoButton.setEnabled(true);
	}

}
