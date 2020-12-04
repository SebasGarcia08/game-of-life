package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Cell;
import model.GameManager;

public class GameUIController implements Initializable {

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private FontAwesomeIconView PlayButton;

	@FXML
	private Canvas canvas;

	@FXML
	private Text chronometer;

	@FXML
	private JFXButton showBtn;

	@FXML
	private JFXButton hideBtn;

	@FXML
	private AnchorPane configPane;

	@FXML
	private StackPane configContainer;

	@FXML
	private JFXSlider speedSlider;

	@FXML
	private Text numGenerations;

	@FXML
	private JFXToggleButton minimumPath;

	public static final int SPEED_MIN = 100;
	public static final int SPEED_INC = 1000;

	private GameManager gm;
	private int currentBoxWidth = 2;
	public static final int[] BOX_WIDTHS = { 15, 20, 30, 40, 50 };
	public static final char[] DIRECTIONS = { 'L', 'R', 'U', 'D' };
	GraphicsContext gc;
	boolean clickCooldownActive = false;
	boolean[][] grid;
	boolean playStatus = false;
	long time1;
	long time2;
	private int speed;
	boolean initScrollPane = false;

	private double vValue;
	private double hValue;

	public static final double GRID_LINE_SIZE = 1;

	private ArrayList<Cell> pathCells;

	public GameUIController() {
		gm = new GameManager();
		grid = gm.getState();
		pathCells = new ArrayList<Cell>();

		vValue = 0.5;
		hValue = 0.5;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		configContainer.setVisible(false);

		initializeScrollPane();
		initCanvasClickEvent();
		initSpeedSliderListener();

		updateSpeed();

		gc = canvas.getGraphicsContext2D();
		drawCanvas(gc, grid, BOX_WIDTHS[currentBoxWidth]);

	}

	@FXML
	void minimunPathActivated(ActionEvent event) {

		if (!minimumPath.isSelected()) {

			for (Cell cell : pathCells) {

				Color color;

				if (grid[cell.getI()][cell.getJ()] == false) {
					color = new Color(47 / 255.0, 48 / 255.0, 48 / 255.0, 1.0);
				} else {
					color = Color.WHITE;
				}

				paintCell(cell.getI(), cell.getJ(), gc, BOX_WIDTHS[currentBoxWidth], color);
			}

			pathCells = new ArrayList<Cell>();

		}
	}

	public void paintCell(int i, int j, GraphicsContext gc, double boxWidth, Color color) {

		gc.setFill(color);
		gc.fillRect((i * boxWidth) + GRID_LINE_SIZE, (j * boxWidth) + GRID_LINE_SIZE, boxWidth - GRID_LINE_SIZE,
				boxWidth - GRID_LINE_SIZE);
	}

	public void updateSpeed() {

		double multiplier = speedSlider.getValue() / 100;

		speed = (int) (SPEED_MIN + SPEED_INC * (1 - multiplier));

	}

	@FXML
	public void showConfigPane(ActionEvent event) {
		showBtn.setVisible(false);
		configContainer.setVisible(true);
	}

	@FXML
	public void hideConfigPane(ActionEvent event) {
		configContainer.setVisible(false);
		showBtn.setVisible(true);
	}

	@FXML
	void maximizeWindow(ActionEvent event) {
		Stage stage = (Stage) canvas.getScene().getWindow();
		if (stage.isFullScreen())
			stage.setFullScreen(false);
		else
			stage.setFullScreen(true);

	}

	@FXML
	void minimizeWindow(ActionEvent event) {
		Stage stage = (Stage) canvas.getScene().getWindow();
		stage.setIconified(true);
	}

	@FXML
	void exit(ActionEvent event) {
		System.exit(0);
	}

	public void slide(Pane root, double yPropertyValue, double endValue, double durationSecs,
			EventHandler<ActionEvent> e) {
		root.translateYProperty().set(yPropertyValue);
		Timeline timeline = new Timeline();
		KeyValue kv1 = new KeyValue(root.translateYProperty(), endValue, Interpolator.EASE_BOTH);
		KeyFrame kf2 = new KeyFrame(Duration.seconds(durationSecs), kv1);
		timeline.getKeyFrames().add(kf2);
		if (e != null)
			timeline.setOnFinished(e);
		timeline.play();
	}

	@FXML
	void step(ActionEvent event) {
		grid = gm.next();
		drawCanvas(gc, grid, BOX_WIDTHS[currentBoxWidth]);
	}

	@FXML
	void reset(ActionEvent event) {
		gm.reset();
		grid = gm.getState();

		numGenerations.setText(gm.getGeneration() + "");
		chronometer.setText(gm.getChronometer());

		drawCanvas(gc, grid, BOX_WIDTHS[currentBoxWidth]);
	}

	@FXML
	void play(ActionEvent event) {

		if (playStatus == false) {
			playStatus = true;
			PlayButton.setGlyphName("PAUSE");

			time1 = System.currentTimeMillis();

			new Thread() {
				public void run() {

					while (playStatus) {
						time2 = System.currentTimeMillis();
						gm.addChronometer(time2 - time1);
						numGenerations.setText(gm.getGeneration() + "");
						chronometer.setText(gm.getChronometer());

						time1 = System.currentTimeMillis();

						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			}.start();

			new Thread() {
				public void run() {
					while (playStatus) {
						gm.next();
						grid = gm.getState();
						drawCanvas(gc, grid, BOX_WIDTHS[currentBoxWidth]);

						try {
							Thread.sleep(speed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

		} else {
			playStatus = false;
			PlayButton.setGlyphName("PLAY");

		}

	}

	public void initializeScrollPane() {
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.requestFocus();
		scrollPane.setPannable(true);
		scrollPane.setHvalue(0.5);
		scrollPane.setVvalue(0.5);

		initSrollPaneDragEvent();
		initScollPaneZoomEvent();

	}

	public void initSpeedSliderListener() {

		speedSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				updateSpeed();
			}

		});
	}

	public void drawGrid(GraphicsContext gc, int xBoxes, int yBoxes, double boxWidth, Color color) {

		gc.setFill(color);
		for (int x = 0; x <= xBoxes; x++) {

			gc.fillRect(x * boxWidth, 0, GRID_LINE_SIZE, gc.getCanvas().getHeight());

		}

		for (int y = 0; y <= yBoxes; y++) {
			gc.fillRect(0, y * boxWidth, gc.getCanvas().getWidth(), GRID_LINE_SIZE);
		}

	}

	public void drawCanvas(GraphicsContext gc, boolean[][] array, double boxWidth) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

				gc.getCanvas().setWidth(array.length * boxWidth);
				gc.getCanvas().setHeight(array[0].length * boxWidth);

				Color customGray = new Color(47 / 255.0, 48 / 255.0, 48 / 255.0, 1.0);
				drawBoxes(gc, array, boxWidth, customGray, Color.WHITE);

				Color minPointsColor = new Color(76 / 255.0, 175 / 255.0, 80 / 255.0, 1.0);
				drawMinimunPathPoints(gc, boxWidth, minPointsColor);

				drawGrid(gc, array.length, array[0].length, boxWidth, Color.WHITE);

				scrollPane.setVvalue(vValue);
				scrollPane.setHvalue(hValue);

			}
		});

	}

	public void drawMinimunPathPoints(GraphicsContext gc, double boxWidth, Color pathColor) {

		if (minimumPath.isSelected()) {

			for (Cell cell : pathCells) {

				gc.setFill(pathColor);
				gc.fillRect(cell.getI() * boxWidth, cell.getJ() * boxWidth, boxWidth, boxWidth);

			}
		}
	}

	public void drawBoxes(GraphicsContext gc, boolean[][] array, double boxWidth, Color defaultColor,
			Color activeColor) {

		for (int i = 0; i < array.length; i++) {

			for (int j = 0; j < array[0].length; j++) {

				if (array[i][j] == false) {
					gc.setFill(defaultColor);
				} else {
					gc.setFill(activeColor);
				}

				gc.fillRect(i * boxWidth, j * boxWidth, boxWidth, boxWidth);

			}

		}
	}

	public void initCanvasClickEvent() {
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (!clickCooldownActive) {

					if (event.getButton() == MouseButton.SECONDARY && minimumPath.isSelected()) {

						int x = (int) (event.getX() / BOX_WIDTHS[currentBoxWidth]);
						int y = (int) (event.getY() / BOX_WIDTHS[currentBoxWidth]);

						if(!grid[x][y]) {
							
							boolean found = false;

							for (int i = 0; i < pathCells.size(); i++) {

								Cell curr = pathCells.get(i);

								if (curr.getI() == x && curr.getJ() == y) {
									pathCells.remove(i);
									found = true;
									break;
								}
							}

							if (!found && pathCells.size() < 2) {
								pathCells.add(new Cell(x, y, true));

							}
						}
						
					} else {

						int x = (int) (event.getX() / BOX_WIDTHS[currentBoxWidth]);
						int y = (int) (event.getY() / BOX_WIDTHS[currentBoxWidth]);
						
						if(!existPathPoint(x, y)) {
							grid[x][y] = grid[x][y] == true ? false : true;

							if (grid[x][y] == true) {
								gm.check(x, y);
							} else {
								gm.uncheck(x, y);
							}
						}
						
					}

					drawCanvas(gc, grid, BOX_WIDTHS[currentBoxWidth]);
				}

			}

		});
	}
	
	public boolean existPathPoint(int i, int j) {
		
		boolean found = false;
		
		for(int z=0; z<pathCells.size(); z++) {
			
			Cell curr = pathCells.get(z);
			if(curr.getI() == i && curr.getJ() == j) {
				found = true;
				break;
			}
			
		}
		
		return found;
		
	}

	public void initSrollPaneDragEvent() {
		scrollPane.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
					clickCooldownActive = true;

					new Thread() {
						public void run() {

							while (clickCooldownActive) {

								hValue = scrollPane.getHvalue();
								vValue = scrollPane.getVvalue();

								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {

									e.printStackTrace();
								}
							}

						}
					}.start();

				}

				if (clickCooldownActive && event.getEventType() == MouseEvent.MOUSE_RELEASED) {

					new Thread() {
						public void run() {
							try {
								Thread.sleep(10);
								clickCooldownActive = false;
							} catch (InterruptedException e) {

								e.printStackTrace();
							}
						}
					}.start();

				}

			}
		});
	}

	public void initScollPaneZoomEvent() {
		scrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {

				boolean changed = false;

				if (event.getDeltaY() < 0 && currentBoxWidth > 0) {
					changed = true;
					currentBoxWidth--;

				} else if (event.getDeltaY() > 0 && currentBoxWidth < BOX_WIDTHS.length - 2) {
					changed = true;
					currentBoxWidth++;
				}

				if (changed) {
					double vValue = scrollPane.getVvalue();
					double hValue = scrollPane.getHvalue();

					drawCanvas(gc, grid, BOX_WIDTHS[currentBoxWidth]);

					scrollPane.setVvalue(vValue);
					scrollPane.setHvalue(hValue);
				}

				event.consume();
			}

		});
	}

	public boolean isValid(char[] moves, Cell start) {
		int i = start.getI();
		int j = start.getJ();
		for (int idx = 0; idx < moves.length; idx++) {
			char move = moves[idx];
			if (move == 'L')
				i -= 1;
			else if (move == 'R')
				i += 1;
			else if (move == 'U')
				j -= 1;
			else if (move == 'D')
				j += 1;
			if (!(0 <= i && i < grid[0].length && 0 <= j && j < grid.length))
				return false;
			else if (grid[j][i]) // Obstacle found
				return false;
			// paint here
		}
		return true;
	}

	public boolean findEnd(char[] moves, Cell start, Cell end) {
		int i = start.getI();
		int j = start.getJ();

		for (int idx = 0; idx < moves.length; idx++) {
			char move = moves[i];
			if (move == 'L')
				i -= 1;
			else if (move == 'R')
				i += 1;
			else if (move == 'U')
				j -= 1;
			else if (move == 'D')
				j += 1;
		}

		return i == end.getI() && j == end.getJ();
	}

	public void shortestPath(Cell start, Cell end) {
		Queue<String> queue = new LinkedList<>();
		queue.add("");
		String add = "";
		while (!findEnd(add.toCharArray(), start, end)) {
			add = queue.poll();
			for (int i = 0; i < DIRECTIONS.length; i++) {
				String put = add + DIRECTIONS[i];
				if (isValid(put.toCharArray(), start)) {
					queue.add(put);
				}
			}
		}
	}
}