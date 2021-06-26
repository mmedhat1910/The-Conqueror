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

public class MessagePane extends BorderPane{
	private HBox buttonsBar;
	private VBox content;
	private HBox titleBox;
	private String message;
	private double width;
	private double height;
	private Pane parent;
	
	
	public MessagePane(Pane pane,String title,double width, double height , Button btn, Node... content) {
		this.parent = pane;
		this.getStyleClass().add("message-pane");
		this.titleBox = new HBox();
		this.width = width;
		this.height = height;
		this.content = new VBox();
		this.buttonsBar = new HBox();
		this.titleBox.getChildren().add(new Label(title));
		this.content.getChildren().addAll(content);
		
		Button exitBtn = new Button("Cancel");
		exitBtn.setOnAction(e->parent.getChildren().remove(this));
		this.buttonsBar.getChildren().addAll(btn, exitBtn);
		
		
		
		this.setMaxHeight(height);
		this.setMaxWidth(width);
		this.buttonsBar.setAlignment(Pos.CENTER_RIGHT);
		this.content.setAlignment(Pos.CENTER);
		this.titleBox.setAlignment(Pos.CENTER);
		

		
		
		
		
		this.setTop(titleBox);
		this.setCenter(this.content);
		this.setBottom(buttonsBar);
		
	}
	
}
