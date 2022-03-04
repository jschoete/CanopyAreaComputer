package instances;

import computation.shapes.Circle;
import computation.shapes.Shape;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Instances {
	
	public static List<Shape> randomInners(){
		return randomInners(42);
	}
	
	public static List<Shape> randomInners(int n){ // with outer0 : inner 63.40956080872252 outer 42.273616058038115
		List<Shape> inners = new ArrayList<>();
		inners.add(new Circle(0, 0, 1.5));
		int x = 20;
		int y = 20;
		int r = 2;
		Random random = new Random(7);
		for (int i=0; i<n; i++){
			inners.add(new Circle(random.nextInt(x) - x / 2.0, random.nextInt(y) - y / 2.0, random.nextDouble() * (r-1) + 1));
		}
		return inners;
	}
	
	public static List<Shape> innersDebugging(){
		List<Shape> inners = new ArrayList<>();
		inners.add(new Circle(-588, 572, 7.051316333315605));
		inners.add(new Circle(-585, 577, 2.5647353660111833));
		inners.add(new Circle(-595, 571, 8.022288328497755));
		inners.add(new Circle(-591, 571, 4.170049220132277));
		return inners;
	}
	
	public static Shape outerDebugging(){
		Shape outer = new Circle(-590, 570, 20);
		return outer;
	}
	
	public static List<Shape> randomForestCircle(){
		return randomForestCircle(52000);
	}
	
	public static List<Shape> randomForestCircle(int n){
		List<Shape> inners = new ArrayList<>();
		int r = 4;
		Random random = new Random(0);
		Circle c = new Circle(0, 0, 3210);
		Point2D.Double p;
		for (int i=0; i<n; i++){
			p = c.getRandomPoint();
			inners.add(new Circle(p.x, p.y, random.nextDouble() * r + 8));
		}
		return inners;
	}
	
	public static List<Shape> randomForestSquare(){
		return randomForestSquare(20000);
	}
	
	public static List<Shape> randomForestSquare(int n){
		List<Shape> inners = new ArrayList<>();
		int x = 7000;
		int y = 7000;
		int r = 15;
		Random random = new Random(0);
		for (int i=0; i<n; i++){
			inners.add(new Circle(random.nextInt(x) - x / 2.0, random.nextInt(y) - y / 2.0, random.nextDouble() * r + 25));
		}
		return inners;
	}
	
	public static Shape outer0(){
		Shape outer = new Circle(0, 0, 5.8);
		return outer;
	}
	
	public static Shape beehive(){
		Shape outer = new Circle(0, 0, 3200);
		return outer;
	}
	
	public static List<Shape> inners0(){
		List<Shape> inners = new ArrayList<>();
		inners.add(new Circle(0, 0, 1));
		inners.add(new Circle(3, 3, 1.5));
		inners.add(new Circle(5, -4, 1.2));
		inners.add(new Circle(1.5, 0.7, 1.5));
		inners.add(new Circle(-3, 4, 1.5));
		inners.add(new Circle(-2.5, -3, 2));
		inners.add(new Circle(-3.5, -1, 2));
		inners.add(new Circle(3, -1, .5));
		inners.add(new Circle(2.5, 3, .5));
		inners.add(new Circle(3.5, 1, 1.2));
		inners.add(new Circle(4.5, 1, 1));
		inners.add(new Circle(5.5, 3, 1.5));
		return inners;
	}
	
	public static Shape outer1(){
		Shape outer = new Circle(0, 0, 3);
		return outer;
	}
	
	public static List<Shape> inners1(){
		List<Shape> inners = new ArrayList<>();
		inners.add(new Circle(1, 0, 2));
		inners.add(new Circle(-1, 0, 2));
		inners.add(new Circle(0, 2, 2));
		return inners;
	}
	
	public static List<Shape> innersEasy(){
		List<Shape> inners = new ArrayList<>();
		inners.add(new Circle(1, 0, 2));
		inners.add(new Circle(-1, 0, 2));
		return inners;
	}
	
	public static Shape outerEasy(){
		Shape outer = new Circle(0, 0, 3);
		return outer;
	}
	
	public static Shape outerDebugging2(){
		return new Circle(64, 64, 64);
	}
	
	public static List<Shape> innersDebugging2(){
		List<Shape> inners = new ArrayList<>();
		inners.add(new Circle(64, 64, 32));
		return inners;
	}
	
//	public static List<Shape> innersClement() throws FileNotFoundException {
//		List<Shape> result = new ArrayList<>();
//		File f = new File("/home/jschoete/Documents/research/overlapping_canopees/code/src/examples/TreesXY_CanopD.txt");
//		Scanner scanner = new Scanner(f);
//		scanner.nextLine();
//		while (scanner.hasNextLine()) {
//			String line = scanner.nextLine();
//			String[] words = line.split("\t");
//			result.add(new Circle(stringToDouble(words[1]), stringToDouble(words[2]), stringToDouble(words[3])));
//		}
//		scanner.close();
//		return result;
//	}
}

