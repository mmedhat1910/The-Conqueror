package view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class AlertPane extends BorderPane{
	public AlertPane(Pane parent, double width, double height, String message) {
		
		this.getStyleClass().add("alert");
		this.setMaxWidth(700); this.setMaxHeight(400);
		TextArea area = new TextArea(message);
		area.setWrapText(true);
		area.setEditable(false);
		this.setCenter(area);
		Label l = new Label("Alert");
		l.setAlignment(Pos.CENTER);
		
		this.setTop(l);
		
		
		setAlignment(l, Pos.CENTER);
		setAlignment(area, Pos.CENTER);
		CustomButton exit = new CustomButton("Cancel",'m');
		exit.setOnMousePressed(e-> {
			
			parent.getChildren().remove(this);
		});
		exit.setDisable(false);
		this.setBottom(exit);
		setAlignment(exit, Pos.CENTER);
	}
	
	
	
}
