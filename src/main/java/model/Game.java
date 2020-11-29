package model;

public interface Game {
	
	public boolean[][] next();
	
	public boolean check(int i, int j);
	
	public boolean uncheck(int i, int j);
	
	public boolean[][] getState();
	
}
