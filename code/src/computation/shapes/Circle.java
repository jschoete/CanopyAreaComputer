package computation.shapes;

import java.awt.geom.Point2D;
import java.util.Random;

public class Circle implements Shape {
	
	private double x;
	private double y;
	private double r;
	
	public Circle(double x, double y, double r){
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	public double getRadius() {
		return r;
	}
	
	public Point2D.Double getCenter(){
		return new Point2D.Double(x, y);
	}
	
	@Override
	public boolean intersects(Shape s) {
		if (s instanceof Circle){
			Circle c = (Circle) s;
			return c.getRadius() + this.getRadius() > c.getCenter().distance(this.getCenter());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Circle (c=" + getCenter().toString() + ", r=" + getRadius() + ")";
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Square(x, y, 2 * r);
	}
	
	@Override
	public Point2D.Double getRandomPoint() {
		Random rand = new Random();
		double angle = 2f * Math.PI * rand.nextDouble();
		double radius = Math.sqrt(rand.nextDouble()) * r;
		return new Point2D.Double(x + radius * Math.cos(angle), y + radius * Math.sin(angle));
	}
	
	@Override
	public boolean contains(Point2D.Double p) {
		return p.distance(this.x, this.y) <= r;
	}
	
	@Override
	public boolean contains(Shape s) {
		if (s instanceof Circle){
			Circle c = (Circle)s;
			return this.getCenter().distance(c.getCenter()) + c.r <= this.r;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Circle){
			Circle c = (Circle) obj;
			return c.getCenter().equals(this.getCenter()) && c.getRadius() == this.getRadius();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int)(this.getCenter().x * this.getCenter().y * this.getRadius());
	}
	
	@Override
	public double getArea() {
		return Math.PI * r * r;
	}
}
