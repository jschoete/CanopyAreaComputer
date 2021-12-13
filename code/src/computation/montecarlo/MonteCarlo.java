package computation.montecarlo;

import computation.AreaComputer;
import computation.shapes.Shape;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;

public class MonteCarlo extends AreaComputer {
	
	private static int sampleSize = 25 * (int)Math.pow(10, 6);
	
	private boolean run;
	private long innerPoints;
	private long outerPoints;
	
	public MonteCarlo(Shape outerShape, List<Shape> innerShapes) {
		this(outerShape, innerShapes, true);
	}
	
	public MonteCarlo(Shape outerShape, Shape innerShape) {
		this(outerShape, Collections.singletonList(innerShape));
	}
	
	public MonteCarlo(Shape outerShape, Shape innerShape, boolean run) {
		this(outerShape, Collections.singletonList(innerShape), run);
	}
	
	public MonteCarlo(Shape outerShape, List<Shape> innerShapes, boolean run) {
		super(outerShape, innerShapes);
		this.outerPoints = 0;
		this.innerPoints = 0;
		this.run = run;
		if (this.run){
			compute();
		}
	}
	
	public void compute(){
		for (int i = 0; i< sampleSize; i++){
			this.addPoint();
			updateAreas();
		}
	}
	
	public void addPoint(){
		addPoint(outerShape.getRandomPoint());
	}
	
	public void addPoint(Point2D.Double p){
		boolean innerPoint = false;
		for (Shape innerShape : innerShapes){
			if (innerShape.contains(p)){
				innerPoint = true;
				break;
			}
		}
		if (innerPoint){
			innerPoints++;
		} else {
			outerPoints++;
		}
	}
	
	public long getInnerPoints(){
		return innerPoints;
	}
	
	public long getOuterPoints(){
		return outerPoints;
	}
	
	public void updateAreas(){
		setInnerArea((double)innerPoints / (double)(outerPoints + innerPoints) * totalArea);
	}
	
}
