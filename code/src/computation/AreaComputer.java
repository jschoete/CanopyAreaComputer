package computation;

import computation.shapes.Circle;
import computation.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

public abstract class AreaComputer {
	
	protected Shape outerShape;
	protected List<Shape> innerShapes;
	protected double totalArea;
	protected double innerArea;
	protected double outerArea;
	
	protected AreaComputer(Shape outerShape, List<Shape> innerShapes){
		this.outerShape = outerShape;
		this.innerShapes = innerShapes;
		this.totalArea = outerShape.getArea();
		this.innerArea = 0;
		this.outerArea = totalArea - innerArea;
	}
	
	public double getTotalArea(){
		return totalArea;
	}
	
	public double getInnerArea(){
		return innerArea;
	}
	
	public double getOuterArea(){
		return outerArea;
	}
	
	public double[] getAreas(){
		return new double[]{totalArea, outerArea, innerArea};
	}
	
	protected void setInnerArea(double area){
		innerArea = area;
		outerArea = totalArea - innerArea;
	}
	
	protected void addInnerArea(double area) {
		setInnerArea(innerArea + area);
	}
	
	protected void subtractInnerArea(double area) {
		setInnerArea(innerArea - area);
	}
	
	protected void setOuterArea(double area){
		outerArea = area;
		innerArea = totalArea - outerArea;
	}
	
	protected void addOuterArea(double area){
		setOuterArea(outerArea + area);
	}
	
	protected void subtractOuterArea(double area){
		setOuterArea(outerArea - area);
	}
	
	protected List<Shape> shapesIntersectingOuterShape(){
		List<Shape> result = new ArrayList<>();
		for (Shape shape : innerShapes){
			if (shape.intersects(outerShape)){
				result.add(shape);
			}
		}
		return result;
	}
	
	public Shape getOuterShape(){
		return outerShape;
	}
	
	public List<Shape> getInnerShapes(){
		return innerShapes;
	}
	
	public abstract void compute();
	public abstract double getErrorMargin();
	public abstract long getSampleSize();
	
}
