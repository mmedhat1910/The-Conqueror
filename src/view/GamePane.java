package view;

import java.util.ArrayList;

import buildings.Building;
import buildings.MilitaryBuilding;
import engine.City;
import exceptions.BuildingInCoolDownException;
import exceptions.MaxLevelException;
import exceptions.MaxRecruitedException;
import exceptions.NotEnoughGoldException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import units.Infantry;
import units.Unit;


public class GamePane extends BorderPane implements CityViewListener{
	private InfoBar infoBar;
	private ActionBox actionBox;
	private StackPane mainPane;
	private GameView gameView;
	private CityView cityView;
	private City currentCity;
	private ArrayList<String> buildingsToBuild;
	private Button mapBtn;
	private MapView mapView;

	
	

	public GamePane(GameView gameView, City currentCity) {
		this.gameView = gameView;
		this.currentCity = currentCity;
		this.setMaxWidth(this.gameView.getWidth());
		this.infoBar = new InfoBar(gameView);
		this.mapBtn = new Button("Map");
		this.actionBox = new ActionBox(gameView, mapBtn);
		this.mainPane = new StackPane();
		this.cityView = new CityView(gameView, currentCity);
		this.mapView = new MapView(gameView, mainPane);
		for(BuildingBlock b : this.cityView.getBlocks())
			b.setListener(actionBox, this);
		
		this.buildingsToBuild = new ArrayList<String>();
		buildingsToBuild.add("Market");
		buildingsToBuild.add("Farm");
		buildingsToBuild.add("ArcheryRange");
		buildingsToBuild.add("Barracks");
		buildingsToBuild.add("Stable");
		mapBtn.setOnAction(e->{
			this.mainPane.getChildren().add(mapView);
			mapBtn.setDisable(true);
		});
			
		this.mainPane.getChildren().add(cityView);
		this.mainPane.setAlignment(Pos.CENTER);
		this.setTop(infoBar);
		this.setBottom(actionBox);
		this.setCenter(mainPane);
		
	}

	public void onExitMap() {
		this.mapBtn.setDisable(false);
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
		HBox unitsBox = new HBox();
		this.cityView.add(unitsBox,0,1,4,1);
		ImageView armyImg = new ImageView("file:resources/images/army/army-icon.png");
		armyImg.setFitWidth(100);
		armyImg.setPreserveRatio(true);
		armyImg.setOnMouseClicked(e->actionBox.onArmyCLicked(this.currentCity.getDefendingArmy(), new Button("Target")));
		unitsBox.getChildren().add(armyImg);
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
			img.setOnMouseClicked(e->actionBox.onUnitClicked(u, new Button("Relocate")));
			
			unitsBox.getChildren().add(img);
			
		}
		this.actionBox.getDetailsBox().setBuilding(building);
		this.gameView.getListener().updateInfo();
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



	

}
