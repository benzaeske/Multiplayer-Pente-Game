import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import netgame.common.Hub;


public class PenteServerController extends Hub {
	
	private PenteServerModel model;
	private boolean running;

	public PenteServerController(int port) throws IOException {
		super(port);
		model = new PenteServerModel();
		setAutoreset(true);
		new Thread() {
			public void start() {
				running = true;
				while (running) {
					try {
						int firstPlayer;
						int secondPlayer;
						if (model.waitingPlayersLength() > 1) {
							firstPlayer = model.getNextPlayer();
							secondPlayer = model.getNextPlayer();
							if (firstPlayer == secondPlayer) {
								continue;
							}
						}
						else {
							continue;
						}
						model.makeMatch(firstPlayer, secondPlayer);
						if (Math.random() * 2 < 1) {
							PenteServerController.this.sendToOne(firstPlayer, "yourTurn");
							PenteServerController.this.sendToOne(secondPlayer, "waitYourTurn");
						}
						else {
							PenteServerController.this.sendToOne(secondPlayer, "yourTurn");
							PenteServerController.this.sendToOne(firstPlayer, "waitYourTurn");
						}
					}
					catch (InterruptedException e) {
						
					}
				}
			}
		}.start();
	}
	
	@Override
	protected void messageReceived(int playerID, Object message) {
		if (message instanceof int[]) {
			PenteServerController.this.sendToOne(model.getOpponent(playerID), message);
		}
		if (message instanceof String) {
			if (message.equals("New Game")) {
				try {
					model.addPlayerToQueue(playerID);
				}
				catch (InterruptedException e) {
					return;
				}
			}
			else if (message.equals("Back")) {
				model.removeFromQueue(playerID);
			}
			else if (message.equals("disconnected")) {
				int opponent = model.getOpponent(playerID);
				model.remove(playerID);
				model.removeFromQueue(playerID);
				this.sendToOne(opponent, "disconnected");
				model.remove(opponent);
				model.removeFromQueue(opponent);
			}
			else if (message.equals("Leave Game")) {
				model.remove(playerID);
			}
			else {
				PenteServerController.this.sendToOne(model.getOpponent(playerID), message);
			}
		}
	}
	
	@Override
	protected void playerDisconnected(int playerID) {
		if (model.inGame(playerID)) {
			int opponent = model.getOpponent(playerID);
			model.remove(playerID);
			this.sendToOne(opponent, "disconnected");
		}
		if (model.isWaiting(playerID)) {
			model.removeFromQueue(playerID);
		}
    }
}
