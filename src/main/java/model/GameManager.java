package model;

public class GameManager {

	private Game game;

	public GameManager() {
		game = new LifeGame(10, 10);
	}
	
	public boolean[][] next() {
		return game.next();
	}

	public boolean check(int i, int j) {
		return this.game.check(i, j);
	}

	public boolean uncheck(int i, int j) {
		return this.game.uncheck(i, j);
	}

	public boolean[][] getState(){
		return this.game.getState();
	}

}
