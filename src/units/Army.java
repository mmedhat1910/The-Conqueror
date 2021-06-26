package units;

import java.util.*;

import exceptions.MaxCapacityException;

public class Army {
	private Status currentStatus; //Initially IDLE
	private ArrayList<Unit> units;
	private int distancetoTarget; //Initially -1
	private String target;
	private String currentLocation;
	private final int maxToHold = 10; //READ ONLY
	
	public Army(String currentLocation) {
		this.currentStatus = Status.IDLE;
		this.units = new ArrayList<Unit>();
		this.distancetoTarget = -1;
		this.target = "";
		this.currentLocation = currentLocation;
		
	}
	public String toString() {
		String s= "Army Location: "+ this.getCurrentLocation();
		s+= "\nStatus: "+this.getCurrentStatus().toString();
		if(!target.equals("")) {
			s+= "\nTarget: "+this.getTarget();
			s+= "\nTurns left to target: "+this.getDistancetoTarget();
		}
		
		s+= "\nCurrent Units Count: "+this.getUnits().size();
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
		unit.setParentArmy(this);
		this.units.add(unit);
		
	}
	
	public void handleAttackedUnit(Unit u) {
		if(u.getCurrentSoldierCount() == 0)
			this.getUnits().remove(u);
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
	
	
	
	
	
}
