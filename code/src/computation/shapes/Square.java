package computation.shapes;

public class Square extends Rectangle{
	
	private double centerX, centerY, sideLength;
	
	public Square(double centerX, double centerY, double sideLength) {
		super(centerX - sideLength / 2, centerX + sideLength / 2,
				centerY - sideLength / 2, centerY + sideLength / 2);
		this.centerX = centerX;
		this.centerY = centerY;
		this.sideLength = sideLength;
	}
}
