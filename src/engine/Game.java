package engine;

import java.io.*;
import java.util.*;

import buildings.EconomicBuilding;
import buildings.Farm;
import buildings.MilitaryBuilding;
import exceptions.BuildingInCoolDownException;
import exceptions.FriendlyFireException;
import exceptions.MaxLevelException;
import exceptions.MaxSeigingPeriodException;
import exceptions.NotEnoughGoldException;
import units.*;

public class Game {
	private Player player;
	private ArrayList<City> availableCities; //READ ONLY
	private ArrayList<Distance> distances; //READ ONLY
	private final int maxTurnCount = 50; //READ ONLY
	private int currentTurnCount;
	
	public Game(String playerName,String playerCity) throws IOException {
		player = new Player(playerName);
		this.distances = new ArrayList<Distance>();
		this.availableCities = new ArrayList<City>();
		this.currentTurnCount = 1;
		this.loadCitiesAndDistances();
		this.player.setTreasury(5000);
		if(playerName.toLowerCase().equals("maestro"))
			this.player.setTreasury(1000000);
		
	
		for(City city : this.availableCities) {
			String cityName =  city.getName();
			if(!cityName.equals(playerCity)) {
				
				this.loadArmy(cityName, cityName.toLowerCase() +"_army.csv");
				
			}else {
				player.getControlledCities().add(city);
			}
		}
		
	}
	
	
//	Getters
	public Player getPlayer() {
		return this.player;
	}
	public ArrayList<City> getAvailableCities(){
		return this.availableCities;
	}
	public ArrayList<Distance> getDistances(){
		return this.distances;
	}
	public int getMaxTurnCount() {
		return this.maxTurnCount;
	}
	public int getCurrentTurnCount() {
		return this.currentTurnCount;
	}
	
//	Setters
	public void setPlayer(Player player) {
		this.player = player;
	}
	public void setCurrentTurnCount(int currentTurnCount) {
		this.currentTurnCount = currentTurnCount;
	}
	
	public static  ArrayList<String> readCSV(String path) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		String currentLine = "";
		FileReader fileReader = new FileReader(path);
		BufferedReader br = new BufferedReader(fileReader);
		while((currentLine = br.readLine()) != null) {
			list.add(currentLine);
		}
		br.close();
		return list;
	}
	
	
	public void loadArmy(String cityName, String path) throws IOException {
		ArrayList<String> list = readCSV(path);
		ArrayList<String> archery_values = readCSV("units_values/archer_values.csv");
		ArrayList<String> cavalry_values = readCSV("units_values/cavalry_values.csv");
		ArrayList<String> infantry_values = readCSV("units_values/infantry_values.csv");

		Army city_army = new Army(cityName);
		city_army.setArmyName(cityName+" defenders");
		ArrayList<Unit> armyUnits = new ArrayList<Unit>();
		for(int i=0;i<list.size();i++) {
			String[] row = list.get(i).split(",");
			Unit unit = null;
			int level = Integer.parseInt(row[1]);
			switch(row[0]) {
				case "Archer":
					String[] a_values = archery_values.get(level).split(",");
					unit = new Archer(
							level, 
							Integer.parseInt(a_values[1]),
							Double.parseDouble(a_values[2]),
							Double.parseDouble(a_values[3]),
							Double.parseDouble(a_values[4])
							);
					break;
					
				case "Infantry":
					String[] i_values = infantry_values.get(level).split(",");
					unit = new Infantry(
							level, 
							Integer.parseInt(i_values[1]),
							Double.parseDouble(i_values[2]),
							Double.parseDouble(i_values[3]),
							Double.parseDouble(i_values[4])
							);
					break;
					
				case "Cavalry":
					String[] c_values = cavalry_values.get(level).split(",");
					unit = new Cavalry(
							level, 
							Integer.parseInt(c_values[1]),
							Double.parseDouble(c_values[2]),
							Double.parseDouble(c_values[3]),
							Double.parseDouble(c_values[4])
							);
					break;
					
			}
			armyUnits.add(unit);
		
			unit.setParentArmy(city_army);
		}
		city_army.setUnits(armyUnits);
		
		for(City city : this.availableCities) {
			if(city.getName().equals(cityName)) {
				city.setDefendingArmy(city_army);
				break;
			}
		}
		
//		System.out.println(armyUnits);
			 
		
	}
	
	
	private void loadCitiesAndDistances() throws IOException {

		ArrayList<String> list = readCSV("distances.csv");

		//loading cities
		Set<String> set = new HashSet<String>();
		for(int i=0 ;i<list.size();i++) {
			String[] s = list.get(i).split(",");
			set.add(s[0]);
			set.add(s[1]);
		}
		for(String city : set) {
			this.availableCities.add(new City(city));
		}
		
		
		
		
		// Loading distances
		for(int i=0;i<list.size();i++) {
			String s = list.get(i);
			String[] city = s.split(",");
			Distance d = new Distance(city[0], city[1], Integer.parseInt(city[2]));
			this.distances.add(d);
		}
		
	}
	
	public void targetCity(Army army, String targetName) {
		if(!army.getTarget().equals(""))
			return;
		
		army.setTarget(targetName);
		Distance distanceToTarget = new Distance("", "", 0);
		for(Distance distance: this.getDistances()) {
			if (distance.getFrom().equals(army.getCurrentLocation()) && distance.getTo().equals(targetName) ||distance.getTo().equals(army.getCurrentLocation()) && distance.getFrom().equals(targetName) )
				distanceToTarget = distance;
		}
		System.out.println(distanceToTarget.getDistance());
		army.setDistancetoTarget(distanceToTarget.getDistance());
		army.setCurrentStatus(Status.MARCHING);
		army.setCurrentLocation("onRoad");
		
	}
	
	public void endTurn() throws MaxSeigingPeriodException {
		if(this.currentTurnCount != maxTurnCount)
			this.currentTurnCount++; //increment current number of turns
		//get initial values of gold and food at the beginning of the old turn
		double totalUpkeep = 0;
		double food = player.getFood();
		double gold = player.getTreasury();
		for(City city: player.getControlledCities()) {
			for(MilitaryBuilding building: city.getMilitaryBuildings()) {
				building.setCoolDown(false); //resets cool down
				building.setCurrentRecruit(0); 
			}
			for(EconomicBuilding building: city.getEconomicalBuildings()) {
				building.setCoolDown(false);
				if(building instanceof Farm) 
					food += building.harvest(); // add harvested food
				else
					gold += building.harvest();	 // add harvested gold
			}
			//assign updated values for food and gold at the end of the turn
			totalUpkeep += city.getDefendingArmy().foodNeeded();
		}
		player.setFood(food);
		player.setTreasury(gold);
		
		
		
		//updates player food after consuming food for all army
		double updatedFood = player.getFood()-totalUpkeep;
		if(totalUpkeep <= player.getFood()) {
			player.setFood(updatedFood);
		}
		else {
			player.setFood(0);
			for(Army army: player.getControlledArmies()) {
				for(Unit unit : army.getUnits()) {
					unit.setCurrentSoldierCount( unit.getCurrentSoldierCount() - (int) (unit.getCurrentSoldierCount() * 0.1) );
					
				}
			}
		}
		
		for (Army a : player.getControlledArmies()) {
			if (!a.getTarget().equals("") && a.getCurrentStatus() == Status.IDLE) {
				a.setCurrentStatus(Status.MARCHING);
				a.setCurrentLocation("onRoad");
			}
			if(a.getDistancetoTarget()>0 &&!a.getTarget().equals(""))
			a.setDistancetoTarget(a.getDistancetoTarget() - 1);
			if (a.getDistancetoTarget() == 0) {
				a.setTargetReached(true);
				a.setCurrentLocation(a.getTarget());
				a.setTarget("");
				a.setDistancetoTarget(-1);
				a.setCurrentStatus(Status.IDLE);
			}
			totalUpkeep +=  a.foodNeeded();

		}	
		
		for(City city: this.getAvailableCities()) {
			if(city.isUnderSiege()) {
				if(city.getTurnsUnderSiege() == 3) {
//					city.setTurnsUnderSiege(-1);
//					city.setUnderSiege(false);
					throw new MaxSeigingPeriodException("You spent 3 turns laying seige on "+city.getName()+", please take action");
				}else {
					city.setTurnsUnderSiege(city.getTurnsUnderSiege()+1);
					for(Unit unit : city.getDefendingArmy().getUnits()) {
						unit.setCurrentSoldierCount( unit.getCurrentSoldierCount() - (int) (unit.getCurrentSoldierCount() * 0.1) );				
					}		
				}
			}
		}		
		
	}
	
	public void occupy(Army a,String cityName) {
		City city = null;
		for(City c : this.availableCities)
			if(c.getName().equals(cityName))
				city = c;
		player.getControlledCities().add(city);
		player.getControlledArmies().remove(a);
		a.setInitialLocation(cityName);
		a.setArmyName(cityName+" defenders");
		city.setDefendingArmy(a);
		city.setUnderSiege(false);
		city.setTurnsUnderSiege(-1);
		a.setCurrentStatus(Status.IDLE);
	}
	
	
	public void autoResolve(Army attacker, Army defender) throws FriendlyFireException, IOException{
		
		if(player.getControlledArmies().contains(attacker) && player.getControlledArmies().contains(defender))
			throw new FriendlyFireException("Friendly fire");
		
		boolean attackerTurn = true; //flag to keep player turn 
		while(attacker.getUnits().size() > 0 && defender.getUnits().size() > 0) {
			// random index to choose random units
			int index1 = (int) (Math.random() * attacker.getUnits().size());
			int index2 = (int) (Math.random() * defender.getUnits().size());
			
			//getting units
			Unit attackerUnit = attacker.getUnits().get(index1);
			Unit defenderUnit = defender.getUnits().get(index2);
			
			if(attackerTurn) {
				attackerUnit.attack(defenderUnit);
				attackerTurn = false;
			}
			else {
				defenderUnit.attack(attackerUnit);
				attackerTurn = true;
			}
			
		}
		
		if(defender.getUnits().size() == 0) {
			occupy(attacker, defender.getCurrentLocation());
		}else {
			for ( City c: this.getAvailableCities()) {
				if(defender.getCurrentLocation().equals(c.getName()))
					c.setTurnsUnderSiege(-1);
					c.setUnderSiege(false);
			}
			player.getControlledArmies().remove(attacker);
		}
		
		
	}
	
	public boolean isGameOver() {
		return player.getControlledCities().size() == availableCities.size() || currentTurnCount > maxTurnCount;
		

	}
	
	
	public static void main(String[] args) throws IOException {
		Game g = new Game("Mohamed", "Cairo");
		System.out.println("Player Name: "+ g.player.getName());
		System.out.println("Food Treasury: "+ g.player.getFood() + ", "+g.player.getTreasury());
		System.out.println(g.player.getControlledCities());
		System.out.println(g.player.getControlledArmies());
		try {
			g.player.build("ArcheryRange", "Cairo");
		} catch (NotEnoughGoldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(g.player.getControlledCities().get(0).getMilitaryBuildings().get(0).getLevel());
		try {
			g.player.getControlledCities().get(0).getMilitaryBuildings().get(0).upgrade();
		} catch (BuildingInCoolDownException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MaxLevelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(g.player.getControlledCities().get(0).getMilitaryBuildings().get(0).getLevel());
		System.out.println(Math.atan2(3,4));

		
		
	}
	
	
	
	
	
	
	
}
