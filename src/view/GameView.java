package view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import controllers.ControllerListener;
import engine.City;
import engine.Distance;
import engine.Player;
import exceptions.FriendlyCityException;
import exceptions.FriendlyFireException;
import exceptions.MaxCapacityException;
import exceptions.TargetNotReachedException;
import javafx.application.Application;
import javafx.collections.MapChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import units.Archer;
import units.Army;
import units.Infantry;
import units.Unit;

public class GameView extends Stage implements ControllerListener, AudioPlayerController {
	private Scene gameScene;
	private GameViewListener listener;

	private String playerName;
	private String playerCity;
	private Image logo;
	private PlayerNamePane playerNamePane;
	private ChooseCityPane chooseCitypane;
	private GamePane gamePane;
	private BattlePane battlePane;
	private ArrayList<City> availableCities;
	private ArrayList<Distance> distances;
	private double width;
	private double height;
	private ArrayList<City> controlledCities;
	private ArrayList<Army> controlledArmies;
	private Player player;

	private double food;
	private double treasury;
	private int turnCount;
	
	
	MediaPlayer mainTrack = audioPlayer(soundtrackPath);
	MediaPlayer battleTrack = audioPlayer(soundtrackPath);
	

	public GameView(double width, double height, boolean fullScreen) {

		
		
		playMainTrack();
		
		this.width = width;
		this.height = height;
		this.playerNamePane = new PlayerNamePane(this);
		this.chooseCitypane = new ChooseCityPane(this);
		
		Army cArmy = new Army("Cairo");
		Army rArmy = new Army("Rome");
		Archer archerC = new Archer(2, 50, 1, 1, 1);
		archerC.setParentArmy(cArmy);
		Archer archerR = new Archer(2, 50, 1, 1, 1);
		archerC.setParentArmy(rArmy);
		cArmy.getUnits().add(archerC);
		rArmy.getUnits().add(archerR);
		rArmy.getUnits().add(archerR);
		rArmy.getUnits().add(archerR);
		rArmy.getUnits().add(archerR);
		rArmy.getUnits().add(archerR);
		rArmy.getUnits().add(archerR);
		rArmy.getUnits().add(archerR);
		rArmy.getUnits().add(archerR);
		rArmy.getUnits().add(archerR);
		rArmy.getUnits().add(archerR);
		rArmy.getUnits().add(archerR);
		rArmy.getUnits().add(archerR);
		cArmy.setArmyName("Cairo Army");
		rArmy.setArmyName("Rome Army");
		this.battlePane = new BattlePane(this, rArmy, cArmy);
		
		this.gameScene = new Scene(playerNamePane);
		gameScene.getStylesheets().add("file:resources/styles/main.css");

		this.logo = new Image("file:resources/images/logo/logo.png");

		this.getIcons().add(logo);
		this.setWidth(width);
		this.setHeight(height);
		this.setTitle("The Conqueror");

		this.setFullScreen(fullScreen);
//		stage.setResizable(false);
		this.setScene(gameScene);
		ImageCursor cursor = new ImageCursor(new Image("file:resources/images/icons/cursor.png"));
		
//		this.getScene().setCursor(cursor);
		this.show();
	}

	public void handleInitArmy(City city, Unit unit) {
		if (this.player.getControlledArmies().size() == 5) {
			gamePane.getMainPane().getChildren()
					.add(new AlertPane(gamePane.getMainPane(), 600, 400, "Cannot Initiate more armies"));
			return;
		}
		this.listener.onInitArmy(city, unit);
//		System.out.println("Initiated");
//		System.out.println(getPlayer().getControlledArmies().size());
		this.controlledArmies = getPlayer().getControlledArmies();
		

	}

	public void updateCityViewState(String status) {
		gamePane.displayDefendingArmy();
		gamePane.getActionBox().getActionButtons().getChildren().clear();
		gamePane.getActionBox().getDetailsBox().clear();
		gamePane.getActionBox().addStatus(status);
		gamePane.getCityView().update();
	}

	public void handleRelocateUnit(Army army, Unit unit) throws MaxCapacityException {
		this.listener.onRelocateUnit(army, unit);
		updateCityViewState(
				unit.getClass().getSimpleName() + "(" + unit.getLevel() + ") Relocated to " + army.getArmyName());
	}

	public void handleTargetClicked(Army army) {
		ChoiceBox<String> cityChoices = new ChoiceBox<>();
		for (City c : availableCities)
			if (!controlledCities.contains(c))
				cityChoices.getItems().add(c.getName());
		Node msgContent = new Label("All Cities are controlled, End game to win");
		if(cityChoices.getItems().size()>0) {
			cityChoices.setValue(cityChoices.getItems().get(0));
			msgContent = cityChoices;
		}
		CustomButton targetFromMsg = new CustomButton("Target",'m');
		MessagePane chooseTargetCity = new MessagePane(this,gamePane.getMainPane(), "Choose Target City", 500, 400,
				targetFromMsg, msgContent);
		gamePane.getMainPane().getChildren().add(chooseTargetCity);
		targetFromMsg.setOnMouseClicked(e -> {
			playClick();
			String targetCity = cityChoices.getValue();
			gamePane.getMainPane().getChildren().remove(chooseTargetCity);
			gamePane.getActionBox().getDetailsBox().setArmy(army);
			this.listener.onTargetSet(army, targetCity);
			this.updateCityViewState(army.getArmyName() + " heading to " + targetCity);
		});

	}

	public City getCityByName(String name) {
		for (City c : availableCities)
			if (c.getName().equals(name))
				return c;
		return null;
	}

	public void onReachingTarget(Army army) {
		City city = getCityByName(army.getCurrentLocation());
		CustomButton laySeigeBtn = new CustomButton("Lay Seige",'m');
		CustomButton battleBtn = new CustomButton("Start Battle",'m');
		String status = army.getArmyName() + " reached " + army.getCurrentLocation();
		ActionAlert laySeigeMessage = new ActionAlert(gamePane.getMainPane(), "Action Needed", 600, 400, status,	laySeigeBtn, battleBtn);
		gamePane.getActionBox().addStatus(status);
		army.setTargetReached(false);
		if(city.isInBattle()) {
			gamePane.getActionBox().addStatus("You tried to attacked a controlled city");
			gamePane.getActionBox().addStatus("The army entered the city");
			city.setInBattle(false);
			
			return;
		}
		city.setInBattle(true);
		laySeigeBtn.setOnMouseClicked(e -> System.out.println("Laysiege clicked"));
		battleBtn.setOnMouseClicked(e -> System.out.println("Enter Battle"));
		laySeigeBtn.setOnMouseClicked(e -> {
			playClick();
			try {
				this.listener.handleLaySeige(army, city);
				gamePane.getMainPane().getChildren().remove(laySeigeMessage);
				updateCityViewState(city.getName() + " is underseige");
			} catch (TargetNotReachedException | FriendlyCityException e1) {
				gamePane.getMainPane().getChildren()
						.add(new AlertPane(gamePane.getMainPane(), 600, 400, e1.getMessage()));
			} finally {
				gamePane.setMapView(gamePane.getMapView());
			}
		});
		battleBtn.setOnMouseClicked(e -> {
			playClick();
			gamePane.getMainPane().getChildren().remove(laySeigeMessage);
			this.enterBattle(army, city);
		});
		
		gamePane.getMainPane().getChildren().add(laySeigeMessage);
		

	}

	public void enterBattle(Army a, City c) {
		playBattle();
		this.battlePane = new BattlePane(this, a, c.getDefendingArmy());
		setPane(battlePane);
	}

	public void onAutoResolve(Army attackingArmy, Army defendingArmy) {
		try {
			this.listener.startResolve(attackingArmy, defendingArmy);
			this.battlePane.update();
		} catch (FriendlyFireException | IOException e) {
			gamePane.getMainPane().getChildren().add(new AlertPane(battlePane.getMainPane(), 500, 600, e.getMessage()));
		}
		

	}

	public void onAttack(Unit selectedUnit, Unit defendingUnit) {
		try {
			this.listener.startAttack(selectedUnit, defendingUnit);
		} catch (FriendlyFireException | IOException e) {
			gamePane.getMainPane().getChildren().add(new AlertPane(battlePane.getMainPane(), 500, 600, e.getMessage()));
		}
		battlePane.update();
	}

	public void visitCity(String cityName) {
		CityView view = gamePane.getCityViewByName(cityName);
		gamePane.setCityView(view);
		gamePane.getInfoBar().update();
		gamePane.onExitMap();
		
	}
	

	@Override
	public void startGame() {
		this.gamePane = new GamePane(this, this.getControlledCities().get(0));
		intitiateGamePane();
		this.stopMainTrack();
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

	public ArrayList<Distance> getDistances() {
		return distances;
	}

	public void setDistances(ArrayList<Distance> distances) {
		this.distances = distances;
	}

	

	@Override
	public void playMainTrack() {
		
		mainTrack.play();
		mainTrack.setOnEndOfMedia(new Runnable() {
		       public void run() {
		    	   mainTrack.seek(Duration.ZERO);
		       }
		   });
		mainTrack.play();
		mainTrack.setVolume(0.5);
	}

	@Override
	public void playBattle() {
		battleTrack.seek(Duration.ZERO);
		battleTrack.play();
		battleTrack.setVolume(0.5);
	}

	@Override
	public void playClick() {
		audioPlayer(clickPath).play();
		
	}

	@Override
	public void playBuild() {
		audioPlayer(buildPath).play();
		
	}

	@Override
	public void playAttack(Unit unit) {
		if(unit instanceof Archer)
			audioPlayer(archerPath).play();
		else if (unit instanceof Infantry)
			audioPlayer(swordPath).play();
		else
			audioPlayer(horsePath).play();
		
	}

	@Override
	public void playWon() {
		audioPlayer(winPath).play();
		
	}

	@Override
	public void playLost() {
		audioPlayer(lostPath).play();
		
	}

	@Override
	public MediaPlayer audioPlayer(String path) {
		Media media = new Media(new File(path).toURI().toString());
		return new MediaPlayer(media);
	}

	@Override
	public void stopMainTrack() {
		mainTrack.stop();
		
	}

	@Override
	public void stopBattle() {
		
		battleTrack.stop();
		
	}

	@Override
	public void stopClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopBuild() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopAttack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopWon() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopLost() {
		// TODO Auto-generated method stub
		
	}

}
