package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import engine.City;
import engine.Game;
import exceptions.FriendlyCityException;
import exceptions.FriendlyFireException;
import exceptions.MaxCapacityException;
import exceptions.MaxSeigingPeriodException;
import exceptions.TargetNotReachedException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import units.Army;
import units.Status;
import units.Unit;
import view.ActionAlert;
import view.AlertPane;
import view.CustomButton;
import view.GameView;
import view.GameViewListener;
import view.MapViewListener;
import view.MessagePane;

public class GameController extends Application implements GameViewListener{
	private GameView view;
	private Game model;
	private ArrayList<String> randomNames = new ArrayList<String>(Arrays.asList(
			"Choose Name",
			"The Destiny Division",
			"The Final Division",
			"The Death\'s Angels",
			"The Maroon Myriad",
			"The Order",
			"The Last Regiment",
			"The Pemir",
			"The Kluxron",
			"The Burad",
			"The Eefarix"
			)); 
	
	private double height = 1080;
	private double width = 1920;
	@Override
	public void start(Stage arg0) throws Exception {
		this.view = new GameView(this.width, this.height, true);
		this.view.setListener(this);
		
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void onStartGame() {
		try {
			this.model = new Game(this.view.getPlayerName(), this.view.getPlayerCity());
		} catch (IOException e) {
			e.printStackTrace();
		}

		initiateView();
		
	}

	public void initiateView() {
		this.view.setFood(model.getPlayer().getFood());
		this.view.setTreasury(model.getPlayer().getTreasury());
		this.view.setTurnCount(model.getCurrentTurnCount());
		this.view.setControlledCities(model.getPlayer().getControlledCities());
		this.view.setPlayer(this.model.getPlayer());
		this.view.setAvailableCities(model.getAvailableCities());
		this.view.setControlledCities(model.getPlayer().getControlledCities());
		this.view.setDistances(model.getDistances());
		this.view.setControlledArmies(model.getPlayer().getControlledArmies());
		this.view.startGame();
	}
	
	@Override
	public void onEndTurn() {
		System.out.println("-------------------------");
		System.out.println("Controlled cities :"+"\n~~~~");
		for(City c: model.getPlayer().getControlledCities())
			System.out.println(c.getName());
		System.out.println("``````````````````````");
		System.out.println("Controlled Armies: "+model.getPlayer().getControlledArmies().size());
		System.out.println("-------------------------");

		if(model.isGameOver()) {
			String msg ="";
			CustomButton endGameBtn = new CustomButton("End Game",'m');
			endGameBtn.setOnMouseClicked(e-> {
				view.playClick();
				view.close();
				System.exit(0);
			});
			if(model.getCurrentTurnCount() == model.getMaxTurnCount()) {
				msg = "You Lost" ;
				view.playLost();
			}else {
				view.playWon();
				msg = "You Won";
			}
			System.out.println("GameOver "+msg);
			ActionAlert gameOverAlert = new ActionAlert(view.getGamePane().getMainPane(), "Game Over", 500, 400, msg, endGameBtn);
			view.getGamePane().getMainPane().getChildren().add(gameOverAlert);
			
		}
		else {
			try {
				this.model.endTurn();
				updateInfo();
				view.getGamePane().setMapView(view.getGamePane().getMapView());
				checkForBattleAction();
				view.getGamePane().getMapView().updateMap();
				view.getGamePane().getActionBox().getDetailsBox().update();
			} catch (MaxSeigingPeriodException e) {
				CustomButton okBtn = new CustomButton("Ok",'m');
				ActionAlert maxSiegingAlert = new ActionAlert(view.getGamePane().getMainPane(), "Battle Action", 600,400,"You are going to be prompt to the battle view to finalize the active seiging",okBtn);
				view.getGamePane().getMainPane().getChildren().add(maxSiegingAlert);
				okBtn.setOnMouseClicked(e1->{
					view.playClick();
					view.getGamePane().getMainPane().getChildren().remove(maxSiegingAlert);
					Army attacking=null; City city=null;
					for(Army a: model.getPlayer().getControlledArmies())
						if(a.getCurrentStatus() == Status.BESIEGING)
							attacking = a;
					for(City c: model.getAvailableCities())
						if(c.isUnderSiege() && c.getTurnsUnderSiege()==3) {
							c.setTurnsUnderSiege(-1);
							c.setUnderSiege(false);
							city = c;
						}
						view.enterBattle(attacking, city);
					
				});
				
			}
		}
	}

	public void handleOccupy(Army army, String cityName) {
		String armyName = army.getArmyName();
		view.getGamePane().getActionBox().addStatus(armyName + " occupied " +cityName);
		model.occupy(army, cityName);
		view.getGamePane().getActionBox().addStatus(armyName +" was renamed to "+ army.getArmyName());
	}
	
	

	public void checkForBattleAction() {
		for(Army army: model.getPlayer().getControlledArmies())
			if(army.isTargetReached())
				view.onReachingTarget(army);
	}
	
	public void handleLaySeige(Army army, City city) throws TargetNotReachedException, FriendlyCityException {
		model.getPlayer().laySiege(army, city);
	}
	
	@Override
	public void startAttack(Unit selectedUnit, Unit defendingUnit) throws FriendlyFireException, IOException {
		selectedUnit.attack(defendingUnit);
	}
	public void startResolve(Army attackingArmy, Army defendingArmy) throws FriendlyFireException, IOException {
		model.autoResolve(attackingArmy, defendingArmy);
	}

	@Override
	public void updateInfo() {
		this.view.setFood(model.getPlayer().getFood());
		this.view.setTreasury(model.getPlayer().getTreasury());
		this.view.setTurnCount(model.getCurrentTurnCount());
		this.view.updateInfoBar();
	}

	@Override
	public void onInitArmy(City city,Unit unit) {
		TextField armyNameField = new TextField();
		armyNameField.getStyleClass().add("army-name-field");
		armyNameField.setPromptText("Choose army name");
		ChoiceBox<String> namesDropdown = new ChoiceBox<String>(FXCollections.observableArrayList(randomNames));
		namesDropdown.setValue("Choose Name");
		Pane parent = view.getGamePane().getMainPane();
		CustomButton chooseBtn = new CustomButton("Initiate",'m');
		VBox vbox = new VBox();
		Label or  = new Label("or");
		or.setFont(Font.font(8));
		or.setStyle("-fx-text-fill: rgb(96, 62, 27)");
		vbox.getChildren().addAll(armyNameField,or,namesDropdown);
		vbox.setAlignment(Pos.CENTER);
		MessagePane chooseNameMsg = new MessagePane(view,parent, "Choose Army Name", 700, 500, chooseBtn, vbox);
		armyNameField.setOnAction(e-> chooseBtn.setDisable(false));
		namesDropdown.setOnAction(e->chooseBtn.setDisable(false));
		chooseBtn.setOnMouseClicked(e-> {
			view.playClick();
			Army created = model.getPlayer().initiateArmy(city, unit);
			String name="";
			if(!armyNameField.getText().equals("")) {
				name = armyNameField.getText();
				parent.getChildren().remove(chooseNameMsg);
				created.setArmyName(name);	
				view.updateCityViewState(created.getArmyName() + " initiated");
				
			}
			else if (!namesDropdown.getValue().equals(namesDropdown.getItems().get(0))) {
				
				name = namesDropdown.getValue();
				randomNames.remove(name);
//				namesDropdown.getItems().remove(name);
				
				parent.getChildren().remove(chooseNameMsg);
				created.setArmyName(name);
				view.updateCityViewState(created.getArmyName() + " initiated");
			}else {
				vbox.getChildren().add(new Label("Please choose a name"));
				chooseBtn.setDisable(true);
			}

			
			//TODO
			
		});
		parent.getChildren().add(chooseNameMsg);
		
		
	}
	
	@Override 
	public void onRelocateUnit(Army army , Unit unit) throws MaxCapacityException {
		for(Army a: model.getPlayer().getControlledArmies())
			if(a.getUnits().contains(unit))
				a.getUnits().remove(unit);
		for(City c: model.getPlayer().getControlledCities())
			if(c.getDefendingArmy().getUnits().contains(unit))
				c.getDefendingArmy().getUnits().remove(unit);
		army.relocateUnit(unit);
	}

	@Override
	public void onTargetSet(Army army, String cityName) {
		model.targetCity(army, cityName);
	}

	

	

	

	
}
