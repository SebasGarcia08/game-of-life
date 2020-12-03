package model;

public class Cell {

	private int i;
	private int j;
	private boolean status;

	public Cell(int i, int j, boolean status) {
		this.i = i;
		this.j = j;
		this.status = status;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public boolean status() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	

}
