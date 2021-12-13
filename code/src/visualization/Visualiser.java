package visualization;

import computation.montecarlo.MonteCarlo;
import computation.shapes.Shape;
import instances.Instances;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JTopology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.ui.painting.BackgroundPainter;
import io.jbotsim.ui.painting.UIComponent;
import visualization.backgroundpainters.PointPainter;
import visualization.backgroundpainters.ResultsPainter;
import visualization.backgroundpainters.SampleSizePainter;
import visualization.backgroundpainters.ShapePainter;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static io.jbotsim.core.Topology.RefreshMode.CLOCKBASED;

public class Visualiser implements ClockListener, BackgroundPainter {
	
	private Converter c;
	private Shape outerShape;
	private List<Shape> innerShapes;
	private MonteCarlo monteCarlo;
	private Topology tp;
	private JViewer jv;
	
	private ShapePainter shapePainter;
	private PointPainter pointPainter;
	private ResultsPainter resultsPainter;
	private SampleSizePainter sampleSizePainter;
	
	public Visualiser(Shape outerShape, Shape innerShape){
		this(outerShape, Collections.singletonList(innerShape));
	}
	
	public Visualiser(Shape outerShape, List<Shape> innerShapes){
		this(outerShape, innerShapes, false);
	}
	
	public Visualiser(Shape outerShape, List<Shape> innerShapes, boolean zoom){
//		tp = new Topology(1000, 800);
		tp = VideoHelper.generateTopology();
		c = new Converter(outerShape, innerShapes, tp, zoom);
		this.outerShape = outerShape;
		this.innerShapes = innerShapes;
		monteCarlo = new MonteCarlo(this.outerShape, this.innerShapes, false);
		tp.setRefreshMode(CLOCKBASED); //doesn't work, using forced update in onClock()
		jv = new JViewer(tp);
		JTopology jtp = jv.getJTopology();
		pointPainter = new PointPainter();
		jtp.addBackgroundPainter(pointPainter);
		resultsPainter = new ResultsPainter();
		jtp.addBackgroundPainter(resultsPainter);
		sampleSizePainter = new SampleSizePainter();
		jtp.addBackgroundPainter(sampleSizePainter);
		shapePainter = new ShapePainter(c.getOuterShape(), c.getInnerShapes());
		jtp.addBackgroundPainter(shapePainter);
		tp.addClockListener(this);
		tp.setTimeUnit(tp.getTimeUnit() / 100); //gotta go fast
		jtp.addBackgroundPainter(this);
	}
	
	@Override
	public void onClock() {
		refreshBugWorkaround(); //TODO remove
		Point2D.Double p1 = outerShape.getRandomPoint();
		Point p2 = c.convert(p1);
		long counter = monteCarlo.getInnerPoints();
		monteCarlo.addPoint(p1);
		monteCarlo.updateAreas();
		if (monteCarlo.getInnerPoints() > counter){
			pointPainter.paintPoint(p2, Colors.canopyPointColor);
		} else {
			pointPainter.paintPoint(p2, Colors.nonCanopyPointColor);
		}
		resultsPainter.paintResults(monteCarlo.getAreas());
		sampleSizePainter.increment();
	}
	
	private void refreshBugWorkaround() { //TODO remove
		tp.addNode(-10, -10);
		tp.removeNode(tp.getNodes().get(0));
	}
	
	@Override
	public void paintBackground(UIComponent uiComponent, Topology tp) {
		Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
		setStroke(g2d, tp);
		setRenderingHints(g2d, tp);
		setColor(g2d, tp);
		
		for (Node n : tp.getNodes()) {
			drawSensingRange(g2d, n);
		}
	}
	
	protected void drawSensingRange(Graphics2D g2d, Node n) {
		double sR = n.getSensingRange();
		if (sR > 0) {
			g2d.setColor(Color.gray);
			g2d.drawOval((int) n.getX() - (int) sR, (int) n.getY() - (int) sR, 2 * (int) sR, 2 * (int) sR);
		}
	}
	
	
	/* <p>Sets the proper {@link java.awt.Color} on the provided {@link Graphics2D} object, with respect to the provided
	 * {@link Topology}.</p>
	 * <p>You can override this method if you need to change the {@link JBackgroundPainter}'s default color management.</p>
	 * @param g2d a {@link Graphics2D} object.
	 * @param topology the associated {@link Topology}.
	 */
	protected void setColor(Graphics2D g2d, Topology topology) {
		g2d.setColor(Color.gray);
	}
	
	/* <p>Sets the proper {@link Stroke} on the provided {@link Graphics2D} object, with respect to the provided
	 * {@link Topology}.</p>
	 * <p>You can override this method if you need to change the {@link JBackgroundPainter}'s default {@link Stroke}.</p>
	 * @param g2d a {@link Graphics2D} object.
	 * @param topology the associated {@link Topology}.
	 */
	protected void setStroke(Graphics2D g2d, Topology topology) {
		g2d.setStroke(new BasicStroke(1));
	}
	
	/**
	 * <p>Sets the proper {@link RenderingHints} on the provided {@link Graphics2D} object, with respect to the provided
	 * {@link Topology}.</p>
	 * <p>You can override this method if you need to change the rendering behavior.</p>
	 * @param g2d a {@link Graphics2D} object.
	 * @param topology the associated {@link Topology}.
	 */
	protected void setRenderingHints(Graphics2D g2d, Topology topology) {
		// default does nothing
	}
	
	public static void main(String[] args) throws IOException {
//		Parser p = new Parser("input/TreesXY_CanopD.txt");
//		List<Shape> innerShapes = p.getInnerShapes();
//		Shape outerShape = p.getOuterShape(173, 50.0); // largest one found (1314m2)
//		new Visualiser(outerShape, innerShapes, true);
//		new Visualiser(Instances.beehive(), Instances.randomForestCircle(), true);
//		new Visualiser(Instances.beehive(), Instances.randomForestSquare(), true);
		new Visualiser(Instances.outer0(), Instances.randomInners(), true);
	}
}
