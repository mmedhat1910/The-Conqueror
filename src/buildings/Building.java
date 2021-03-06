package buildings;

import exceptions.*;

abstract public class Building {
	private int cost;
	private int level;
	private int upgradeCost;
	private boolean coolDown;
	private String type;
	
	public Building(int cost , int upgradeCost, String type){
		this.cost = cost;
		this.level = 1;
		this.upgradeCost = upgradeCost;
		this.coolDown = true;
		this.setType(type);
	}
	public String toString() {
		return this.type +"\nLevel: "+this.level+"\nUpgrade Cost: "+this.upgradeCost ;//+"\nBuilding in cooldown: "+this.coolDown;
	}
	
	public  void upgrade() throws BuildingInCoolDownException, MaxLevelException
	{
		if(coolDown)
			throw new BuildingInCoolDownException("Building is in cool down. Wait for the next turn ");
		if(level==3)
			throw new MaxLevelException("Maximum level reached!!");
		coolDown=true;
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
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}





