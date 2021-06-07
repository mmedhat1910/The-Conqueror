package engine;

import java.util.ArrayList;

import buildings.*;
import exceptions.*;
import units.Army;
import units.Status;
import units.Unit;

public class Player {
	private String name; // READ ONLY
	private ArrayList<City> controlledCities; // READ ONLY
	private ArrayList<Army> controlledArmies; // READ ONLY
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

	public ArrayList<City> getControlledCities() {
		return this.controlledCities;
	}

	public ArrayList<Army> getControlledArmies() {
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

	public void recruitUnit(String type, String cityName)
			throws BuildingInCoolDownException, MaxRecruitedException, NotEnoughGoldException {
		City city = null;
		
		for (City c : this.getControlledCities()) {
			
			if (c.getName().equals(cityName)) {
				city = c;
				break;
			}
		}
		if(city==null)
			return;
		MilitaryBuilding building = new ArcheryRange();
		
		if (type.equals("Archer")) {
			for (MilitaryBuilding b : city.getMilitaryBuildings()) {
				if (b instanceof ArcheryRange) {
					building = b;
					break;
				}
			}
		} else if (type.equals("Cavalry")) {
			for (MilitaryBuilding b : city.getMilitaryBuildings()) {
				if (b instanceof Stable) {
					building = b;
					break;
				}
			}
		} else {
			for (MilitaryBuilding b : city.getMilitaryBuildings()) {
				if (b instanceof Barracks) {
					building = b;
					break;
				}
			}
		}
		
		
		
		
		if (this.getTreasury() < building.getRecruitmentCost())
			throw new NotEnoughGoldException("No Enough gold to recruit " + type);

		Unit recruitedUnit = building.recruit();
		recruitedUnit.setParentArmy(city.getDefendingArmy());
		city.getDefendingArmy().getUnits().add(recruitedUnit);
		this.setTreasury(this.getTreasury() - building.getRecruitmentCost());
		recruitedUnit.setParentArmy(city.getDefendingArmy());
	}

	public void build(String type, String cityName) throws NotEnoughGoldException {
		City city = new City("");
		for (City c : this.getControlledCities()) {
			if (c.getName().equals(cityName)) {
				city = c;
				break;
			}
		}
		

		Building building;
		if (type.equals("ArcheryRange")) {
			building = new ArcheryRange();
		} else if (type.equals("Stable")) {
			building =  new Stable();
		} else if (type.equals("Barracks")) {
			building =new Barracks();
		} else if (type.equals("Farm")) {
			building =new Farm();
		} else {
			building =  new Market();
		}
		if (this.getTreasury() < building.getCost())
			throw new NotEnoughGoldException("No enough gold to build " + type);

		if (building instanceof ArcheryRange) {
			for (MilitaryBuilding b : city.getMilitaryBuildings()) {
				if (b instanceof ArcheryRange)
					return;
			}
			city.getMilitaryBuildings().add((ArcheryRange) building);

		} else if (building instanceof Stable) {
			for (MilitaryBuilding b : city.getMilitaryBuildings()) {
				if (b instanceof Stable)
					return;
			}
			city.getMilitaryBuildings().add((Stable) building);
			
		} else if (building instanceof Barracks) {
			for (MilitaryBuilding b : city.getMilitaryBuildings()) {
				if (b instanceof Barracks)
					return;
			}
			city.getMilitaryBuildings().add((Barracks) building);
			
		} else if (building instanceof Farm) {
			for (EconomicBuilding b : city.getEconomicalBuildings()) {
				if (b instanceof Farm)
					return;
			}
			city.getEconomicalBuildings().add((Farm) building);
		} else if (building instanceof Market) {
			for (EconomicBuilding b : city.getEconomicalBuildings()) {
				if (b instanceof Market)
					return;
			}

			city.getEconomicalBuildings().add((Market) building);
		}
		
		this.setTreasury(this.getTreasury() - building.getCost());
		building.setCoolDown(true);
	}

	public void upgradeBuilding(Building b)
			throws NotEnoughGoldException, BuildingInCoolDownException, MaxLevelException {
		if (this.getTreasury() < b.getUpgradeCost())
			throw new NotEnoughGoldException("No gold to upgrade the building");
		double upgradeCost = b.getUpgradeCost();
		b.upgrade();
		this.setTreasury(this.getTreasury() - upgradeCost);

		

	}

	public void initiateArmy(City city, Unit unit) {

		Army newArmy = new Army(city.getName());
		newArmy.getUnits().add(unit);
		city.getDefendingArmy().getUnits().remove(unit);
		unit.setParentArmy(newArmy);
		this.getControlledArmies().add(newArmy);
	}

	public void laySiege(Army army, City city) throws TargetNotReachedException, FriendlyCityException {
		if (this.controlledCities.contains(city))
			throw new FriendlyCityException("Laying seige on friendly city");
		if (army.getCurrentLocation() != city.getName())
			throw new TargetNotReachedException(city.getName() + " not reached yet");
		army.setCurrentStatus(Status.BESIEGING);
		city.setUnderSiege(true);
		city.setTurnsUnderSiege(0);
	}

}
