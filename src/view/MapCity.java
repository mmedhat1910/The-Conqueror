package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MapCity extends ImageView{
	private String cityName;
	private Pane parent;
	private double xCoordinate;
	private double yCoordinate;
	public MapCity(String cityName, Pane parent, double xCoordinate, double yCoordinate) {
		this.setFitWidth(300);
		this.cityName = cityName;
		this.parent = parent;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		
		this.setImage(new Image("file:resources/images/map/"+this.cityName+".png"));
		this.setPreserveRatio(true);
		this.relocate(xCoordinate, yCoordinate);
		
		this.setOnMouseClicked(e->{
			System.out.println(this.cityName);
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
