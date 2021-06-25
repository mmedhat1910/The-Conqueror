package view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ActionBox extends FlowPane implements CityViewListener{
	private GameView gameView;
	private HBox detailsBox;
	

	private VBox actionButtons;
	private TextArea statusBox;
	
	public ActionBox(GameView gameView) {
		
	this.gameView = gameView;
	this.getStyleClass().add("action-box");
	this.setMaxHeight(gameView.getHeight()*0.15);
	
	this.detailsBox = new HBox();
	this.detailsBox.setPrefWidth(gameView.getWidth()*0.3);
	
	this.actionButtons = new VBox();
	this.actionButtons.setPrefWidth(gameView.getWidth()*0.2);

	
	this.statusBox = new TextArea();
	this.statusBox.setPrefWidth(gameView.getWidth()*0.3);
	this.statusBox.setText("Hello\nWorld\nI'am\n"+gameView.getPlayerName());
	
	this.getChildren().add(detailsBox);
	this.getChildren().add(actionButtons);
	this.getChildren().add(statusBox);
	this.getChildren().add(new Button("Map"));
	
	this.setAlignment(Pos.CENTER);
	

	}
	
	public HBox getDetailsBox() {
		return detailsBox;
	}

	public void setDetailsBox(HBox detailsBox) {
		this.detailsBox = detailsBox;
	}

	public VBox getActionButtons() {
		return actionButtons;
	}

	public void setActionButtons(VBox actionButtons) {
		this.actionButtons = actionButtons;
	}

	public TextArea getStatusBox() {
		return statusBox;
	}

	public void setStatusBox(TextArea statusBox) {
		this.statusBox = statusBox;
	}

	

	@Override
	public void onBuildingClicked(Button... buttons) {
		this.actionButtons.getChildren().clear();
		this.actionButtons.getChildren().addAll(buttons);
		
	}

	@Override
	public void onBuild(BuildingBlock b) {
		// TODO Auto-generated method stub
		
	}
}
