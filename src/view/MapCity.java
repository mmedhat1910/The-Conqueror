package view;

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
	public MapCity(String cityName, MapView p, double xCoordinate, double yCoordinate) {
		this.setFitWidth(300);
		this.cityName = cityName;
		this.parent = p;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		
		this.setImage(new Image("file:resources/images/map/"+this.cityName+".png"));
		this.setPreserveRatio(true);
		this.relocate(xCoordinate, yCoordinate);
		
		Button visitBtn =  new Button("Visit City");
		Button targetBtn = new Button("Target");
		visitBtn.setOnAction(e->{
			System.out.println("Visit" + cityName);
		});
		targetBtn.setOnAction(e->{
			System.out.println("Target" + cityName);
		});
		
		this.setOnMouseClicked(e->{
			parent.notifyListenersCityClicked(cityName, visitBtn, targetBtn);
		});
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
