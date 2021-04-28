package engine;

import java.util.ArrayList;
import units.Army;

public class Player {
	private String name;  //READ ONLY
	private ArrayList<City> controlledCities; //READ ONLY
	private ArrayList<Army> controlledArmies; //READ ONLY
	private double treasury;
	private double food;
	
	public Player(String name) {
		this.name = name;
		this.controlledArmies = new ArrayList<Army>();
		this.controlledCities = new ArrayList<City>();
	}
	
	
//	Getters
	public String getName() {
		return this.name;
	}
	public ArrayList<City> getControlledCities(){
		return this.controlledCities;
	}
	public ArrayList<Army> getControlledArmies(){
		return this.controlledArmies;
	}
	public double getTreasury() {
		return this.treasury;
	}
	public double getFood() {
		return this.food;
	}
	
//	Setters
	public void setTreasury(double treasury) {
		this.treasury = treasury;
	}
	public void setFood(double food) {
		this.food = food;
	}
	
}
