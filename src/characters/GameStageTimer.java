
package characters;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;



public class GameStageTimer extends AnimationTimer{	
	private GraphicsContext gc;
	private Scene currentScene;

	//Froggy
	private Froggy frog;  
	private static boolean left;
	private static boolean right;
	private static boolean jump;
	
	//Predators
	private Fish left_fish;
	private Fish right_fish;
	private Bird left_bird;
	private Bird right_bird;
	
	//Power-Ups
	private Apple apple;
	private Spring spring;

	//Background Images
	private static final Image UNDERWATER_BG = new Image(GameStage.class.getResourceAsStream("/images/underwaterBG.png"), 500, 700, false, false);
	private static final Image TRANSITION_BG = new Image(GameStage.class.getResourceAsStream("/images/transitionBG.png"), 500, 700, false, false);
	private static final Image CLOUDS_BG = new Image(GameStage.class.getResourceAsStream("/images/skyBG.png"), 500, 700, false, false);
	private double changeInBg;
	private double counter;
	private int flag = 0;

	//Platform
	private int index=-1;
	private ArrayList<Platform> platform;
	
	//MediaSound
	private MediaPlayer jumpSound;
	private MediaPlayer gameOverSound;
	private MediaPlayer bgmusic;
	
	private static final Media MUSIC = new Media(GameStage.class.getResource("/sounds/bgmusic.mp3").toExternalForm()); // https://www.youtube.com/watch?v=Ljqe4Nj7nBA
	private static final Media JUMP = new Media(GameStage.class.getResource("/sounds/jump.mp3").toExternalForm()); //https://www.youtube.com/watch?v=B5EYAWGumZU
	private static final Media GAMEOVER = new Media(GameStage.class.getResource("/sounds/gameover.mp3").toExternalForm()); //https://www.youtube.com/watch?v=zURzynVgfRo
	
	private static Random r = new Random();
	
	public GameStageTimer(Scene scene, GraphicsContext gc, Scene mainscene, Stage main) {
    	this.gc = gc;	
    	this.frog = new Froggy("Samoyed");
    	this.currentScene = scene;
    	
    	//Predator
		this.left_fish = new Fish(600, 800, Fish.getFacingLeftFish());
		this.right_fish = new Fish(600, 800, Fish.getFacingRightFish());
		this.left_bird = new Bird(600, 800,	Bird.getFacingLeftBird());
		this.right_bird = new Bird(600, 800, Bird.getFacingRightBird());
		
		//Power-ups
		this.apple = new Apple(600, 800);
		this.spring = new Spring(600, 800);
				
		//Media Sound
		BGmusic();
        jumpSound = new MediaPlayer(JUMP);
        gameOverSound = new MediaPlayer(GAMEOVER);
        
        //Platforms
        this.platform = new ArrayList<Platform>();
    	this.platform.add(new Platform(r.nextInt(GameStage.WINDOW_WIDTH-100),450));
    	this.platform.add(new Platform(r.nextInt(GameStage.WINDOW_WIDTH-100),300));
    	this.platform.add(new Platform(r.nextInt(GameStage.WINDOW_WIDTH-100),150));
    	this.platform.add(new Platform(r.nextInt(GameStage.WINDOW_WIDTH-100),0));
    	this.platform.add(new Platform(r.nextInt(GameStage.WINDOW_WIDTH-100),-150));
		
    	this.eventHandlers();
    }

	@Override
	public void handle(long currentNanotime) {
		this.background();
		this.renderSprites();
		this.moveFroggy(currentNanotime);
		
		this.mainEvent();
		this.generatePowerups(currentNanotime);
		
		this.drawTime(); // draws the status of score in the top left corner
		
		if (frog.isWithSpring()) { //frog with spring will proceed to jump before spring will take effect
			if(frog.isJumping() && frog.getyVel() < 0) this.moveDown(-(frog.getyVel()*3));
			else this.moveDown(10); //moves down all sprites in the game at the same time as the platform
		} else this.moveDown(5);
		
		if(!this.frog.isAlive()) { // checks if froggy still alive
			this.stop();
        	this.gameOver();	  //stops the game and displays gameover
        	this.bgmusic.stop();
      	}
		this.springed(currentNanotime);
	}
	
	private void background() {
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc.drawImage(GameStageTimer.UNDERWATER_BG, 0, 0);
		
		if (changeInBg >= GameStage.WINDOW_HEIGHT) {
			changeInBg = 0;
			flag ++;
		}
		if (this.frog.getScore() > 3 && this.frog.getScore() < 10) {
			this.generateFish(100, 300);
		} else if (this.frog.getScore() >= 10 && flag == 0) {                    //background will start to scroll down
			this.generateFish(changeInBg, GameStage.WINDOW_HEIGHT-changeInBg);   //transition from underwater to UPLB
			this.gc.drawImage(TRANSITION_BG, 0, changeInBg - TRANSITION_BG.getHeight());
			this.gc.drawImage(GameStageTimer.UNDERWATER_BG, 0, this.changeInBg);
		} else if (flag == 1) {
			if (changeInBg <= 100) {
				this.generateBird(100, changeInBg+200); //generation of bird will depend on the size of the sky background in the game
			} else {
				this.generateBird(100, 300);
			}
			this.gc.drawImage(CLOUDS_BG, 0, changeInBg - CLOUDS_BG.getHeight());  //transition from UPLB to the sky background
			this.gc.drawImage(TRANSITION_BG, 0, this.changeInBg);
		} else if (flag > 1) {
			this.generateBird(100, 300);
			this.gc.drawImage(CLOUDS_BG, 0, changeInBg - CLOUDS_BG.getHeight());  //sky background
			this.gc.drawImage(CLOUDS_BG, 0, this.changeInBg);
		}
		
	}
	
	private void renderSprites() {  //render all sprites
		this.renderPlatform();
		this.frog.render(gc);
		this.left_fish.render(gc);
		this.right_fish.render(gc);
		this.left_bird.render(gc);
		this.right_bird.render(gc);
		this.apple.render(gc);
		this.spring.render(gc);
	}

	private void eventHandlers() {
		this.currentScene.setOnKeyPressed( new EventHandler<KeyEvent>() {
					public void handle(KeyEvent e){
						System.out.println(e.getCode());
						if (e.getCode() == KeyCode.RIGHT) {
							GameStageTimer.right = true;
					    } else if (e.getCode() == KeyCode.LEFT) {
							GameStageTimer.left = true;

					    } else if (e.getCode() == KeyCode.UP && !jump) {
							GameStageTimer.jump = true;
					    }
						
    	            }
				}
		);
		
		this.currentScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
					public void handle(KeyEvent e) {
						System.out.println(e.getCode());
						if (e.getCode() == KeyCode.RIGHT) { //constant
							GameStageTimer.right = false;
							frog.setxVel(0);
					    } else if (e.getCode() == KeyCode.LEFT) {
							GameStageTimer.left = false;
							frog.setxVel(0);
					    } else if (e.getCode() == KeyCode.UP) {
					    	GameStageTimer.jump = false;
					    	
					    }
    	            }
				}
			);
	}
	
	
	// Method for displaying the score during gameplay
	private void drawTime(){
		Font mainFont = Font.font("Verdana", FontWeight.BOLD, 20);
		
		this.gc.setFill(Color.WHITE);
		this.gc.setStroke(Color.DARKGREEN); // Border color
	    this.gc.setLineWidth(3); // Border width
	    
	    this.gc.setFont(mainFont);
	    
	 // Check if the frog is alive to display the score
		if (frog.isAlive()) {
			
			// Draw the score text on the canvas
	        String scoreText = "Score: " + frog.getScore();
	        this.gc.strokeText(scoreText,10, 22);
	        this.gc.fillText(scoreText, 10,22);

		
		} 
	}
	
	
	//method for displaying gameover
	private void gameOver() {
		  	jumpSound.stop(); 
		    gameOverSound.setVolume(1);
		    gameOverSound.play();

		    // Set the font for the text
		    Font mainFont = Font.font("Verdana", FontWeight.BOLD, 50);
		    Font secondaryFont = Font.font("Arial", FontWeight.BOLD, 30);

		    // Set the fill color and stroke color for the text and its outline
		    this.gc.setFill(Color.YELLOW);
		    this.gc.setStroke(Color.DARKGREEN); // Border color
		    this.gc.setLineWidth(3); // Border width

		    // Draw with a border outline
		    this.gc.setFont(mainFont);
		    this.gc.strokeText("GAME OVER!", 70, 350);
		    this.gc.fillText("GAME OVER!", 70, 350);

		    if (!this.frog.isAlive()) { 
		        // Draw "FROGGY DIED" with a border outline
		        this.gc.setFont(secondaryFont);
		        this.gc.strokeText("FROGGY DIED :(", (GameStage.WINDOW_WIDTH / 4) , (GameStage.WINDOW_HEIGHT / 2) + 40);
		        this.gc.fillText("FROGGY DIED :(", (GameStage.WINDOW_WIDTH / 4), (GameStage.WINDOW_HEIGHT / 2) + 40);

		        // Show the score on the screen
		        String scoreText = "Score: " + frog.getScore();
		        this.gc.strokeText(scoreText, (GameStage.WINDOW_WIDTH / 4) + 55, (GameStage.WINDOW_HEIGHT / 2) + 80);
		        this.gc.fillText(scoreText, (GameStage.WINDOW_WIDTH / 4) + 55, (GameStage.WINDOW_HEIGHT / 2) + 80);

		        // Output the final score to the console
		        System.out.println(" LOSES! | Final Score: " + frog.getScore());
		    }
	}
	
	//method for playing the music
	private void BGmusic() {
		   this.bgmusic = new MediaPlayer(MUSIC);
		   this.bgmusic.setVolume(0.5);
		   this.bgmusic.setCycleCount(MediaPlayer.INDEFINITE); // Play the music indefinitely
		   this.bgmusic.play();
		}

	//FROG movement
	private void moveFroggy(long currentNanotime) {
		frog.move(currentNanotime, spring);
		if (GameStageTimer.right) {
			moveRight();
		}
		if (GameStageTimer.left) {
			moveLeft();
		}
		if (GameStageTimer.jump) {
			jumpFrog(currentNanotime);
		}
	}
	
	private void moveLeft() {
		if (this.frog.getXPos() >= 0){
			frog.setxVel(-4);
		}
	}
	private void moveRight() {
		if (this.frog.getXPos() <= 400){ 
			frog.setxVel(4);
		}
	}
	
	private void jumpFrog(long currentNanotime) { //frog will jump for 0.8 seconds and will proceed to fall
		if (!frog.isJumping() && !frog.isWithSpring()) {
			frog.setJumpingStart(currentNanotime/1000000000.0);  //store start time for jumping
			frog.setJumping(true);
			frog.setGravity(8.0);
            jumpSound.stop(); // Stop any previous instances
            jumpSound.setVolume(5);
            jumpSound.play(); //sound effect for jumping
		}
	}
	
	//Genearate FISH and make it move
	private void generateFish(double y1, double y2) {
		if(frog.getScore()% 5 == 0 && frog.getScore() != 0) {
			if (!left_fish.isAlive() && !right_fish.isAlive()) {
				int num = r.nextInt(3); //random pick between left or right fish
				double randomize_y = r.nextDouble(y2) + (y1); //range depends on the background
				if (num == 0) { //randomize initial position of left facingfish
					left_fish.generate(500, randomize_y);
				}
				if (num == 1) { //randomize initial position of right facing fish
					right_fish.generate(-100, randomize_y);
				}
			}
		}
		moveFish(); //movefish
	}
		
	private void moveFish() {
		if (left_fish.isAlive()) { //move left facing fish until it reach the end of the screen
			left_fish.setxVel(-3);
			left_fish.move();
			left_fish.checkCollision(this.frog); //check collision with froggy

		}
		if (right_fish.isAlive()) { //move right facing fish until it reach the end of the screen
			right_fish.setxVel(3);
			right_fish.move();
			right_fish.checkCollision(this.frog);// check collision with froggy
		}
			
	}
	
	//Genearate BIRD and make it move
	private void generateBird(double y1, double y2) {
		if(frog.getScore()% 5 == 0 && frog.getScore() != 0) {
			if ( !left_bird.isAlive() && !right_bird.isAlive()) {
				int num = r.nextInt(2); //random pick between left or right bird
				double randomize_y = r.nextDouble(y2) + y1;
				if (num == 0) { //randomize initial position of left facing bird
					left_bird.generate(500, randomize_y);
				}
				if (num == 1) { //randomize initial position of right facing bird
					right_bird.generate(-100, randomize_y);

				}
			}
		}
		moveBird();
	}
		
	private void moveBird() {
		if (left_bird.isAlive()) { //move left facing bird until it reach the end of the screen
			left_bird.setxVel(-3);
			left_bird.move();
			left_bird.checkCollision(this.frog);
		}
		if (right_bird.isAlive()) { //move right facing bird until it reach the end of the screen
			right_bird.setxVel(3);
			right_bird.move();
			right_bird.checkCollision(this.frog);
		}	
	}

	//PLATFORM
	private void renderPlatform() {
		for (Platform platform : this.platform){
			platform.render(this.gc);
		}
	}
	
	//Generates platform with their x positions on random.
	//also generates random spikes and random platforms again on different
	//positions
	private void generatePlatform() {
		int x = r.nextInt(GameStage.WINDOW_WIDTH-100);
		Platform p = new Platform(x,-300);
		this.platform.add(p);
		checkPlatformSpacing();
		int x1 = r.nextInt(GameStage.WINDOW_WIDTH-100);
		while (x1+p.getWidth()+10>x && x1<x+p.getWidth()+10) {
			x1=r.nextInt(GameStage.WINDOW_WIDTH-100);
		}
		int random =  r.nextInt(10);
		if (random < 3) {
			Platform q = new Platform(x1,-300, Platform.getSpikyImg());
			this.platform.add(q);
		}
		else if (random==3 || random==4) this.platform.add(new Platform(x1,-300));
		checkPlatformCollisions();		
		
	}
	//iterates through the arrayList of platforms and removes the platform
	//that is no longer visible on the game
	private void removePlatform() {
		for(int i = 0; i < this.platform.size(); i++){
			Platform m = this.platform.get(i);
			if (!m.isVisible()) {
				this.platform.remove(i);
				frog.incrementScore();
			}
		}
	}
	//method that moves down the entire arrayList of platforms
	private void movePlatformDown() {
		for(int i = 0; i < this.platform.size(); i++){
			Platform m = this.platform.get(i);
			m.setDownPlatform(true);
			m.move();
		}
	}
	
	//method that is responsible for the collision of frog to the platform,
	//and does the appropriate actions when true
	private void checkForContact(Froggy frog) {
		for(int i = 0; i < this.platform.size(); i++){
			Platform m = this.platform.get(i);
			if(frog.getYPos()+50<m.getYPos() && m.checkCollision_Frog(frog)) {
				if(!m.didCollide()) { //checks if the platform has not collided with the frog yet
					m.setDidCollide(true);
					generatePlatform(); //generates a new platform
				}
				if (m.isSpiky()) { //takes into consideration if the platform is spiked
					if(!frog.isWithShield()) {
						frog.die();	//frog will die if collided with the spiked platform if it doesnt have shield
					}
				}
				stopFrog(); //stops the frog when collided with a platform
				this.index=i; //sets the value of the index to the index of the platform that has been collided with the frog
			}
		}
		
	}
	
	//method that will handle the frog's jumping, falling, and stopped attribute
	//it also handles all the corresponding platform attributes together with the frog
	private void mainEvent() {
		if(frog.isJumping() && frog.getYPos()<=200) { //moves platform if and only if frog is jumping and it reaches appropriate maximum y position
			movePlatformDown();
		}
		if(frog.isFalling()) { //checks for collision if the frog is falling
			checkForContact(frog);
		}
		if(frog.isStopped() && this.index!=-1) { //if frog is stopped
			if(!frog.isWithSpring() && index<=this.platform.size()) {
				Platform n=this.platform.get(index); //arbitrarily gets the specific platform with the index returned earlier by the checkForContact method
				if(!frog.isJumping() && !frog.isFalling() && n.didCollide()) {
					if(!n.checkCollision_Frog(frog)) { //monitors that specific platform if there is no collision anymore
						frog.setFalling(true); //if so, then the frog will fall
					}
				}
			} 
		}
		removePlatform(); //method for removing the platforms
	}
	
	//method that will run if the frog acquired a spring
	private void springed(long currentNanotime) {
		if(frog.isWithSpring()) {
			frog.setYPos(frog.getYPos());
			frog.setJumping(true); //sets the frog as jumping
			frog.setFalling(false);
			for(int i = 0; i < this.platform.size(); i++){
				Platform m = this.platform.get(i);
				m.setDownPlatform(true);
				m.move(); //moves the platform momentarily
			}
			if(this.changeInBg % 15 == 0) {
				generatePlatform(); //generates a platform based on how much the background picture moved
			}
		}
	}
	//method that will run and checks if there are 2 platforms that are far from each other
	//if so, readjusts the position of the most recent platform
	private void checkPlatformSpacing() {
		Platform m = this.platform.get(this.platform.size()-1);
		Platform p = this.platform.get(this.platform.size()-2);
//		Platform q = this.platform.get(this.platform.size()-3);
		
		if(p.getYPos()-(m.getYPos()+m.getHeight())> 150) {
			m.setYPos(p.getYPos()-150);
		}
	}
	
	//method that checks if there are any collisions/overlapping among the platforms
	//if so, then removes a platform
	private void checkPlatformCollisions() {
		for(int i = this.platform.size()-1; i >= 3; i--){
			Platform m = this.platform.get(i);
			Platform p = this.platform.get(i-1);
			Platform q = this.platform.get(i-2);
			Platform r = this.platform.get(i-3);
			if(m.collidesWith(p) || m.collidesWith(q) || m.collidesWith(r)) {
				this.platform.remove(i);
			}
		}
	}
	//method that stops the frog, by setting its jumping and falling attributes to false
	//and by fixing its position, including its x and y velocity
	private void stopFrog() {
		frog.setYPos(frog.getYPos()+12);
		frog.setyVel(0);
		frog.setxVel(0);
		frog.setJumping(false);
		frog.setFalling(false);
		frog.setStopped(true);
	}
	
	private void generatePowerups(long currentNanotime) { //generate powerups
		if (frog.getScore() % 6 == 0 && frog.getScore() != 0) { //generate apple if score if conditions are met
			if (!apple.isExist() && !frog.isWithShield()) { //apple is not currently in game and frog does not have shield on
				int randomize_x = r.nextInt(400); //randomize position of apple
				apple.setXPos(randomize_x);
				apple.setYPos(-150);
				apple.setExist(true);
			}
		}
		if (currentNanotime/1000000000.0 - spring.getStart() >= 30 && frog.getScore() > 10) { //generate spring every 30 seconds
			if (!spring.isExist() && !frog.isWithSpring()) { //spring is not in game and frog does not have spring on
				int randomX = r.nextInt(400);
				spring.setX(randomX);
				spring.setYPos(-150);
				spring.setExist(true);
			}
		}
		activatePowerUps(currentNanotime);
		removePowerUps(currentNanotime);
	}
	
	private void activatePowerUps(long currentNanotime) {
		if (apple.isExist() && !frog.isWithShield()) {//activate apple if shield in not activated and frog collided with apple
			apple.checkCollision(this.frog, currentNanotime);

		}
		if (spring.isExist() && !frog.isWithSpring()){ //activate spring if frog does not have spring on and frog is not falling
			spring.checkCollision(frog, platform, currentNanotime);
			if (frog.isWithSpring()) {
				this.counter = changeInBg;
			}
		}
	}
	
	private void removePowerUps(long currentNanotime) { //both powerups will last for 5 seconds
		if (frog.isWithShield() && (currentNanotime/1000000000.0) - apple.getStart() >= 5) { 
			apple.setExist(false);
			frog.setWithShield(false); //reset image for normal froggy
			frog.loadImage(Froggy.getFrog());
		}
		if (frog.isWithSpring() && (currentNanotime/1000000000.0) - spring.getStart() >= 5) {
			spring.setExist(false);
			frog.setWithSpring(false);
			frog.setFalling(true); //froggy will now fall if there the effects of the spring has worn out
		}
		if(!frog.isVisible()) frog.die(); //froggy will die it is falls outside the scene
	}
	
	private void moveDown(double speed) { //move other sprites in the scene other than the frog along with the down movement of platform
		if((frog.isJumping() && frog.getYPos()<=200) || frog.isWithSpring()) {
			apple.setyVel(speed); //set speed as change in velocity, same as the platform
			apple.move();
			spring.setyVel(speed);
			spring.move();
			if (frog.getScore() > 10) {
				changeInBg += 1;
			}
			if(left_bird.isAlive()) {
				left_bird.setyVel(speed);
			}
			if(right_bird.isAlive()) {
				right_bird.setyVel(speed);
			}
			if(left_fish.isAlive()) {
				left_fish.setyVel(speed);
			}
			if(right_fish.isAlive()) {
				right_fish.setyVel(speed);
			}
		} else {
			apple.setyVel(0); //stop moving down if platform has stopped
			spring.setyVel(0);
			left_bird.setyVel(0);
			right_bird.setyVel(0);
			left_fish.setyVel(0);
			right_fish.setyVel(0);
		}
	}
}
	
	
	