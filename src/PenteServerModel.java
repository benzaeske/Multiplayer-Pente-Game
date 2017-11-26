import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


public class PenteServerModel {
	
	private ConcurrentHashMap<Integer, Integer> currentMatches
		= new ConcurrentHashMap<Integer, Integer>();
	
	private LinkedBlockingQueue<Integer> waitingPlayers
		= new LinkedBlockingQueue<Integer>();
	
	public void addPlayerToQueue(int playerID) throws InterruptedException {
		currentMatches.remove(playerID);
		try {
			waitingPlayers.put(playerID);
		}
		catch (InterruptedException e) {
			
		}
	}
	
	public void makeMatch(int firstPlayer, int secondPlayer) {
		if (!currentMatches.containsKey(firstPlayer))
			currentMatches.put(firstPlayer, secondPlayer);
		if (!currentMatches.containsKey(secondPlayer))
			currentMatches.put(secondPlayer, firstPlayer);
	}
	
	public int getOpponent(int playerID) {
		return currentMatches.get(playerID);
	}
	
	public int getNextPlayer() throws InterruptedException {
		return waitingPlayers.take();
	}
	
	public void remove(int playerID) {
		currentMatches.remove(playerID);
	}
	
	public void removeFromQueue(int playerID) {
		waitingPlayers.remove(playerID);
	}
	
	public int waitingPlayersLength() {
		return waitingPlayers.size();
	}
	
	public boolean inGame(int playerID) {
		return (currentMatches.containsKey(playerID));
	}
	
	public boolean isWaiting(int playerID) {
		return (waitingPlayers.contains(playerID));
	}

}
