package view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ActionAlert extends BorderPane{
	private HBox buttonsBar;
	


	private HBox titleBox;
	private String message;
	private double width;
	private double height;
	private Pane parent;
	private TextArea content;
	
	
	public ActionAlert(Pane pane,String title,double width, double height , String msg,CustomButton... btns) {
		this.parent = pane;
		this.getStyleClass().add("message-pane");
		this.titleBox = new HBox();
		this.width = Math.max(width, 700);
		this.height = Math.max( height, 500);
		
		this.buttonsBar = new HBox();
		Label titleLabel = new Label(title);
		titleLabel.setStyle("-fx-text-fill: rgb(96, 62, 27)");
		this.titleBox.getChildren().add(titleLabel);
		buttonsBar.setSpacing(10);
//		setAlignment(buttonsBar, Pos.CENTER);
		this.buttonsBar.getChildren().addAll(btns);
		this.content = new TextArea(msg);
		this.content.setEditable(false);
		this.content.setWrapText(true);
//		Button exitBtn = new Button("Cancel (SHEELO)");
//		exitBtn.setOnAction(e->parent.getChildren().remove(this));
		
//		this.buttonsBar.getChildren().add(exitBtn);
		
		this.setMaxHeight(height);
		this.setMaxWidth(width);
		this.buttonsBar.setAlignment(Pos.BOTTOM_CENTER);
		this.titleBox.setAlignment(Pos.CENTER);
		
		
		
		this.setTop(titleBox);
		this.setCenter(content);
		this.setBottom(buttonsBar);
		
	}

}
