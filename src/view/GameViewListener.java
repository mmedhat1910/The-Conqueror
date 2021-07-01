package view;

import java.io.IOException;

import engine.City;
import exceptions.FriendlyCityException;
import exceptions.FriendlyFireException;
import exceptions.MaxCapacityException;
import exceptions.TargetNotReachedException;
import units.Army;
import units.Unit;

public interface GameViewListener {
	public void onStartGame();
	public void onEndTurn();
	public void updateInfo();
	public void onInitArmy(City city,Unit unit);
//	public void exitGame();
	public void onRelocateUnit(Army army, Unit unit) throws MaxCapacityException;
	public void onTargetSet(Army army, String cityName);
	public void handleLaySeige(Army army, City city) throws TargetNotReachedException, FriendlyCityException;
	public void startAttack(Unit selectedUnit, Unit defendingUnit) throws FriendlyFireException, IOException;
	public void startResolve(Army attackingArmy, Army defendingArmy) throws FriendlyFireException, IOException;
	public void handleOccupy(Army attackingArmy, String currentLocation);
}
