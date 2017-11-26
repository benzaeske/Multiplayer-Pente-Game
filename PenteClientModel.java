import java.util.ArrayList;


public class PenteClientModel {
	
	private int[][] grid; // Grid of player tiles. 0 = no tile, 1 = yellow tile, 2 = green tile.
	
	private int[] lastMove; // Holds the coordinate pair of the last move,
								// and -1 if no previous move exists.
	
	private ArrayList<int[]> capturedPairs;
	
	private boolean isYourTurn; // True if it is your turn.
	
	private int winner; // 0 if no winner, 1 = yellow winner, 2 = green winner
	
	private int yourCaptures;
	
	private boolean wonByFive;
	
	private boolean wonByCaptures;
	
	private int opponentCaptures;
	
	private boolean youWentFirst;
	
	private int numTurns = 0;
	
	private boolean inGame;
	
	public PenteClientModel() {
		grid = new int[19][19];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = 0;
			}
		}
		lastMove = new int[2];
		for (int i = 0; i < lastMove.length; i++) {
			lastMove[i] = -1;
		}
		capturedPairs = new ArrayList<int[]>();
		isYourTurn = true;
		winner = 0;
		yourCaptures = 0;
		opponentCaptures = 0;
		wonByFive = false;
		wonByCaptures = false;
		inGame = false;
	}
	
	public void reset() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = 0;
			}
		}
		for (int i = 0; i < lastMove.length; i++) {
			lastMove[i] = -1;
		}
		winner = 0;
		capturedPairs.clear();
		yourCaptures = 0;
		opponentCaptures = 0;
		wonByFive = false;
		wonByCaptures = false;
	}
	
	public boolean isWinner() {
		return(winner != 0);
	}
	
	public int getWinner() {
		return winner;
	}
	
	public void checkForWinner() {
		if (yourCaptures >= 5) {
			winner = 1;
			wonByCaptures = true;
		}
		if (opponentCaptures >= 5) {
			winner = 2;
			wonByCaptures = true;
		}
		checkRows();
		checkCols();
		checkUpperDiagonals();
		checkLowerDiagonals();
	}
	
	public void checkRows() {
		for (int i = 0; i < 18; i++) {
			for (int j = 0; j < 18; j++) {
				if (grid[i][j] != 0) {
					int count = 0;
					int type = grid[i][j];
					int row = i;
					int col = j;
					while (grid[row][col] == type) {
						count++;
						row++;
						if (row > 18 || col > 18)
							break;
					}
					if (count >= 5) {
						winner = type;
						wonByFive = true;
					}
				}
			}
		}
	}
	
	public void checkCols() {
		for (int i = 0; i < 18; i++) {
			for (int j = 0; j < 18; j++) {
				if (grid[i][j] != 0) {
					int count = 0;
					int type = grid[i][j];
					int row = i;
					int col = j;
					while (grid[row][col] == type) {
						count++;
						col++;
						if (col > 18 || row > 18)
							break;
					}
					if (count >= 5) {
						winner = type;
						wonByFive = true;
					}
				}
			}
		}
	}
	
	public void checkLowerDiagonals() {
		for (int i = 0; i < 15; i++) {
			for (int j = i, k = 18; j < 18 && k >= 0; j++, k--) {
				if (grid[j][k] != 0) {
					int row = j;
					int col = k;
					int type = grid[j][k];
					int count = 0;
					while(grid[row][col] == type) {
						count++;
						row++;
						col--;
						if (row > 18 || col < 0)
							break;
					}
					if (count >= 5) {
						winner = type;
						wonByFive = true;
					}
				}
			}
		}
		for (int i = 18; i >= 4; i--) {
			for (int j = i, k = 18; j >= 0 && k >= 0; j--, k--) {
				if (grid[j][k] != 0) {
					int row = j;
					int col = k;
					int type = grid[j][k];
					int count = 0;
					while(grid[row][col] == type) {
						count++;
						row--;
						col--;
						if (row < 0 || col < 0)
							break;
					}
					if (count >= 5) {
						winner = type;
						wonByFive = true;
					}
				}
			}
		}
	}

	public void checkUpperDiagonals() {
		for (int i = 17; i >= 4; i--) {
			for (int j = 0, k = i; j < 18 && k >= 0; j++, k--) {
				if (grid[j][k] != 0) {
					int type = grid[j][k];
					int row = j;
					int col = k;
					int count = 0;
					while (grid[row][col] == type) {
						count++;
						row++;
						col--;
						if (row > 18 || col < 0) {
							break;
						}
					}
					if (count >= 5) {
						winner = type;
						wonByFive = true;
					}
				}
			}
		}
		for (int i = 17; i >= 0; i--) {
			for (int j = 18, k = i; j >= 0 && k >= 0; j--, k--) {
				if (grid[j][k] != 0) {
					int type = grid[j][k];
					int count = 0;
					int row = j;
					int col = k;
					while (grid[row][col] == type) {
						count++;
						row--;
						col--;
						if (row < 0 || col < 0)
							break;
					}
					if (count >= 5) {
						winner = type;
						wonByFive = true;
					}
				}
			}
		}
	}
	
	public void checkForCapturedPair(int x, int y) {
		int type = grid[x][y];
		if (!(x + 3 > 18)) {
			if ((grid[x + 1][y] == 3 - type) && (grid[x + 2][y] == 3 - type) 
					&& (grid[x + 3][y] == type)) {
				capturedPairs.add(new int[]{x + 1, y, 3 - type});
				capturedPairs.add(new int[]{x + 2, y, 3 - type});
				removePlayerTile(x + 1, y);
				removePlayerTile(x + 2, y);
			}
		}
		if (!(x - 3 < 0)) {
			if ((grid[x - 1][y] == 3 - type) && (grid[x - 2][y] == 3 - type) 
					&& grid[x - 3][y] == type) {
				capturedPairs.add(new int[]{x - 1, y, 3 - type});
				capturedPairs.add(new int[]{x - 2, y, 3 - type});
				removePlayerTile(x - 1, y);
				removePlayerTile(x - 2, y);
			}
		}
		if (!(y + 3 > 18)) {
			if (grid[x][y + 1] == 3 - type && grid[x][y + 2] == 3 - type 
					&& grid[x][y + 3] == type) {
				capturedPairs.add(new int[]{x, y + 1, 3 - type});
				capturedPairs.add(new int[]{x, y + 2, 3 - type});
				removePlayerTile(x, y + 1);
				removePlayerTile(x, y + 2);
			}
		}
		if (!(y - 3 < 0)) {
			if (grid[x][y - 1] == 3 - type && grid[x][y - 2] == 3 - type && grid[x][y - 3] == type) {
				capturedPairs.add(new int[]{x, y - 1, 3 - type});
				capturedPairs.add(new int[] {x, y - 2, 3 - type});
				removePlayerTile(x, y - 1);
				removePlayerTile(x, y - 2);
			}
		}
		if (!(x + 3 > 18 || y + 3 > 18)) {
			if (grid[x + 1][y + 1] == 3 - type && grid[x + 2][y + 2] == 3 - type && grid[x + 3][y + 3] == type) {
				capturedPairs.add(new int[]{x + 1, y + 1, 3 - type});
				capturedPairs.add(new int[] {x + 2, y + 2, 3 - type});
				removePlayerTile(x + 1, y + 1);
				removePlayerTile(x + 2, y + 2);
			}
		}
		if (!(x - 3 < 0 || y - 3 < 0)) {
			if (grid[x - 1][y - 1] == 3 - type && grid[x - 2][y - 2] == 3 - type && grid[x - 3][y - 3] == type) {
				capturedPairs.add(new int[]{x - 1, y - 1, 3 - type});
				capturedPairs.add(new int[] {x - 2, y - 2, 3 - type});
				removePlayerTile(x - 1, y - 1);
				removePlayerTile(x - 2, y - 2);
			}
		}
		if (!(x + 3 > 18 || y - 3 < 0)) {
			if (grid[x + 1][y - 1] == 3 - type && grid[x + 2][y - 2] == 3 - type && grid[x + 3][y - 3] == type) {
				capturedPairs.add(new int[]{x + 1, y - 1, 3 - type});
				capturedPairs.add(new int[] {x + 2, y - 2, 3 - type});
				removePlayerTile(x + 1, y - 1);
				removePlayerTile(x + 2, y - 2);
			}
		}
		if (!(x - 3 < 0 || y + 3 > 18)) {
			if (grid[x - 1][y + 1] == 3 - type && grid[x - 2][y + 2] == 3 - type && grid[x - 3][y + 3] == type) {
				capturedPairs.add(new int[]{x - 1, y + 1, 3 - type});
				capturedPairs.add(new int[] {x - 2, y + 2, 3 - type});
				removePlayerTile(x - 1, y + 1);
				removePlayerTile(x - 2, y + 2);
			}
		}
	}
	
	public void removePlayerTile(int x, int y) {
		grid[x][y] = 0;
	}
	
	public boolean addPlayerTile(int x, int y, int type) {
		if (grid[x][y] == 0) {
			grid[x][y] = type;
			return true;
		}
		else {
			return false;
		}
	}
	
	public void yourTurn() {
		isYourTurn = true;
	}
	
	public int playerTileCount() {
		int tileCount = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] != 0) {
					tileCount++;
				}
			}
		}
		return tileCount;
	}
	
	public int[] getLastMove() {
		return lastMove;
	}
	
	public void setLastMove(int x, int y) {
		this.lastMove[0] = x;
		this.lastMove[1] = y;
	}
	
	public int[][] getGrid() {
		return grid;
	}
	
	public boolean isYourTurn() {
		return isYourTurn;
	}
	
	public void setYourTurn(boolean turn) {
		this.isYourTurn = turn;
	}
	
	public ArrayList<int[]> getCapturedPairs() {
		return capturedPairs;
	}
	
	public void setYourCaptures(int numPairs) {
		yourCaptures = numPairs;
	}
	
	public void setOpponentCaptures(int numPairs) {
		opponentCaptures = numPairs;
	}
	
	public int getYourCaptures() {
		return yourCaptures;
	}
	
	public int getOpponentCaptures() {
		return opponentCaptures;
	}
	
	public void clearCapturedPairs() {
		capturedPairs.clear();
	}
	
	public void incrementNumTurns() {
		numTurns++;
	}
	
	public int numTurns() {
		return numTurns;
	}
	
	public void setYouWentFirst(boolean youWentFirst) {
		this.youWentFirst = youWentFirst;
	}
	
	public boolean youWentFirst() {
		return youWentFirst;
	}
	
	public void updateCaptures() {
		for (int i = 0; i < capturedPairs.size(); i += 2) {
			int[] pair = capturedPairs.get(i);
			if (pair[2] == 2) {
				yourCaptures++;
			}
			else if (pair[2] == 1) {
				opponentCaptures++;
			}
		}
	}
	
	public boolean wonByCaptures() {
		return wonByCaptures;
	}
	
	public boolean wonByFive() {
		return wonByFive;
	}
	
	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
	
	public boolean inGame() {
		return inGame;
	}
	
}
