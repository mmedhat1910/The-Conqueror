package view;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import engine.City;
import engine.Distance;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import units.Army;
import units.Status;
import units.Unit;

public class MapView extends Pane {
	private ArrayList<MapViewListener> listeners;
	private GameView gameView;
	private double width;
	private double height;
	private MapCity rome;
	private MapCity sparta;
	private MapCity cairo;
	private Pane parent;
	private Button closeMap ;
//	private MapArmy mapArmy;
	private ArrayList<MapArmy> mapArmies; 
	public MapView(GameView gameView, Pane parent) {
		this.gameView = gameView;
		this.parent = parent;
		this.getStyleClass().add("map-view");
		this.listeners = new ArrayList<MapViewListener>();
		this.width = gameView.getWidth();
		this.height = gameView.getHeight() - (gameView.getHeight()*0.15 + 50);
		this.minWidth(width);
		this.minHeight(height);
		this.setMaxWidth(width);
		this.setMaxHeight(height);
		this.mapArmies = new ArrayList<>();
//		this.mapArmy= new MapArmy(this, new Army("", "123"), null, 50);
		closeMap= new Button("Close");
		closeMap.setOnAction(e->{
			parent.getChildren().remove(this);
			gameView.getGamePane().onExitMap();
		});
		closeMap.setMinWidth(100);
		
		
		rome = new MapCity("Rome", this, gameView, (this.width*0.3)-300,  (this.height*0.1));
		sparta = new MapCity("Sparta", this, gameView,(this.width*0.7),  (this.height*0.1));
		cairo = new MapCity("Cairo", this,gameView ,(this.width*0.5)-150, (this.height*0.6));
	}
	
	
	public void updateMap() {
		this.getChildren().clear();
		locateCloseBtn();
		locateCities();
		try {
			locateArmies();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(City c: gameView.getAvailableCities())
			if(gameView.getControlledCities().contains(c))
				this.locateDefendingArmy(c.getName(), c.getDefendingArmy());
		
	}
	public void locateCloseBtn() {
		this.getChildren().add(closeMap);
		closeMap.relocate(width-100, 0);
	}
	public void locateCities() {
		this.getChildren().addAll(rome, sparta, cairo);
	}
	public void locateDefendingArmy(String cityName, Army defendingArmy) {
		MapArmy army = new MapArmy(this,defendingArmy, cityName+" Defenders", 100);
		this.getChildren().add(army);
		if(cityName.equals("Cairo"))
			army.relocate((this.width*0.5), (this.height*0.6)-50);
		else if(cityName.equals("Rome"))
			army.relocate((this.width*0.3)-50, (this.height*0.1));
		else
			army.relocate((this.width*0.7)-70, (this.height*0.1));
			
	}

	
	
	
	public void locateArmies() throws Exception {
		int idx =0;
		for(Army army: gameView.getControlledArmies()) {
			MapArmy armyImg = new MapArmy(this,army,null, 50);
			this.getChildren().add(armyImg);
			if(army.getCurrentStatus() == Status.MARCHING) {
				double[] position = getMarchingArmyPosition(army);
				armyImg.relocate(position[0]+(idx+10), position[1]);
				System.out.println(Arrays.toString(position));
				idx++;
			}
			else {
				locateArmyInCity(armyImg, army, idx);
				idx++;
			}
		}
	}
	
	
	public double[] getMarchingArmyPosition(Army army) throws Exception {
		String fromCity = army.getInitialLocation();
		String toCity = army.getTarget();
		MapCity targetCity = getMapCityByName(toCity);
		MapCity parentCity = getMapCityByName(fromCity);
		double xMapDistance = targetCity.getxCoordinate() - parentCity.getxCoordinate();
		double yMapDistance =  targetCity.getyCoordinate() - parentCity.getyCoordinate();
		double angle = Math.atan2(yMapDistance, xMapDistance);
		
		double actualDistance = getDistance(fromCity, toCity);
		if(actualDistance == -1)
			throw new Exception("Distance loaded wrong");
		
		double stepRatio = (actualDistance-army.getDistancetoTarget()+1)/actualDistance;
		System.out.println(stepRatio);
		double x = parentCity.getxCoordinate() + 100 +(stepRatio * xMapDistance * Math.abs(Math.cos(angle) )); 
		double y = parentCity.getyCoordinate() + (stepRatio * yMapDistance *  Math.abs(Math.sin(angle) )); 
		double[] coordinates = {x,y};
		return coordinates;
	}
	
	public void locateArmyInCity(MapArmy mapArmy, Army army , int idx){
		if(army.getCurrentLocation().toLowerCase().equals("rome"))
			mapArmy.relocate(rome.getxCoordinate()+(50*(idx%5)) +100, rome.getyCoordinate()+70);
		else if(army.getCurrentLocation().toLowerCase().equals("sparta"))
			mapArmy.relocate(sparta.getxCoordinate()+(50*(idx%5)), sparta.getyCoordinate()+70);
		else if(army.getCurrentLocation().toLowerCase().equals("cairo"))
			mapArmy.relocate(cairo.getxCoordinate()+(50*(idx%5)+20), cairo.getyCoordinate()+50);
	}
	
	public MapCity getMapCityByName(String name) {
		switch(name) {
			case "Cairo":return cairo;
			case "Rome" : return rome;
			case "Sparta": return sparta;
		}
		return null;
	}
	
	public double getDistance(String city1, String city2) {
		for(Distance d: gameView.getDistances()) {
			if((d.getFrom().equals(city1)&&d.getTo().equals(city2)) || (d.getFrom().equals(city2)&&d.getTo().equals(city1))){
				return d.getDistance();
			}
		}
		return -1;
	}
	
	
	
	
	
//	public void locateArmies1(Army army, int idx) {
//		String[] str = army.getArmyName().split(" ");
//		MapArmy mapArmy = mapArmies.get(idx);
//		mapArmy.setArmy(army);
////		marchingArmy.setC
//		if(this.getChildren().contains(mapArmy))
//			this.getChildren().remove(mapArmy);
//
//		for(String s: str)
//			if(s.equals("defenders")) {
//				this.getChildren().remove(mapArmy);
//				return;
//				
//			}
//		if(army.getUnits().size()==0) {
//			this.getChildren().remove(mapArmy);
//			return;
//		}
//			
//		
//		this.getChildren().add(mapArmy);
//		if(army.getCurrentStatus() != Status.MARCHING) {
//			if(army.getCurrentLocation().toLowerCase().equals("rome"))
//				mapArmy.relocate(rome.getxCoordinate()+(50*(idx%5)), rome.getyCoordinate()+70);
//			else if(army.getCurrentLocation().toLowerCase().equals("sparta"))
//				mapArmy.relocate(sparta.getxCoordinate()+(50*(idx%5)), sparta.getyCoordinate()+70);
//			else if(army.getCurrentLocation().toLowerCase().equals("cairo"))
//				mapArmy.relocate(cairo.getxCoordinate()+(50*(idx%5)+20), cairo.getyCoordinate()+50);
////				a.relocate(rome.getxCoordinate()+(50*(idx%5)), rome.getxCoordinate()+70*(idx/5));
//		}else {
//			double x = Math.random()*100 + this.width*0.5;
//			double y = Math.random()*100 + this.height*0.3;
//			mapArmy.relocate(x, y);
//			System.out.println(mapArmy);
////			System.out.println("locate armies");
//			//TODO: do the math
//		}
//	}
	
	
	public void setListener(MapViewListener... listeners) {
		for(MapViewListener l : listeners)
			this.listeners.add(l);
	}
	public void setListeners(ArrayList<MapViewListener> listeners) {
		this.listeners = listeners;
	}
	public void notifyListenersMapOpened() {
		for(MapViewListener l : listeners)
			l.onMapViewOpen();
	}
	public void notifyListenersCityClicked(String cityName, CustomButton...buttons) {
		for(MapViewListener l : listeners)
			l.onCityClicked(cityName, buttons);
	}
	public void notifyListenersArmyClicked(Army a, CustomButton... buttons) {
		for(MapViewListener l : listeners)
			l.onArmyClicked(a, buttons);
	}
	public void showUnitsInfo(Army army) {
		TextArea unitsData = new TextArea();
		unitsData.setEditable(false);
		String data="";
//		TODO
		for(Unit u: army.getUnits()) {
			data+=u.getClass().getSimpleName();
			data+="\nLevel: "+u.getLevel();
			data+="\nSoldier Count: "+u.getCurrentSoldierCount();
			data+="\nMax Soldier Count: "+u.getMaxSoldierCount();
			data+="\n ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
		}
		System.out.println();
		unitsData.setText(data);	
		unitsData.setMinHeight(500);
		parent.getChildren().add(new MessagePane(gameView,parent, "Army Units", 700, 700, null,unitsData ));
	}
	public void onTargetBtnClicked(Army army) {
		gameView.handleTargetClicked(army);
	}
	public void onVisitClicked(String cityName) {
		gameView.visitCity(cityName);
		
	}
	public ArrayList<MapArmy> getMapArmies() {
		return mapArmies;
	}
	public void setMapArmies(ArrayList<MapArmy> mapArmies) {
		this.mapArmies = mapArmies;
	}
}
