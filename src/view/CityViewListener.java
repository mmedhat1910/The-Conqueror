package view;

import buildings.Building;
import javafx.scene.control.Button;
import units.Army;
import units.Unit;

public interface CityViewListener {
	public void onBuildingClicked(Building b,CustomButton... buttons);
	public void onBuild(BuildingBlock b);
	public void onUpgrade(BuildingBlock b);
	public void onRecruit(Building b, String buildingType);
	
}
