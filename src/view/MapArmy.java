package view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import units.Army;
import units.Status;

public class MapArmy extends VBox{
	private Army army;
	private String title;
	private ImageView imageContainer;
	private MapView parentView;
	
	public MapArmy(MapView parent,Army a, String title,double size) {
		this.parentView = parent;
		this.title = title;
		this.army = a;
		
		
		CustomButton targetBtn = new CustomButton("Target",'l');
		CustomButton unitsDetails = new CustomButton("Check info",'l');
		targetBtn.setOnMouseClicked(e->{ parent.gameView.playClick(); parent.onTargetBtnClicked(army);});
		unitsDetails.setOnMouseClicked(e->{ parent.gameView.playClick();   parent.showUnitsInfo(army);});
		this.imageContainer = new ImageView("file:resources/images/army/icons/army.png");
		String[] s = army.getArmyName().split(" ");
		for(String str: s )
			if(str.equals("defenders") || army.getCurrentStatus()!=Status.IDLE) {		
				this.setOnMouseClicked(e-> parent.notifyListenersArmyClicked(army, unitsDetails));
				imageContainer.setImage(new Image("file:resources/images/army/icons/defending.png"));
			}else
				this.setOnMouseClicked(e-> parent.notifyListenersArmyClicked(army, unitsDetails,targetBtn));
//		imageContainer.setImage(new Image("file:resources/images/army/army-icon.png"));
		imageContainer.setFitWidth(size);
		imageContainer.setPreserveRatio(true);
		this.setAlignment(Pos.CENTER);
		Label label = new Label(title);
		label.getStyleClass().add("def-army-label");
		if(title != null)
			this.getChildren().add(label);
		this.getChildren().add( imageContainer);
		
//		targetBtn
		
		
		
		
	}

	public Army getArmy() {
		return army;
	}

	public String getTitle() {
		return title;
	}

	public MapView getParentView() {
		return parentView;
	}

	public void setArmy(Army army) {
		this.army = army;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setParentView(MapView parentView) {
		this.parentView = parentView;
	}

	

}
