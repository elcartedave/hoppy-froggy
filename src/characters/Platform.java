package characters;




import javafx.scene.image.Image;

public class Platform extends Sprite {
	private final static double WIDTH=95;
	private final static double HEIGHT=50;
	private boolean downPlatform;
	private boolean didCollide;
	private boolean spiked = false;
	private final static Image PLATFORM_IMG = new Image(GameStage.class.getResourceAsStream("/images/platform1.png"));
	private final static Image SPIKY_IMG = new Image(GameStage.class.getResourceAsStream("/images/platforSpikem.png"));

	public Platform(double x, double y, double width, double height) { //default constructor for the platform
		super(x,y,PLATFORM_IMG,width,height);
		downPlatform=false;
		didCollide=false;
	}
	
	public Platform(double x, double y) { //constructor that will be invoked when generating a platform
        this(x, y, WIDTH, HEIGHT);
    }
	
	
	public Platform(double x, double y, Image IMG) { //constructor used for implementing a spiky platform
		super(x,y,IMG, WIDTH, HEIGHT);
		this.spiked = true;
	}
	
	public boolean checkCollision_Frog(Froggy frog) { //returns true if a platform collided with a frog
		if(this.collidesWith(frog)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void move() { //moves the platform by 5 as long as downPlatform is true
		if(this.downPlatform) {
			this.y += 5;
		}
		this.downPlatform=false;
		
		if(this.y>=GameStage.WINDOW_HEIGHT) { //makes the platform not visible if the platform is out of the window
			this.setVisible(false);
		}
	}
	
	public boolean isDownPlatform() {
		return downPlatform;
	}

	public void setDownPlatform(boolean downPlatform) {
		this.downPlatform = downPlatform;
	}
	
	public boolean didCollide() {
		return didCollide;
	}
	
	public void setDidCollide(boolean didCollide) {
		this.didCollide=didCollide;
	}
	
	public boolean isSpiky() {
		return spiked;
	}
	public void setSpiky(boolean spiked) {
		this.spiked=spiked;
	}

	public static Image getSpikyImg() {
		return SPIKY_IMG;
	}
}