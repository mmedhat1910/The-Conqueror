package controllers;

import java.io.IOException;
import java.util.ArrayList;

import engine.City;
import engine.Game;
import exceptions.FriendlyCityException;
import exceptions.FriendlyFireException;
import exceptions.MaxCapacityException;
import exceptions.MaxSeigingPeriodException;
import exceptions.TargetNotReachedException;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import units.Army;
import units.Status;
import units.Unit;
import view.ActionAlert;
import view.AlertPane;
import view.GameView;
import view.GameViewListener;
import view.MapViewListener;

public class GameController extends Application implements GameViewListener{
	private GameView view;
	private Game model;
	
	
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
		ArrayList<Army> contArm = new ArrayList<Army>();
		Army a = new Army("Cairo");
		a.setCurrentStatus(Status.BESIEGING);
		a.setCurrentLocation("Rome");
		
		contArm.add(a);
////		contArm.add(new Army("Cairo"));
////		contArm.add(new Army("Cairo"));
////		contArm.add(new Army("Rome"));
////		contArm.add(new Army("Rome"));
////		contArm.add(new Army("Rome"));
////		contArm.add(new Army("Sparta"));
////		contArm.add(new Army("Sparta"));
////		contArm.add(new Army("Sparta"));
//		this.view.setControlledArmies(contArm);
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
				
			} catch (MaxSeigingPeriodException e) {
				Button okBtn = new Button("Ok");
				
				view.getGamePane().getMainPane().getChildren().add(new ActionAlert(view.getGamePane().getMainPane(), "Battle Action", 600,400,"You are going to be prompt to the battle view to finalize the active seiging",okBtn));
				okBtn.setOnAction(e1->{
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
		char letter= (char) ( this.model.getPlayer().getControlledArmies().indexOf(created)+65);
		created.setArmyName("Army "+letter);
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
