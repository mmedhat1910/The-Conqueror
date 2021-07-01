package view;

import controllers.GameController;
import javafx.animation.PauseTransition;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import units.Army;
import units.Unit;

public class BattlePane extends BorderPane{
	private GameView gameView;
	private Army attackingArmy;
	private Army defendingArmy;
	private FlowPane attackingSide;
	private FlowPane defendingSide;
	private FlowPane actionButtons;
	private Button attack;
	private StackPane mainPane;
	private Button autoResolve;
	private Button surrender;
	private Unit attackingUnit;
	private Unit defendingUnit;
	private TextArea logArea;
	private FlowPane actionArea;
	public BattlePane(GameView gameView, Army attacking, Army defending) {
		this.attackingArmy =  attacking;
		this.defendingArmy =defending;
		this.setMainPane(new StackPane());
		this.attackingSide = new FlowPane();
		this.defendingSide = new FlowPane();
		this.attackingSide.setOrientation(Orientation.VERTICAL);
		this.defendingSide.setOrientation(Orientation.VERTICAL);
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
		this.logArea = new TextArea();
		logArea.setEditable(false);
		this.actionArea = new FlowPane();
		this.actionArea.getChildren().add(logArea);
		this.actionArea.getChildren().add(attack);
		this.actionArea.getChildren().add(autoResolve);
//		this.actionButtons.getChildren().add(surrender);
		
		
		
		attack.setOnAction(e-> handleAttack());
		autoResolve.setOnAction(e->handleAutoresolve());
		if(checkIfBattleEnded()) {
			gameView.getListener().handleOccupy(attackingArmy, defendingArmy.getCurrentLocation());
		}
		this.setBottom(actionArea);
		this.setCenter(mainPane);
		showArmies();
		this.setTop(new Label(attackingArmy.getArmyName()+" vs. "+defendingArmy.getArmyName()));
	}

	public void addLog(String log) {
		String old =logArea.getText();
		logArea.setText(log+"\n"+old);
	}
	private void handleAutoresolve() {
		addLog("You decided to autoresolve the battle");
		gameView.onAutoResolve(attackingArmy, defendingArmy);
		
		
	}

	private void handleAttack() {
		
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		if(this.attackingUnit != null && this.defendingUnit != null) {
			
			int defCount = defendingUnit.getCurrentSoldierCount();
			addLog("Your turn");
			gameView.onAttack(attackingUnit, defendingUnit);
			int diff = defCount-defendingUnit.getCurrentSoldierCount();
			addLog("Defender lost "+ diff+" soldiers");
			if(checkIfBattleEnded()) {
				return;
			}
			pause.setOnFinished(e-> {
				
				int randomIndex; addLog("Defenders turn");
				randomIndex = (int) (Math.random()*defendingArmy.getUnits().size());
				defendingUnit = defendingArmy.getUnits().get(randomIndex);
				randomIndex =  (int) (Math.random()*attackingArmy.getUnits().size());
				attackingUnit = attackingArmy.getUnits().get(randomIndex);
				int attCount = attackingUnit.getCurrentSoldierCount();
				gameView.onAttack(defendingUnit, attackingUnit);
				int diff2 = attCount - attackingUnit.getCurrentSoldierCount();
				addLog("You lost "+ diff2+" soldiers");
				attackingUnit = null; defendingUnit =null;
			});
			pause.playFromStart();
		if(checkIfBattleEnded())
			return;
			
		}else if(attackingUnit==null){
			addLog("Please choose a unit to attack with");
		}else {
			addLog("Please choose defender's unit to attack");
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
				this.attackingUnit=unit;
				addLog("Your selected unit: "+attackingUnit.getClass().getSimpleName()+" "+attackingUnit.getLevel());
				
			});
		}
			
		defendingSide.getChildren().add(new Label(defendingArmy.getArmyName()));
		for(Unit unit: defendingArmy.getUnits()) {
			Button unitBtn = new Button(unit.getClass().getSimpleName()+" "+unit.getLevel() +" "+unit.getCurrentSoldierCount());
			defendingSide.getChildren().add(unitBtn);
			
			unitBtn.setOnAction(e->{	
				this.defendingUnit=unit;
				addLog("Your selected defender unit: "+defendingUnit.getClass().getSimpleName()+" "+defendingUnit.getLevel());
			});
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
	
	public boolean checkIfBattleEnded() {
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
		
		exitBattle.setOnAction(e->{
			gameView.setPane(gameView.getGamePane());
			gameView.getGamePane().getMapView().updateMap();
		});
		if(ended) {
			this.mainPane.getChildren().add(new ActionAlert(mainPane, "Battle is over", 600, 400, msg, exitBattle));
			gameView.getGamePane().getActionBox().addStatus(msg +" the battle");
			this.attack.setDisable(true);
			this.autoResolve.setDisable(true);
		}
		return ended;
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
