package controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;

public class Game {
	
	@FXML
	private ScrollPane scrollPane;

	@FXML
	private Canvas canvas;

	private int currentBoxWidth = 2;
	public static final int[] BOX_WIDTHS = { 10, 20, 30, 40, 50 };
	GraphicsContext gc;
	boolean clickCooldownActive = false;
	
}