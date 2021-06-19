package view;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameView extends Application{
	private Scene gameScene;
	private String playerName;
	private String playerCity;
	private Image logo;
	private Stage stage;
	private PlayerNamePane playerNamePane;
	private ChooseCityPane chooseCitypane;
	private ArrayList<String> availableCities;  
	
	
	private double height = 1080;
	
	
	private double width = 1920;
	
	public Scene getGameScene() {
		return gameScene;
	}
	public void setGameScene(Scene gameScene) {
		this.gameScene = gameScene;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public String getPlayerCity() {
		return playerCity;
	}
	public void setPlayerCity(String playerCity) {
		this.playerCity = playerCity;
	}
	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getWidth() {
		return width;
	}
	
	
	public void setPanel(Pane pane) {
		this.stage.getScene().setRoot(pane);
	}
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception  {
		this.stage =  new Stage();
		this.playerNamePane = new PlayerNamePane(this);
		this.chooseCitypane = new ChooseCityPane(this);
		this.gameScene = new Scene(chooseCitypane);
		this.logo = new Image("file:resources/images/logo/logo.png");
		
		
		stage.getIcons().add(logo);
		stage.setWidth(this.width);
		stage.setHeight(this.height);
		stage.setTitle("The Conqueror");
		gameScene.getStylesheets().add("file:resources/styles/main.css");

		stage.setFullScreen(true);
//		stage.setResizable(false);
		stage.setScene(gameScene);
		stage.show();	
	}
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
