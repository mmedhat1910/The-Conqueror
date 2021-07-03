package view;

import java.util.ArrayList;

import buildings.ArcheryRange;
import buildings.Barracks;
import buildings.Building;
import buildings.Farm;
import engine.City;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import units.Army;
import units.Status;
import units.Unit;

public class CityView extends GridPane {
	private GameView gameView;
	private City city;
	private BuildingBlock[] blocks;
	private FlowPane defendingArmyBox;
	private GamePane gamePane;
	

	

	public FlowPane getDefendingArmyBox() {
		return defendingArmyBox;
	}


	public void setDefendingArmyBox(FlowPane defendingArmyBox) {
		this.defendingArmyBox = defendingArmyBox;
	}


	public CityView(GameView gameView,GamePane gPane, City c) {
		this.gameView = gameView;
		this.gamePane = gPane;
		this.defendingArmyBox = new FlowPane();
		this.add(defendingArmyBox,0,1,4,1);
		this.setMaxWidth(this.gameView.getWidth());
		this.blocks = new BuildingBlock[5];
		this.city = c;
		for(int i=0;i<blocks.length;i++)
			blocks[i] = new BuildingBlock(null, this.gameView,this);
		for(City c2 : gameView.getAvailableCities()) {
			if(c2.getName().equals(c.getName())) {
				int i;
				for(i=0; i<c2.getAllBuildings().size();i++){
					blocks[i].setBuilding(c2.getAllBuildings().get(i));
				}
					
			}
					
		}
		this.getStyleClass().add("city-view");
		this.setAlignment(Pos.CENTER);
		for(int i=0;i<blocks.length;i++) {
			this.add(blocks[i].getImage(), i, 0);
		}
		for(int i =0;i<gameView.getControlledArmies().size();i++) {
			FlowPane pane = new FlowPane();
			
			for(Unit u: gameView.getControlledArmies().get(i).getUnits())
				pane.getChildren().add(new Label(u.getClass().getSimpleName()+" " +u.getLevel()));
			this.add(pane, 0, i+2, 5, 1);
		}
//		gamePane.displayDefendingArmy();
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


	public void update() {
		for(int i =0;i<gameView.getControlledArmies().size();i++) {
			Army army = gameView.getControlledArmies().get(i);
			int row = i+2;
			this.getChildren().removeIf(node->getRowIndex(node) ==row);
			FlowPane pane = new FlowPane();
			pane.setHgap(10);
			ImageView icon = new ImageView("file:resources/images/army/icons/army.png");
			icon.setFitWidth(80);
			icon.setPreserveRatio(true);
			CustomButton targetBtn = new CustomButton("Target",'l');
			targetBtn.setOnMouseClicked(e->{
				gameView.playClick();
				gameView.handleTargetClicked(army);
				});
			
			icon.setOnMouseClicked(e->gameView.getGamePane().getActionBox().onArmyClicked(army, targetBtn));
			pane.getChildren().add(icon);
			for(Unit u: army.getUnits()) {
				UnitIcon unitBtn = new UnitIcon(u.getClass().getSimpleName().toLowerCase(), u.getLevel());
				unitBtn.setOnMouseClicked(e1->{
					gamePane.getActionBox().onUnitClicked(u, gamePane.getRelocateBtn());
				});
				pane.getChildren().add(unitBtn);
				
			}
			
			if(army.getCurrentStatus()!= Status.IDLE)
				this.getChildren().removeIf(node->getRowIndex(node) ==row);
			else
				this.add(pane, 0, row, 5, 1);
		}
	}


	public GamePane getGamePane() {
		return gamePane;
	}


	public void setGamePane(GamePane gamePane) {
		this.gamePane = gamePane;
	}

}
