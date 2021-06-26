package view;

import buildings.ArcheryRange;
import buildings.Barracks;
import buildings.Building;
import buildings.EconomicBuilding;
import buildings.Farm;
import buildings.Market;
import buildings.MilitaryBuilding;
import buildings.Stable;
import javafx.scene.control.TextArea;
import units.Army;
import units.Unit;

public class DetailsBox extends TextArea {
	private Building building;
	private Army army;
	private Unit unit;
	
	public DetailsBox() {
		this.setText("Details Box");
		this.setEditable(false);
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
		
		if(building == null) {
			this.setText("You can build here");
		}else {
			this.setText(this.building.toString());
		}
		
		
	}

	
	public void addText(String s) {
		String text = this.getText();
		this.setText(text +="\n"+s);
	}
	public Army getArmy() {
		return army;
	}

	public void setArmy(Army army) {
		this.army= army;
		this.setText(army.toString());
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
		this.setText(unit.toString());
	};
}
