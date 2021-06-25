package view;

import buildings.ArcheryRange;
import buildings.Barracks;
import buildings.Building;
import buildings.Farm;
import engine.City;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class CityView extends GridPane {
	private GameView gameView;
	private City city;
	private BuildingBlock[] blocks;
	
	

	public CityView(GameView gameView, City c) {
		this.gameView = gameView;
		this.setMaxWidth(this.gameView.getWidth());
		this.blocks = new BuildingBlock[5];
		this.city = c;
		for(int i=0;i<blocks.length;i++)
			blocks[i] = new BuildingBlock(null, this.gameView,this);
		this.getStyleClass().add("city-view");
		this.setAlignment(Pos.CENTER);
		for(int i=0;i<blocks.length;i++) {
			this.add(blocks[i].getImage(), i, 0);
		}
		
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
	public BuildingBlock[] getBlocks() {
		return blocks;
	}

	public void setBlocks(BuildingBlock[] blocks) {
		this.blocks = blocks;
	}

}
