package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


public class GamePane extends BorderPane{
	private InfoBar infoBar;
	private ActionBox actionBox;
	private StackPane mainPane;
	private GameView gameView;

	public GamePane(GameView gameView) {
		this.gameView = gameView;
		this.infoBar = new InfoBar(gameView);
		this.actionBox = new ActionBox(gameView);
		this.mainPane = new StackPane();
		
		
		
		this.setTop(infoBar);
		this.setBottom(actionBox);
		this.setCenter(mainPane);
		
	}

}
