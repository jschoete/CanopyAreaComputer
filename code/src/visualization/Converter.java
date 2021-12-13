package visualization;

import computation.shapes.Circle;
import computation.shapes.Rectangle;
import computation.shapes.Shape;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Converter {
	
	private static double paddingLeftRight = 50;
	private static double paddingUpDown = 20;
	
	private double preShiftX;
	private double preShiftY;
	private double scale;
	private double postShiftX;
	private double postShiftY;
	private Shape outerShape;
	private List<Shape> innerShapes;
	
	public Converter(Shape outerShape, List<Shape> innerShapes, Topology tp){
		this(outerShape, innerShapes, tp, false);
	}
	
	public Converter(Shape outerShape, List<Shape> innerShapes, Topology tp, boolean zoom){
		Rectangle bbox;
		if (zoom){
			bbox = outerShape.getBoundingBox();
		} else {
			bbox = computeBoundingBox(innerShapes);
		}
		Point2D.Double center = bbox.getCenter();
		preShiftX = -center.x;
		preShiftY = -center.y;
		scale = (tp.getHeight() - 2f * paddingUpDown) / bbox.getHeight();
		postShiftX = tp.getWidth() / 2f;
		postShiftY = tp.getHeight() / 2f;
		this.outerShape = shiftAndScale(outerShape);
		this.innerShapes = new ArrayList<>();
		for (Shape shape : innerShapes){
			this.innerShapes.add(shiftAndScale(shape));
		}
	}
	
	private Rectangle computeBoundingBox(List<Shape> innerShapes) {
		double leftMost = innerShapes.get(0).getCenter().x;
		double left;
		for (Shape s : innerShapes){
			left = s.getBoundingBox().getLeftTopPoint().x;
			if (left < leftMost)
				leftMost = left;
		}
		double topMost = innerShapes.get(0).getCenter().y;
		double top;
		for (Shape s : innerShapes){
			top = s.getBoundingBox().getLeftTopPoint().y;
			if (top < topMost)
				topMost = top;
		}
		double rightMost = innerShapes.get(0).getCenter().x;
		double right;
		for (Shape s : innerShapes){
			right = s.getBoundingBox().getRightBottomPoint().x;
			if (right > rightMost)
				rightMost = right;
		}
		double bottomMost = innerShapes.get(0).getCenter().y;
		double bottom;
		for (Shape s : innerShapes){
			bottom = s.getBoundingBox().getRightBottomPoint().y;
			if (bottom > bottomMost)
				bottomMost = bottom;
		}
		return new Rectangle(leftMost, rightMost, topMost, bottomMost);
	}
	
	private Shape shiftAndScale(Shape shape) {
		if (shape instanceof Circle){
			Point2D.Double center = shape.getCenter();
			center = new Point2D.Double((center.x + preShiftX) * scale + postShiftX, (center.y + preShiftY) * scale + postShiftY);
			double radius = ((Circle) shape).getRadius() * scale;
			return new Circle(center.x, center.y, radius);
		}
		return null;
	}
	
	public Shape getOuterShape() {
		return outerShape;
	}
	
	public List<Shape> getInnerShapes() {
		return innerShapes;
	}
	
	public Point convert(Point2D.Double p) {
		return new Point((p.x + preShiftX) * scale + postShiftX, (p.y + preShiftY) * scale + postShiftY);
	}
}
