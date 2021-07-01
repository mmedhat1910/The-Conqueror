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
	private String state;
	public DetailsBox(GameView gameView) {
		this.setText("");
		this.state = "start";
		this.setEditable(false);
		this.getStyleClass().add("details-box");
		this.setMaxHeight(gameView.getHeight()*0.2);
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
		this.state = "building";
		if(building == null) {
			this.setText("You can build here");
		}else {
			this.setText(this.building.toString());
		}
		
		
	}

	public void update() {
		switch(this.state) {
		case "building": setBuilding(building); break;
		case "army": setArmy(army); break;
		case "unit" :setUnit(unit); break;
		default: this.setText("");
		
		}
	};
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
		this.state = "army";
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
		this.setText(unit.toString());
		this.state = "unit";
	}

	
}
