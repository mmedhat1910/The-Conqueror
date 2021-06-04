package engine;

import java.util.ArrayList;

import buildings.*;
import exceptions.*;
import units.Army;
import units.Status;
import units.Unit;


public class Player {
	private String name;  //READ ONLY
	private ArrayList<City> controlledCities; //READ ONLY
	private ArrayList<Army> controlledArmies; //READ ONLY
	private double treasury;
	private double food;
	
	public Player(String name) {
		this.name = name;
		this.controlledArmies = new ArrayList<Army>();
		this.controlledCities = new ArrayList<City>();
	}
	
	
//	Getters
	public String getName() {
		return this.name;
	}
	public ArrayList<City> getControlledCities(){
		return this.controlledCities;
	}
	public ArrayList<Army> getControlledArmies(){
		return this.controlledArmies;
	}
	public double getTreasury() {
		return this.treasury;
	}
	public double getFood() {
		return this.food;
	}
	
//	Setters
	public void setTreasury(double treasury) {
		this.treasury = treasury;
	}
	public void setFood(double food) {
		this.food = food;
	}
	
	public void recruitUnit(String type,String cityName) throws BuildingInCoolDownException, MaxRecruitedException, NotEnoughGoldException{
		City city = new City("");
		for (int i =0;i<this.getControlledCities().size();i++) {
				if(this.getControlledCities().get(i).getName().equals(cityName)) {
					city = this.getControlledCities().get(i);
					break;
				}
		}
		
		ArrayList<MilitaryBuilding> militaryBuildings = city.getMilitaryBuildings();
		MilitaryBuilding myBuilding = null;
		for(MilitaryBuilding building: militaryBuildings) {
			if(type.equals("Archer")) {
				if(building instanceof ArcheryRange) {
					myBuilding = building;
					break;
				}
			}else if(type.equals("Cavalry")) {
				if(building instanceof Stable) {
					myBuilding = building;
					break;
				}
			}else if(type.equals("Infantry")) {
				if(building instanceof Barracks) {
					myBuilding = building;
					break;
				}
			}
		}
		if(this.getTreasury() < myBuilding.getRecruitmentCost())
			throw new NotEnoughGoldException("No Enough gold to recruit "+type);
		
		Unit recruitedUnit = myBuilding.recruit();
		city.getDefendingArmy().getUnits().add(recruitedUnit);
		this.setTreasury(this.getTreasury() - myBuilding.getRecruitmentCost());
		recruitedUnit.setParentArmy(city.getDefendingArmy());
	}
	
	
	public void build(String type,String cityName) throws NotEnoughGoldException{
		City city = new City("");
		for (int i =0;i<this.getControlledCities().size();i++) {
				if(this.getControlledCities().get(i).getName().equals(cityName)) {
					city = this.getControlledCities().get(i);
					break;
				}
		}
		Building building;
		boolean isMilitary = true;
		if(type.equals("ArcheryRange")) {
			building = new ArcheryRange();
		}else if(type.equals("Stable")) {
			building = new Stable();
		}else if(type.equals("Barracks")) {
			building = new Barracks();
		}else if(type.equals("Farm")) {
			building = new Farm();
			isMilitary = false;
		}else {
			building = new Market();
			isMilitary = false;
		}
		if(isMilitary)
			city.getMilitaryBuildings().add((MilitaryBuilding) building);
		else
			city.getEconomicalBuildings().add((EconomicBuilding) building);
		
		if(this.getTreasury() < building.getCost())
			throw new NotEnoughGoldException("No enough gold to build "+type);
		
		this.setTreasury(this.getTreasury() - building.getCost());
		building.setCoolDown(true);
	}
	public void upgradeBuilding(Building b) throws NotEnoughGoldException, BuildingInCoolDownException, MaxLevelException{
		if(this.getTreasury() < b.getUpgradeCost())
			throw new NotEnoughGoldException("No gold to upgrade the building");
		this.setTreasury(this.getTreasury() - b.getUpgradeCost());
		b.upgrade();
	}
	public void initiateArmy(City city,Unit unit) {
		Army newArmy = new Army(city.getName());
		newArmy.getUnits().add(unit);
		city.getDefendingArmy().getUnits().remove(unit);
		unit.setParentArmy(newArmy);
		this.getControlledArmies().add(newArmy);
	}
	public void laySiege(Army army,City city) throws TargetNotReachedException, FriendlyCityException{
		army.setCurrentStatus(Status.BESIEGING);
		city.setUnderSiege(true);
		city.setTurnsUnderSiege(1);
	}
	
	
}
