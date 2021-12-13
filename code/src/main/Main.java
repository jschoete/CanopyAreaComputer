package main;

import computation.AreaComputer;
import computation.exact.Exact;
import computation.montecarlo.MonteCarlo;
import computation.shapes.Shape;
import postanalysis.NAChecker;

import java.io.IOException;
import java.util.List;

public class Main {
	
	static String fileName = "TreesXY_CanopD.txt";
	static String method = "Exact"; // "Exact" or "MonteCarlo"
	static boolean checkNA = false;
	
	public static void main(String[] args) throws IOException {
//		String input = args[0];
		String input = "input/" + fileName;
		Parser p = new Parser(input, method);
		List<Shape> innerShapes = p.getInnerShapes();
		int n = innerShapes.size();
		AreaComputer areaComputer = null;
		Shape outerShape;
		for (int treeIndex=1; treeIndex<=n; treeIndex++){
			for (double radius = 2.5; radius <= 50; radius += 2.5){
				outerShape = p.getOuterShape(treeIndex, radius);
				if (method.equals("Exact")){
					areaComputer = new Exact(outerShape, innerShapes, true);
				} else if (method.equals("MonteCarlo")){
					areaComputer = new MonteCarlo(outerShape, innerShapes, true);
				}
				p.writeUncanopiedResult(treeIndex, areaComputer.getOuterArea());
				p.writeCanopiedResult(treeIndex, areaComputer.getInnerArea());
				System.out.println("Areas computed for tree " + p.getTreeName(treeIndex) + " with buffer radius " + radius + "m.");
			}
		}
		if (checkNA){
			NAChecker.check(p.getOutputUncanopied());
		}
		
	}
	
}
