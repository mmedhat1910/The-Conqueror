package view;

import java.util.ArrayList;

import controllers.ControlListener;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameView extends Stage implements ControlListener{
	private Scene gameScene;
	private GameViewListener listener;
	

	private String playerName;
	private String playerCity;
	private Image logo;
	private PlayerNamePane playerNamePane;
	private ChooseCityPane chooseCitypane;
	private GamePane gamePane;
	private ArrayList<String> availableCities; 
	private double width;
	private double height;
	
	
	private double food;
	private double treasury;
	private int turnCount;

	public GameViewListener getListener() {
		return listener;
	}

	public void setListener(GameViewListener listener) {
		this.listener = listener;
	}
	
	public double getFood() {
		return food;
	}

	public void setFood(double food) {
		this.food = food;
	}

	public double getTreasury() {
		return treasury;
	}

	public void setTreasury(double treasury) {
		this.treasury = treasury;
	}

	public int getTurnCount() {
		return turnCount;
	}

	public void setTurnCount(int turnCount) {
		this.turnCount = turnCount;
	}


	
	public double getStageWidth() {
		return this.width;
	}

	public double getStageHeight() {
		return this.height;
	}

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


	public Image getLogo() {
		return logo;
	}


	public void setLogo(Image logo) {
		this.logo = logo;
	}


	public PlayerNamePane getPlayerNamePane() {
		return playerNamePane;
	}


	public void setPlayerNamePane(PlayerNamePane playerNamePane) {
		this.playerNamePane = playerNamePane;
	}


	public ChooseCityPane getChooseCitypane() {
		return chooseCitypane;
	}


	public void setChooseCitypane(ChooseCityPane chooseCitypane) {
		this.chooseCitypane = chooseCitypane;
	}


	public ArrayList<String> getAvailableCities() {
		return availableCities;
	}


	public void setAvailableCities(ArrayList<String> availableCities) {
		this.availableCities = availableCities;
	}


	public void setPane(Pane pane) {
		this.getScene().setRoot(pane);
	}
	
	
	public GameView(double width, double height, boolean fullScreen) {

		this.width = width;
		this.height = height;
		this.playerNamePane = new PlayerNamePane(this);
		this.chooseCitypane = new ChooseCityPane(this);
		this.gameScene = new Scene(playerNamePane);
		this.logo = new Image("file:resources/images/logo/logo.png");
		
		
		this.getIcons().add(logo);
		this.setWidth(width);
		this.setHeight(height);
		this.setTitle("The Conqueror");
		gameScene.getStylesheets().add("file:resources/styles/main.css");

		
		this.setFullScreen(fullScreen);
//		stage.setResizable(false);
		this.setScene(gameScene);
		this.show();	
	}

	@Override
	public void startGame() {
		this.gamePane = new GamePane(this);
		updateGamePane();
//		System.out.println(this.getTreasury());
	}
	
	public void updateGamePane() {
		setPane(gamePane);
	}

	
	
	public GamePane getGamePane() {
		return gamePane;
	}

	

	
	
	
	

	
	
}
