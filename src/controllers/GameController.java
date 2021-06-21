package controllers;

import java.io.IOException;

import engine.Game;
import javafx.application.Application;
import javafx.stage.Stage;
import view.GameView;
import view.GameViewListener;

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
		updateView();
		
	}

	void updateView() {
		this.view.setFood(model.getPlayer().getFood());
		this.view.setTreasury(model.getPlayer().getTreasury());
		this.view.setTurnCount(model.getCurrentTurnCount());
		this.view.startGame();
	}
	
	@Override
	public void onEndTurn() {
		if(model.getCurrentTurnCount() == model.getMaxTurnCount())
			System.out.println("Game Over");
		else {
			this.model.endTurn();
			updateView();
		}
	}
	
}
