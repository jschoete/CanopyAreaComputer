package computation;

import computation.exact.Vector;

import java.awt.geom.Point2D;

public class Geometry {
	
	public static double computeSignedAngle(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3){ // https://stackoverflow.com/questions/2150050/finding-signed-angle-between-vectors
		return normalizeBetweenPiAndMinusPi(Math.atan2(p3.y - p2.y, p3.x - p2.x) - Math.atan2(p1.y - p2.y, p1.x - p2.x));
	}
	
	private static double normalizeBetweenPiAndMinusPi(double angle) {
		while (angle > Math.PI){
			angle -= 2.0 * Math.PI;
		}
		while (angle < -Math.PI){
			angle += 2.0 * Math.PI;
		}
		return angle;
	}
	
	public static double computeCounterClockwiseAngle(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3){ // https://stackoverflow.com/questions/14066933/direct-way-of-computing-clockwise-angle-between-2-vectors
		double x1 = p3.x - p2.x;
		double x2 = p1.x - p2.x;
		double y1 = p3.y - p2.y;
		double y2 = p1.y - p2.y;
		return normalizeBetweenZeroAndTwoPi(Math.atan2(x1*y2 - y1*x2, x1*x2 + y1*y2));
	}
	
	private static double normalizeBetweenZeroAndTwoPi(double angle) {
		while (angle < 0){
			angle += 2.0 * Math.PI;
		}
		while (angle > 2.0 * Math.PI){
			angle -= 2.0 * Math.PI;
		}
		return angle;
	}
	
	public static double computeSignedAngle(Vector v){
		return computeSignedAngle(new Point2D.Double(v.getSource().x + 1, v.getSource().y), v.getSource(), v.getTarget());
	}
}
