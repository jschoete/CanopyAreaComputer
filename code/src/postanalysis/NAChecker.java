package postanalysis;

import computation.DecimalCutter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static main.Parser.stringToDouble;

public class NAChecker {
	
	public static void check(String output) throws IOException {
		Scanner scanner = new Scanner(new File(output));
		String line = scanner.nextLine();
		String[] radiiStrings = line.split("\t");
		List<Double> bufferAreas = new ArrayList();
		String radiusString;
		for (int i=1; i<radiiStrings.length; i++){
			radiusString = radiiStrings[i];
			bufferAreas.add(DecimalCutter.cut(Math.PI * Math.pow(stringToDouble(radiusString), 2), 2));
		}
		Writer writer = new FileWriter(output.split("\\.")[0] + "_NAChecked.txt");
		writer.write(line + "\n");
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			String[] results = line.split("\t");
			writer.write(results[0]);
			double result;
			for (int i=1; i<results.length; i++){
				result = stringToDouble(results[i]);
				if (result == bufferAreas.get(i-1)){
					writer.write("\tN/A");
				} else {
					writer.write("\t" + result);
				}
			}
			writer.write("\n");
		}
		scanner.close();
		writer.close();
	}
	
	public static void main(String[] args) throws IOException {
		String output = "output/TreesXY_CanopD_resultsExact.txt";
		NAChecker.check(output);
	}
}
