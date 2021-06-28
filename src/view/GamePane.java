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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
	private Button mapBtn;
	private MapView mapView;
	
	private VBox stickyButtons;
	private Button initArmyBtn;
	private Button relocateBtn;
	private FlowPane defendingArmyBox;
	private ArrayList<CityView> cities;
	
	public GamePane(GameView gameView, City currentCity) {
		this.gameView = gameView;
		this.currentCity = currentCity;
		this.cities = new ArrayList<>();
		for(City c: gameView.getAvailableCities())
			this.cities.add(new CityView(gameView, c));
		
		this.initArmyBtn = new Button("Initiate Army");
		this.setRelocateBtn(new Button("Relocate Unit"));
		this.stickyButtons = new VBox();
		this.actionBox = new ActionBox(gameView, this.stickyButtons);
		this.mainPane = new StackPane();
		this.cityView = this.getCityViewByName(currentCity.getName());
		
		this.mapView = new MapView(gameView, mainPane);
		this.defendingArmyBox = new FlowPane();
		this.cityView.add(defendingArmyBox,0,1,4,1);
		this.setMaxWidth(this.gameView.getWidth());
		this.infoBar = new InfoBar(gameView);
		this.mapBtn = new Button("Map");
		
		for(BuildingBlock b : this.cityView.getBlocks())
			b.setListener(actionBox, this);
		mapView.setListener(this);
		this.buildingsToBuild = new ArrayList<String>();
		buildingsToBuild.add("Market 1500");
		buildingsToBuild.add("Farm 1000");
		buildingsToBuild.add("ArcheryRange 1500");
		buildingsToBuild.add("Barracks 2000");
		buildingsToBuild.add("Stable 2500");
		mapBtn.setOnAction(e->{
			if(this.mainPane.getChildren().contains(mapView))
				this.mainPane.getChildren().remove(mapView);
			this.mainPane.getChildren().add(mapView);
			this.mapView.notifyListenersMapOpened();
			this.onMapViewOpen();
			mapBtn.setDisable(true);
		});
		initArmyBtn.setOnAction(e-> System.out.println("Initiate Army clicked"));
//		this.actionBox.getActionButtons().getChildren().add(initArmyBtn);
		
		
		
		stickyButtons.getChildren().addAll(mapBtn);
		this.mapView.setListener(actionBox);
		this.mainPane.getChildren().add(cityView);
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
	public void onBuildingClicked(Building b,Button... buttons) {
		this.setBottom(actionBox);
	}



	@Override
	public void onBuild(BuildingBlock block) {
		ChoiceBox<String> choiceBox = new ChoiceBox<String>(FXCollections.observableArrayList(buildingsToBuild));
		Button buildBtn = new Button("Build");
		
		if(block.getBuildingType()==null)
			buildBtn.setDisable(true);

		VBox msgContent = new VBox();
		msgContent.getChildren().add(choiceBox);
		choiceBox.setOnAction(e->{
			block.setBuildingType(choiceBox.getValue());
			buildBtn.setDisable(false);
			if(msgContent.getChildren().size()>1)
				msgContent.getChildren().remove(msgContent.getChildren().size()-1);
		});
		choiceBox.setValue(buildingsToBuild.get(0));
		choiceBox.getStyleClass().add("build-dropdown");
		MessagePane dialog = new MessagePane(this.mainPane,"Build", this.gameView.getWidth()*0.4, this.gameView.getHeight()*0.4, buildBtn , msgContent);
		
		buildBtn.setOnAction(e->{
			try {
				Building b = block.startBuild();
				block.notifyListenersOnBuild();
				if(b == null)
					msgContent.getChildren().add(new Label("Building Already exists"));
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
					AlertPane alert = new AlertPane(this.mainPane, 500, 300, "Cannot upgrade as "+ e.getMessage());
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
			this.gameView.getPlayer().recruitUnit(unitType, this.currentCity.getName());
			
		} catch (BuildingInCoolDownException | MaxRecruitedException | NotEnoughGoldException e) {
			AlertPane alert = new AlertPane(this.mainPane, 500, 300, "Cannot recruit as "+ e.getMessage());
			this.mainPane.getChildren().add(alert);
		}
		
		
		ImageView armyImg = new ImageView("file:resources/images/army/army-icon.png");
		armyImg.setFitWidth(100);
		armyImg.setPreserveRatio(true);
		armyImg.setOnMouseClicked(e->actionBox.onArmyClicked(this.currentCity.getDefendingArmy(), new Button("Target")));
		defendingArmyBox.getChildren().add(armyImg);
		displayDefendingArmy();
		
		this.actionBox.getDetailsBox().setBuilding(building);
		this.gameView.getListener().updateInfo();
	}

	

	public void displayDefendingArmy() {
		defendingArmyBox.getChildren().clear();
		ImageView armyImg = new ImageView("file:resources/images/army/army-icon.png");
		armyImg.setFitWidth(100);
		armyImg.setPreserveRatio(true);
		armyImg.setOnMouseClicked(e->actionBox.onArmyClicked(this.currentCity.getDefendingArmy()));
		defendingArmyBox.getChildren().add(armyImg);
		for(Unit u : this.currentCity.getDefendingArmy().getUnits()) {
			ImageView img = new ImageView();
			if(u instanceof Archer)
				img.setImage(new Image("file:resources/images/army/archer"+u.getLevel()+".png"));
			else if (u instanceof Infantry)
				img.setImage(new Image("file:resources/images/army/infantry"+u.getLevel()+".png"));
			else
				img.setImage(new Image("file:resources/images/army/cavalry"+u.getLevel()+".png"));
			img.setFitWidth(100);
			img.setPreserveRatio(true);
			img.setOnMouseClicked(e->{
				actionBox.onUnitClicked(u, relocateBtn, initArmyBtn);
			
			});
			//TODO units are added here
			defendingArmyBox.getChildren().add(img);
		}
		
	}
	
	
	@Override
	public void onMapViewOpen() {
		for(City c: gameView.getAvailableCities())
			if(gameView.getControlledCities().contains(c))
				this.mapView.locateDefendingArmy(c.getName(), c.getDefendingArmy());
		int i=0;
		for(Army a: gameView.getControlledArmies()) {
			this.mapView.locateArmies(a, i);
			i++;
		}
	}
	
	
	@Override
	public void onCityClicked(String cityName, Button...buttons ) {
		
		
	}

	public CityView getCityView() {
		return cityView;
	}



	public void setCityView(CityView cityView) {
		this.cityView = cityView;
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



	public Button getMapBtn() {
		return mapBtn;
	}



	public void setMapBtn(Button mapBtn) {
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
	public void onUnitClicked(Unit u, Button... buttons) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onArmyClicked(Army a, Button... buttons) {
		// TODO Auto-generated method stub
		
	}

	public VBox getStickyButtons() {
		return stickyButtons;
	}

	public void setStickyButtons(VBox stickyButtons) {
		this.stickyButtons = stickyButtons;
	}

	public Button getInitArmyBtn() {
		return initArmyBtn;
	}

	public void setInitArmyBtn(Button initArmyBtn) {
		this.initArmyBtn = initArmyBtn;
	}


	public StackPane getMainPane() {
		return mainPane;
	}


	public void setMainPane(StackPane mainPane) {
		this.mainPane = mainPane;
	}


	public Button getRelocateBtn() {
		return relocateBtn;
	}


	public void setRelocateBtn(Button relocateBtn) {
		this.relocateBtn = relocateBtn;
	}


	public ArrayList<CityView> getCities() {
		return cities;
	}


	public void setCities(ArrayList<CityView> cities) {
		this.cities = cities;
	}

	

	

}
