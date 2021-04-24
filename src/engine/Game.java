package engine;

import java.io.*;
import java.util.ArrayList;

public class Game {
	private Player player;
	private ArrayList<City> availableCities; //READ ONLY
	private ArrayList<Distance> distances; //READ ONLY
	private final int maxTurnCount = 30; //READ ONLY
	private int currentTurnCount;
	
	public Game(String playerName,String playerCity) throws IOException {
		player = new Player(playerName);
		this.currentTurnCount = 1;
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
	
	
}
