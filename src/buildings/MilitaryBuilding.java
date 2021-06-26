package buildings;

import exceptions.*;
import units.Unit;

abstract public class MilitaryBuilding extends Building{
	private int recruitmentCost;
	private int currentRecruit;
	private final int maxRecruit = 3;
	
	public MilitaryBuilding(int cost, int upgrdeCost, int recruitmentCost, String type) {
		super(cost, upgrdeCost, type);
		this.recruitmentCost = recruitmentCost;
		
	}
	public String toString() {
		return super.toString() + "\nRecruitment Cost: "+this.recruitmentCost+"\nRecruitment Count: "+this.currentRecruit;
		
	}
//	Getters
	public int getRecruitmentCost() {
		return this.recruitmentCost;
	}
	public int getCurrentRecruit() {
		return this.currentRecruit;
	}
	public int getMaxRecruit() {
		return this.maxRecruit;
	}
	
//	Setters
	public void setRecruitmentCost(int recruitmentCost) {
		this.recruitmentCost = recruitmentCost;
	}
	public void setCurrentRecruit(int currentRecruit) {
		this.currentRecruit = currentRecruit;
	}
	
	public abstract Unit recruit() throws BuildingInCoolDownException, MaxRecruitedException;
	
}
