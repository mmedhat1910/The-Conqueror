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
		if (city == null)
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

		Unit recruitedUnit = building.recruit();
		if (this.getTreasury() < building.getRecruitmentCost())
			throw new NotEnoughGoldException("No Enough gold to recruit " + type);
		this.treasury -= building.getRecruitmentCost();
		recruitedUnit.setParentArmy(city.getDefendingArmy());
		city.getDefendingArmy().getUnits().add(recruitedUnit);
	}

	public Building build(String type, String cityName) throws NotEnoughGoldException {
		City city = new City("");
		for (City c : this.getControlledCities()) {
			if (c.getName().equals(cityName)) {
				city = c;
				break;
			}
		}

		Building building = null;
		switch (type.toLowerCase()) {
		case "archeryrange":
			building = new ArcheryRange();
			break;
		case "barracks":
			building = new Barracks();
			break;
		case "stable":
			building = new Stable();
			break;
		case "farm":
			building = new Farm();
			break;
		case "market":
			building = new Market();
		}

		if (this.getTreasury() < building.getCost())
			throw new NotEnoughGoldException("No enough gold to build " + type);

		if (building instanceof ArcheryRange) {
			for (MilitaryBuilding b : city.getMilitaryBuildings()) {
				if (b instanceof ArcheryRange)
					return null;
			}
			city.getMilitaryBuildings().add((ArcheryRange) building);
			city.getAllBuildings().add((ArcheryRange) building);

		} else if (building instanceof Stable) {
			for (MilitaryBuilding b : city.getMilitaryBuildings()) {
				if (b instanceof Stable)
					return null;
			}
			city.getMilitaryBuildings().add((Stable) building);
			city.getAllBuildings().add((Stable) building);

		} else if (building instanceof Barracks) {
			for (MilitaryBuilding b : city.getMilitaryBuildings()) {
				if (b instanceof Barracks)
					return null;
			}
			city.getMilitaryBuildings().add((Barracks) building);
			city.getAllBuildings().add((Barracks) building);

		} else if (building instanceof Farm) {
			for (EconomicBuilding b : city.getEconomicalBuildings()) {
				if (b instanceof Farm)
					return null;
			}
			city.getEconomicalBuildings().add((Farm) building);
			city.getAllBuildings().add((Farm) building);
		} else if (building instanceof Market) {
			for (EconomicBuilding b : city.getEconomicalBuildings()) {
				if (b instanceof Market)
					return null;
			}

			city.getEconomicalBuildings().add((Market) building);
			city.getAllBuildings().add((Market) building);
		}

		this.setTreasury(this.getTreasury() - building.getCost());
		building.setCoolDown(true);
		return building;
	}

	public void upgradeBuilding(Building b)
			throws NotEnoughGoldException, BuildingInCoolDownException, MaxLevelException {
		if (this.getTreasury() < b.getUpgradeCost())
			throw new NotEnoughGoldException("No gold to upgrade the building");
		double upgradeCost = b.getUpgradeCost();
		b.upgrade();
		this.treasury -= upgradeCost;
	}

	public Army initiateArmy(City city, Unit unit) {

		Army army = new Army(city.getName());
		army.getUnits().add(unit);
		city.getDefendingArmy().getUnits().remove(unit);
		unit.setParentArmy(army);
		this.controlledArmies.add(army);
		return army;
	}

	public void laySiege(Army army, City city) throws TargetNotReachedException, FriendlyCityException {
		if (this.controlledCities.contains(city))
			throw new FriendlyCityException("Laying seige on friendly city");
		if (!army.getCurrentLocation().equals(city.getName()))
			throw new TargetNotReachedException("Target: (" + city.getName() + ") not reached yet");
		army.setCurrentStatus(Status.BESIEGING);
		city.setUnderSiege(true);
		city.setTurnsUnderSiege(0);
	}

}
