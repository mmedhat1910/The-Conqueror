package units;

import java.lang.Math.*;
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
	
	
	public void attack(Unit target) throws FriendlyFireException{
		if(target.getParentArmy().equals(this.parentArmy))
			throw new FriendlyFireException("Attaching friendly army");
		int level = this.getLevel();
		int unitCount =this.getCurrentSoldierCount();
		int targetCount = target.getCurrentSoldierCount();
		
		
			target.getParentArmy().handleAttackedUnit(target);
			
		
		if(this instanceof Archer) {
			if(target instanceof Archer) {
				switch(level) {
					case 1: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.3*unitCount) ); break;
					case 2: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.4*unitCount) ); break;
					case 3: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.5*unitCount) ); break;
				}
			}else if (target instanceof Infantry) {
				switch(level) {
					case 1: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.2*unitCount) ); break;
					case 2: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.3*unitCount) ); break;
					case 3: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.4*unitCount) ); break;
				}
			}else if (target instanceof Cavalry) {
				switch(level) {
					case 1: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.1*unitCount) ); break;
					case 2: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.1*unitCount) ); break;
					case 3: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.2*unitCount) ); break;
				}
			}
		}else if(this instanceof Infantry) {
			if(target instanceof Archer) {
				switch(level) {
					case 1: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.3*unitCount) ); break;
					case 2: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.4*unitCount) ); break;
					case 3: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.5*unitCount) ); break;
				}
			}else if (target instanceof Infantry) {
				switch(level) {
					case 1: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.1*unitCount) ); break;
					case 2: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.2*unitCount) ); break;
					case 3: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.3*unitCount) ); break;
				}
			}else if (target instanceof Cavalry) {
				switch(level) {
					case 1: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.1*unitCount) ); break;
					case 2: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.2*unitCount) ); break;
					case 3: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.25*unitCount) ); break;
				}
			}
		}else if(this instanceof Cavalry) {
			if(target instanceof Archer) {
				switch(level) {
					case 1: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.5*unitCount) ); break;
					case 2: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.6*unitCount) ); break;
					case 3: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.7*unitCount) ); break;
				}
			}else if (target instanceof Infantry) {
				switch(level) {
					case 1: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.3*unitCount) ); break;
					case 2: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.4*unitCount) ); break;
					case 3: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.5*unitCount) ); break;
				}
			}else if (target instanceof Cavalry) {
				switch(level) {
					case 1: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.2*unitCount) ); break;
					case 2: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.2*unitCount) ); break;
					case 3: target.setCurrentSoldierCount(targetCount - (int) Math.ceil( 0.3*unitCount) ); break;
				}
			}
		}
		
	}
	
}















