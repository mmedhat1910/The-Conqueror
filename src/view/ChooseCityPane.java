package view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ChooseCityPane extends BorderPane {
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
		grid.setMaxHeight(this.gameView.getHeight()*0.8);
		grid.setMaxWidth(this.gameView.getWidth()*0.7);
		for(int i=0 ;i<this.cities.length;i++)
			grid.add(new Button(cities[i]), i, 0);
		grid.setAlignment(Pos.CENTER);
		
		
		this.setTop(titleBox);
		this.setCenter(grid);
	}
}
