package view;

import engine.City;
import units.Army;
import units.Unit;

public interface GameViewListener {
	public void onStartGame();
	public void onEndTurn();
	public void updateInfo();
	public Army onInitArmy(City city,Unit unit);
//	public void exitGame();
}
