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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import units.Army;
import units.Status;
import units.Unit;
import view.ActionAlert;
import view.AlertPane;
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
		
		this.view.setControlledArmies(model.getPlayer().getControlledArmies());
		this.view.startGame();
	}
	
	@Override
	public void onEndTurn() {
		if(model.getCurrentTurnCount() == model.getMaxTurnCount()||model.isGameOver()) {
			String msg ="";
			Button endGameBtn = new Button("End Game");
			endGameBtn.setOnAction(e-> {
				view.close();
				System.exit(0);
			});
			msg = model.getCurrentTurnCount() == model.getMaxTurnCount()?  "You Lost" : "You Won";
				
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
				Button okBtn = new Button("Ok");
				ActionAlert maxSiegingAlert = new ActionAlert(view.getGamePane().getMainPane(), "Battle Action", 600,400,"You are going to be prompt to the battle view to finalize the active seiging",okBtn);
				view.getGamePane().getMainPane().getChildren().add(maxSiegingAlert);
				okBtn.setOnAction(e1->{
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
		view.getGamePane().getActionBox().addStatus(armyName + "occupied " +cityName);
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
	public Army onInitArmy(City city,Unit unit) {
		
		Army created = model.getPlayer().initiateArmy(city, unit);
		TextField armyNameField = new TextField();
		armyNameField.setPromptText("Choose army name");
		ChoiceBox<String> namesDropdown = new ChoiceBox<String>(FXCollections.observableArrayList(randomNames));
		namesDropdown.setValue("Choose Name");
		Pane parent = view.getGamePane().getMainPane();
		Button chooseBtn = new Button("Initiate");
		VBox vbox = new VBox();
		vbox.getChildren().addAll(armyNameField,namesDropdown);
		MessagePane chooseNameMsg = new MessagePane(parent, "Choose Army Name", 500, 400, chooseBtn, vbox);
		armyNameField.setOnAction(e-> chooseBtn.setDisable(false));
		namesDropdown.setOnAction(e->chooseBtn.setDisable(false));
		chooseBtn.setOnAction(e-> {
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
		
		return created;
		
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
