package characters;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Spring extends Sprite{
	private final static double WIDTH = 70;
	private final static double HEIGTH = 99;
	
	private boolean exist;
	private double start;
	
	//for sound effects when collided with spring
	private static final Image IMG = new Image(GameStage.class.getResourceAsStream("/images/powerupsSpring.png"), WIDTH, HEIGTH, false, false);
	private static final Media KOKAK = new Media(GameStage.class.getResource("/sounds/kokak.mp3").toExternalForm());
	private static final Media BOOST = new Media(GameStage.class.getResource("/sounds/boost.mp3").toExternalForm());	
	private MediaPlayer kokak;
	private MediaPlayer boost;

	Spring(double xPos, double yPos){
		super(xPos, yPos, IMG, WIDTH, HEIGTH);
		this.exist = true;
		this.kokak = new MediaPlayer(KOKAK);
		this.boost = new MediaPlayer(BOOST);
		this.start = 0;
	}
	
	void checkCollision(Froggy frog, ArrayList<Platform> p, long currentNanotime) {
		if(this.collidesWith(frog)) {
			kokak.stop();
	        kokak.setVolume(1);
	        kokak.play();
	        boost.stop();
	        boost.setVolume(1);
			boost.play();
			
			
			frog.setWithSpring(true);
			System.out.println("Power-Up: Spring");
			this.setXPos(600); //set position of spring outside the scene
			this.setYPos(800);
			this.start = currentNanotime/1000000000.0; //note  the strating time of the spring
		}
	}
	
	public void move() { //move spring
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