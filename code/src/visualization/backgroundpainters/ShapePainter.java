package visualization.backgroundpainters;

import computation.shapes.Circle;
import computation.shapes.Shape;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.painting.BackgroundPainter;
import io.jbotsim.ui.painting.UIComponent;
import visualization.Colors;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class ShapePainter implements BackgroundPainter {
	
	private Shape outerShape;
	private List<Shape> innerShapes;
	private Graphics2D g2d;
	
	public ShapePainter(Shape outerShape, List<Shape> innerShapes){
		this.outerShape = outerShape;
		this.innerShapes = innerShapes;
	}
	
	@Override
	public void paintBackground(UIComponent uiComponent, Topology topology) {
		g2d = (Graphics2D) uiComponent.getComponent();
		drawInnerShapes();
		drawOuterShape();
	}
	
	private void drawOuterShape() {
		g2d.setColor(Colors.nonCanopyColor);
		drawShape(outerShape);
	}
	
	private void drawInnerShapes() {
		g2d.setColor(Colors.canopyColor);
		for (Shape shape : innerShapes){
			drawShape(shape);
		}
	}
	
	private void drawShape(Shape shape) {
		if (shape instanceof Circle){
			Point2D.Double center = shape.getCenter();
			double radius = ((Circle) shape).getRadius();
			g2d.drawOval((int)(center.x - radius), (int)(center.y - radius), (int)(radius * 2f), (int)(radius * 2f));
		}
	}
}
