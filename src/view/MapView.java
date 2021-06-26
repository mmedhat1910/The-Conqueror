package view;


import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MapView extends Pane {
	private GameView gameView;
	private double width;
	private double height;
	public MapView(GameView gameView, Pane parent) {
		this.gameView = gameView;
		this.getStyleClass().add("map-view");
		this.width = gameView.getWidth();
		this.height = gameView.getHeight() - (gameView.getHeight()*0.15 + 50);
		this.minWidth(width);
		this.minHeight(height);
		this.setMaxWidth(width);
		this.setMaxHeight(height);
		
		Button closeMap = new Button("Close");
		closeMap.setOnAction(e->{
			parent.getChildren().remove(this);
			gameView.getGamePane().onExitMap();
		});
		closeMap.setMinWidth(100);
		this.getChildren().add(closeMap);
		closeMap.relocate(width-100, 0);
		
		MapCity rome = new MapCity("Rome", this, (this.width*0.3)-300,  (this.height*0.1));
		MapCity sparta = new MapCity("Sparta", this,(this.width*0.7),  (this.height*0.1));
		MapCity cairo = new MapCity("Cairo", this, (this.width*0.5)-150, (this.height*0.6));
		
		
		this.getChildren().addAll(rome, sparta, cairo);
		
	}
	
}
