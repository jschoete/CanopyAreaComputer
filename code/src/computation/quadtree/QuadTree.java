package computation.quadtree;

import computation.AreaComputer;
import computation.shapes.Shape;
import computation.shapes.axisaligned.Square;
import instances.Instances;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class QuadTree extends AreaComputer {
	
	private static int maxTreeDepth = 15;
	// depth 9 is max 349524 squares (1398100 for depth 10) although in practice around 9000
	// depth 15 in practice is around 900.000 squares total
	
	private Queue<Square> queue, aux;
	private int treeDepth;
	private boolean run;
	private double greyArea; // area between inside and outside area
	private long nbSquares;
	
	public QuadTree(Shape outerShape, List<Shape> innerShapes) {
		this(outerShape, innerShapes, true);
	}
	
	public QuadTree(Shape outerShape, Shape innerShape) {
		this(outerShape, Collections.singletonList(innerShape));
	}
	
	public QuadTree(Shape outerShape, Shape innerShape, boolean run) {
		this(outerShape, Collections.singletonList(innerShape), run);
	}
	
	public QuadTree(Shape outerShape, List<Shape> innerShapes, boolean run) {
		super(outerShape, innerShapes);
		greyArea = this.outerShape.getBoundingBox().getArea();
		nbSquares = 0;
		outerArea = 0;
		innerArea = totalArea;
		queue = new LinkedList<>();
		aux = new LinkedList<>();
		Square bbox = new Square(this.outerShape.getBoundingBox());
		queue.add(bbox);
		treeDepth = 1;
		this.run = run;
		if (this.run){
			compute();
		}
	}
	
	@Override
	public void compute() {
		while (treeDepth <= maxTreeDepth){
			treatNextSquare();
		}
	}
	
	public void treatNextSquare(){
		Square square = queue.remove();
		if (completelyInside(square) || completelyOutsideOuterShape(square)){
			greyArea -= square.getArea();
		} else if (completelyOutside(square) && completelyInsideOuterShape(square)){
			greyArea -= square.getArea();
			addOuterArea(square.getArea());
		} else {
			List<Square> subSquares = quadDivide(square);
			aux.addAll(subSquares);
			nbSquares += 3;
		}
		if (queue.isEmpty()){
			treeDepth++;
			queue.addAll(aux);
			aux.clear();
		}
	}
	
	public boolean completelyInside(Square square) {
		for (Shape innerShape : innerShapes){
			if (innerShape.contains(square)){
				return true;
			}
		}
		//TODO does there exist other cases which allow easy detection?
		return false;
	}
	
	private boolean completelyOutsideOuterShape(Square square) {
		return !outerShape.contains(square) && !outerShape.intersects(square);
	}
	
	public boolean completelyOutside(Square square) {
		for (Shape innerShape : innerShapes){
			if (square.contains(innerShape) || square.intersects(innerShape)){
				return false;
			}
		}
		return true;
	}
	
	public boolean completelyInsideOuterShape(Square subSquare) {
		return outerShape.contains(subSquare);
	}
	
	public static List<Square> quadDivide(Square square) {
		List<Square> result = new ArrayList<>();
		Point2D topLeft = square.getTopLeftPoint();
		Point2D center = square.getCenter();
		Point2D bottomRight = square.getBottomRightPoint();
		result.add(new Square(topLeft, center));
		result.add(new Square(new Point2D.Double(center.getX(), topLeft.getY()),
							new Point2D.Double(bottomRight.getX(), center.getY())));
		result.add(new Square(new Point2D.Double(topLeft.getX(), center.getY()),
							new Point2D.Double(center.getX(), bottomRight.getY())));
		result.add(new Square(center, bottomRight));
		return result;
	}
	
	@Override
	public double getErrorMargin() {
		return innerArea / (innerArea - greyArea) - 1; // rough estimate (outer shape boundary grey area)
	}
	
	@Override
	public long getSampleSize() {
		return nbSquares;
	}
	
	public Square peekQueue(){
		return queue.peek();
	}
	
	public static void main(String[] args) {
		AreaComputer ac = new QuadTree(Instances.outer0(), Instances.randomInners());
//		AreaComputer ac = new QuadTree(Instances.outerDebugging2(), Instances.innersDebugging2());
//		ac.compute();
		System.out.println(ac.getInnerArea());
		System.out.println(ac.getOuterArea());
		System.out.println(ac.getErrorMargin());
		System.out.println("done");
	}
}
