package engine;
import java.util.ArrayList;
import buildings.*;
import units.Army;

public class City {
	private String name; //READ ONLY
	private ArrayList<EconomicBuilding> economicalBuildings; //READ ONLY
	private ArrayList<MilitaryBuilding> militaryBuildings; //READ ONLY
	private Army defendingArmy;
	private int turnsUnderSiege;
	private boolean underSiege;
	
	public City(String name) {
		this.name = name;
		this.underSiege = false;
	}
	
	
//	Getters
	public String getName() {
		return this.name;
	}
	public ArrayList<EconomicBuilding> getEconomicalBuildings(){
		return this.economicalBuildings;
	}
	public ArrayList<MilitaryBuilding> getMilitaryBuildings(){
		return this.militaryBuildings;
	}
	public Army getDefendingArmy() {
		return this.defendingArmy;
	}
	public int getTurnsUnderSiege() {
		return this.turnsUnderSiege;
	}
	public boolean isUnderSiege() {
		return this.underSiege;
	}
	
//	Setters
	public void setDefendingArmy(Army defendingArmy) {
		this.defendingArmy = defendingArmy;
	}
	public void setTurnsUnderSiege(int turnsUnderSiege) {
		this.turnsUnderSiege = turnsUnderSiege;
	}
	public void setUnderSiege(boolean underSiege) {
		this.underSiege = underSiege;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
