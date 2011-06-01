/**
 * 
 */
package ro.ddanciu.finite.elements.api.readers;

import static java.lang.String.format;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.PoliLine;

/**
 * @author dan
 * 
 * Reads a poliline from an CSV as an {@link InputStream}.
 * 
 * Each line represents a 2D {@link Point}; a Point is defined by <code>x</code> and <code>y</code> coordinates.
 * <br/><br/>
 * Format:
 * <pre>
 * px1, py1
 * px2, py2
 * px3, py3
 * ...
 * pxi, pyi
 * </pre>
 * 
 * Sample:
 *<pre>
 *3.00, 0.00
 *2.45, 5,68
 *...
 *5.94, 2.76
 *</pre>
 */
public class ElementsReader implements Closeable {
	
	private Scanner scanner;
	
	public ElementsReader(InputStream stream) {
		scanner = new Scanner(stream).useDelimiter("\\s+|\\,\\s*|^\\s*$");
	}
	
	public PoliLine readPoliLine() {
		List<Point> points = readPoints();
		return new PoliLine(points.toArray(new Point[points.size()]));
	}

	public List<Point> readPoints() {
		List<Point> points = new ArrayList<Point>();
		while (scanner.hasNextLine()) {
			Point p = readPoint();
			points.add(p);
		}
		return points;
	}
	
	public Point readPoint() {
		BigDecimal x;
		if (scanner.hasNextBigDecimal()) {
			try {
				x = scanner.nextBigDecimal();
			} catch (Exception e) {
				System.out.println("Bla: " + scanner.next());
				throw new RuntimeException(e);
			}
		} else {
			throw new InputMismatchException(format("No such thing, no X coordinate. Got: >%s<", scanner.next()));
		}
		
		BigDecimal y;
		if (scanner.hasNextBigDecimal()) {
			try {
				y = scanner.nextBigDecimal();
			} catch (Exception e) {
				System.out.println("Bla: " + scanner.next());
				throw new RuntimeException(e);
			}
		} else {
			throw new InputMismatchException(format("No such thing, no Y coordinate. Got: >%s<", scanner.next()));
		}
		
		return new Point(x, y);
	}

	@Override
	public void close() throws IOException {
		scanner.close();
	}

}
