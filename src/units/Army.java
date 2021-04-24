package units;

import java.util.*;

public class Army {
	private Status currentStatus; //Initially IDLE
	private ArrayList<Unit> units;
	private int distancetoTarget; //Initially -1
	private String target;
	private String currentLocation;
	private final int maxToHold = 10; //READ ONLY
	
	public Army(String currentLocation) {
		this.currentStatus = Status.IDLE;
		this.distancetoTarget = -1;
		this.target = "";
		this.currentLocation = currentLocation;
		
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
	}
	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}
	
	
	
	
	
	
	
	
	
	
}
