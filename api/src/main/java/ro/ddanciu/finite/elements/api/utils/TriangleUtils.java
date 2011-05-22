package ro.ddanciu.finite.elements.api.utils;

import static java.lang.String.format;
import static ro.ddanciu.finite.elements.api.Constants.MY_CNTX;

import java.math.BigDecimal;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.Segment;
import ro.ddanciu.finite.elements.api.Triangle;

public class TriangleUtils {


	private static final BigDecimal TWO = new BigDecimal("2");

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
		
		BigDecimal x = a.add(b, MY_CNTX).add(c, MY_CNTX);
		return x;
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
	
	/**
	 * http://en.wikipedia.org/wiki/Circumscribed_circle#Coordinates_of_circumcenter
	 * 
	 * @param t
	 * @return
	 */
	public static Point circumcenter(Triangle t) {

		BigDecimal ax = t.getP1().getX();
		BigDecimal ay = t.getP1().getY();
		BigDecimal bx = t.getP2().getX();
		BigDecimal by = t.getP2().getY();
		BigDecimal cx = t.getP3().getX();
		BigDecimal cy = t.getP3().getY();
		
		BigDecimal ax2 = ax.pow(2, MY_CNTX);
		BigDecimal ay2 = ay.pow(2, MY_CNTX);
		BigDecimal bx2 = bx.pow(2, MY_CNTX);
		BigDecimal by2 = by.pow(2, MY_CNTX);
		BigDecimal cx2 = cx.pow(2, MY_CNTX);
		BigDecimal cy2 = cy.pow(2, MY_CNTX);
		
		BigDecimal ay2ax2 = ay2.add(ax2, MY_CNTX);
		BigDecimal by2bx2 = by2.add(bx2, MY_CNTX);
		BigDecimal cy2cx2 = cy2.add(cx2, MY_CNTX);
		
		BigDecimal d = TWO.multiply(
			ax.multiply(by.subtract(cy, MY_CNTX), MY_CNTX)
				.add(bx.multiply(cy.subtract(ay, MY_CNTX), MY_CNTX), MY_CNTX)
				.add(cx.multiply(ay.subtract(by, MY_CNTX), MY_CNTX), MY_CNTX), 
			MY_CNTX);
		
		
		BigDecimal xn = ay2ax2.multiply(by.subtract(cy, MY_CNTX))
			.add(by2bx2.multiply(cy.subtract(ay, MY_CNTX), MY_CNTX), MY_CNTX)
			.add(cy2cx2.multiply(ay.subtract(by, MY_CNTX), MY_CNTX), MY_CNTX);
	
		BigDecimal yn = ay2ax2.multiply(cx.subtract(bx, MY_CNTX))
			.add(by2bx2.multiply(ax.subtract(cx, MY_CNTX), MY_CNTX), MY_CNTX)
			.add(cy2cx2.multiply(bx.subtract(ax, MY_CNTX), MY_CNTX), MY_CNTX);

		BigDecimal x = xn.divide(d, MY_CNTX).round(MY_CNTX);
		BigDecimal y = yn.divide(d, MY_CNTX).round(MY_CNTX);
		
		return new Point(x, y);
	}
}
