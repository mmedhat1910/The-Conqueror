package view;

import engine.City;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import units.Army;

public class MapCity extends ImageView{
	private String cityName;
	private MapView parent;
	private double xCoordinate;
	private double yCoordinate;
	private GameView gameView;
	public MapCity(String cityName, MapView p, GameView gameV, double xCoordinate, double yCoordinate) {
		this.gameView = gameV;
		this.setFitWidth(300);
		this.cityName = cityName;
		this.parent = p;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		
		this.setImage(new Image("file:resources/images/map/"+this.cityName+".png"));
		this.setPreserveRatio(true);
		this.relocate(xCoordinate, yCoordinate);
		
		CustomButton visitBtn =  new CustomButton("Visit City",'l');
		visitBtn.setOnMouseClicked(e->{
			parent.onVisitClicked(cityName);
		});
		

		this.setOnMouseClicked(e->{
			ActionBox action = this.gameView.getGamePane().getActionBox();
			action.getActionButtons().getChildren().clear();
			City c = getCityByName(this.cityName);
			if (gameView.getControlledCities().contains(c)) {
				action.getDetailsBox().setText(cityName + " is Controlled");
				action.getActionButtons().getChildren().add(visitBtn);
			}else {
				action.getDetailsBox().setText(cityName + " is Enemy");
			}
		});
	
	}
	
	
	public City getCityByName(String name) {
		for(City c: gameView.getAvailableCities())
			if(c.getName().equals(name))
				return c;
		return null;
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	
	
	public double getxCoordinate() {
		return xCoordinate;
	}
	public void setxCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
	}
	public double getyCoordinate() {
		return yCoordinate;
	}
	public void setyCoordinate(double yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
	
}
