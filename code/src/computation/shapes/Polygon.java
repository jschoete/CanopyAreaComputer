package computation.shapes;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Polygon implements Shape {
	
	private List<Point2D.Double> nodes;
	
	public Polygon(Point2D.Double node){
		this(Collections.singletonList(node));
	}
	
	public Polygon(List<Point2D.Double> nodes){
		this.nodes = new ArrayList<>(nodes);
	}
	
	public List<Point2D.Double> getNodes() {
		return new ArrayList<>(nodes);
	}
	
	public void addNode(Point2D.Double p){
		nodes.add(p);
	}
	
	@Override
	public double getArea() { // https://www.wikihow.com/Calculate-the-Area-of-a-Polygon
		double result = 0;
		Point2D.Double p1;
		Point2D.Double p2;
		int n = nodes.size();
		for (int i=0; i<n; i++){
			p1 = nodes.get(i);
			p2 = nodes.get((i+1)%n);
			result += p1.getX() * p2.getY();
		}
		for (int i=0; i<n; i++){
			p1 = nodes.get(i);
			p2 = nodes.get((i+1)%n);
			result -= p1.getY() * p2.getX();
		}
		return -result / 2.0;
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
	public boolean contains(Point2D.Double p) {
		return false;
	}
	
	@Override
	public boolean contains(Shape s) {
		return false;
	}
	
	@Override
	public Point2D.Double getCenter() {
		return null;
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
