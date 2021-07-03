package view;

import java.util.ArrayList;

import controllers.GameController;
import javafx.animation.PauseTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
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
	private CustomButton attack;
	private StackPane mainPane;
	private CustomButton autoResolve;
	private Button surrender;
	private Unit attackingUnit;
	private Unit defendingUnit;
	private TextArea logArea;
	private HBox actionArea;
	private ArrayList<BattleUnit> attackerBattleUnits = new ArrayList<>();
	private ArrayList<BattleUnit> defenderBattleUnits = new ArrayList<>();
	public BattlePane(GameView gameView, Army attacking, Army defending) {
		this.attackingArmy =  attacking;
		this.defendingArmy =defending;
		this.setMainPane(new StackPane());
		this.attackingSide = new FlowPane();
		this.defendingSide = new FlowPane();
		this.attackingSide.setOrientation(Orientation.VERTICAL);
		this.defendingSide.setOrientation(Orientation.VERTICAL);
		this.actionButtons = new FlowPane();
		this.attack = new CustomButton("Attack",'l');
		this.autoResolve =new CustomButton("Auto Resolve Battle",'l');
		this.surrender = new Button("Surrender");
		this.getStyleClass().add("battle-view");
		this.gameView= gameView;
		this.setMaxWidth(gameView.getWidth());
		this.setMaxHeight(gameView.getHeight());
		this.attackingSide.setMaxWidth(gameView.getWidth()*0.2);
		this.attackingSide.setMinWidth(gameView.getWidth()*0.2);
		
		this.logArea = new TextArea();
		logArea.setEditable(false);
		logArea.setStyle("-fx-background-color: rgba(0,0,0,0.4); "
				+ "-fx-text-fill: white;"
				+ "-fx-padding: 20 50;"
				);
		
		
		this.actionArea = new HBox();
		actionArea.setAlignment(Pos.CENTER);
		actionArea.setSpacing(10);
		this.actionArea.getChildren().addAll(attack,autoResolve);
//		this.actionButtons.getChildren().add(surrender);
		
		this.setPadding(new Insets(10,30,0,30));
		
		attack.setOnMouseClicked(e-> handleAttack());
		autoResolve.setOnMouseClicked(e->handleAutoresolve());
		if(checkIfBattleEnded()) {
			gameView.getListener().handleOccupy(attackingArmy, defendingArmy.getCurrentLocation());
		}
		
		VBox bottomBox = new VBox();
		bottomBox.setSpacing(10);
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.getChildren().addAll(actionArea, logArea);
		bottomBox.setMinWidth(gameView.getWidth()*0.7);
		this.setBottom(bottomBox);
		
		this.setCenter(mainPane);
		showArmies();
		
		Label titleLabel = new Label(attackingArmy.getArmyName()+" vs. "+defendingArmy.getArmyName());
		titleLabel.getStyleClass().add("battle-title");
		titleLabel.setStyle("-fx-text-fill: #0f5e6e;\n"
				+ "	-fx-font-weight: bold;\n"
				+ "	-fx-font-size: 3em;\n"
				+ "	-fx-text-align: center;"
				+ " -fx-padding: 20px ;"+
				"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0.5, 0.5, 0)");
		
		titleLabel.setAlignment(Pos.CENTER);
		setAlignment(titleLabel, Pos.TOP_CENTER);
		setAlignment(bottomBox, Pos.CENTER);
		this.setTop(titleLabel);
	}

	public void addLog(String log) {
		String old =logArea.getText();
		logArea.setText(log+"\n"+old);
	}
	private void handleAutoresolve() {
		gameView.playClick();
		addLog("You decided to autoresolve the battle");
		gameView.onAutoResolve(attackingArmy, defendingArmy);
		
		
	}

	private void handleAttack() {
		gameView.playClick();
		PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
		if(this.attackingUnit != null && this.defendingUnit != null) {
			
			int defCount = defendingUnit.getCurrentSoldierCount();
			addLog("Your turn");
			gameView.onAttack(attackingUnit, defendingUnit);
			gameView.playAttack(attackingUnit);
			int diff = defCount-defendingUnit.getCurrentSoldierCount();
			addLog("Defender lost "+ diff+" soldiers");
			if(checkIfBattleEnded()) {
				gameView.getListener().handleOccupy(attackingArmy, defendingArmy.getCurrentLocation());
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
				gameView.playAttack(defendingUnit);
				int diff2 = attCount - attackingUnit.getCurrentSoldierCount();
				addLog("You lost "+ diff2+" soldiers");
				attackingUnit = null; defendingUnit =null;
			});
			pause.playFromStart();
		if(checkIfBattleEnded()) {
			gameView.getListener().handleOccupy(attackingArmy, defendingArmy.getCurrentLocation());
			return;
		}
			
			
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
		BattleArmy attackingBA = new BattleArmy(attackingArmy.getArmyName());
		
		attackingSide.getChildren().add(attackingBA);
		attackingSide.setColumnHalignment(HPos.CENTER);
		for(Unit unit: attackingArmy.getUnits()) {
			BattleUnit unitBtn = new BattleUnit(unit);
			attackingSide.getChildren().add(unitBtn);
			attackerBattleUnits.add(unitBtn);
			unitBtn.setOnMouseClicked(e->{	
				this.attackingUnit=unit;
				addLog("Your selected unit: "+attackingUnit.getClass().getSimpleName()+" "+attackingUnit.getLevel());
				deselectAllAttaching();
				unitBtn.setStyle("-fx-border-color:red;"
						+ "-fx-border-width: 3;"
						+ "-fx-border-insets: 2 2 2 2");
			});
		}
			
		BattleArmy defendingBA = new BattleArmy(defendingArmy.getArmyName());
		
		defendingSide.getChildren().add(defendingBA);
		defendingSide.setColumnHalignment(HPos.CENTER);
		for(Unit unit: defendingArmy.getUnits()) {
			BattleUnit unitBtn = new BattleUnit(unit);
			defendingSide.getChildren().add(unitBtn);
			defenderBattleUnits.add(unitBtn);
			unitBtn.setOnMouseClicked(e->{	
				this.defendingUnit=unit;
				addLog("Your selected defender unit: "+defendingUnit.getClass().getSimpleName()+" "+defendingUnit.getLevel());
				deselectAllDefending();
				unitBtn.setStyle("-fx-border-color:red;"
						+ "-fx-border-width: 3;"
						+ "-fx-border-insets: 2 2 2 2");
			});
		}

		this.setRight(defendingSide);
		this.setLeft(attackingSide);
		setAlignment(attackingSide, Pos.CENTER);
		setAlignment(defendingSide, Pos.CENTER);
	}
	
	
	public void deselectAllAttaching() {
		for(BattleUnit u : attackerBattleUnits)
			u.setStyle("-fx-border-width: 0;");
	}
	public void deselectAllDefending() {
		for(BattleUnit u : defenderBattleUnits)
			u.setStyle("-fx-border-width: 0;");
	}
	public void update() {
		checkIfBattleEnded();
		showArmies();
	}
	
	public boolean checkIfBattleEnded() {
		String msg= ""; boolean ended = false;
		CustomButton exitBattle = new CustomButton("Exit Battle",'m');
		if(attackingArmy.getUnits().size() == 0) {
			msg = "You lost";
			ended = true;
		}
		else if (defendingArmy.getUnits().size()== 0) {
			msg ="You win";
			ended = true;
		}
		
		exitBattle.setOnMouseClicked(e->{
			gameView.stopBattle();
			gameView.playClick();
			gameView.setPane(gameView.getGamePane());
			gameView.getGamePane().getMapView().updateMap();
		});
		if(ended) {
			ActionAlert alert=new ActionAlert(mainPane, "Battle is over", 600, 400, msg, exitBattle);
			mainPane.setAlignment(Pos.CENTER);
			setAlignment(alert, Pos.CENTER);
			this.mainPane.getChildren().add(alert);
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
