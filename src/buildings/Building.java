package buildings;

import exceptions.*;

abstract public class Building {
	private int cost;
	private int level;
	private int upgradeCost;
	private boolean coolDown;
	
	public Building(int cost , int upgradeCost){
		this.cost = cost;
		this.level = 1;
		this.upgradeCost = upgradeCost;
		this.coolDown = true;
	}
//	Getters
	public int getCost() {return this.cost;}
	public int getLevel() {return this.level;}
	public int getUpgradeCost() {return this.upgradeCost;}
	public boolean isCoolDown() {return this.coolDown;}
	
//	Setters
	public void setLevel(int level) {
		this.level = level;
	}
	public void setUpgradeCost(int upgradeCost) {
		this.upgradeCost = upgradeCost;
	}
	public void setCoolDown(boolean coolDown) {
		this.coolDown = coolDown;
	}
	
	public void upgrade() throws BuildingInCoolDownException, MaxLevelException{
		if(this.coolDown)
			throw new BuildingInCoolDownException("Building is cooling down");
		if(this.getLevel() == 3)
			throw new MaxLevelException("Maximum level possible");
		coolDown = true;
	}
}





