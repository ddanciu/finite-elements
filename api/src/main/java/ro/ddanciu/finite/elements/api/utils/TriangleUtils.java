package ro.ddanciu.finite.elements.api.utils;

import static java.lang.String.format;
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
	
	/**
	 * Three points are a counter-clockwise turn if ccw > 0, clockwise if
	 * ccw < 0, and collinear if ccw = 0 because ccw is a determinant that
	 * gives the signed area of the triangle formed by p1, p2, and p3.	
	 */
	public static int counterClockwise(Point p1, Point p2, Point p3) {
	    BigDecimal a = (p2.getX().subtract(p1.getX())).multiply((p3.getY().subtract(p1.getY())));
		BigDecimal b = (p2.getY().subtract(p1.getY())).multiply((p3.getX().subtract(p1.getX())));
		return a.subtract(b).intValue();
	    
	}
}
