package view;

import controllers.GameController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import units.Army;
import units.Unit;

public class BattlePane extends BorderPane{
	private GameView gameView;
	private Army attackingArmy;
	private Army defendingArmy;
	private VBox attackingSide;
	private VBox defendingSide;
	private FlowPane actionButtons;
	private Button attack;
	private StackPane mainPane;
	private Button autoResolve;
	private Button surrender;
	private Unit selectedUnit;
	public BattlePane(GameView gameView, Army attacking, Army defending) {
		this.attackingArmy =  attacking;
		this.defendingArmy =defending;
		this.setMainPane(new StackPane());
		this.attackingSide = new VBox();
		this.defendingSide = new VBox();
		this.actionButtons = new FlowPane();
		this.attack = new Button("Attack");
		this.autoResolve =new Button("Auto Resolve Battle");
		this.surrender = new Button("Surrender");
		this.getStyleClass().add("battle-view");
		this.gameView= gameView;
		this.setMaxWidth(gameView.getWidth());
		this.setMaxHeight(gameView.getHeight());
		this.attackingSide.setMaxWidth(gameView.getWidth()*0.2);
		this.attackingSide.setMinWidth(gameView.getWidth()*0.2);
		this.actionButtons.getChildren().add(attack);
		this.actionButtons.getChildren().add(autoResolve);
//		this.actionButtons.getChildren().add(surrender);
		
		
		attack.setOnAction(e-> handleAttack());
		autoResolve.setOnAction(e->handleAutoresolve());
		
		this.setBottom(actionButtons);
		this.setCenter(mainPane);
		showArmies();
		this.setTop(new Label(attackingArmy.getArmyName()+" vs. "+defendingArmy.getArmyName()));
	}

	private void handleAutoresolve() {
		gameView.onAutoResolve(attackingArmy, defendingArmy);
		
	}

	private void handleAttack() {
		int randomIndex = (int) (Math.random()*defendingArmy.getUnits().size());
		Unit defendingUnit = defendingArmy.getUnits().get(randomIndex);
		if(this.selectedUnit != null) {
			gameView.onAttack(selectedUnit, defendingUnit);
			randomIndex = (int) (Math.random()*defendingArmy.getUnits().size());
			defendingUnit = defendingArmy.getUnits().get(randomIndex);
			randomIndex =  (int) (Math.random()*attackingArmy.getUnits().size());
			Unit randomAttacker = attackingArmy.getUnits().get(randomIndex);
			gameView.onAttack(defendingUnit, randomAttacker);
			selectedUnit = null;
		}else {
			System.out.println("Select a unit");
		}
	}

	public GameView getGameView() {
		return gameView;
	}
	public void showArmies() {
		this.defendingSide.getChildren().clear();
		this.attackingSide.getChildren().clear();
		attackingSide.getChildren().add(new Label(attackingArmy.getArmyName()));
		for(Unit unit: attackingArmy.getUnits()) {
			Button unitBtn = new Button(unit.getClass().getSimpleName()+" "+unit.getLevel()+" "+unit.getCurrentSoldierCount());
			attackingSide.getChildren().add(unitBtn);
			
			unitBtn.setOnAction(e->{	
				this.selectedUnit=unit;
				System.out.println(selectedUnit.getClass().getSimpleName());
			});
		}
			
		defendingSide.getChildren().add(new Label(defendingArmy.getArmyName()));
		for(Unit unit: defendingArmy.getUnits()) {
			Button unitBtn = new Button(unit.getClass().getSimpleName()+" "+unit.getLevel() +" "+unit.getCurrentSoldierCount());
			defendingSide.getChildren().add(unitBtn);
		}

		this.setRight(defendingSide);
		this.setLeft(attackingSide);
		setAlignment(attackingSide, Pos.CENTER);
		setAlignment(defendingSide, Pos.CENTER);
	}
	
	public void update() {
		checkIfBattleEnded();
		showArmies();
	}
	
	public void checkIfBattleEnded() {
		String msg= ""; boolean ended = false;
		Button exitBattle = new Button("Exit");
		if(attackingArmy.getUnits().size() == 0) {
			msg = "You lost";
			ended = true;
		}
		else if (defendingArmy.getUnits().size()== 0) {
			msg ="You win";
			ended = true;
		}
		
		System.out.println(msg);
		exitBattle.setOnAction(e->gameView.setPane(gameView.getGamePane()));
		if(ended) {
			this.mainPane.getChildren().add(new ActionAlert(mainPane, "Battle is over", 600, 400, msg, exitBattle));
		}
	}
	
	public void setGameView(GameView gameView) {
		this.gameView = gameView;
	}

	public Army getDefendingArmy() {
		return defendingArmy;
	}

	public void setDefendingArmy(Army defendingArmy) {
		this.defendingArmy = defendingArmy;
	}

	public Army getAttackingArmy() {
		return attackingArmy;
	}

	public void setAttackingArmy(Army attackingArmy) {
		this.attackingArmy = attackingArmy;
	}

	public StackPane getMainPane() {
		return mainPane;
	}

	public void setMainPane(StackPane mainPane) {
		this.mainPane = mainPane;
	}

	

	

}
