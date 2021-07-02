package view;


import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChooseCityPane extends BorderPane {
	private Image title;
	private ImageView titleView;
	private GameView gameView;
	


	String[] cities = { "Rome","Cairo", "Sparta"};
	
	
	
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
			VBox cityBox = new VBox();
			ImageView cityImg = new ImageView("file:resources/images/cities/"+cities[i].toLowerCase()+".png");
			Label label = new Label(cities[i]);
			cityBox.setOnMouseClicked(e->{
				this.gameView.setPlayerCity(label.getText());
				this.gameView.getListener().onStartGame();
			});
			label.getStyleClass().add("city-label");
			cityImg.setFitWidth(200);
			cityImg.setPreserveRatio(true);
			cityBox.getChildren().addAll(cityImg, label);
			cityBox.setAlignment(Pos.CENTER);
			cityBox.setOnMouseEntered(e->cityImg.setFitWidth(250));
			cityBox.setOnMouseExited(e->cityImg.setFitWidth(200));
			grid.setHgap(50);
			grid.add(cityBox, i%3, i/3);
			
		}
		grid.setAlignment(Pos.CENTER);
		
		
		this.setTop(titleBox);
		this.setCenter(grid);
	}


	
}
