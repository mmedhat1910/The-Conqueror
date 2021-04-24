package units;

public class Unit {
	private int level; //READ ONLY
	private int maxSoldierCount; //Read Only
	private int currentSoldierCount;
	private double idleUpkeep; //Read only
	private double marchingUpKeep; //Read only
	private double siegeUpKeep;//Read Only
	
	
	public Unit(int level,int maxSoldierConunt,double idleUpkeep, double
			marchingUpkeep,double siegeUpkeep) {
		this.level = level;
		this.marchingUpKeep = maxSoldierConunt;
		this.idleUpkeep = idleUpkeep;
		this.marchingUpKeep = marchingUpkeep;
		this.siegeUpKeep = siegeUpkeep;
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
	public double getIdleUpKeep() {
		return this.idleUpkeep;
	}
	public double getMarchingUpKeep() {
		return this.marchingUpKeep;
	}
	public double getSiegeUpKeep() {
		return this.siegeUpKeep;
	}
	
//	Setters
	public void setCurrentSoldierCount(int count) {
		this.currentSoldierCount = count;
	}
	
	
}















