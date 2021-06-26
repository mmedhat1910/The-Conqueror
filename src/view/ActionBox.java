package view;

import buildings.Building;
import buildings.EconomicBuilding;
import buildings.MilitaryBuilding;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import units.Army;
import units.Unit;

public class ActionBox extends FlowPane implements CityViewListener{
	private GameView gameView;
	private DetailsBox detailsBox;
	private Button mapBtn;

	

	private VBox actionButtons;
	private TextArea statusBox;
	
	public ActionBox(GameView gameView, Button mapBtn) {
		
	this.gameView = gameView;
	this.setMapBtn(mapBtn);
	this.getStyleClass().add("action-box");
	this.setMaxHeight(gameView.getHeight()*0.15);
	this.detailsBox = new DetailsBox();
	this.detailsBox.setPrefWidth(gameView.getWidth()*0.3);
	
	this.actionButtons = new VBox();
	this.actionButtons.setPrefWidth(gameView.getWidth()*0.2);

	
	this.statusBox = new TextArea();
	this.statusBox.setPrefWidth(gameView.getWidth()*0.3);
	this.statusBox.setText("Hello\nWorld\nI'am\n"+gameView.getPlayerName());
	
	
	
	
	this.getChildren().add(detailsBox);
	this.getChildren().add(actionButtons);
	this.getChildren().add(statusBox);
	this.getChildren().add(mapBtn);
	
	this.setAlignment(Pos.CENTER);
	

	}
	
	

	

	

	@Override
	public void onBuildingClicked(Building building ,Button... buttons) {
		this.actionButtons.getChildren().clear();
		this.actionButtons.getChildren().addAll(buttons);
		
		this.detailsBox.setBuilding(building);
		
		
	}


	public void onUnitClicked(Unit u, Button... buttons) {
		this.detailsBox.setUnit(u);
		this.actionButtons.getChildren().clear();
		this.actionButtons.getChildren().addAll(buttons);
	}
	public void onArmyCLicked(Army a, Button... buttons) {
		this.detailsBox.setArmy(a);
		this.actionButtons.getChildren().clear();
		this.actionButtons.getChildren().addAll(buttons);
	}
	
	@Override
	public void onBuild(BuildingBlock b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(BuildingBlock b) {
		this.detailsBox.setBuilding(b.getBuilding());
		
	}
	
	@Override
	public void onRecruit(Building b, String buildingType) {
		this.detailsBox.setBuilding(b);
		
		
	}
	
	public DetailsBox getDetailsBox() {
		return detailsBox;
	}



	public void setDetailsBox(DetailsBox detailsBox) {
		this.detailsBox = detailsBox;
	}
	
	public VBox getActionButtons() {
		return actionButtons;
	}

	public void setActionButtons(VBox actionButtons) {
		this.actionButtons = actionButtons;
	}

	public TextArea getStatusBox() {
		return statusBox;
	}

	public void setStatusBox(TextArea statusBox) {
		this.statusBox = statusBox;
	}







	public Button getMapBtn() {
		return mapBtn;
	}







	public void setMapBtn(Button mapBtn) {
		this.mapBtn = mapBtn;
	}












}
