package ro.ddanciu.finite.elements.api.utils;

import static java.lang.String.format;
import static java.math.MathContext.DECIMAL128;
import static ro.ddanciu.finite.elements.api.Constants.MY_CNTX;

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
	
	public static BigDecimal area(Triangle t) {
		BigDecimal a = t.getP1().getX().subtract(t.getP3().getX(), MY_CNTX);
		BigDecimal b = t.getP1().getY().subtract(t.getP3().getY(), MY_CNTX);
		BigDecimal c = t.getP2().getX().subtract(t.getP3().getX(), MY_CNTX);
		BigDecimal d = t.getP2().getY().subtract(t.getP3().getY(), MY_CNTX);
		
		return (a.multiply(d, MY_CNTX).subtract(b.multiply(c, MY_CNTX))).abs().multiply(new BigDecimal(0.5), MY_CNTX);
	}
	
	public static BigDecimal perimeter(Triangle t) {

		BigDecimal a = t.getE1().length();
		BigDecimal b = t.getE2().length();
		BigDecimal c = t.getE3().length();
		
		BigDecimal x = a.add(b, DECIMAL128).add(c, DECIMAL128);
		return x;
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


	/**
	 * http://en.wikipedia.org/wiki/Incenter#Coordinates_of_the_incenter
	 * @param t
	 * @return
	 */
	public static Point incenter(Triangle triangle) {
		
		BigDecimal perimeter = perimeter(triangle);
		
		BigDecimal x1 = triangle.getE1().length().multiply(triangle.getP1().getX(), DECIMAL128);
		BigDecimal x2 = triangle.getE2().length().multiply(triangle.getP2().getX(), DECIMAL128);
		BigDecimal x3 = triangle.getE3().length().multiply(triangle.getP3().getX(), DECIMAL128);
		
		BigDecimal y1 = triangle.getE1().length().multiply(triangle.getP1().getY(), DECIMAL128);
		BigDecimal y2 = triangle.getE2().length().multiply(triangle.getP2().getY(), DECIMAL128);
		BigDecimal y3 = triangle.getE3().length().multiply(triangle.getP3().getY(), DECIMAL128);
		
		BigDecimal x = x1.add(x2, DECIMAL128).add(x3, DECIMAL128).divide(perimeter, DECIMAL128);
		BigDecimal y = y1.add(y2, DECIMAL128).add(y3, DECIMAL128).divide(perimeter, DECIMAL128);

		return new Point(x, y); 
	}
}
