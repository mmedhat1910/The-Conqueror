package view;

import javafx.scene.control.Button;
import units.Army;
import units.Unit;

public interface MapViewListener {
	public void onMapViewOpen();
	public void onCityClicked(String cityName, CustomButton... btns);
	public void onVisitClicked(String cityName);
	public void onTargetClicked(String cityName);
	public void onUnitClicked(Unit u, CustomButton... buttons);
	public void onArmyClicked(Army a, CustomButton... buttons);
}
