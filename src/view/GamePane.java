package view;

import java.util.ArrayList;

import buildings.Building;
import buildings.MilitaryBuilding;
import engine.City;
import exceptions.BuildingInCoolDownException;
import exceptions.MaxCapacityException;
import exceptions.MaxLevelException;
import exceptions.MaxRecruitedException;
import exceptions.NotEnoughGoldException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import units.Archer;
import units.Army;
import units.Infantry;
import units.Unit;


public class GamePane extends BorderPane implements CityViewListener, MapViewListener{
	private InfoBar infoBar;
	private ActionBox actionBox;
	private StackPane mainPane;
	private GameView gameView;
	private CityView cityView;
	private City currentCity;
	private ArrayList<String> buildingsToBuild;
	private CustomButton mapBtn;
	private MapView mapView;
	
	private VBox stickyButtons;
	private CustomButton initArmyBtn;
	private CustomButton relocateBtn;
	private ArrayList<CityView> cities;
	
	public GamePane(GameView gameView, City currentCity) {
		this.gameView = gameView;
		this.currentCity = currentCity;
		this.cities = new ArrayList<>();
		for(City c: gameView.getAvailableCities())
			this.cities.add(new CityView(gameView,this, c));
		
		this.initArmyBtn = new CustomButton("Initiate Army",'l');
		this.setRelocateBtn(new CustomButton("Relocate Unit",'l'));
		this.stickyButtons = new VBox();
		this.actionBox = new ActionBox(gameView, this.stickyButtons);
		this.mainPane = new StackPane();
		
		CityView firstCity =  this.getCityViewByName(currentCity.getName());
		this.setCityView(firstCity);
		
		this.mapView = new MapView(gameView, mainPane);
		
		this.setMaxWidth(this.gameView.getWidth());
		this.infoBar = new InfoBar(gameView);
		this.mapBtn = new CustomButton("Map",'s');
		mapBtn.getStyleClass().add("map-btn");
//		for(BuildingBlock b : this.cityView.getBlocks())
//			b.setListener(actionBox, this);
		mapView.setListener(this);
		this.buildingsToBuild = new ArrayList<String>();
		buildingsToBuild.add("Market");
		buildingsToBuild.add("Farm");
		buildingsToBuild.add("ArcheryRange");
		buildingsToBuild.add("Barracks");
		buildingsToBuild.add("Stable");
		mapBtn.setMaxWidth(gameView.getWidth()*0.08);
		mapBtn.setOnMouseClicked(e->{
			gameView.playClick();
			if(this.mainPane.getChildren().contains(mapView))
				this.mainPane.getChildren().remove(mapView);
			this.mainPane.getChildren().add(mapView);
			this.mapView.notifyListenersMapOpened();
			this.onMapViewOpen();
			mapBtn.setDisable(true);
		});
		initArmyBtn.setOnMouseClicked(e-> System.out.println("Initiate Army clicked"));
//		this.actionBox.getActionButtons().getChildren().add(initArmyBtn);
		
		
		
		stickyButtons.getChildren().addAll(mapBtn);
		this.mapView.setListener(actionBox);
		this.mainPane.setAlignment(Pos.CENTER);
		this.setTop(infoBar);
		this.setBottom(actionBox);
		this.setCenter(mainPane);
		
	}


	
	public Army getArmyByName(String name) {
		for (Army a : gameView.getControlledArmies())
			if(a.getArmyName().equals(name))
				return a;
		return null;
	}
	
	public CityView getCityViewByName(String name) {
		for(CityView view : this.cities)
			if(view.getCity().getName().equals(name))
				return view;
		return null;
	}
	
	public void onExitMap() {
		this.mapBtn.setDisable(false);
		this.initArmyBtn.setDisable(false);
		this.actionBox.getDetailsBox().clear();
		this.actionBox.getActionButtons().getChildren().clear();
	}

	@Override
	public void onBuildingClicked(Building b,CustomButton... buttons) {

	}



	@Override
	public void onBuild(BuildingBlock block) {
		ChoiceBox<String> choiceBox = new ChoiceBox<String>(FXCollections.observableArrayList(buildingsToBuild));
		CustomButton buildBtn = new CustomButton("Build",'m');
		if(block.getBuildingType()==null)
			buildBtn.setDisable(true);

		VBox msgContent = new VBox();
		Label buildingCost = new Label("Building cost: 1500");
		msgContent.setAlignment(Pos.CENTER);
		buildingCost.setStyle("-fx-text-fill: rgb(96, 62, 27)");
		MessagePane dialog = new MessagePane(gameView,this.mainPane,"Build", 600, 400, buildBtn , buildingCost, choiceBox);
		choiceBox.setOnAction(e->{
			block.setBuildingType(choiceBox.getValue());
			switch(choiceBox.getValue()) {
			case "Farm" : buildingCost.setText("Building cost: 1000"); break;
			case "Market": buildingCost.setText("Building cost: 1500"); break;
			case "ArcheryRange" : buildingCost.setText("Building cost: 1500"); break;
			case "Barracks" : buildingCost.setText("Building cost: 2000"); break;
			case "Stable" : buildingCost.setText("Building cost: 2500"); break;
			default :  buildingCost.setText("N/A");
			}
			buildBtn.setDisable(false);
			if(msgContent.getChildren().size()>1)
				msgContent.getChildren().remove(msgContent.getChildren().size()-1);
			
			dialog.getContent().getChildren().clear();
			dialog.getContent().getChildren().addAll(buildingCost, choiceBox);
		});
		choiceBox.setValue(buildingsToBuild.get(0));
		choiceBox.getStyleClass().add("build-dropdown");
		
		buildBtn.setOnMouseClicked(e->{
			gameView.playClick();
			try {
				Building b = block.startBuild();
				block.notifyListenersOnBuild();
				Label error = new Label("Building Already exists");
				error.setFont(Font.font(10));
				error.setStyle("-fx-text-fill: rgb(200, 53, 48)");
				if(b == null)
					dialog.getContent().getChildren().add(error);
				else
					this.mainPane.getChildren().remove(dialog);
			
			} catch (NotEnoughGoldException e1) {
				msgContent.getChildren().add(new Label(e1.getMessage()));
				buildBtn.setDisable(true);
			}
			this.gameView.getListener().updateInfo();
		});
		
		
		
		this.mainPane.getChildren().add(dialog);

	}

	

	@Override
	public void onUpgrade(BuildingBlock block) {
		
		
				try {
					this.gameView.getPlayer().upgradeBuilding(block.getBuilding());
					block.upgraded();
				} catch (NotEnoughGoldException | BuildingInCoolDownException | MaxLevelException e) {
					AlertPane alert = new AlertPane(this.mainPane, 600, 400, "Cannot upgrade as "+ e.getMessage());
					this.mainPane.getChildren().add(alert);
				}
				this.gameView.getListener().updateInfo();
				
	}



	@Override
	public void onRecruit(Building building, String buildingType) {
		String unitType="";
		switch(buildingType) {
		case "ArcheryRange": unitType = "Archer"; break;
		case "Barracks": unitType = "Infantry"; break;
		case "Stable": unitType = "Cavalry"; break;
		}
		try {
			this.gameView.getPlayer().recruitUnit(unitType, this.cityView.getCity().getName());
			
		} catch (BuildingInCoolDownException | MaxRecruitedException | NotEnoughGoldException e) {
			AlertPane alert = new AlertPane(this.mainPane, 600, 400, "Cannot recruit as "+ e.getMessage());
			this.mainPane.getChildren().add(alert);
		}
		
		
		ImageView armyImg = new ImageView("file:resources/images/army/icons/defending.png");
		armyImg.setFitWidth(100);
		armyImg.setPreserveRatio(true);
		armyImg.setOnMouseClicked(e->actionBox.onArmyClicked(this.currentCity.getDefendingArmy(), new CustomButton("Target",'l')));
		this.cityView.getDefendingArmyBox().getChildren().add(armyImg);
		displayDefendingArmy();
		
		this.actionBox.getDetailsBox().setBuilding(building);
		this.gameView.getListener().updateInfo();
	}

	

	public void displayDefendingArmy() {
		this.cityView.getDefendingArmyBox().getChildren().clear();
		ImageView armyImg = new ImageView("file:resources/images/army/icons/defending.png");
		armyImg.setFitWidth(80);
		armyImg.setPreserveRatio(true);
		armyImg.setOnMouseClicked(e->actionBox.onArmyClicked(this.cityView.getCity().getDefendingArmy()));
		this.cityView.getDefendingArmyBox().getChildren().add(armyImg);
		for(Unit u : this.cityView.getCity().getDefendingArmy().getUnits()) {
			UnitIcon img;
			if(u instanceof Archer)
				img= new UnitIcon("archer",u.getLevel());
			else if (u instanceof Infantry)
				img = new UnitIcon("infantry",u.getLevel());
			else
				img = new UnitIcon("cavalry",u.getLevel());
			img.setOnMouseClicked(e->{
				actionBox.onUnitClicked(u, relocateBtn, initArmyBtn);
			
			});
			//TODO units are added here
			this.cityView.getDefendingArmyBox().getChildren().add(img);
			this.cityView.getDefendingArmyBox().setHgap(10);
		}
		
	}
	
	
	@Override
	public void onMapViewOpen() {
		this.mapView.updateMap();
		
	}
	
	
	@Override
	public void onCityClicked(String cityName, CustomButton...buttons ) {
		
		
	}

	public CityView getCityView() {
		return cityView;
	}


//TODO
	public void setCityView(CityView cityView) {
		if(this.cityView != null)
			this.getMainPane().getChildren().remove(this.cityView);
		this.cityView = cityView;
		this.getMainPane().getChildren().add(cityView);
		
		for(BuildingBlock b : this.cityView.getBlocks())
			b.setListener(actionBox, this);
		this.currentCity = this.cityView.getCity();
		displayDefendingArmy();
	}



	
	
	
	public City getCurrentCity() {
		return currentCity;
	}



	public void setCurrentCity(City currentCity) {
		this.currentCity = currentCity;
		this.getCityView().setCity(currentCity);
	}
	public InfoBar getInfoBar() {
		return infoBar;
	}



	public void setInfoBar(InfoBar infoBar) {
		this.infoBar = infoBar;
	}



	public ActionBox getActionBox() {
		return actionBox;
	}



	public void setActionBox(ActionBox actionBox) {
		this.actionBox = actionBox;
	}



	public CustomButton getMapBtn() {
		return mapBtn;
	}



	public void setMapBtn(CustomButton mapBtn) {
		this.mapBtn = mapBtn;
	}

	public MapView getMapView() {
		return mapView;
	}

	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}

	@Override
	public void onVisitClicked(String cityName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTargetClicked(String cityName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitClicked(Unit u, CustomButton... buttons) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onArmyClicked(Army a, CustomButton... buttons) {
		// TODO Auto-generated method stub
		
	}

	public VBox getStickyButtons() {
		return stickyButtons;
	}

	public void setStickyButtons(VBox stickyButtons) {
		this.stickyButtons = stickyButtons;
	}

	public CustomButton getInitArmyBtn() {
		return initArmyBtn;
	}

	public void setInitArmyBtn(CustomButton initArmyBtn) {
		this.initArmyBtn = initArmyBtn;
	}


	public StackPane getMainPane() {
		return mainPane;
	}


	public void setMainPane(StackPane mainPane) {
		this.mainPane = mainPane;
	}


	public CustomButton getRelocateBtn() {
		return relocateBtn;
	}


	public void setRelocateBtn(CustomButton relocateBtn) {
		this.relocateBtn = relocateBtn;
	}


	public ArrayList<CityView> getCities() {
		return cities;
	}


	public void setCities(ArrayList<CityView> cities) {
		this.cities = cities;
	}

	

	

}
