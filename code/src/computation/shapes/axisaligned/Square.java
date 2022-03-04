package computation.shapes.axisaligned;

import java.awt.geom.Point2D;

public class Square extends Rectangle{
	
	public Square(double centerX, double centerY, double sideLength) {
		super(centerX - sideLength / 2, centerX + sideLength / 2,
				centerY - sideLength / 2, centerY + sideLength / 2);
	}
	
	public Square(Rectangle rectangle){
		super(rectangle);
	}
	
	public Square(Point2D topLeftPoint, Point2D bottomRightPoint){
		super(topLeftPoint.getX(), bottomRightPoint.getX(), topLeftPoint.getY(), bottomRightPoint.getY());
	}
	
	public Square(double x1, double x2, double y1, double y2){
		super(x1, x2, y1, y2);
	}
	
	public double getSideLength(){
		return Math.abs(x2 - x1);
	}
}
