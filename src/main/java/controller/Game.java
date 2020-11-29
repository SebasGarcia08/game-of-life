package controller;

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
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game implements Initializable {

	@FXML
	private Canvas canvas;

	@FXML
	private JFXButton showBtn;

	@FXML
	private JFXButton hideBtn;

	@FXML
	private AnchorPane configPane;

	@FXML
	private StackPane configContainer;

	public Game() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configContainer.setVisible(false);
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

	}

	@FXML
	void reset(ActionEvent event) {

	}

	@FXML
	void play(ActionEvent event) {

	}
}