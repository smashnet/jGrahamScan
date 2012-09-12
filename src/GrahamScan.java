/**
*	This is a little java proggy that uses the GrahamScan Algorithm
*	to determine the convex hull of a given amount of points. The 
*	points are supplied to the program through a textfile of the 
*	following format, where the left number is the x coordinate and
*	the right number the y coordinate:
*
*	12.0 - 4.0
*	7.0 - 7.0
*	3.0 - 13.0
*	6.0 - 13.0
*	4.0 - 2.0
*	4.0 - 14.0
*	9.0 - 10.0
*	4.0 - 13.0
*	8.0 - 12.0
*	2.0 - 12.0
*
*	Author	: Nicolas Inden
*	Date	: 17.09.2010
*	License	: Free (No License)
**/

package graham;

import java.io.*;
import java.util.*;

public class GrahamScan {
	public static void main(String[] args) throws IOException{
		if(args.length < 1 || args.length > 1){
			System.out.println("Usage: java GrahamScan filename\nView README for correct fileformat");
			System.exit(0);
		}
		LinkedList<Coords> list = getCoordsFromFile(args[0]);
		System.out.println("Read following coords from file:");
		printCoords(list);
		list = polarSort(list);
		System.out.println("Sorted by polar-arc:");
		printCoords(list);
		System.out.println("Now computing convex hull...");
		LinkedList<Coords> convexHull = graham(list);
		System.out.println("Finished computation. Convex hull is:");
		printCoords(convexHull);
	}
	
	private static LinkedList<Coords> graham(LinkedList<Coords> list) {
		LinkedList<Coords> stack = new LinkedList<Coords>();
		Coords next;
		stack.push(list.get(0));
		stack.push(list.get(1));
		int i = 2;
		int n = list.size();
		double debug;
		while(i < n) {
			System.out.println("Stack: ");
			printCoords(stack);
			System.out.println("Now comparing: ");
			System.out.print("P0: ");
			printCoord(stack.get(1));
			System.out.print(" - ");
			System.out.print("P1: ");
			printCoord(stack.get(0));
			System.out.print(" - ");
			System.out.print("P2: ");
			printCoord(list.get(i));
			System.out.println();
			if((debug = compVect(stack.get(1),stack.get(0),list.get(i))) >= 0.0) {
				System.out.println("P2 is left of P0->P1: putting P2 on stack...");
				stack.push(list.get(i));
				i++;
			} else {
				System.out.println("P2 is right of P0->P1: removing P1 from stack...");
				stack.pop();
			}
		}
		
		return stack;
	}
	
	private static LinkedList<Coords> polarSort(LinkedList<Coords> list) {
		//find point with lowest x-coord
		int i = 0;
		double lowest = 9999999999.0;
		int lowIndex = 0;
		Coords tmp;
		while(i < list.size()) {
			tmp = list.get(i);
			if(tmp.getX() < lowest){
				lowest = tmp.getX();
				lowIndex = i;
			}else if(tmp.getX() == lowest) {
				if(tmp.getY() < list.get(lowIndex).getY()) {
					lowIndex = i;
				}
			}
			i++;
		}
		Coords lowestCoord = list.remove(lowIndex);

		
		LinkedList<Coords> sorted = new LinkedList<Coords>();
		sorted.add(list.poll());
		while(list.size() > 0) {
			tmp = list.poll();
			i = sorted.size();
			while(i > 0 && compVect(lowestCoord,sorted.get(i-1),tmp) < 0.0) {
				i--;
			}
			sorted.add(i,tmp);
		}
		sorted.add(0,lowestCoord);
		return sorted;
	}
	
	private static double compVect(Coords p0, Coords p1, Coords p2) {
		double res;
		res = ((p1.getX()-p0.getX())*(p2.getY()-p0.getY())) - ((p2.getX()-p0.getX())*(p1.getY()-p0.getY()));
		return res;
	}
	
	private static void printCoords(LinkedList<Coords> list) {
		int i = 0;
		Coords tmp;
		System.out.println("--------");
		while(i < list.size()) {
			tmp = list.get(i);
			System.out.println("(" + tmp.getX() + "," + tmp.getY() + ")");
			i++;
		}
		System.out.println("--------");
	}
	
	private static void printCoord(Coords xy) {
		System.out.print("x: " + xy.getX() + " y: " + xy.getY());
	}
	
	private static LinkedList<Coords> getCoordsFromFile(String filename){
		try{
			File file = new File(filename);
			BufferedReader br = new BufferedReader(new FileReader(file));
			LinkedList<Coords> list = new LinkedList<Coords>();
			String tmp = "";
			String tmp1[] = new String[3];
			while((tmp = br.readLine()) != null) {
				//Parse and merge into int array
				tmp1 = tmp.split(" ");
				list.add(new Coords(Double.parseDouble(tmp1[0]),Double.parseDouble(tmp1[2])));
			}
			return list;
		}catch(IOException ioe){
			System.out.println("Error: " + ioe);
			System.exit(0);
		}
		return null;
	}
	
	static class Coords {
		private double x;
		private double y;

		public Coords(double newX, double newY) {
			this.x = newX;
			this.y = newY;
		}

		public void setX(double newX) {
			this.x = newX;
		}

		public double getX() {
			return this.x;
		}

		public void setY(double newY) {
			this.y = newY;
		}

		public double getY() {
			return this.y;
		}
	}
}
