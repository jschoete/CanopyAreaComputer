package computation.exact;

import java.awt.geom.Point2D;

import static computation.Geometry.computeSignedAngle;

public class Vector {
	
	private Point2D.Double source;
	private Point2D.Double target;
	private boolean rightSideInside;
	private double angle;
	
	public Vector(Point2D.Double source, Point2D.Double target, boolean rightSideInside){
		this.source = source;
		this.target = target;
		this.rightSideInside = rightSideInside;
		this.angle = computeSignedAngle(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector){
			Vector other = (Vector)obj;
			return other.source.equals(this.source) && other.target.equals(this.target) && other.rightSideInside == this.rightSideInside;
		}
		return false;
	}
	
	public boolean rightSideInside(){
		return rightSideInside;
	}
	
	public Point2D.Double getSource() {
		return source;
	}
	
	public Point2D.Double getTarget() {
		return target;
	}
	
	@Override
	public int hashCode() {
		if (rightSideInside)
			return source.hashCode() + target.hashCode();
		return -(source.hashCode() + target.hashCode());
	}
	
	@Override
	public String toString() {
		return "\nVector (source=" + source.toString() + ", target=" + target.toString() + ", rightSideInside=" + rightSideInside + ")";
	}
}
