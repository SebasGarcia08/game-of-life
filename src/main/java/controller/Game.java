package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game implements Initializable {

	@FXML
	private Canvas canvas;

	@FXML
	private JFXButton configBtn;

	private Pane configs;

	@FXML
	private StackPane configContainer;

	public Game() {
		try {
			configs = (Pane) FXMLLoader.load(getClass().getResource("/fxml/options.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		configContainer.getChildren().add(configs);
		configContainer.setVisible(false);
		
		slide(configs, 100, 0, 0.5, ef -> configs.setVisible(false));
				
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> System.out.println("Canvas pressed"));
		
		configBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			configBtn.setVisible(false);
			Scene scene = configBtn.getScene();
			System.out.println("ON");
			configContainer.setVisible(true);
			
			int newY = (int) scene.getHeight() / 4;
			configs.setVisible(true);
			slide(configs, 0, newY, 0.5, ef -> {
				configBtn.setLayoutY(scene.getHeight() - newY);
				configBtn.setVisible(true);
			});
		});

		configs.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			Scene scene = configBtn.getScene();
			System.out.println("OFF");
			configContainer.setVisible(false);
			slide(configs, scene.getHeight() / 4, 0, 0.5, ef -> configs.setVisible(false));
			configBtn.setLayoutY(scene.getHeight());
		});

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

	private void slide(Pane root, double yPropertyValue, double endValue, double durationSecs,
			EventHandler<ActionEvent> e) {
		root.translateYProperty().set(yPropertyValue);
		Timeline timeline = new Timeline();
		KeyValue kv1 = new KeyValue(root.translateYProperty(), endValue, Interpolator.EASE_IN);
		KeyFrame kf2 = new KeyFrame(Duration.seconds(durationSecs), kv1);
		timeline.getKeyFrames().add(kf2);
		if (e != null)
			timeline.setOnFinished(e);
		timeline.play();
	}
}