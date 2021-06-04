package engine;

import java.io.*;
import java.util.*;

import exceptions.FriendlyFireException;
import units.*;

public class Game {
	private Player player;
	private ArrayList<City> availableCities; //READ ONLY
	private ArrayList<Distance> distances; //READ ONLY
	private final int maxTurnCount = 30; //READ ONLY
	private int currentTurnCount;
	
	public Game(String playerName,String playerCity) throws IOException {
		player = new Player(playerName);
		this.distances = new ArrayList<Distance>();
		this.availableCities = new ArrayList<City>();
		this.currentTurnCount = 1;
		this.loadCitiesAndDistances();
		
	
		for(City city : this.availableCities) {
			String cityName =  city.getName();
			if(!cityName.equals(playerCity)) {
				
				this.loadArmy(cityName, cityName.toLowerCase() +"_army.csv");
				
			}else {
				player.getControlledCities().add(new City(cityName));
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
		
//		System.out.println(readCSV("units_values/archer_values.csv"));
//		System.out.println(Arrays.toString(archery_values.get(1).split(",")));
		
		Army city_army = new Army(cityName);
		ArrayList<Unit> armyUnits = new ArrayList<Unit>();
		for(int i=0;i<list.size();i++) {
//			System.out.println(list.get(i));
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
		//TODO implement game:targetCity
	}
	
	public void endTurn() {
		//TODO implement game:endTurn
	}
	
	public void occupy(Army a,String cityName) {
		//TODO implement game:occupy
	}
	
	public void autoResolve(Army attacker, Army defender) throws FriendlyFireException{
		//TODO implement game:autoResolve
	}
	
	public boolean isGameOver() {
		//TODO implement game:isGameOver
		return false;
	}
	
	
	public static void main(String[] args) throws IOException {
		Game g = new Game("Mohamed", "Cairo");
//		for(int i = 0;i<g.getDistances().size();i++) {
//			System.out.print(g.getDistances().get(i).getFrom()+ " ");
//			System.out.print(g.getDistances().get(i).getTo() + " \n");
//		}
//		System.out.println(g.getAvailableCities().get(2).getName());
	}
	
	
	
	
	
	
	
}
