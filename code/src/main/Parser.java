package main;

import computation.DecimalCutter;
import computation.shapes.Circle;
import computation.shapes.Shape;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
	
	private File input;
	private File outputUncanopied;
	private File outputCanopied;
	private int currentRowIndexUncanopied;
	private int currentRowIndexCanopied;
	
	public Parser(String input, String method) throws IOException {
		input = normalizeInput(input);
		this.currentRowIndexUncanopied = 0;
		this.currentRowIndexCanopied = 0;
		this.input = new File("input/" + input);
		this.outputUncanopied = new File("output/" + input.substring(0, input.lastIndexOf('.')) + "_canopiedArea_" + method + ".txt");
		this.outputCanopied = new File("output/" + input.substring(0, input.lastIndexOf('.')) + "_uncanopiedArea_" + method + ".txt");
		if (!this.outputUncanopied.exists()) {
			initializeOutput(outputUncanopied);
		}
		if (!this.outputCanopied.exists()) {
			initializeOutput(outputCanopied);
		}
	}
	
	private String normalizeInput(String input) {
		String[] split = input.split("./input/");
		input = split[split.length-1];
		split = input.split("input/");
		return split[split.length-1];
	}
	
	private void initializeOutput(File output) throws IOException {
		output.getParentFile().mkdirs();
		output.createNewFile();
		Writer writer = new FileWriter(output, true);
		writer.write("Tree \\ Buffer radius (m)");
		for (double i = 2.5; i <= 50; i += 2.5) {
			writer.write("\t" + i);
		}
		writer.close();
	}
	
	public List<Shape> getInnerShapes() throws FileNotFoundException {
		List<Shape> result = new ArrayList();
		Scanner scanner = new Scanner(input);
		scanner.nextLine();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] words = line.split("\t");
			result.add(new Circle(stringToDouble(words[1]), -stringToDouble(words[2]), stringToDouble(words[3])/2.0));
		}
		scanner.close();
		return result;
	}
	
	public static double stringToDouble(String word) {
		return Double.parseDouble(word.replaceAll(",", "."));
	}
	
	public void writeUncanopiedResult(int rowIndex, double result) throws IOException {
		Writer writer = new FileWriter(outputUncanopied, true);
		if (currentRowIndexUncanopied != rowIndex){
			currentRowIndexUncanopied++;
			Scanner scanner = new Scanner(input);
			for (int i=0; i<rowIndex; i++)
				scanner.nextLine();
			String line = scanner.nextLine();
			String treeName = line.split("\t")[0];
			writer.write("\n" + treeName);
			scanner.close();
		}
		writer.write("\t" + DecimalCutter.cut(result, 2));
		writer.close();
	}
	
	public void writeCanopiedResult(int rowIndex, double result) throws IOException {
		Writer writer = new FileWriter(outputCanopied, true);
		if (currentRowIndexCanopied != rowIndex){
			currentRowIndexCanopied++;
			Scanner scanner = new Scanner(input);
			for (int i=0; i<rowIndex; i++)
				scanner.nextLine();
			String line = scanner.nextLine();
			String treeName = line.split("\t")[0];
			writer.write("\n" + treeName);
			scanner.close();
		}
		writer.write("\t" + DecimalCutter.cut(result, 2));
		writer.close();
	}
	
	public Shape getOuterShape(String treeName, String radius) throws FileNotFoundException {
		return getOuterShape(treeIndex(treeName), radiusToDouble(radius));
	}
	
	public Shape getOuterShape(int x, int y) throws FileNotFoundException {
		return getOuterShape(x, (double)y * 2.5);
	}
	
	public Shape getOuterShape(int treeIndex, double radius) throws FileNotFoundException {
		Scanner scanner = new Scanner(input);
		for (int i=0; i<treeIndex; i++){
			scanner.nextLine();
		}
		String line = scanner.nextLine();
		scanner.close();
		String[] words = line.split("\t");
		return new Circle(stringToDouble(words[1]), -stringToDouble(words[2]), radius);
	}
	
	private int treeIndex(String treeName) throws FileNotFoundException {
		int result = 0;
		Scanner scanner = new Scanner(input);
		String line = scanner.nextLine();
		String s = line.split("\t")[0];
		while (!s.equals(treeName)){
			result++;
			line = scanner.nextLine();
			s = line.split("\t")[0];
		}
		scanner.close();
		return result;
	}
	
	private double radiusToDouble(String radius) {
		if (radius.charAt(radius.length()-1) == 'm')
			radius = radius.substring(0, radius.length()-2);
		return stringToDouble(radius);
	}
	
	public String getTreeName(int index) throws FileNotFoundException {
		Scanner scanner = new Scanner(input);
		for (int i=0; i<index; i++)
			scanner.nextLine();
		String line = scanner.nextLine();
		scanner.close();
		return line.split("\t")[0];
	}
	
	public String getOutputUncanopied(){
		return outputUncanopied.getAbsolutePath();
	}
}
