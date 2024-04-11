package characters;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	protected Image img;
	protected double x, y;
	protected double width;
	protected double height;
	protected double yVel;  //for fish movement
	protected double xVel;
	protected boolean visible;
	
	
	public Sprite(double x, double y, Image image, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.loadImage(image);
		this.visible = true;
	}

	protected void loadImage(Image image) {
		try {
	        this.img = image;
	        this.width = this.img.getWidth();
	        this.height = this.img.getHeight();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void render(GraphicsContext gc){
        gc.drawImage(this.img, this.x, this.y, this.width, this.height);
    }

	public Image getImage(){
		return this.img;
	}

	public double getXPos(){
		return this.x;
	}
	
	public void setXPos(double x) {
		this.x = x;
	}

	public double getYPos(){
		return this.y;
	}
	
	public void setYPos(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getyVel() {
		return yVel;
	}

	public void setyVel(double yVel) {
		this.yVel = yVel;
	}

	public double getxVel() {
		return xVel;
	}

	public void setxVel(double xVel) {
		this.xVel = xVel;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	private Rectangle2D getBounds(){
		return new Rectangle2D(this.x, this.y, this.width, this.height);
	}

	protected boolean collidesWith(Sprite rect2)	{
		Rectangle2D rectangle1 = this.getBounds();
		Rectangle2D rectangle2 = rect2.getBounds();

		return rectangle1.intersects(rectangle2);
	}
	
}