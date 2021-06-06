package units;

import java.io.IOException;

import java.util.ArrayList;

import engine.Game;
import exceptions.FriendlyFireException;

abstract public class Unit {
	private int level; //READ ONLY
	private int maxSoldierCount; //Read Only
	private int currentSoldierCount;
	private double idleUpkeep; //Read only
	private double marchingUpkeep; //Read only
	private double siegeUpkeep;//Read Only
	private Army parentArmy;
	
	
	
	public Unit(int level,int maxSoldierCount,double idleUpkeep, double
			marchingUpkeep,double siegeUpkeep) {
		this.level = level;
		this.maxSoldierCount = maxSoldierCount;
		this.idleUpkeep = idleUpkeep;
		this.marchingUpkeep = marchingUpkeep;
		this.siegeUpkeep = siegeUpkeep;
		this.currentSoldierCount = maxSoldierCount;
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
	
	public Army getParentArmy() {
		return parentArmy;
	}
//	Setters
	public void setCurrentSoldierCount(int count) {
		this.currentSoldierCount = count;
	}
	
	public void setParentArmy(Army parentArmy) {
		this.parentArmy = parentArmy;
	}
	
	
	public void attack(Unit target) throws FriendlyFireException, IOException{
		if(target.getParentArmy().equals(this.parentArmy))
			throw new FriendlyFireException("Attaching friendly army");
		int level = this.getLevel();
		int unitCount =this.getCurrentSoldierCount();
		int targetCount = target.getCurrentSoldierCount();
		
		
			target.getParentArmy().handleAttackedUnit(target);
			
		
		if(this instanceof Archer) {
			ArrayList<String> factors = Game.readCSV("attacking_factors/archer_attack.csv");
			int factor_index;
			
			if(target instanceof Archer) {
				factor_index = level;
			}else if (target instanceof Infantry) {
				factor_index = level +3;
			}else {
				factor_index = level + 6;
			}
			String[] row = factors.get(factor_index).split(",");
			double factor = Double.parseDouble(row[2]);
			
			int newtargetSoldierCount = targetCount - (int) Math.ceil(factor*unitCount);
			if(newtargetSoldierCount < 0) 
				target.setCurrentSoldierCount(0);
			else
				target.setCurrentSoldierCount(newtargetSoldierCount);
			target.getParentArmy().handleAttackedUnit(target);
			
		}else if(this instanceof Infantry) {
			ArrayList<String> factors = Game.readCSV("attacking_factors/infantry_attack.csv");
			int factor_index;
			
			if(target instanceof Archer) {
				factor_index = level;
			}else if (target instanceof Infantry) {
				factor_index = level +3;
			}else {
				factor_index = level + 6;
			}
			String[] row = factors.get(factor_index).split(",");
			double factor = Double.parseDouble(row[2]);
			int newtargetSoldierCount = targetCount - (int) Math.ceil(factor*unitCount);
			if(newtargetSoldierCount < 0) 
				target.setCurrentSoldierCount(0);
			else
				target.setCurrentSoldierCount(newtargetSoldierCount);
			target.getParentArmy().handleAttackedUnit(target);
		}else if(this instanceof Cavalry) {
			ArrayList<String> factors = Game.readCSV("attacking_factors/cavalry_attack.csv");
			int factor_index;
			
			if(target instanceof Archer) {
				factor_index = level;
			}else if (target instanceof Infantry) {
				factor_index = level +3;
			}else {
				factor_index = level + 6;
			}
			String[] row = factors.get(factor_index).split(",");
			double factor = Double.parseDouble(row[2]);
			int newtargetSoldierCount = targetCount - (int) Math.ceil(factor*unitCount);
			if(newtargetSoldierCount < 0) 
				target.setCurrentSoldierCount(0);
			else
				target.setCurrentSoldierCount(newtargetSoldierCount);
			
			target.getParentArmy().handleAttackedUnit(target);
			
		}
		
	}
	
}















