package characters;


import javafx.scene.image.Image;

public class Froggy extends Sprite{
	private String name;
	private int score=0;
	
	private final static double WIDTH = 60;  //frog width and height
	private final static double HEIGHT = 67;

	private static final Image FROG = new Image(GameStage.class.getResourceAsStream("/images/froggy.png"), WIDTH, HEIGHT, false, false);
	private static final Image SHIELDED_FROG = new Image(GameStage.class.getResourceAsStream("/images/shieldedFroggy.png"), WIDTH, HEIGHT, false, false);

	private final static double INITIAL_X = 200;  //for positioning
	private final static double INITIAL_Y = 550;

	private boolean jumping = false;  //for jumping
	private boolean falling = false;
	private double jumpingStart;
	private double gravity = 0.0;

	private static final int WINDOW_HEIGHT = 700;
	private static final int WINDOW_WIDTH = 500;
	
	private boolean alive;
	private boolean withShield = false;
	private boolean withSpring = false;
	private boolean stopped;
	
	Froggy(String name) {
		super(INITIAL_X, INITIAL_Y, FROG, WIDTH, HEIGHT);
		this.name = name;
		this.alive = true;
	}

	public void move(long currentNanotime, Spring spring) { //controls the movement of frog
        x += xVel;
        y += yVel;
        if (x <= 0) x=0;
        if ( x >= WINDOW_WIDTH-WIDTH) x = 400;
        if (y <= 0) y = 0;
        
        if (jumping) { //fix time for jumping and falling
        	if (this.isWithSpring()) { //jumping with spring on
        		if(y<=200) {  //jump until it reach 200 in terms of the size on the screen
            		setyVel(0); //stay in that position for 5 seconds
            	}else {
            		setyVel(-2);
            	}
        		if ((currentNanotime/1000000000.0) - spring.getStart() >= 5){
        			falling = false; //false down after 5 seconds
        		}
        	}else { //usual jump
        		gravity -= 0.05;
            	if(y<=200) { //jump with velocity of gravity
            		setyVel(0);
            	}else {
            		setyVel((int)-gravity);
            	}
            	if ((currentNanotime/1000000000.0)-jumpingStart>=0.8) {//jumping will last for 0.8 seconds
            		jumping = false; //froggy will proceed to false  until collision  is met 
            		falling = true;
            	}
        	}
        }
        if (falling) {
        	if (this.isWithSpring()) { //froggy is falling and collided with spring
        		if ((currentNanotime/1000000000.0) - spring.getStart() >= 5){ //will proceed to jumping
        			falling = false; //falling will start after 5 seconds/ spring will stop 
        		}
        	} else {
    			gravity += 0.06; //froggy will fall with the gravity 
            	setyVel((int)gravity);
    		}
        }
        
        if (getYPos() > WINDOW_HEIGHT) { //froggy will die if position is outside the scene
    		this.visible = false;
    		this.die();
    		falling = false;
    	}
	}
	
    void die(){
    	this.alive = false;
    }

	public boolean isWithShield() {
		return withShield;
	}

	public void setWithShield(boolean withShield) {
		this.withShield = withShield;
	}

	public boolean isWithSpring() {
		return withSpring;
	}

	public void setWithSpring(boolean withSpring) {
		this.withSpring = withSpring;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public double getJumpingStart() {
		return jumpingStart;
	}

	public void setJumpingStart(double jumpingStart) {
		this.jumpingStart = jumpingStart;
	}

	public double getGravity() {
		return gravity;
	}

	public void setGravity(double gravity) {
		this.gravity = gravity;
	}

	public static Image getShieldedFrog() {
		return SHIELDED_FROG;
	}

	public static double getInitialX() {
		return INITIAL_X;
	}

	public static double getInitialY() {
		return INITIAL_Y;
	}

	public void setScore(int score) {
		this.score += score;
	}

	int getScore(){
		return this.score;
	}
	
	boolean isAlive(){
    	return this.alive;
    } 
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public static Image getFrog() {
		return FROG;
	}
	
	public void incrementScore() {
		this.score++;
		System.out.println("Score: "+ score);
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	public void setStopped(boolean stopped) {
		this.stopped=stopped;
	}
}
