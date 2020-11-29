package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class ConfigPane extends Pane {

    @FXML
    private AnchorPane configPane;

    private Game game;
    
    @FXML
    void hideConfigPane(ActionEvent event) {
    	
    }

    public void setGame(Game game) {
    	this.game = game;
    }
}
