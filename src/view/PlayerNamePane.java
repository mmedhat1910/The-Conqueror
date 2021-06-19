package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayerNamePane extends BorderPane implements EventHandler<ActionEvent> {

	private Image logo;
	private ImageView logoView;
	private TextField nameField;
	private Button nextbtn;
	private GameView gameView;
	
	public PlayerNamePane(GameView gameView) {
		this.gameView = gameView;
		this.getStyleClass().add("get-name-panel");
		
		this.logo = new  Image("file:resources/images/logo/logo3.png");
		this.logoView = new ImageView(logo);
		
		this.nameField = new TextField();
		this.nameField.setPromptText("Enter your name");
		this.nameField.setPrefHeight(50);
		this.nameField.setMaxWidth(0.3*gameView.getWidth());
		this.nameField.getStyleClass().add("name-field");
		
		this.nextbtn = new Button("Next");
		this.nextbtn.setOnAction(this);
		
		VBox vbox = new VBox();
		vbox.getChildren().add(logoView);
		vbox.getChildren().add(nameField);
		vbox.getChildren().add(nextbtn);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(30);
		this.setCenter(vbox);
	}

	@Override
	public void handle(ActionEvent arg0) {
		if(!nameField.getText().equals("")) {
			String playerName = nameField.getText();
			System.out.println(playerName);
			gameView.setPlayerName(playerName);
			System.out.println(gameView.getPlayerName());
		}
		else
			System.out.println("Enter a valid name");
		
		
	}

}
