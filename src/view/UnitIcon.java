package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import units.Unit;

public class UnitIcon extends ImageView{
	private String type;
	private int level;
	
	public UnitIcon(String type, int level) {
		this.type = type;
		this.level =level;
		
		Image unit = new Image("file:resources/images/army/icons/"+this.type+this.level+".png");
		this.setFitWidth(70);
		this.setPreserveRatio(true);
		this.setImage(unit);
	}

	
	public int getLevel() {
		return level;
	}

	

	public void setLevel(int level) {
		this.level = level;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

}
