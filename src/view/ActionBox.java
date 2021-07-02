package view;

import buildings.Building;
import buildings.EconomicBuilding;
import buildings.MilitaryBuilding;
import engine.City;
import exceptions.FriendlyCityException;
import exceptions.MaxCapacityException;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import units.Army;
import units.Status;
import units.Unit;

public class ActionBox extends FlowPane implements CityViewListener, MapViewListener {
	private GameView gameView;
	private DetailsBox detailsBox;
	private VBox stickyButtons;

	private VBox actionButtons;
	private TextArea statusBox;

	public ActionBox(GameView gameView, VBox stickyButtons) {
		this.setMinWidth(gameView.getWidth());
		this.gameView = gameView;
		this.stickyButtons = stickyButtons;
		this.getStyleClass().add("action-box");
		this.setMaxHeight(gameView.getHeight() * 0.1);
		this.detailsBox = new DetailsBox(gameView);
		this.detailsBox.setMinWidth(gameView.getWidth() * 0.35);
		this.detailsBox.setMaxWidth(gameView.getWidth() * 0.35);
		
		this.actionButtons = new VBox();
		this.actionButtons.setMinWidth(gameView.getWidth() * 0.20);
		this.actionButtons.setMaxWidth(gameView.getWidth() * 0.20);
		this.actionButtons.setAlignment(Pos.CENTER);
		this.actionButtons.setSpacing(10);
		this.statusBox = new TextArea();
		this.statusBox.setEditable(false);
		this.statusBox.setWrapText(true);
		this.statusBox.setMinWidth(gameView.getWidth() * 0.35);
		this.statusBox.setMaxWidth(gameView.getWidth() * 0.35);
		this.statusBox.setMaxHeight(gameView.getHeight()*0.2);
		this.statusBox.setText(
				"Hello " + gameView.getPlayerName() + "\n*Tip: Start by building a market to avoid being broke");
		
		this.getChildren().add(detailsBox);
		this.getChildren().add(actionButtons);
		this.getChildren().add(statusBox);
		this.getChildren().add(stickyButtons);

		this.setAlignment(Pos.CENTER_LEFT);

	}

	@Override
	public void onBuildingClicked(Building building, CustomButton... buttons) {
		this.actionButtons.getChildren().clear();
		this.actionButtons.getChildren().addAll(buttons);
		this.detailsBox.setBuilding(building);

	}
	public void onUnitClicked(Unit u, CustomButton... buttons) {
		this.detailsBox.setUnit(u);
		this.actionButtons.getChildren().clear();
		gameView.getGamePane().getInitArmyBtn().setOnMouseClicked(e -> gameView.handleInitArmy(gameView.getGamePane().getCurrentCity(), u));
		gameView.getGamePane().getRelocateBtn().setOnMouseClicked(e -> onRelocateBtnClicked(u));
		if (gameView.getGamePane().getCurrentCity().getDefendingArmy().getUnits().contains(u))
			this.actionButtons.getChildren().add(buttons[1]);
		this.actionButtons.getChildren().addAll(buttons[0]);
	}

	public void onRelocateBtnClicked(Unit unit) {
		ChoiceBox<String> armyNames = new ChoiceBox<>();
		
		for (Army a : this.gameView.getControlledArmies()) {
			if(a.getCurrentStatus()==Status.IDLE && a.getCurrentLocation().equals(unit.getParentArmy().getCurrentLocation()) && a != unit.getParentArmy())
				armyNames.getItems().add(a.getArmyName());
		}
		if(!stringContains(unit.getParentArmy().getArmyName(), "defenders"))
			armyNames.getItems().add("Defending Army");
		
		
		CustomButton relocateFromMessage = new CustomButton("Relocate",'m');
		armyNames.setOnAction(e -> relocateFromMessage.setDisable(false));
		Node messageContent = armyNames;
		if (armyNames.getItems().size() == 0) {
			messageContent = new Label("No army available");
			relocateFromMessage.setDisable(true);
		} else {
			armyNames.setValue(armyNames.getItems().get(0));
			relocateFromMessage.setDisable(false);
		}
		MessagePane messagePane = new MessagePane(gameView,gameView.getGamePane().getMainPane(), "Choose Army", 600, 500,
				relocateFromMessage, messageContent);
		relocateFromMessage.setOnMouseClicked(e1 -> {
			try {
				Army army;
				if(armyNames.getValue().equals("Defending Army")) {
					army = getCityByName(unit.getParentArmy().getCurrentLocation()).getDefendingArmy();
				}else {
					army = getArmyByName(armyNames.getValue().toString());
				}
				gameView.handleRelocateUnit(army, unit);
				gameView.getGamePane().getMainPane().getChildren().remove(messagePane);
				gameView.getGamePane().getCityView().update();
				
			} catch (MaxCapacityException e) {
				messagePane.getContent().getChildren().add(new Label(e.getMessage()));
				relocateFromMessage.setDisable(true);
			}

		});
		gameView.getGamePane().getMainPane().getChildren().add(messagePane);
	}
	
	public void toggleEnableBtns() {
		for(Node node: this.actionButtons.getChildren()) {
			if(node instanceof CustomButton)
				((CustomButton)node).setDisable(!((CustomButton)node).isDisable());
		}
	}
	
	public City getCityByName(String cityName) {
		for(City c: gameView.getAvailableCities())
			if(c.getName().equals(cityName))
				return c;
		return null;
			
	}

	public Army getArmyByName(String name) {
		for (Army a : gameView.getControlledArmies())
			if (a.getArmyName().equals(name))
				return a;
		return null;
	}

	public void onArmyClicked(Army a, CustomButton... buttons) {
		this.detailsBox.setArmy(a);
		this.actionButtons.getChildren().clear();
		Button battleBtn = new Button("Enter Battle");
		if (a.getCurrentStatus() == Status.BESIEGING) {
			for (City c : gameView.getAvailableCities())
				if (c.getName().equals(a.getCurrentLocation())) {
					detailsBox.addText("Turns Beseiging: " + c.getTurnsUnderSiege());
					battleBtn.setOnAction(e -> gameView.enterBattle(a, c));
					
				}
//			if(a.getCurrentStatus() == Status.IDLE)
//				this.actionButtons.getChildren().add(battleBtn);

		}
		if (buttons.length != 0)
			this.actionButtons.getChildren().addAll(buttons);
	}

	@Override
	public void onCityClicked(String cityName, CustomButton... buttons) {
		

	}
	
	public boolean stringContains(String a, String c) {
		String[] str = a.split(" ");
		for(String s: str)
			if(c.equals(s))
				return true;
		return false;
		
	}

	public void addStatus(String s) {
		String currentStatus = this.statusBox.getText();
		statusBox.setText(s + "\n" + currentStatus);
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

	@Override
	public void onMapViewOpen() {
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


}
