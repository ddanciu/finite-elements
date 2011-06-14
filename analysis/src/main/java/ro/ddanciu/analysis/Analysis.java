/**
 * 
 */
package ro.ddanciu.analysis;

import static java.lang.String.format;
import static java.math.MathContext.DECIMAL128;
import static ro.ddanciu.finite.elements.api.Constants.MY_RND;
import static ro.ddanciu.finite.elements.api.Constants.MY_SCALE;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import ro.ddanciu.finite.elements.api.Line;
import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.PoliLine;
import ro.ddanciu.finite.elements.api.Segment;
import ro.ddanciu.finite.elements.api.readers.ElementsReader;

/**
 * @author dan
 *
 */
public class Analysis {
	
	public static void main(String[] args) throws FileNotFoundException {
		
		if (args.length != 1) {
			System.err.println("Provide imput file!");
			System.exit(1);
		}
		
		List<PoliLine> toAnalise = new ElementsReader(new FileInputStream(args[0])).readPoliLines();
		
		int counter = 1;
		for (PoliLine poliLine : toAnalise) {
			if (poliLine.size() != 8) {
				System.out.println(format("Wrong quad8 element (ignoring): %s", poliLine));
				continue;
			}
			
			Point[] points = poliLine.toArray(new Point[8]); //for performance
			Point[] element = new Point[8];
			element[0] = points[0];
			element[1] = points[2];
			element[2] = points[4];
			element[3] = points[6];
			element[4] = points[1];
			element[5] = points[3];
			element[6] = points[5];
			element[7] = points[7];
			
			BigDecimal[] f4 = f4(element);
			BigDecimal f5 = f5(poliLine);
			out(counter, f1(element), f2(element), f3(element), f4[0], f4[1], f5);
			
			counter++;
			
		}		
	}

	private static BigDecimal f1(Point[] points) {
		BigDecimal d02 = new Segment(points[0], points[2]).length();
		BigDecimal d13 = new Segment(points[1], points[3]).length();
		return d02.divide(d13, DECIMAL128).setScale(MY_SCALE, MY_RND);
	}

	private static BigDecimal f2(Point[] points) {
		BigDecimal d46 = new Segment(points[4], points[6]).length();
		BigDecimal d57 = new Segment(points[5], points[7]).length();
		return d46.divide(d57, DECIMAL128).setScale(MY_SCALE, MY_RND);
	}

	private static BigDecimal f3(Point[] points) {
		BigDecimal d26 = new Segment(points[2], points[6]).length();
		BigDecimal d04 = new Segment(points[0], points[4]).length();
		return d04.divide(d26, DECIMAL128).setScale(MY_SCALE, MY_RND);
	}

	private static BigDecimal[] f4(Point[] points) {
		Line line46 = new Segment(points[4], points[6]).getLine();
		Line line57 = new Segment(points[5], points[7]).getLine();
		Point m = line46.intersects(line57);
		BigDecimal[] res = new BigDecimal[2];
		if (m == null) {
			System.out.println(format("Wrong poliline (paralel-medians): %s", Arrays.toString(points)));
			System.exit(1);
		} else {
			BigDecimal d7m = new Segment(points[7], m).length();
			BigDecimal d5m = new Segment(points[5], m).length();
			res[0] = d7m.divide(d5m, DECIMAL128).setScale(MY_SCALE, MY_RND);
			
			BigDecimal d4m = new Segment(points[4], m).length();
			BigDecimal d6m = new Segment(points[6], m).length();
			res[1] = d4m.divide(d6m, DECIMAL128).setScale(MY_SCALE, MY_RND);
		}
		return res ;
	}
	
	private static BigDecimal f5(PoliLine poliLine) {
		
		Segment[] lines = new Segment[8];
		int i = 0;
		for (Segment s : poliLine.segments()) {
			lines[i++] = s;
		}
		
		BigDecimal sum = BigDecimal.ZERO; 
		int j = 8;
		while (j > 0) {
			int s1 = j--%8;
			int s2 = j--;
			
			double angle = lines[s1].getLine().angle(lines[s2].getLine());
			BigDecimal term = new BigDecimal(Math.PI / 2 - angle).pow(2);
			sum = sum.add(term, DECIMAL128);
		}
		return new BigDecimal(Math.sqrt(sum.doubleValue()));
	}

	private static void out(int counter, BigDecimal f1, BigDecimal f2, BigDecimal f3, BigDecimal f41, BigDecimal f42, BigDecimal f5) {
		System.out.println(format("%3d, %8.4f, %8.4f, %8.4f, %8.4f, %8.4f, %8.4f", counter, f1, f2, f3, f41, f42, f5));
	}

}
