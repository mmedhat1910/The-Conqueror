package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameView extends Application  implements EventHandler<ActionEvent>{
	private Scene startGame;
	
	private BorderPane landingPane;
	private Stage stage;
	private Image logo;
	private TextField nameField;
	private Button startbtn;
	private BorderPane pane;
	private Scene game;
	
	public Scene getStartGame() {
		return startGame;
	}
	public BorderPane getLandingPane() {
		return landingPane;
	}
	public Stage getStage() {
		return stage;
	}
	public String getplayerName() {
		return nameField.getText();
	}
	@Override
	public void start(Stage primaryStage) throws Exception  {
		this.stage =  new Stage();
		this.landingPane =new BorderPane();
		this.pane = new BorderPane();
		this.startGame = new Scene(landingPane);
		this.game = new Scene(pane);
		
		this.logo = new Image("file:resources/images/logo/logo.png");
		this.nameField = new TextField();
		this.startbtn = new Button();
		startbtn.setText("Start Game");
		startbtn.setOnAction(this);
		
		ImageView logoView = new ImageView();
		stage.getIcons().add(logo);
		stage.setWidth(1920);
		stage.setHeight(1080);

		

		nameField.setPrefHeight(50);
		nameField.setMaxWidth(0.3*stage.getWidth());
		nameField.setPromptText("Enter your name");
		
		nameField.getStyleClass().add("name-field");

		
		HBox topBox = new HBox();
		VBox vbox = new VBox();
		logoView.setImage(logo);
		startGame.getStylesheets().add("file:resources/styles/main.css");
		landingPane.setTop(topBox);

		vbox.getChildren().add(logoView);
		vbox.getChildren().add(nameField);
		vbox.getChildren().add(startbtn);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(30);
		
		landingPane.setCenter(vbox);
		
		
		stage.setTitle("The Conqueror");
		
//		stage.setFullScreen(true);
//		stage.setResizable(false);
		stage.setScene(startGame);
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void handle(ActionEvent e) {
		if(!nameField.getText().equals(""))
			System.out.println(nameField.getText());
		else
			System.out.println("Enter a valid name");
//		pane.setCenter(new Label(nameField.getText()));
//		stage.setScene(game);
	}
	
}
