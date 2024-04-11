
package characters;

//Import necessary JavaFX libraries
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class GameStage {
	private Stage stage;
	private Scene mainScene;
	private Scene gameScene;
	private Scene instScene;
	private Scene devScene;
	private Group root;
	private Canvas canvas;
	
	public static final int WINDOW_HEIGHT = 700;
	public static final int WINDOW_WIDTH = 500;
	
	private static final Image ICON = new Image(GameStage.class.getResourceAsStream("/images/froggy.png")); //image for icon
	
	//images for background, developer info, and instructions
	private static final Image BACKGROUND = new Image(GameStage.class.getResourceAsStream("/images/background.png"), 500, 700, false, false);
	private static final Image ABOUT_DEV = new Image(GameStage.class.getResourceAsStream("/images/AboutDev.png"));
	private static final Image INSTRUCTIONS = new Image(GameStage.class.getResourceAsStream("/images/Instructions.png"));
	
	
	// Initialize root, game scene, and canvas
	public GameStage() {
		this.root = new Group();
		this.gameScene = new Scene(root,GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
	}
	
	// Set the stage and configure initial scenes
	public void setStage(Stage stage) {
		this.stage = stage;
		this.root.getChildren().addAll(GameStage.createCanvas(), canvas);
		this.stage.setTitle("Hoppy Froggy");

		this.forGameScene(stage);
		this.forDevScene(stage);
		this.forInstScene(stage);
		
		this.stage.setScene(this.mainScene);
		this.stage.setResizable(false);   //fix size of the stage
		stage.getIcons().add(ICON); //sets the icon of the stage
		this.stage.show();
	}
	
	 // Create a canvas with a background image
	private static Canvas createCanvas() {
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);        
	    GraphicsContext gc = canvas.getGraphicsContext2D();  // Draw the background image on the canvas
	    gc.drawImage(GameStage.BACKGROUND, 0, 0);
	    return canvas;
	}
	
	 // Set up the scrollable instruction scene
	private void forInstScene(Stage stage) {
		ScrollPane root = new ScrollPane();	
	    this.instScene = new Scene(root,GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
	    root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // Disable horizontal scrolling
		root.setContent(this.createInst());
	}
	
	// Set up the scene displaying developer information
	private void forDevScene(Stage stage) {
		StackPane root = new StackPane();
	    this.devScene = new Scene(root,GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
	    root.getChildren().add(this.createDev());
	}
	
	// Set up the initial menu screen with buttons for different actions
	private void forGameScene(Stage stage){
		StackPane root = new StackPane();
		
		// new canvas for the menu scene
		root.getChildren().add(GameStage.createCanvas());
      
		// adding the buttons in a vbox
      VBox vbox = new VBox();
      vbox.setAlignment(Pos.CENTER);
      vbox.setPadding(new Insets(5));
      vbox.setSpacing(0);

      // images for the buttons
      Image title = new Image(GameStage.class.getResourceAsStream("/images/yellowtitle.png"), 350, 150, false, false);
      ImageView titleView = new ImageView(title);
      
      Image image = new Image(GameStage.class.getResourceAsStream("/images/start.png"), 200, 100, false, false);
      ImageView startgame = new ImageView(image);
      
      Image image2 = new Image(GameStage.class.getResourceAsStream("/images/about.png"), 200, 100, false, false);
      ImageView about = new ImageView(image2);
      
      Image image3 = new Image(GameStage.class.getResourceAsStream("/images/developers.png"), 200, 100, false, false);
      ImageView developers = new ImageView(image3);
      
      
      Button b1 = new Button();
      Button b2 = new Button();
      Button b3 = new Button();
      
      //sets the design for the buttons
      b1.setGraphic(startgame);
      b1.setStyle("-fx-background-color:transparent;-fx-padding:0;-fx-background-size:0;");
      
      b2.setGraphic(about);
      b2.setStyle("-fx-background-color:transparent;-fx-padding:0;-fx-background-size:0;");
      
      b3.setGraphic(developers);
      b3.setStyle("-fx-background-color:transparent;-fx-padding:0;-fx-background-size:0;");
      
      
      vbox.getChildren().addAll(titleView, b1, b2, b3);
      
      // Define actions for the buttons to switch scenes
      
      // changes the scene into the game scene
      b1.setOnAction(new EventHandler<ActionEvent>() {
          @Override 
          public void handle(ActionEvent e) {
              setGameScene(stage);		
          }
      });
      // changes the scene into the Instruction scene
      b2.setOnAction(new EventHandler<ActionEvent>() {
          @Override 
          public void handle(ActionEvent e) {
              setInstScene(stage);	
          }
      });
      // changes the scene into the Developer scene
      b3.setOnAction(new EventHandler<ActionEvent>() {
          @Override 
          public void handle(ActionEvent e) {
              setDevScene(stage);
          }
      });
      
      root.getChildren().add(vbox);
      this.mainScene = new Scene(root,GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
	}
	
	 // Set the scene to the main menu
	 void setMenuScene(Stage stage) {
		stage.setScene(mainScene);
	}

	// Start the GameStageTimer and set the scene to the game screen
    void setGameScene(Stage stage) {
	   GraphicsContext gc = this.canvas.getGraphicsContext2D();	
	   GameStageTimer gametimer = new GameStageTimer(gameScene, gc, mainScene,stage);
	   gametimer.start();
	   
	   stage.setScene(this.gameScene);	
}
    
    // Set the scene to display instructions and about the game
	private void setInstScene(Stage stage) {

		stage.setScene(instScene);
	}
	
	 // Set the scene to display developer information
	private void setDevScene(Stage stage) {
		stage.setScene(devScene);
	}
	
	// Create the pane for instructions display
	private Pane createInst() {
		Pane about = new Pane();
		ImageView img1 = new ImageView(INSTRUCTIONS); //image for instruction
		
		Image image1 = new Image(GameStage.class.getResourceAsStream("/images/back.png"), 100, 50, false, false);
		ImageView button = new ImageView(image1);
		
		//back button in order to return to main menu
		Button b1 = new Button();
		b1.setGraphic(button);
		b1.setStyle("-fx-background-color:transparent;-fx-padding:0;-fx-background-size:0;");
		b1.setOnMouseClicked(event -> setMenuScene(stage));
		b1.relocate(350,950);
		
		about.getChildren().addAll(createCanvas(),img1,b1);
		
		return about;
	}
	
	// Create the pane for developer information display
	private Pane createDev() {
		Pane dev = new Pane();
		ImageView img1 = new ImageView(ABOUT_DEV); //image for about the developers
		
		Image image1 = new Image(GameStage.class.getResourceAsStream("/images/back.png"), 100, 50, false, false);
		ImageView button = new ImageView(image1);
		
		//back button in order to return to main menu
		Button b1 = new Button();
		b1.setGraphic(button);
		b1.setStyle("-fx-background-color:transparent;-fx-padding:0;-fx-background-size:0;");
		b1.setOnMouseClicked(event -> setMenuScene(stage));
		b1.relocate(340,630);
		
		dev.getChildren().addAll(createCanvas(),img1,b1);
		
		return dev;
		
	}
	
}
