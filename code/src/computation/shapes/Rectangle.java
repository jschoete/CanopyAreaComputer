package computation.shapes;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class Rectangle implements Shape {
	
	private double x1, x2, y1, y2;
	
	public Rectangle(double x1, double x2, double y1, double y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return this;
	}
	
	@Override
	public Point2D.Double getRandomPoint() {
		Random r = new Random();
		return new Point2D.Double(x1 + r.nextDouble() * (x2 - x1), y1 + r.nextDouble() * (y2 - y1));
	}
	
	@Override
	public boolean contains(Point2D.Double p) {
		return p.x <= this.x2 && p.x >= this.x1
				&& p.y <= this.y2 && p.y >= this.y1;
	}
	
	@Override
	public boolean contains(Shape s) {
		return false;
	}
	
	@Override
	public double getArea() {
		return (x2 - x1) * (y2 - y1);
	}
	
	@Override
	public Point2D.Double getCenter() {
		return new Point2D.Double(x1 + (x2 - x1) / 2, y1 + (y2 - y1) / 2);
	}
	
	@Override
	public boolean intersects(Shape s) {
		return false;
	}
	
	@Override
	public String toString() {
		return "Rectangle with top left corner " + new Point2D.Double(x1, y1).toString() + " and right bottom corner " + new Point2D.Double(x2, y2);
	}
	
	public double getHeight(){
		return y2 - y1;
	}
	
	public double getWidth(){
		return x2 - x1;
	}
	
	public Point2D.Double getLeftTopPoint(){
		return new Point2D.Double(x1, y1);
	}
	
	public Point2D.Double getRightBottomPoint(){
		return new Point2D.Double(x2, y2);
	}
}
