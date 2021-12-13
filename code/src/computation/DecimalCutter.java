package computation;

public class DecimalCutter {
	
	public static double cut(double d, int nbDecimals){
		double powerTen = Math.pow(10, nbDecimals);
		return ((double)((int)(d *powerTen)))/powerTen;
	}
}
