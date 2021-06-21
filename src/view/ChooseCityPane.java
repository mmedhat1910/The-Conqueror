package view;


import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ChooseCityPane extends BorderPane implements EventHandler<ActionEvent> {
	private Image title;
	private ImageView titleView;
	private GameView gameView;
	


	String[] cities = {"Cairo", "Roma", "Sparta"};
	
	
	
	public ChooseCityPane(GameView gameView) {
		this.gameView = gameView;
		this.title = new Image("file:resources/images/map/choose-city.png");
		this.titleView = new ImageView(title);
		this.getStyleClass().add("get-city-panel");
		
		
		
		HBox titleBox = new HBox();
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPadding(new Insets(30, 0, 0, 0));
		titleBox.getChildren().add(titleView);
		
		GridPane grid = new GridPane();
		grid.getStyleClass().add("cities-map");
		grid.setMaxHeight(this.gameView.getStageHeight()*0.8);
		grid.setMaxWidth(this.gameView.getStageWidth()*0.7);
		for(int i=0 ;i<this.cities.length;i++) {
			Button btn = new Button(cities[i]);
			grid.add(btn, i%3, i/3);
			btn.setOnAction(this);
		}
		grid.setAlignment(Pos.CENTER);
		
		
		this.setTop(titleBox);
		this.setCenter(grid);
	}


	@Override
	public void handle(ActionEvent e) {
		Button btn =  (Button) e.getSource();
		this.gameView.setPlayerCity(btn.getText());
		this.gameView.getListener().onStartGame();
	}
}
