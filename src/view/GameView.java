package view;

import java.util.ArrayList;

import controllers.ControlListener;
import engine.City;
import engine.Player;
import javafx.application.Application;
import javafx.collections.MapChangeListener;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import units.Army;
import units.Unit;

public class GameView extends Stage implements ControlListener{
	private Scene gameScene;
	private GameViewListener listener;
	

	private String playerName;
	private String playerCity;
	private Image logo;
	private PlayerNamePane playerNamePane;
	private ChooseCityPane chooseCitypane;
	private GamePane gamePane;
	private ArrayList<City> availableCities; 
	private double width;
	private double height;
	private ArrayList<City> controlledCities;
	private ArrayList<Army> controlledArmies;
	private Player player;
	
	
	private double food;
	private double treasury;
	private int turnCount;

	
	
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

	public void handleInitArmy(City city,Unit unit) {
		if(this.player.getControlledArmies().size()==5) {
			gamePane.getMainPane().getChildren().add(new AlertPane(gamePane.getMainPane(), 500, 400, "Cannot Initiate more armies"));
			return;
		}
		this.listener.onInitArmy(city, unit);
		System.out.println("Initiated");
		System.out.println(getPlayer().getControlledArmies().size());
		this.controlledArmies = getPlayer().getControlledArmies();
		gamePane.displayDefendingArmy();
		gamePane.getCityView().update();
	}
	
	@Override
	public void startGame() {
		this.gamePane = new GamePane(this, this.getControlledCities().get(0));
		intitiateGamePane();
//		System.out.println(this.getTreasury());
	}
	
	public void updateInfoBar() {
		this.gamePane.getInfoBar().update();
	}
	
	public void intitiateGamePane() {
		setPane(gamePane);
	}

	
	
	public GamePane getGamePane() {
		return gamePane;
	}

	public ArrayList<City> getControlledCities() {
		return controlledCities;
	}

	public void setControlledCities(ArrayList<City> controlledCities) {
		this.controlledCities = controlledCities;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	public ArrayList<City> getAvailableCities() {
		return availableCities;
	}
	public void setAvailableCities(ArrayList<City> availableCities) {
		this.availableCities = availableCities;
	}


	public ArrayList<Army> getControlledArmies() {
		return controlledArmies;
	}


	public void setControlledArmies(ArrayList<Army> controlledArmies) {
		this.controlledArmies = controlledArmies;
	}

	

	
	
	
	
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


	


	public void setPane(Pane pane) {
		this.getScene().setRoot(pane);
	}
	
	
	
}
