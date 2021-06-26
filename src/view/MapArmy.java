package view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import units.Army;

public class MapArmy extends VBox{
	private Army army;
	private String title;
	private ImageView imageContainer;
	private MapView parent;
	
	public MapArmy(MapView parent,Army army, String title,double size) {
		this.parent = parent;
		this.title = title;
		this.imageContainer = new ImageView("file:resources/images/army/army-icon.png");
		this.army = army;
//		imageContainer.setImage(new Image("file:resources/images/army/army-icon.png"));
		imageContainer.setFitWidth(size);
		imageContainer.setPreserveRatio(true);
		this.setAlignment(Pos.CENTER);
		Label label = new Label(title);
		label.getStyleClass().add("def-army-label");
		if(title != null)
			this.getChildren().add(label);
		this.getChildren().add( imageContainer);
		
		Button unitsDetails = new Button("Check units info");
		unitsDetails.setOnAction(e->parent.showUnitsInfo(army));
		this.setOnMouseClicked(e-> parent.notifyListenersArmyClicked(army, unitsDetails));
	}

	

}
