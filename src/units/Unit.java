package units;

abstract public class Unit {
	private int level; //READ ONLY
	private int maxSoldierCount; //Read Only
	private int currentSoldierCount;
	private double idleUpkeep; //Read only
	private double marchingUpkeep; //Read only
	private double siegeUpkeep;//Read Only
	
	
	public Unit(int level,int maxSoldierCount,double idleUpkeep, double
			marchingUpkeep,double siegeUpkeep) {
		this.level = level;
		this.maxSoldierCount = maxSoldierCount;
		this.idleUpkeep = idleUpkeep;
		this.marchingUpkeep = marchingUpkeep;
		this.siegeUpkeep = siegeUpkeep;
	}
//	Getters
	public int getLevel() {
		return this.level;
	}
	public int getMaxSoldierCount() {
		return this.maxSoldierCount;
	}
	public int getCurrentSoldierCount() {
		return this.currentSoldierCount;
	}
	public double getIdleUpkeep() {
		return this.idleUpkeep;
	}
	public double getMarchingUpkeep() {
		return this.marchingUpkeep;
	}
	public double getSiegeUpkeep() {
		return this.siegeUpkeep;
	}
	
//	Setters
	public void setCurrentSoldierCount(int count) {
		this.currentSoldierCount = count;
	}
	
	
}















