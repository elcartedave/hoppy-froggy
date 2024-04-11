package application;

import characters.*;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		GameStage window = new GameStage();
		window.setStage(stage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
