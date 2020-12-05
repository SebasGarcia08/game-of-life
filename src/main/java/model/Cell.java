package model;

public class Cell {

	private int i;
	private int j;
	private boolean status;
	private int[] coords;

	public Cell(int i, int j, boolean status) {
		this.i = i;
		this.j = j;
		this.coords = new int[] {i, j};
		this.status = status;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
		this.coords[0] = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
		this.coords[1] = j;
	}

	public boolean status() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public int[] getCoords() {
		return this.coords;
	}
}
