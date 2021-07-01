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
import javafx.scene.text.Font;

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
		this.nameField.setMaxWidth(0.3*gameView.getStageWidth());
		this.nameField.getStyleClass().add("name-field");
		
		this.nextbtn = new Button("Next");
		this.nextbtn.getStyleClass().add("start-btn");
		this.nextbtn.setPrefSize(150, 50);
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
		String playerName;
		if(nameField.getText().equals("")) 
			playerName = "Maestro";
		//TODO change this
					
		else {
			playerName = nameField.getText();
		}
		this.gameView.setPlayerName(playerName);
		System.out.println(gameView.getPlayerName());
		this.gameView.setPane(gameView.getChooseCitypane());
		
		
		
	}

}
