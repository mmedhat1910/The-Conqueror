package units;

import java.util.*;

import exceptions.MaxCapacityException;

public class Army {
	private Status currentStatus; //Initially IDLE
	private ArrayList<Unit> units;
	private int distancetoTarget; //Initially -1
	private String target;
	private String currentLocation;
	private boolean targetReached = false;
	
	private String armyName;
	private final int maxToHold = 15; //READ ONLY
	
	public Army(String... args) {
		this.currentLocation= args[0]; 
		this.currentStatus = Status.IDLE;
		this.units = new ArrayList<Unit>();
		this.distancetoTarget = -1;
		this.target = "";
		if(args.length>1)
			this.armyName = args[1];
	}
	public String toString() {
		String s= "";
		if(this.getArmyName()!=null)
			s+=this.getArmyName() + "\n";
		s+="Army Location: "+ this.getCurrentLocation();
		s+= "\nStatus: "+this.getCurrentStatus().toString();
		s+= "\nCurrent Units Count: "+this.getUnits().size();
		if(!target.equals("")) {
			s+= "\nTarget: "+this.getTarget();
			s+= "\nTurns left to target: "+this.getDistancetoTarget();
		}else {
			s+= "\nTarget: N/A";
			s+= "\nTurns left to target: N/A";
		}
		
		return s;
	}
//	Getter
	public Status getCurrentStatus() {
		return this.currentStatus;
	}
	public ArrayList<Unit> getUnits(){
		return this.units;
	}
	public int getDistancetoTarget() {
		return this.distancetoTarget;
	}
	public String getTarget() {
		return this.target;
	}
	public String getCurrentLocation() {
		return this.currentLocation;
	}
	public int getMaxToHold() {
		return this.maxToHold;
	}
	
//	Setters
	public void setCurrentStatus(Status status) {
		this.currentStatus = status;
	}
	public void setUnits(ArrayList<Unit> units) {
		this.units = units;
	}
	public void setDistancetoTarget(int distancetoTarget) {
		this.distancetoTarget = distancetoTarget;
	}
	public void setTarget(String target) {
		this.target = target;
		this.currentStatus = Status.MARCHING;
	}
	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}
	
	
	
	public void relocateUnit(Unit unit) throws MaxCapacityException{
		if(this.units.size() == this.maxToHold)
			throw new MaxCapacityException("Cannot add more units to the army");
		
		units.add(unit);
		unit.getParentArmy().units.remove(unit);
		unit.setParentArmy(this);
		
	}
	
	public void handleAttackedUnit(Unit u) {
		if(u.getCurrentSoldierCount()<=0){
			u.setCurrentSoldierCount(0);
			units.remove(u);
		}
	}
	
	public double foodNeeded() {
		double foodNeeded = 0;
		
		for (Unit unit : units ) {
			int soldierCount = unit.getCurrentSoldierCount();
			if(this.getCurrentStatus() == Status.IDLE) 
				foodNeeded += soldierCount * unit.getIdleUpkeep();
			else if(this.getCurrentStatus() == Status.BESIEGING)
				foodNeeded += soldierCount * unit.getSiegeUpkeep();
			else if (this.getCurrentStatus() == Status.MARCHING)
				foodNeeded += soldierCount * unit.getMarchingUpkeep();
		}
		return foodNeeded;
	}
	public String getArmyName() {
		return armyName;
	}
	public void setArmyName(String armyName) {
		this.armyName = armyName;
	}
	public boolean isTargetReached() {
		return targetReached;
	}
	public void setTargetReached(boolean targetReached) {
		this.targetReached = targetReached;
	}

	
	
	
	
	
}
