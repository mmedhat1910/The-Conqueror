package view;

import javafx.scene.control.Button;
import units.Army;
import units.Unit;

public interface MapViewListener {
	public void onMapViewOpen();
	public void onCityClicked(String cityName, Button... btns);
	public void onVisitClicked(String cityName);
	public void onTargetClicked(String cityName);
	public void onUnitClicked(Unit u, Button... buttons);
	public void onArmyClicked(Army a, Button... buttons);
}
