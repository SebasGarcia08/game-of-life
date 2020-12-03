package model;

public class GameManager {

	private Game game;
	private int generation;
	private long chronometer;
	
	public GameManager() {
		game = new LifeGame(200, 200); //200, 200
		chronometer = 0;
		generation = 0;
	}
	
	public boolean[][] next() {
		generation += 1;
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
	
	public boolean reset() {
		this.generation = 0;
		this.chronometer = 0;
		return game.reset();
	}
	
	public String getChronometer() {
		
		long seconds = (chronometer / 1000) % 60;
		long minutes = (chronometer / 60000) % 60;
		String sSec = seconds < 10 ? ("0" + seconds) : ("" + seconds);
		String sMin = minutes < 10 ? ("0" + minutes) : ("" + minutes);

		return sMin + ":" + sSec;
	}
	
	public void addChronometer(long time) {
		this.chronometer += time;
	}
	
	public int getGeneration() {
		return generation;
	}
	
}
