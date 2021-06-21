package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class InfoBar extends BorderPane{
	private GameView gameView;
	
	public InfoBar(GameView gameView) {
		this.gameView = gameView;
		this.getStyleClass().add("info-bar");
		this.setMaxHeight(50);
		this.setPrefWidth(gameView.getWidth());
		this.setPadding(new Insets(5, 10,5,10 ));

		HBox keepUpDataBox = new HBox();
		
		ImageView goldIcon  = new ImageView("file:resources/images/icons/gold2.png");
		goldIcon.setFitHeight(50); goldIcon.setFitWidth(50);
		keepUpDataBox.getChildren().add(goldIcon);
		
		keepUpDataBox.getChildren().add(new Label(gameView.getTreasury()+""));
		
		ImageView foodIcon = new ImageView("file:resources/images/icons/apple.png");
		foodIcon.setFitHeight(40); foodIcon.setFitWidth(40);
		keepUpDataBox.getChildren().add(foodIcon);
		
		keepUpDataBox.getChildren().add(new Label(gameView.getFood()+""));
		keepUpDataBox.setAlignment(Pos.CENTER);
		this.setLeft(keepUpDataBox);

		HBox playerDataBox = new HBox();
		playerDataBox.getChildren().add(new Label(gameView.getPlayerName()));
		playerDataBox.getChildren().add(new Label(gameView.getPlayerCity()));
		playerDataBox.setAlignment(Pos.CENTER);
		
		this.setCenter(playerDataBox);
		
		HBox turnDataBox = new HBox();
		turnDataBox.getChildren().add(new Label("Turn: "+  gameView.getTurnCount()));
		Button endTurnBtn = new Button("End Turn");
		endTurnBtn.setOnAction(e->gameView.getListener().onEndTurn());
		turnDataBox.getChildren().add(endTurnBtn);
		
//		Button exitBtn = new Button("Exit");
//		exitBtn.setOnAction(e->gameView.close());
//		turnDataBox.getChildren().add(exitBtn);
		
		turnDataBox.setAlignment(Pos.CENTER);
		this.setRight(turnDataBox);
	}
	
}
