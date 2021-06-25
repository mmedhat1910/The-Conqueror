package view;

import javafx.scene.control.Button;

public interface CityViewListener {
	public void onBuildingClicked(Button... buttons);
	public void onBuild(BuildingBlock b);
}
