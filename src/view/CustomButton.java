package view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class CustomButton extends StackPane{
	private String title;
	private char size;
	private ImageView btn = new ImageView();
	private Label label;
	private double width = 200;
	public CustomButton(String title, char size) {
		this.title = title;
		this.size = size;
		
		Image normal_small = new Image("file:resources/images/buttons/normal_small.png");
		Image normal_large = new Image("file:resources/images/buttons/normal_large.png");
		
		Image hover_small = new Image("file:resources/images/buttons/hover_small.png");
		Image hover_large = new Image("file:resources/images/buttons/hover_large.png");
		
		this.minHeight(60);
		
		this.label = new Label(title);
		
		
		switch(size) {
			case 'l': {
				this.getStyleClass().add("custom-btn-lg");
				this.btn = new ImageView(normal_large);
				this.setOnMouseEntered(e->this.btn.setImage(hover_large));
				this.setOnMouseExited(e-> this.btn.setImage(normal_large));
				this.setWidth(250);
			} break;
			case 'm':{
				this.getStyleClass().add("custom-btn-md");
				this.btn = new ImageView(normal_small);
				this.setOnMouseEntered(e->this.btn.setImage(hover_small));
				this.setOnMouseExited(e-> this.btn.setImage(normal_small));
				this.setWidth(200);
			}break;
			case 's':{
				this.getStyleClass().add("custom-btn-sm");
				this.btn = new ImageView(normal_small);
				this.setOnMouseEntered(e->this.btn.setImage(hover_small));
				this.setOnMouseExited(e-> this.btn.setImage(normal_small));
				this.setWidth(150);
			}break;
			default: System.out.println("Incorrect size for btn");
			
		}
		label.setTextOverrun(OverrunStyle.CLIP);
		label.setMinHeight(Region.USE_PREF_SIZE);
		label.setAlignment(Pos.CENTER);
		label.setMinWidth(200);
		this.btn.setPreserveRatio(true);
		this.setAlignment(Pos.CENTER);
		btn.setFitWidth(width);
		this.setMaxWidth(width);
		this.getChildren().add(btn);
		this.getChildren().add(label);
		
	}

	public char getSize() {
		return size;
	}

	public void setSize(char size) {
		this.size = size;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getFitWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

}
