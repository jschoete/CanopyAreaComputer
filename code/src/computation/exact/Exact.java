package computation.exact;

import computation.AreaComputer;
import computation.Geometry;
import computation.shapes.Circle;
import computation.shapes.CircleSlice;
import computation.shapes.Polygon;
import computation.shapes.Shape;
import instances.Instances;

import java.awt.geom.Point2D;
import java.util.*;

import static computation.exact.DoubleEpsilon.epsilon;
import static computation.Geometry.computeSignedAngle;

public class Exact extends AreaComputer {
	
	public Exact(Shape outerShape, List<Shape> innerShapes, boolean run) {
		super(outerShape, innerShapes);
		if (run) {
			compute();
		}
	}
	
	public Exact(Shape outerShape, List<Shape> innerShapes) {
		this(outerShape, innerShapes, false);
	}
	
	@Override
	public void compute() {
		List<Shape> shapes = shapesIntersectingOuterShape();
		double areaUnionInner = computeArea(shapes);
		List<Shape> allShapes = new ArrayList<>(shapes);
		allShapes.add(outerShape);
		double areaUnionInnerOuter = computeArea(allShapes, 1);
		setOuterArea(areaUnionInnerOuter - areaUnionInner); // updates inner area automatically
	}
	
	private double computeArea(List<Shape> shapes) {
		return computeArea(shapes, -1);
	}
	
	private double computeArea(List shapes, int nbComponents) {
		double result = 0;
		List<List<Shape>> components;
		if (nbComponents == -1) {
			components = computeComponents(shapes);
		} else { // nbComponents == 1
			components = Collections.singletonList(shapes);
		}
		for (List<Shape> component : components) {
			removeRedundantShapes(component);
			result += computeAreaComponent(component);
		}
		return result;
	}
	
	public static void removeRedundantShapes(List<Shape> component) {
		Shape s1, s2;
		Set<Integer> toRemove = new HashSet<Integer>();
		for (int i = 0; i < component.size(); i++) {
			s1 = component.get(i);
			for (int j = 0; j < component.size(); j++) {
				if (toRemove.contains(j)) {
					continue;
				}
				if (i != j) {
					s2 = component.get(j);
					if (s2.contains(s1)) {
						toRemove.add(i);
						break;
					}
				}
			}
		}
		List<Integer> aux = new ArrayList(toRemove);
		Collections.sort(aux);
		Collections.reverse(aux);
		for (int i : aux) {
			component.remove(i);
		}
	}
	
	public static List<List<Shape>> computeComponents(List<Shape> shapes) {
		List<List<Shape>> result = new ArrayList<>();
		Set<Shape> covered = new HashSet<>(shapes.size());
		Queue<Shape> queue = new LinkedList<>();
		Shape currentShape;
		for (Shape shape : shapes) {
			if (covered.contains(shape)) {
				continue;
			}
			List<Shape> component = new ArrayList<>();
			queue.add(shape);
			covered.add(shape);
			component.add(shape);
			while (!queue.isEmpty()) {
				currentShape = queue.poll();
				for (Shape otherShape : shapes) {
					if (!covered.contains(otherShape) && currentShape.intersects(otherShape)) {
						queue.add(otherShape);
						covered.add(otherShape);
						component.add(otherShape);
					}
				}
			}
			result.add(component);
		}
		return result;
	}
	
	private double computeAreaComponent(List<Shape> component) { // https://stackoverflow.com/questions/69899785/how-to-compute-the-set-of-polygons-from-a-set-of-overlapping-circles
		if (component.size() == 1) {
			return component.get(0).getArea();
		}
		Map<Point2D.Double, List<Vector>> vectorMap = computeVectorMap(component);
		List<CircleSlice> circleSlices = computeCircleSlices(vectorMap, component);
		double result = 0;
		for (CircleSlice circleSlice : circleSlices) {
			result += circleSlice.getArea();
		}
		List<Polygon> polygons = computePolygons(vectorMap, component);
		for (Polygon polygon : polygons) {
			result += polygon.getArea();
		}
		return result;
	}
	
	private List<CircleSlice> computeCircleSlices(Map<Point2D.Double, List<Vector>> vectorMap, List<Shape> component) {
		List<CircleSlice> result = new ArrayList<>();
		Point2D.Double circleCenter;
		List<Vector> vectors;
		Vector vector, vectorAfter;
		for (Shape shape : component) {
			circleCenter = shape.getCenter();
			vectors = vectorMap.get(circleCenter);
			if (vectors != null) {
				int n = vectors.size();
				for (int i = 0; i < vectors.size(); i++) {
					vector = vectors.get(i);
					if (vector.rightSideInside()) {
						vectorAfter = vectors.get((i + 1) % n);
						CircleSlice circleSlice = new CircleSlice(circleCenter, vectorAfter.getTarget(), vector.getTarget());
						result.add(circleSlice);
					}
				}
			}
		}
		return result;
	}
	
	private List<Polygon> computePolygons(Map<Point2D.Double, List<Vector>> vectorMap, List<Shape> component) {
		List<Polygon> result = new ArrayList<>();
		while (!vectorMap.isEmpty()) {
			Point2D.Double start = vectorMap.entrySet().iterator().next().getKey();
			Polygon polygon = new Polygon(start);
			List<Vector> vectors = vectorMap.get(start);
			Point2D.Double next = null;
			Point2D.Double previous = start;
			int i;
			for (i = 0; i < vectors.size(); i++) {
				if (vectors.get(i).rightSideInside()) {
					next = vectors.get(i).getTarget();
					break;
				}
			}
			while (!next.equals(start)) {
				polygon.addNode(next);
				vectors = vectorMap.get(next);
				i = vectors.indexOf(new Vector(next, previous, false));
				Collections.rotate(vectors, -(i + 1));
				previous = next;
				for (i = 0; i < vectors.size(); i++) {
					if (vectors.get(i).rightSideInside()) {
						next = vectors.get(i).getTarget();
						break;
					}
				}
			}
			result.add(polygon);
			removePolygon(vectorMap, polygon);
		}
		return result;
	}
	
	private void removePolygon(Map<Point2D.Double, List<Vector>> vectorMap, Polygon polygon) {
		List<Point2D.Double> nodes = polygon.getNodes();
		int n = nodes.size();
		Point2D.Double current, next;
		for (int i = 0; i < n; i++) {
			current = nodes.get(i);
			next = nodes.get((i + 1) % n);
			Vector there = new Vector(current, next, true);
			Vector andBackAgain = new Vector(next, current, false); // The Hobbit
			vectorMap.get(current).remove(there);
			if (vectorMap.get(current).isEmpty()) {
				vectorMap.remove(current);
			}
			vectorMap.get(next).remove(andBackAgain);
			if (vectorMap.get(next).isEmpty()) {
				vectorMap.remove(next);
			}
		}
	}
	
	public static Map<Point2D.Double, List<Vector>> computeVectorMap(List<Shape> component) {
		int n = component.size();
		Shape s1, s2;
		Map<Point2D.Double, List<Vector>> vectorMap = new HashMap<>();
		Map<Point2D.Double, List<Vector>> vectors;
		Point2D.Double p;
		List<Vector> l;
		List<Vector> fused;
		for (int i = 0; i < n; i++) {
			s1 = component.get(i);
			for (int j = i + 1; j < n; j++) {
				s2 = component.get(j);
				vectors = computeVectors(s1, s2, component);
				for (Map.Entry<Point2D.Double, List<Vector>> entry : vectors.entrySet()) {
					p = entry.getKey();
					l = entry.getValue();
					if (vectorMap.containsKey(p)) {
						fused = fuse(vectorMap.get(p), l);
						vectorMap.put((Point2D.Double) p.clone(), new ArrayList<>(fused));
					} else {
						vectorMap.put((Point2D.Double) p.clone(), new ArrayList<>(l));
					}
				}
			}
		}
		return vectorMap;
	}
	
	private static Map<Point2D.Double, List<Vector>> computeVectors(Shape s1, Shape s2, List<Shape> component) {
		Map<Point2D.Double, List<Vector>> result = new HashMap<>();
		if (s1 instanceof Circle && s2 instanceof Circle) {
			Circle c1 = (Circle) s1;
			Circle c2 = (Circle) s2;
			List<Point2D.Double> intersections = computeIntersections(s1, s2);
			if (intersections.isEmpty()) {
				return result;
			}
			for (Shape shape : component) {
				Circle c = (Circle) shape;
				if (intersections.get(1).distance(c.getCenter()) < c.getRadius() - epsilon) {
					intersections.remove(1);
					break;
				}
			}
			for (Shape shape : component) {
				Circle c = (Circle) shape;
				if (intersections.get(0).distance(c.getCenter()) < c.getRadius() - epsilon) {
					intersections.remove(0);
					break;
				}
			}
			if (intersections.isEmpty()) {
				return result;
			}
			List<Vector> center1Vectors = new ArrayList<>();
			List<Vector> center2Vectors = new ArrayList<>();
			List<Vector> intersection1Vectors = new ArrayList<>();
			Point2D.Double center1 = c1.getCenter();
			Point2D.Double center2 = c2.getCenter();
			Point2D.Double intersection1 = intersections.get(0);
			double angle = Geometry.computeSignedAngle(center2, center1, intersection1);
			if (angle < Math.PI && angle > 0) {
				center1Vectors.add(new Vector(center1, intersection1, true));
				intersection1Vectors.add(new Vector(intersection1, center1, false));
				center2Vectors.add(new Vector(center2, intersection1, false));
				intersection1Vectors.add(new Vector(intersection1, center2, true));
			} else {
				center1Vectors.add(new Vector(center1, intersection1, false));
				intersection1Vectors.add(new Vector(intersection1, center1, true));
				center2Vectors.add(new Vector(center2, intersection1, true));
				intersection1Vectors.add(new Vector(intersection1, center2, false));
			}
			if (intersections.size() == 2) {
				Point2D.Double intersection2 = intersections.get(1);
				List<Vector> intersection2Vectors = new ArrayList<>();
				if (angle < Math.PI && angle > 0) {
					center1Vectors.add(new Vector(center1, intersection2, false));
					intersection2Vectors.add(new Vector(intersection2, center1, true));
					center2Vectors.add(new Vector(center2, intersection2, true));
					intersection2Vectors.add(new Vector(intersection2, center2, false));
				} else {
					center1Vectors.add(new Vector(center1, intersection2, true));
					intersection2Vectors.add(new Vector(intersection2, center1, false));
					center2Vectors.add(new Vector(center2, intersection2, false));
					intersection2Vectors.add(new Vector(intersection2, center2, true));
				}
				order(intersection2Vectors);
				result.put(intersection2, intersection2Vectors);
			}
			order(intersection1Vectors);
			order(center1Vectors);
			order(center2Vectors);
			result.put(intersection1, intersection1Vectors);
			result.put(center1, center1Vectors);
			result.put(center2, center2Vectors);
		}
		return result;
	}
	
	private static void order(List<Vector> vectors) {
		List<Vector> aux = new ArrayList<>();
		double minAngle;
		Vector minVector;
		while (!vectors.isEmpty()) {
			minVector = vectors.get(0);
			minAngle = computeSignedAngle(minVector);
			for (int i = 1; i < vectors.size(); i++) {
				Vector vector = vectors.get(i);
				double angle = computeSignedAngle(vector);
				if (angle < minAngle) {
					minVector = vector;
					minAngle = angle;
				}
			}
			aux.add(minVector);
			vectors.remove(minVector);
		}
		vectors.addAll(aux);
	}
	
	private static List<Point2D.Double> computeIntersections(Shape s1, Shape s2) { // https://stackoverflow.com/questions/3349125/circle-circle-intersection-points
		List<Point2D.Double> result = new ArrayList<>();
		if (s1 instanceof Circle && s2 instanceof Circle) {
			Circle circle1 = (Circle) s1;
			Circle circle2 = (Circle) s2;
			if (circle1.getCenter().distance(circle2.getCenter()) >= circle1.getRadius() + circle2.getRadius()) {
				return result;
			}
			Point2D.Double center1 = circle1.getCenter();
			Point2D.Double center2 = circle2.getCenter();
			double radius1 = circle1.getRadius();
			double radius2 = circle2.getRadius();
			double d = center1.distance(center2);
			double a = (Math.pow(radius1, 2) - Math.pow(radius2, 2) + Math.pow(d, 2)) / (2 * d);
			double h = Math.sqrt(Math.pow(radius1, 2) - Math.pow(a, 2));
			Point2D.Double middle = new Point2D.Double(center1.x + a * (center2.x - center1.x) / d,
					center1.y + a * (center2.y - center1.y) / d);
			double x1 = middle.x + h * (center2.y - center1.y) / d;
			double x2 = middle.x - h * (center2.y - center1.y) / d;
			double y1 = middle.y - h * (center2.x - center1.x) / d;
			double y2 = middle.y + h * (center2.x - center1.x) / d;
			result.add(new Point2D.Double(x1, y1));
			result.add(new Point2D.Double(x2, y2));
		}
		return result;
	}
	
	private static List<Vector> fuse(List<Vector> l1, List<Vector> l2) {
		List<Vector> result = new ArrayList<>();
		int i = 0;
		int j = 0;
		Vector v1, v2;
		while (i < l1.size() && j < l2.size()) {
			v1 = l1.get(i);
			v2 = l2.get(j);
			if (computeSignedAngle(v1) < computeSignedAngle(v2)) {
				result.add(v1);
				i++;
			} else {
				result.add(v2);
				j++;
			}
		}
		while (i < l1.size()) {
			result.add(l1.get(i));
			i++;
		}
		while (j < l2.size()) {
			result.add(l2.get(j));
			j++;
		}
		return result;
	}
	
	public static void main(String[] args) {
		AreaComputer ac = new Exact(Instances.beehive(), Instances.randomForestSquare());
//		AreaComputer ac = new Exact(Instances.outer0(), Instances.randomInners());
		ac.compute();
		System.out.println(ac.getInnerArea());
		System.out.println(ac.getOuterArea());
		System.out.println("done");
//		System.out.println(computeIntersections(new Circle(0, 0, 1), new Circle(1.5, 0.7, 1.5)));
	}
}
