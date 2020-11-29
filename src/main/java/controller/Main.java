package controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

	private static boolean[][] grid;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private Canvas canvas;

	private int currentBoxWidth = 2;
	public static final int[] BOX_WIDTHS = { 10, 20, 30, 40, 50 };
	GraphicsContext gc;
	boolean clickCooldownActive = false;

	public static void main(String[] args) {

		grid = new boolean[200][200];

		grid[0][0] = true;
		grid[5][5] = true;

		grid[2][2] = true;

		grid[9][9] = true;

		grid[99][99] = true;

		launch(args);
	}

	@FXML
	void action1(ActionEvent event) {
		System.out.println("X: " + scrollPane.getHvalue() + " Y: " + scrollPane.getVvalue());

	}

	@FXML
	void action2(ActionEvent event) {
		scrollPane.setVvalue(1);
		scrollPane.setHvalue(1);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("window.fxml"));
		fxmlLoader2.setController(this);
		BorderPane bp = fxmlLoader2.load();

		Scene scene = new Scene(bp);

		// Initialize ScrollPane
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.requestFocus();
		scrollPane.setPannable(true);
		scrollPane.setHvalue(0.5);
		scrollPane.setVvalue(0.5);

		scrollPane.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
					clickCooldownActive = true;
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

		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (!clickCooldownActive) {
					int x = (int) (event.getX() / BOX_WIDTHS[currentBoxWidth]);
					int y = (int) (event.getY() / BOX_WIDTHS[currentBoxWidth]);

					grid[x][y] = grid[x][y] == true ? false : true;
					drawCanvas(gc, grid, BOX_WIDTHS[currentBoxWidth]);
				}

			}

		});

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

		// graphics context
		gc = canvas.getGraphicsContext2D();

		drawCanvas(gc, grid, BOX_WIDTHS[currentBoxWidth]);

		primaryStage.setTitle("Grid Testing");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void initializeScrollPane() {
		
	}
	
	public static void drawCanvas(GraphicsContext gc, boolean[][] array, double boxWidth) {

		gc.getCanvas().setWidth(array.length * boxWidth);
		gc.getCanvas().setHeight(array[0].length * boxWidth);

		Color customGray = new Color(47 / 255.0, 48 / 255.0, 48 / 255.0, 1.0);
		drawBoxes(gc, array, boxWidth, customGray, Color.WHITE);

		drawGrid(gc, array.length, array[0].length, boxWidth, Color.WHITE);

	}

	public static void drawBoxes(GraphicsContext gc, boolean[][] array, double boxWidth, Color defaultColor,
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

	public static void drawGrid(GraphicsContext gc, int xBoxes, int yBoxes, double boxWidth, Color color) {

		gc.setFill(color);
		for (int x = 0; x <= xBoxes; x++) {

			gc.fillRect(x * boxWidth, 0, 1, gc.getCanvas().getHeight());

		}

		for (int y = 0; y <= yBoxes; y++) {
			gc.fillRect(0, y * boxWidth, gc.getCanvas().getWidth(), 1);
		}

	}

//	scrollPane.addEventHandler(Event.ANY, new EventHandler<Event>() {
//	@Override
//	public void handle(Event event) {
//		
//		System.out.println(event.getEventType());
//		
//	}
//
//});

//	scrollPane.addEventHandler(DragEvent.ANY, new EventHandler<DragEvent>() {
//
//		@Override
//		public void handle(DragEvent event) {
//
//			System.out.println("EXE");
//			System.out.println(event.getEventType());
//
//		}
//
//	});

//	scrollPane.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
//
//		@Override
//		public void handle(MouseEvent event) {
//
//			if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
//				scrollPane.startDragAndDrop(TransferMode.ANY);
//				dragged = true;
//				x0 = event.getX();
//				y0 = event.getY();
//				
//				scrollPane.setPannable(true);
//				
//				System.out.println("Detected");
//				
////				new Thread() {
////					public void run() {
////						while(dragged) {
////							System.out.println("Being Dragged");
////							try {
////								currentThread().sleep(100);
////							} catch (InterruptedException e) {
////								
////								e.printStackTrace();
////							}
////							
////							double x1 = event.getX();
////							double y1 = event.getY();
////							
////							System.out.println(MouseInf);
////							
////						}
////					}
////				}.start();
////				
//
////			} else if (dragged && event.getEventType() == MouseEvent.MOUSE_MOVED) {
////
////				System.out.println("Moved");
////
////				double x1 = event.getX();
////				double y1 = event.getY();
////
////				System.out.println("X:" + (x1 - x0) + " Y:" + (y1 - y0));
////
////				x0 = x1;
////				y0 = y1;
//
//			} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
//
//				System.out.println("Release");
//				dragged = false;
//			}
//			
//			
//			
//
//		}
//
//	});

//	scrollPane.addEventHandler(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
//
//		@Override
//		public void handle(MouseEvent event) {
//			//scrollPane.startFullDrag();
//			System.out.println(event.toString());
//			System.out.println("ASS");
//		}
//
//	});

//	scrollPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
//		@Override
//		public void handle(MouseEvent event) {
//			
//			System.out.println(event.toString());
//			//event.getCode().toString().equals("ESCAPE")
//			
//			
//		}
//
//	});

}
