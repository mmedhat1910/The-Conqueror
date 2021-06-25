package view;

import engine.City;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class GamePane extends BorderPane implements CityViewListener{
	private InfoBar infoBar;
	private ActionBox actionBox;
	private StackPane mainPane;
	private GameView gameView;
	private CityView cityView;
	private City currentCity;
	

	
	public CityView getCityView() {
		return cityView;
	}



	public void setCityView(CityView cityView) {
		this.cityView = cityView;
	}



	
	
	
	public City getCurrentCity() {
		return currentCity;
	}



	public void setCurrentCity(City currentCity) {
		this.currentCity = currentCity;
		this.getCityView().setCity(currentCity);
	}
	public InfoBar getInfoBar() {
		return infoBar;
	}



	public void setInfoBar(InfoBar infoBar) {
		this.infoBar = infoBar;
	}



	public ActionBox getActionBox() {
		return actionBox;
	}



	public void setActionBox(ActionBox actionBox) {
		this.actionBox = actionBox;
	}


	public GamePane(GameView gameView, City currentCity) {
		this.gameView = gameView;
		this.currentCity = currentCity;
		this.setMaxWidth(this.gameView.getWidth());
		this.infoBar = new InfoBar(gameView);
		this.actionBox = new ActionBox(gameView);
		this.mainPane = new StackPane();
		this.cityView = new CityView(gameView, currentCity);
		for(BuildingBlock b : this.cityView.getBlocks())
			b.setListener(actionBox, this);
		
		this.mainPane.getChildren().add(cityView);
		this.mainPane.setAlignment(Pos.CENTER);
		this.setTop(infoBar);
		this.setBottom(actionBox);
		this.setCenter(mainPane);
		
	}



	@Override
	public void onBuildingClicked(Button... buttons) {
		this.setBottom(actionBox);
		
	}



	@Override
	public void onBuild(BuildingBlock b) {

		
		
		ChoiceBox<String> choiceBox = new ChoiceBox<String>();

		choiceBox.getItems().add("ArcheryRange");
		choiceBox.getItems().add("Barracks");
		choiceBox.getItems().add("Stable");
		choiceBox.getItems().add("Farm");
		choiceBox.getItems().add("Market");
		
		BorderPane dialog = new BorderPane();
		String buildingType =  choiceBox.getValue();
		
	
		
		choiceBox.setOnAction(e->System.out.println(buildingType));
		b.setBuildingType("ArcheryRange");
		b.startBuild();
		Button exitBtn = new Button("Exit");
		exitBtn.setOnAction(e->this.mainPane.getChildren().remove(dialog));
		
		dialog.setMaxSize(this.gameView.getWidth()*0.4, this.gameView.getHeight()*0.4);
		dialog.setTop(exitBtn);
		dialog.setCenter(choiceBox);
//		this.mainPane.getChildren().add(dialog);
		
//		this.setCenter(mainPane);
	}

}
