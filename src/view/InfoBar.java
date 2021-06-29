package view;

import engine.City;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class InfoBar extends BorderPane{
	private GameView gameView;
	private Label foodLabel;
	private City city;
	private Label cityLabel;
	private Label goldLabel;
	private Label turnLabel;
	
	
	public InfoBar(GameView gameView) {
		this.gameView = gameView;
		this.getStyleClass().add("info-bar");
		this.setMaxHeight(50);
		this.setPrefWidth(gameView.getWidth());
		this.setPadding(new Insets(5, 10,5,10 ));
		this.foodLabel = new Label(gameView.getFood()+"");
		this.goldLabel = new Label(gameView.getTreasury()+"");
		this.turnLabel = new Label("Turn: "+  gameView.getTurnCount());
		this.cityLabel = new Label(gameView.getPlayerCity());
		HBox keepUpDataBox = new HBox();
		
		ImageView goldIcon  = new ImageView("file:resources/images/icons/gold2.png");
		goldIcon.setFitHeight(50); goldIcon.setFitWidth(50);
		keepUpDataBox.getChildren().add(goldIcon);
		
		keepUpDataBox.getChildren().add(goldLabel);
		
		ImageView foodIcon = new ImageView("file:resources/images/icons/apple.png");
		foodIcon.setFitHeight(40); foodIcon.setFitWidth(40);
		keepUpDataBox.getChildren().add(foodIcon);
		
		keepUpDataBox.getChildren().add(foodLabel);
		keepUpDataBox.setAlignment(Pos.CENTER);
		this.setLeft(keepUpDataBox);

		HBox playerDataBox = new HBox();
		playerDataBox.getChildren().add(new Label(gameView.getPlayerName()));
		playerDataBox.getChildren().add(cityLabel);
		playerDataBox.setAlignment(Pos.CENTER);
		
		this.setCenter(playerDataBox);
		
		HBox turnDataBox = new HBox();
		turnDataBox.getChildren().add(turnLabel);
		Button endTurnBtn = new Button("End Turn");
		endTurnBtn.setOnAction(e->gameView.getListener().onEndTurn());
		turnDataBox.getChildren().add(endTurnBtn);
		
		Button exitBtn = new Button("Exit");
		exitBtn.setOnAction(e->gameView.close());
		turnDataBox.getChildren().add(exitBtn);
		
		turnDataBox.setAlignment(Pos.CENTER);
		this.setRight(turnDataBox);
	}

	public void update() {
		foodLabel.setText(String.format("%.1f", this.gameView.getFood()));
		goldLabel.setText(String.format("%.1f",this.gameView.getTreasury()));
		turnLabel.setText("Turn: " + this.gameView.getTurnCount());
		cityLabel.setText(this.gameView.getGamePane().getCurrentCity().getName());
	}
	
	
	public Label getFoodLabel() {
		return foodLabel;
	}

	
	public void setFoodLabel(Label foodLabel) {
		this.foodLabel = foodLabel;
	}

	public Label getGoldLabel() {
		return goldLabel;
	}

	public void setGoldLabel(Label goldLabel) {
		this.goldLabel = goldLabel;
	}

	public Label getTurnLabel() {
		return turnLabel;
	}

	public void setTurnLabel(Label turnLabel) {
		this.turnLabel = turnLabel;
	}

	public Label getCityLabel() {
		return cityLabel;
	}

	public void setCityLabel(Label cityLabel) {
		this.cityLabel = cityLabel;
	}
}
