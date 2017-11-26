import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import netgame.common.Client;


public class PenteClientController implements ActionListener, MouseListener, WindowListener {
	
	private static final int DEFAULT_PORT = 45017;
	
	PenteClientView view;
	PenteClientModel model;
	
	PenteClientConnection connection;
	
	public PenteClientController(PenteClientView view, PenteClientModel model) {
		String host = JOptionPane.showInputDialog("Enter the server address that you wish to connect to: ");
		if (host == null) {
			return;
		}
		try {
			connection = new PenteClientConnection(host, DEFAULT_PORT);
			connection.setAutoreset(true);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.view = view;
		this.model = model;
		this.view.addActionListener(this);
		this.view.addWindowListener(this);
		this.view.addMouseListener(this);
		this.view.setGrid(model.getGrid());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (model.isWinner()) {
			return;
		}
		if (!model.isYourTurn()) {
			JOptionPane.showMessageDialog(null, "You must wait your turn!");
			return;
		}
		if (model.getLastMove()[0] != -1) {
			JOptionPane.showMessageDialog(null, "You can only place one tile per turn!");
			return;
		}
		int x = Math.round((float)(e.getX()  - view.getOffSetX()) / 28);
		if (x < 0)
			x = 0;
		if (x > 18) {
			x = 18;
		}
		int y = Math.round((float)(e.getY()  - view.getOffSetY()) / 28);
		if (y < 0)
			y = 0;
		if (y > 18)
			y = 18;
		if (model.youWentFirst() && model.numTurns() == 0 && (x != 9 || y != 9)) {
			JOptionPane.showMessageDialog(null, "You must place your first tile in the center square!");
			return;
		}
		if (model.youWentFirst() && model.numTurns() == 1) {
			if ((x > 6 && x < 12) && (y > 6 && y < 12)) {
				JOptionPane.showMessageDialog(null, "Your second` move must be outside the inner ring!");
				return;
			}
		}
		if (model.addPlayerTile(x, y, 1)) {
			model.setLastMove(x, y);
			model.checkForCapturedPair(model.getLastMove()[0], model.getLastMove()[1]);
			view.setGrid(model.getGrid());
			view.repaint();
		}
		else {
			JOptionPane.showMessageDialog(null, "You cannot place your tile on top of another!");
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(PenteClientView.UNDO_COMMAND)) {
			if (!model.isYourTurn()) {
				JOptionPane.showMessageDialog(null, "You must wait your turn!");
				return;
			}
			if (model.getCapturedPairs().size() != 0) {
				for (int[] array : model.getCapturedPairs()) {
					model.addPlayerTile(array[0], array[1], array[2]);
				}
				model.clearCapturedPairs();
			}
			if (model.getLastMove()[0] == -1) {
				JOptionPane.showMessageDialog(null, "No previous move to undo");
				return;
			}
			int[] lastMove = model.getLastMove();
			model.removePlayerTile(lastMove[0], lastMove[1]);
			view.setGrid(model.getGrid());
			view.repaint();
			model.setLastMove(-1, -1);
		}
		else if (command.equals(PenteClientView.QUIT_COMMAND)) {
			if (model.inGame()) {
				connection.send("disconnected");
			}
			else {
				connection.send("Leave Game");
			}
			model.reset();
			view.setYourCapturesText(0);
			view.setOpponentCapturesText(0);
			model.setYourTurn(false);
			view.showPanel("welcomePanel");
			view.showButtons();
		}
		else if (command.equals(PenteClientView.END_TURN_COMMAND)) {
			view.hideButtons();
			if (!model.isYourTurn()) {
				JOptionPane.showMessageDialog(null, "It is not your turn yet!");
				return;
			}
			if (model.isYourTurn() && model.getLastMove()[0] == -1) {
				JOptionPane.showMessageDialog(null, "You must make a move before ending your turn!");
				return;
			}
			connection.send(model.getLastMove());
			connection.resetOutput();
			model.updateCaptures();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			model.checkForWinner();
			if (model.isWinner()) {
				model.setInGame(false);
				if (model.getWinner() == 1) {
					if (model.wonByCaptures()) {
						view.hideButtons();
						JOptionPane.showMessageDialog(null, "You win!\n You captured five of your opponent's pairs!");
					}
					else if (model.wonByFive()) {
						view.hideButtons();
						JOptionPane.showMessageDialog(null, "You win!\nYou got a five in a row!");
					}
				}
			}
			view.setYourCapturesText(model.getYourCaptures());
			view.setOpponentCapturesText(model.getOpponentCaptures());
			model.clearCapturedPairs();
			model.setLastMove(-1, -1);
			view.repaint();
			model.setYourTurn(false);
			model.incrementNumTurns();
			view.hideButtons();
		}
		else if (command.equals(PenteClientView.NEW_GAME_COMMAND)) {
			connection.send("New Game");
			view.showPanel("waitingPanel");
		}
		else if (command.equals(PenteClientView.CONNECTING_BACK_COMMAND)) {
			connection.send("Back");
			view.showPanel("welcomePanel");
			view.repaint();
		}
		else if (command.equals(PenteClientView.EXIT_COMMAND)) {
			connection.disconnect();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
		}
		else if (command.equals(PenteClientView.INSTRUCTIONS_COMMAND)) {
			view.showPanel("rulesInstructionPanel");
		}
		else if (command.equals(PenteClientView.RULES_BACK_COMMAND)) {
			view.showPanel("welcomePanel");
		}
		else if (command.equals(PenteClientView.NEXT_COMMAND)) {
			view.showPanel("exampleCapturesPanel");
		}
		else if (command.equals(PenteClientView.PREVIOUS_COMMAND)) {
			view.showPanel("rulesInstructionPanel");
		}
		else if (command.equals(PenteClientView.DONE_COMMAND)) {
			view.showPanel("welcomePanel");
		}
	}
	
	private class PenteClientConnection extends Client {
		
		public PenteClientConnection(String host, int port) throws IOException {
			super(host, port);
		}

		@Override
		protected void messageReceived(Object message) {
			if (message instanceof int[]) {
				updateGame((int[]) message);
			}
			if (message instanceof String) {
				if (message.equals("disconnected")) {
					if (!model.inGame()) {
						return;
					}
					JOptionPane.showMessageDialog(null, "Your opponent disconnected");
					model.reset();
					view.setGrid(model.getGrid());
					view.setYourCapturesText(0);
					view.setOpponentCapturesText(0);
					view.repaint();
					view.showPanel("welcomePanel");
				}
				else if (message.equals("yourTurn")) {
					model.reset();
					model.setInGame(true);
					view.showPanel("gamePanel");
					model.setYouWentFirst(true);
					model.setYourTurn(true);
					JOptionPane.showMessageDialog(null, "You go first. Make your move.");
					view.requestFocus();
					view.showButtons();
				}
				else if (message.equals("waitYourTurn")) {
					model.reset();
					model.setInGame(true);
					view.setGrid(model.getGrid());
					view.showPanel("gamePanel");
					model.setYouWentFirst(false);
					model.setYourTurn(false);
					JOptionPane.showMessageDialog(null, "Your opponent gets the first move.");
					view.requestFocus();
					view.hideButtons();
				}
			}
			
		}
		
	}
	
	public void updateGame(int[] playerTile) {
		view.showButtons();
		model.addPlayerTile(playerTile[0], playerTile[1], 2);
		model.checkForCapturedPair(playerTile[0], playerTile[1]);
		model.updateCaptures();
		view.setYourCapturesText(model.getYourCaptures());
		view.setOpponentCapturesText(model.getOpponentCaptures());
		model.clearCapturedPairs();
		model.checkForWinner();
		model.setYourTurn(true);
		view.setGrid(model.getGrid());
		view.repaint();
		if (model.isWinner()) {
			model.setInGame(false);
			if (model.getWinner() == 2) {
				if (model.wonByCaptures()) {
					JOptionPane.showMessageDialog(null, "You lost!\nYour opponent captured five pairs!");
					view.hideButtons();
				}
				else if (model.wonByFive()) {
					JOptionPane.showMessageDialog(null, "You lost!\nYour opponent got a five in a row!");
					view.hideButtons();
				}
				
			}
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (model.inGame()) {
			connection.send("disconnected");
			connection.disconnect();
		}
		else {
			view.dispose();
			connection.disconnect();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}	
}
