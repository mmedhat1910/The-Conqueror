package view;

import java.util.function.Function;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class MessagePane extends BorderPane{
	private HBox buttonsBar;
	private VBox content;


	private HBox titleBox;
	private String message;
	private double width;
	private double height;
	private Pane parent;
	private GameView gameView;
	
	public MessagePane(GameView gameView,Pane pane,String title,double width, double height , CustomButton btn, Node... content) {
		this.gameView = gameView;
		this.parent = pane;
		this.getStyleClass().add("message-pane");
		this.titleBox = new HBox();
		this.width = Math.max(this.gameView.getWidth()*0.5,width);
		this.height = Math.max(this.gameView.getHeight()*0.5, height);
		this.content = new VBox();
		this.buttonsBar = new HBox();
		Label l = new Label(title);
		this.titleBox.getStyleClass().add("title-box");
		this.titleBox.getChildren().add(l);
		this.content.getChildren().addAll(content);
		this.content.setAlignment(Pos.CENTER);
		this.content.setSpacing(20);
		CustomButton cancel = new CustomButton("Cancel",'m');
		cancel.setOnMouseClicked(e->{
			parent.getChildren().remove(this); 
//			this.gameView.getGamePane().getActionBox().toggleEnableBtns();
//			this.gameView.getGamePane().getInfoBar().toggleEnableBtns();
			});
		if(btn != null)
			this.buttonsBar.getChildren().add(btn);
		btn.setOnMouseClicked(e->{
			btn.getOnMouseClicked();
//			this.gameView.getGamePane().getActionBox().toggleEnableBtns();
//			this.gameView.getGamePane().getInfoBar().toggleEnableBtns();
		});
		this.buttonsBar.getChildren().add(cancel);
		this.buttonsBar.setSpacing(10);
//		this.gameView.getGamePane().getInfoBar().toggleEnableBtns();
//		this.gameView.getGamePane().getActionBox().toggleEnableBtns();
		
		this.setMaxHeight(height);
		this.setMaxWidth(width);
		this.content.setAlignment(Pos.CENTER);
		this.buttonsBar.setAlignment(Pos.CENTER);
		this.titleBox.setAlignment(Pos.CENTER);
		

		
		
		
		
		this.setTop(titleBox);
		this.setCenter(this.content);
		this.setBottom(buttonsBar);
		
	}
	public VBox getContent() {
		return content;
	}


	public void setContent(VBox content) {
		this.content = content;
	}

}
