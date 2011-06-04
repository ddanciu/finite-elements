package ro.ddanciu.finite.elements.api.utils;

import static java.lang.String.format;
import static java.math.MathContext.DECIMAL128;

import java.math.BigDecimal;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.Segment;
import ro.ddanciu.finite.elements.api.Triangle;

public class TriangleUtils {


	public static Segment segmentInCommon(Triangle a, Triangle b) {

		Point[] pointsInCommon = new Point[2];
		int i = 0;
		try {
			for (Point x : a) {
				for (Point y : b) {
					if (x.equals(y)) {
						pointsInCommon[i++] = x;
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException(format("The given triangles are equal! <%s,%s>", a, b));
		}
		
		Segment segment;
		if (i == 2) {
			segment = new Segment(pointsInCommon[0], pointsInCommon[1]);
		} else {
			segment = null;
		}
		return segment;
	}
	
	/**
	 * Three points are a counter-clockwise turn if ccw > 0, clockwise if
	 * ccw < 0, and collinear if ccw = 0 because ccw is a determinant that
	 * gives the signed area of the triangle formed by p1, p2, and p3.	
	 */
	public static int counterClockwise(Point p1, Point p2, Point p3) {
	    BigDecimal a = (p2.getX().subtract(p1.getX(), DECIMAL128)).multiply((p3.getY().subtract(p1.getY(), DECIMAL128)), DECIMAL128);
		BigDecimal b = (p2.getY().subtract(p1.getY(), DECIMAL128)).multiply((p3.getX().subtract(p1.getX(), DECIMAL128)), DECIMAL128);
		return a.subtract(b, DECIMAL128).signum();
	    
	}


}
