package view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class AlertPane extends BorderPane{
	public AlertPane(Pane parent, double width, double height, String message) {
	
		this.getStyleClass().add("alert");
		this.setMaxWidth(width); this.setMaxHeight(height);
		TextArea area = new TextArea(message);
		area.setEditable(false);
		this.setCenter(area);
		this.setTop(new Label("Alert"));
		
		Button exit = new Button("Cancel");
		exit.setOnAction(e-> {
			parent.getChildren().remove(this);
		});
		this.setBottom(exit);
		setAlignment(exit, Pos.BOTTOM_RIGHT);
	}
	
}
