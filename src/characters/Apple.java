package characters;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Apple extends Sprite{
	private final static double WIDTH = 70;
	private final static double HEIGTH = 99;
	
	private boolean exist;
	private double start;
	
	private static final Image IMG = new Image(GameStage.class.getResourceAsStream("/images/powerupsApple.png"), WIDTH, HEIGTH, false, false);
	protected MediaPlayer kokak;
	private static final Media KOKAK = new Media(GameStage.class.getResource("/sounds/kokak.mp3").toExternalForm());
	
	
	Apple(double xPos, double yPos){
		super(xPos, yPos, IMG, WIDTH, HEIGTH);
		this.exist = false;
		this.kokak = new MediaPlayer(KOKAK);
	}
	
	void checkCollision(Froggy frog, long currentNanotime) {
		if(this.collidesWith(frog)) { //activates shield if apple collides with frog
			kokak.stop(); // sound for collision
	        kokak.setVolume(1);
	        kokak.play();
			
			System.out.println("Power-Up: Apple");
			frog.loadImage(Froggy.getShieldedFrog()); //load a different image indicating the activation of shield
			frog.setWithShield(true);
			this.setXPos(600);  //set position of apple outside the scene
			this.setYPos(800);
			this.start = currentNanotime/1000000000.0;
		}
	}
	
	
	public void move() {
		this.y += yVel;
		if (y > 750) exist = false; 
	}

	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	public double getStart() {
		return start;
	}

	public void setStart(double start) {
		this.start = start;
	}
}
