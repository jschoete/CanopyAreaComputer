package computation.shapes.axisaligned;

import computation.shapes.Circle;
import computation.shapes.Shape;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class Rectangle implements computation.shapes.Shape {
	
	protected double x1, x2, y1, y2;
	
	public Rectangle(double x1, double x2, double y1, double y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	public Rectangle(Rectangle rectangle){
		this.x1 = rectangle.x1;
		this.x2 = rectangle.x2;
		this.y1 = rectangle.y1;
		this.y2 = rectangle.y2;
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
	public boolean contains(Point2D p) {
		return p.getX() <= this.x2 && p.getX() >= this.x1
				&& p.getY() <= this.y2 && p.getY() >= this.y1;
	}
	
	@Override
	public boolean contains(computation.shapes.Shape s) {
		if (s instanceof Circle){
			Circle c = (Circle) s;
			Point2D center = c.getCenter();
			double radius = c.getRadius();
			Point2D up = new Point2D.Double(center.getX(), center.getY() - radius);
			Point2D down = new Point2D.Double(center.getX(), center.getY() + radius);
			Point2D left = new Point2D.Double(center.getX() - radius, center.getY());
			Point2D right = new Point2D.Double(center.getX() + radius, center.getY());
			if (!this.contains(up) || !this.contains(down) || !this.contains(left) || !this.contains(right)){
				return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public double getArea() {
		return (x2 - x1) * (y2 - y1);
	}
	
	@Override
	public Point2D.Double getCenter() {
		return new Point2D.Double(x1 + (x2 - x1) / 2.0, y1 + (y2 - y1) / 2.0);
	}
	
	@Override
	public boolean intersects(Shape s) {
		if (s instanceof Circle){
			return s.intersects(this);
		}
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
	
	public Point2D.Double getTopLeftPoint(){
		return new Point2D.Double(x1, y1);
	}
	
	public Point2D.Double getBottomRightPoint(){
		return new Point2D.Double(x2, y2);
	}
	
	public Point2D.Double getTopRightPoint(){
		return new Point2D.Double(x2, y1);
	}
	
	public Point2D.Double getBottomLeftPoint(){
		return new Point2D.Double(x1, y2);
	}
	
	public ArrayList<Point2D> getVertices(){
		ArrayList<Point2D> result = new ArrayList<>();
		result.add(getTopLeftPoint());
		result.add(getTopRightPoint());
		result.add(getBottomLeftPoint());
		result.add(getBottomRightPoint());
		return result;
	}
	
//	public static void main(String[] args) {
//		Rectangle r = new Rectangle(10, 20, 10, 30);
//
//	}

}
