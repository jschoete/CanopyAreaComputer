package computation.shapes;

import computation.shapes.axisaligned.Rectangle;
import computation.shapes.axisaligned.Square;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

public class Circle implements Shape {
	
	private double x;
	private double y;
	private double radius;
	
	public Circle(double x, double y, double radius){
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public double getRadius() {
		return radius;
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
		if (s instanceof Rectangle){ // https://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
			Rectangle rectangle = (Rectangle) s;
			double xNormalized = Math.abs(this.x - rectangle.getCenter().getX());
			double yNormalized = Math.abs(this.y - rectangle.getCenter().getY());
			if (xNormalized >= (rectangle.getWidth() / 2.0 + radius) || yNormalized >= (rectangle.getHeight() / 2.0 + radius)){
				return false;
			}
			if (xNormalized < rectangle.getWidth() / 2.0 || yNormalized < rectangle.getHeight() / 2.0){
				return true;
			}
			double cornerDistanceSquared = Math.pow(xNormalized - rectangle.getWidth() / 2.0, 2)
								+ Math.pow(yNormalized - rectangle.getHeight() / 2.0, 2); // faster than square root
			return cornerDistanceSquared < Math.pow(radius, 2);
//			// doesn't work for a low, horizontally elongated square between the top and center of some large circle
//			Square sq = (Square) s;
//			List<Point2D> vertices = sq.getVertices();
//			Point2D v0 = vertices.get(0);
//			if (this.contains(v0)){
//				if (!this.contains(vertices.get(1)) || !this.contains(vertices.get(2)) || !this.contains(vertices.get(3))){
//					return true;
//				}
//			} else {
//				if (this.contains(vertices.get(1)) || this.contains(vertices.get(2)) || this.contains(vertices.get(3))){
//					return true;
//				}
//			}
//			Point2D up = new Point2D.Double(this.getCenter().getX(), this.getCenter().getY() - radius);
//			Point2D down = new Point2D.Double(this.getCenter().getX(), this.getCenter().getY() + radius);
//			Point2D left = new Point2D.Double(this.getCenter().getX() - radius, this.getCenter().getY());
//			Point2D right = new Point2D.Double(this.getCenter().getX() + radius, this.getCenter().getY());
//			if (sq.contains(up)){
//				if (!sq.contains(down) || !sq.contains(left) || !sq.contains(right)){
//					return true;
//				}
//			} else {
//				if (sq.contains(down) || sq.contains(left) || sq.contains(right)){
//					return true;
//				}
//			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Circle (c=" + getCenter().toString() + ", r=" + getRadius() + ")";
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Square(x, y, 2 * radius);
	}
	
	@Override
	public Point2D.Double getRandomPoint() {
		Random rand = new Random();
		double angle = 2f * Math.PI * rand.nextDouble();
		double radius = Math.sqrt(rand.nextDouble()) * this.radius;
		return new Point2D.Double(x + radius * Math.cos(angle), y + radius * Math.sin(angle));
	}
	
	@Override
	public boolean contains(Point2D p) {
		return p.distance(this.x, this.y) <= radius;
	}
	
	@Override
	public boolean contains(Shape s) {
		if (s instanceof Circle){
			Circle c = (Circle)s;
			return this.getCenter().distance(c.getCenter()) + c.radius <= this.radius;
		}
		if (s instanceof Square){
			Square sq = (Square)s;
			for (Point2D p : sq.getVertices()){
				if (!this.contains(p)){
					return false;
				}
			}
			return true;
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
		return Math.PI * radius * radius;
	}
}
