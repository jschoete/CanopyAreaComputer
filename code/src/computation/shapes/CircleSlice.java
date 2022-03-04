package computation.shapes;

import computation.shapes.axisaligned.Rectangle;

import java.awt.geom.Point2D;

import static computation.Geometry.computeSignedAngle;
import static computation.Geometry.computeCounterClockwiseAngle;

public class CircleSlice implements Shape {
	
	private Point2D.Double center;
	private Point2D.Double rightPoint;
	private Point2D.Double leftPoint;
	private double angle;
	private double radius;
	
	public CircleSlice(Point2D.Double center, Point2D.Double rightPoint, Point2D.Double leftPoint){
		this.center = center;
		this.rightPoint = rightPoint;
		this.leftPoint = leftPoint;
		this.angle = computeCounterClockwiseAngle(rightPoint, center, leftPoint);
		this.radius = center.distance(rightPoint);
	}
	
	@Override
	public double getArea() {
		return Math.abs(angle / 2.0 * radius * radius);
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return null;
	}
	
	@Override
	public Point2D.Double getRandomPoint() {
		return null;
	}
	
	@Override
	public boolean contains(Point2D p) {
		return false; //TODO
	}
	
	@Override
	public boolean contains(Shape s) {
		return false;
	}
	
	@Override
	public Point2D.Double getCenter() {
		return center;
	}
	
	@Override
	public boolean intersects(Shape s) {
		return false;
	}
	
	@Override
	public String toString() {
		return null;
	}
}
