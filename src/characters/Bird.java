package characters;

import javafx.scene.image.Image;

public class Bird extends Sprite{
	private final static double WIDTH = 64; 
	private final static double HEIGHT = 50;
	
	private static final Image FACING_LEFT_BIRD = new Image(GameStage.class.getResourceAsStream("/images/bird_right.png"));
	private static final Image FACING_RIGHT_BIRD = new Image(GameStage.class.getResourceAsStream("/images/bird_left.png"));
	
	private boolean alive;
	
		
	Bird(double xPos, double yPos, Image img){
		super(xPos, yPos, img, WIDTH, HEIGHT);
		this.yVel = 0;
		this.xVel = 0;
		this.alive = false;
	}
	

	void move() { //move bird across the scene
		if(this.alive) {
			x += xVel;
			y += yVel;
			
			if (x < -100) {//set movement to 0 if it reach the end of the screen
				x = 500;
				alive = false;
				this.setxVel(0);  
			}
			if (x > 500) {//set movement to 0 if it reach the end of the screen
				alive = false;
				this.setxVel(0);
			}
			if (y > 750) {//set movement to 0 if it reach the end of the screen
				alive = false;
				this.setyVel(0);
			}
		}
	}
	
	void generate(double x, double y) { 
		if (!this.alive) {// generate bird
			this.alive = true;
			this.setXPos(x);
			this.setYPos(y);
		}
	}
	
	void checkCollision(Froggy frog){
		if(this.collidesWith(frog) && !frog.isWithShield()){
			System.out.println("Collided");
			frog.die();		
		}
	}


	public boolean isAlive() {
		return alive;
	}


	public void setAlive(boolean alive) {
		this.alive = alive;
	}


	public static Image getFacingLeftBird() {
		return FACING_LEFT_BIRD;
	}


	public static Image getFacingRightBird() {
		return FACING_RIGHT_BIRD;
	}

}
