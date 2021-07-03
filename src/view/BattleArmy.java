package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class BattleArmy extends VBox{

	private String title;
	
	public BattleArmy(String t) {
		this.title = t;
		ImageView image = new ImageView("file:resources/images/army/icons/army.png");
		image.setFitWidth(80);
		image.setPreserveRatio(true);
		Label label = new Label(title);
		label.setStyle("-fx-text-fill: #0f5e6e;"
				+ "-fx-font-size: 0.8em;"
				+ "-fx-font-weight: bold");
		
		
		this.getChildren().addAll(label,image);
		this.setAlignment(Pos.CENTER);
	}

}
