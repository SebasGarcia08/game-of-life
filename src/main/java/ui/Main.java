package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	Parent root;
	double xOffset, yOffset;

	@Override
	public void start(Stage primaryStage) {
		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/game.fxml"));
			Scene scene = new Scene(root);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setFullScreen(true);
			primaryStage.setScene(scene);
			primaryStage.setTitle("The Game Of Life");
			primaryStage.show();
			scene.setFill(Color.TRANSPARENT);
			
			root.setOnMousePressed((event) -> {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			});
			

			root.setOnMouseDragged((event) -> {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);

			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}