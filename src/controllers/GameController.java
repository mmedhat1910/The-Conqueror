package controllers;

import java.io.IOException;
import java.util.ArrayList;

import engine.City;
import engine.Game;
import javafx.application.Application;
import javafx.stage.Stage;
import units.Army;
import units.Status;
import units.Unit;
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
		this.view.setControlledArmies(contArm);
//		this.view.setControlledArmies(model.getPlayer().getControlledArmies());
		this.view.startGame();
	}
	
	@Override
	public void onEndTurn() {
		if(model.getCurrentTurnCount() == model.getMaxTurnCount())
			System.out.println("Game Over");
		else {
			this.model.endTurn();
			updateInfo();
			
		}
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
		return model.getPlayer().initiateArmy(city, unit);
		
	}

	

	

	
}
