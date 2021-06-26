package view;

import java.util.ArrayList;

import buildings.ArcheryRange;
import buildings.Barracks;
import buildings.Building;
import buildings.Farm;
import buildings.Market;
import buildings.MilitaryBuilding;
import buildings.Stable;
import engine.City;
import engine.Player;
import exceptions.BuildingInCoolDownException;
import exceptions.MaxLevelException;
import exceptions.MaxRecruitedException;
import exceptions.NotEnoughGoldException;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class BuildingBlock {
	private Building building;
	private String buildingType;
	private City city;
	
	private Player player;
	private GameView gameView;
	private double maxWidth;
	private ImageView image;
	private CityView cityView;
	private ArrayList<CityViewListener> listeners;
	
	Button buildBtn;
	Button upgradeBtn ;
	Button recruitBtn;


	



	public BuildingBlock(Building building, GameView gameView, CityView cityView) {
		this.gameView = gameView;
		this.building = building;
		this.cityView = cityView;
		this.listeners = new ArrayList<>();
		this.maxWidth = gameView.getWidth()*0.19;
		this.image = new ImageView();

		buildBtn = new Button("Build");
		upgradeBtn = new Button("Upgrade");
		recruitBtn = new Button("Recruit");
		
		this.player = this.gameView.getPlayer();
		this.city = this.cityView.getCity();
		
		buildBtn.setOnAction(e1 -> {
			for(CityViewListener l: this.listeners)
				l.onBuild(this);
		});
		
		upgradeBtn.setOnAction(e2 -> {
			for(CityViewListener l: this.listeners)
				l.onUpgrade(this);
		});
		
		recruitBtn.setOnAction(e3->{
			for(CityViewListener l: this.listeners)
				l.onRecruit(this.building, this.buildingType);
		});
		
		

		
		image.setImage(getBlockImage());
		image.setPreserveRatio(true);
		image.setFitWidth(maxWidth);	
		image.setOnMouseClicked(e->{
			if(this.building == null)
				for(CityViewListener l: this.listeners)
					l.onBuildingClicked(this.building, buildBtn);
			else
				notifyListenersOnBuild();
			
		});
	}
	
	
	public Building startBuild() throws NotEnoughGoldException {
		building = player.build(buildingType, city.getName());
		if(building != null) {
			image.setImage(getBlockImage());
		}
		
		return building;
	}
	
	public void upgraded() {
		image.setImage(getBlockImage());
		notifyListenersOnBuild();
	}
	
	public void notifyListenersOnBuild() {
		for(CityViewListener l: this.listeners) {
			if(this.building instanceof Farm || this.building instanceof Market )
				l.onBuildingClicked(this.building,upgradeBtn);
			else{
				l.onBuildingClicked(this.building,upgradeBtn, recruitBtn);
			}
		}
	}
	
	private Image getBlockImage() {
		String path="file:resources/images/buildings/empty-block.png";
		Building b = this.building;
		if(b instanceof ArcheryRange) {
			path = "file:resources/images/buildings/archery-range" + b.getLevel()+".png";
			buildingType = "ArcheryRange";
		}else if(b instanceof Barracks) {
			path = "file:resources/images/buildings/barracks" + b.getLevel()+".png";
			buildingType = "Barracks";
		}else if(b instanceof Stable) {
			path = "file:resources/images/buildings/stable" + b.getLevel()+".png";
			buildingType = "Stable";
		}else if(b instanceof Farm) {
			path = "file:resources/images/buildings/farm" + b.getLevel()+".png";
			buildingType = "Farm";
		}else if(b instanceof Market) {
			path = "file:resources/images/buildings/market" + b.getLevel()+".png";
			buildingType = "Market";
		}
		return new Image(path);
	}


	
	public Building getBuilding() {
		return building;
	}
	public void setBuilding(Building building) {
		this.building = building;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}
	public ImageView getImage() {
		return this.image;
	}
	
	public String getBuildingType() {
		return buildingType;
	}



	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	public ArrayList<CityViewListener> getListeners() {
		return listeners;
	}



	public void setListeners(ArrayList<CityViewListener> listeners) {
		this.listeners = listeners;
	}
	
	public void setListener(CityViewListener... listeners) {
		for(CityViewListener l : listeners)
			this.listeners.add(l);
	}



	



}
