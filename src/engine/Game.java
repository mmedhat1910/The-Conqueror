package engine;

import java.io.*;
import java.util.*;

public class Game {
	private Player player;
	private ArrayList<City> availableCities; //READ ONLY
	private ArrayList<Distance> distances; //READ ONLY
	private final int maxTurnCount = 30; //READ ONLY
	private int currentTurnCount;
	
	public Game(String playerName,String playerCity) throws IOException {
		player = new Player(playerName);
		this.distances = new ArrayList<Distance>();
		this.currentTurnCount = 1;
		this.loadCitiesAndDistances();
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
	
	public static  ArrayList<String> readCSV (String path) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		String currentLine = "";
		FileReader fileReader = new FileReader(path);
		BufferedReader br = new BufferedReader(fileReader);
		while((currentLine = br.readLine()) != null) {
			list.add(currentLine);
		}
		return list;
	}
	
	public static void loadArmy(String cityName, String path) throws IOException {
		
	}
	private void loadCitiesAndDistances() throws IOException {
		String currentLine = "";
		Set<String> set = new HashSet<String>();
		FileReader fileReader = new FileReader("CSV_Files/distances.csv");
		BufferedReader br = new BufferedReader(fileReader);
		while((currentLine = br.readLine()) != null) {
			String[] city = currentLine.split(",");
			
//			Load Cities
		
			
			
			
			
			
//			Load Distances
			Distance d = new Distance(city[0], city[1], Integer.parseInt(city[2]));
			this.distances.add(d);
			
			
		}
		
	}
	
	
	public static void main(String[] args) throws IOException {
		Game g = new Game("Mohamed", "Cairo");
		for(int i = 0;i<g.getDistances().size();i++) {
			System.out.print(g.getDistances().get(i).getFrom()+ " ");
			System.out.print(g.getDistances().get(i).getTo() + " \n");
		}
	}
	
	
	
	
	
	
	
}
