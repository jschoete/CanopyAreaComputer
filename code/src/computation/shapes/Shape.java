package computation.shapes;

import java.awt.geom.Point2D;

public interface Shape {
	
	Rectangle getBoundingBox();
	Point2D.Double getRandomPoint();
	boolean contains(Point2D.Double p);
	boolean contains(Shape s);
	double getArea();
	Point2D.Double getCenter();
	boolean intersects(Shape s);
	String toString();
}
