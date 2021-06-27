package view;


import java.util.ArrayList;

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
		
		Button closeMap = new Button("Close");
		closeMap.setOnAction(e->{
			parent.getChildren().remove(this);
			gameView.getGamePane().onExitMap();
		});
		closeMap.setMinWidth(100);
		this.getChildren().add(closeMap);
		closeMap.relocate(width-100, 0);
		
		rome = new MapCity("Rome", this, (this.width*0.3)-300,  (this.height*0.1));
		sparta = new MapCity("Sparta", this,(this.width*0.7),  (this.height*0.1));
		cairo = new MapCity("Cairo", this, (this.width*0.5)-150, (this.height*0.6));
		
		
		
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
	public void locateArmies(Army army, int idx) {
		MapArmy a = new MapArmy(this, army, null, 50);
		this.getChildren().add(a);
		if(army.getCurrentStatus() != Status.MARCHING) {
			if(army.getCurrentLocation().toLowerCase().equals("rome"))
				a.relocate(rome.getxCoordinate()+(50*(idx%5)), rome.getyCoordinate()+70);
			else if(army.getCurrentLocation().toLowerCase().equals("sparta"))
				a.relocate(sparta.getxCoordinate()+(50*(idx%5)), sparta.getyCoordinate()+70);
			else if(army.getCurrentLocation().toLowerCase().equals("cairo"))
				a.relocate(cairo.getxCoordinate()+(50*(idx%5)+20), cairo.getyCoordinate()+50);
//				a.relocate(rome.getxCoordinate()+(50*(idx%5)), rome.getxCoordinate()+70*(idx/5));
		}else {
			double x = Math.random()*100 + this.width*0.5;
			double y = Math.random()*100 + this.height*0.3;
			a.relocate(x, y);
			//TODO: do the math
		}
	}
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
	public void notifyListenersCityClicked(String cityName, Button...buttons) {
		for(MapViewListener l : listeners)
			l.onCityClicked(cityName, buttons);
	}
	public void notifyListenersArmyClicked(Army a, Button... buttons) {
		for(MapViewListener l : listeners)
			l.onArmyClicked(a, buttons);
	}
	public void showUnitsInfo(Army army) {
		TextArea unitsData = new TextArea();
		unitsData.setEditable(false);
		String data="";
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
		parent.getChildren().add(new MessagePane(parent, "Army Units", 700, 700, null,unitsData ));
	}
	public void onTargetBtnClicked(Army army) {
		gameView.handleTargetClicked(army);
	}
	public void onVisitClicked(String cityName) {
		gameView.visitCity(cityName);
		
	}
}
