package view;

import buildings.Building;
import buildings.EconomicBuilding;
import buildings.MilitaryBuilding;
import engine.City;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import units.Army;
import units.Status;
import units.Unit;

public class ActionBox extends FlowPane implements CityViewListener, MapViewListener{
	private GameView gameView;
	private DetailsBox detailsBox;
	private VBox stickyButtons;

	

	private VBox actionButtons;
	private TextArea statusBox;
	
	public ActionBox(GameView gameView, VBox stickyButtons) {
		
	this.gameView = gameView;
	this.stickyButtons = stickyButtons;
	this.getStyleClass().add("action-box");
	this.setMaxHeight(gameView.getHeight()*0.15);
	this.detailsBox = new DetailsBox();
	this.detailsBox.setPrefWidth(gameView.getWidth()*0.3);
	
	this.actionButtons = new VBox();
	this.actionButtons.setPrefWidth(gameView.getWidth()*0.2);

	
	this.statusBox = new TextArea();
	this.statusBox.setEditable(false);
	this.statusBox.setPrefWidth(gameView.getWidth()*0.3);
	this.statusBox.setText("Hello "+ gameView.getPlayerName()+"\n*Tip: Start by building a market to avoid being broke");
	
	
	
	
	this.getChildren().add(detailsBox);
	this.getChildren().add(actionButtons);
	this.getChildren().add(statusBox);
	this.getChildren().add(stickyButtons);
	
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
		gameView.getGamePane().getInitArmyBtn().setOnAction(e->gameView.handleInitArmy(gameView.getGamePane().getCurrentCity(), u));
		if(gameView.getGamePane().getCurrentCity().getDefendingArmy().getUnits().contains(u))
			this.actionButtons.getChildren().add(buttons[1]);
		this.actionButtons.getChildren().addAll(buttons[0]);
	}
	
	
	
	public void onArmyClicked(Army a, Button... buttons) {
		this.detailsBox.setArmy(a);
		if(a.getCurrentStatus()==Status.BESIEGING)
			for(City c: gameView.getAvailableCities())
				if(c.getName().toLowerCase().equals(a.getCurrentLocation().toLowerCase()))
					detailsBox.addText("Turns Beseiging: "+c.getTurnsUnderSiege());
		this.actionButtons.getChildren().clear();
		if(buttons.length!=0)
		this.actionButtons.getChildren().addAll(buttons);
	}
	
	public void addStatus(String s) {
		String currentStatus =this.statusBox.getText();
		statusBox.setText(s+ "\n"+currentStatus); 
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







	






	@Override
	public void onMapViewOpen() {
		
		
	}


	@Override
	public void onCityClicked(String cityName, Button...buttons) {
		for(City c: gameView.getControlledCities())
			if(c.getName().equals(cityName)) {
				this.detailsBox.setText(cityName+" is Controlled");
				this.actionButtons.getChildren().clear();
				this.actionButtons.getChildren().add(buttons[0]);
			}else {
				this.detailsBox.setText(cityName+" is Enemy");
				this.actionButtons.getChildren().clear();
				this.actionButtons.getChildren().add(buttons[1]);
			}
		
	}







	@Override
	public void onVisitClicked(String cityName) {
		// TODO Auto-generated method stub
		
	}







	@Override
	public void onTargetClicked(String cityName) {
		// TODO Auto-generated method stub
		
	}







	public VBox getStickyButtons() {
		return stickyButtons;
	}







	public void setStickyButtons(VBox stickyButtons) {
		this.stickyButtons = stickyButtons;
	}












}
