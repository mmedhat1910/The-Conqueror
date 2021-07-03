package view;

import com.sun.javafx.scene.traversal.Direction;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import units.Unit;

public class BattleUnit extends VBox{
	private UnitIcon icon;
	private ProgressBar countBar;
	private Unit unit;
	
	public BattleUnit(Unit u) {
		
		this.unit = u;
		this.icon = new UnitIcon(unit.getClass().getSimpleName().toLowerCase(), unit.getLevel());
		icon.setStyle("-fx-padding:0;");
		double maxCount = unit.getMaxSoldierCount();
		double currentCount = unit.getCurrentSoldierCount();
		this.countBar = new ProgressBar(currentCount/maxCount);
//		countBar.setRotate(-90);
		countBar.setMaxHeight(25);
		countBar.setStyle("-fx-accent: green;"
				+ "-fx-text-box-border: rgba(0,0,0,0.2);"
				+ "   -fx-background-insets: 0; ");
		
		StackPane pane = new StackPane();
		Label label = new Label(String.format("%.0f",currentCount));
		String color = (currentCount/maxCount) > 0.5? "white": "black";
		label.setStyle("-fx-font-size: 15px;"
				+ "-fx-text-fill: "+color+";");
		pane.getChildren().addAll(countBar, label);
		
		
		
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(5.0);
		dropShadow.setOffsetX(3.0);
		dropShadow.setOffsetY(3.0);
		dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
		
		this.setOnMouseClicked(e->{
			
		});
		
		this.getChildren().addAll(icon,pane);
		this.setSpacing(8);
		this.setAlignment(Pos.CENTER);
		
	}
	
	public UnitIcon getIcon() {
		return icon;
	}
	public ProgressBar getCount() {
		return countBar;
	}
	public void setIcon(UnitIcon icon) {
		this.icon = icon;
	}
	public void setCount(ProgressBar count) {
		this.countBar = count;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	

}
