/**
 * 
 */
package ro.ddanciu.finite.elements.api;

import static java.math.MathContext.DECIMAL128;
import static ro.ddanciu.finite.elements.api.Constants.MY_RND;
import static ro.ddanciu.finite.elements.api.Constants.MY_SCALE;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * @author dan
 *
 */
public class Triangle extends PoliLine {

	private static final BigDecimal TWO = new BigDecimal(2);
	
	private static final long serialVersionUID = 1L;

	private final Segment e1;
	
	private final Segment e2;
	
	private final Segment e3;
	
	/**
	 * Computed at first request.
	 * @see  this#incenter()
	 */
	private transient Point incenter = null;

	/**
	 * Computerd at first request.
	 * @see this#perimeter()
	 */
	private transient BigDecimal perimeter = null;
	
	public Triangle(Point p1, Point p2, Point p3) {
		super(p1, p2, p3);
		Iterator<Segment> segments = super.segments().iterator();
		this.e1 = segments.next();
		this.e2 = segments.next();
		this.e3 = segments.next();
		
		if (p1.equals(p2) || p1.equals(p3) || p2.equals(p3)) {
			System.out.println("Problems!");
			System.exit(1);
		} else if (
				new Segment(p1, p2).getLine().distance(p3).doubleValue() == 0 ||
				new Segment(p2, p3).getLine().distance(p1).doubleValue() == 0 ||
				new Segment(p3, p1).getLine().distance(p2).doubleValue() == 0 
		) {
			System.out.println("Problems!");
			System.exit(1);
		}
	}
	
	public Triangle(Segment e1, Segment e2, Segment e3) {
		super(e1, e2, e3);
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
	}

	public Segment getE1() {
		return e1;
	}

	public Segment getE2() {
		return e2;
	}

	public Segment getE3() {
		return e3;
	}

	public Point getP1() {
		return e2.getP2();
	}

	public Point getP2() {
		return e3.getP2();
	}

	public Point getP3() {
		return e1.getP2();
	}

	@Override
	public boolean equals(Object o) {

		if (! (o instanceof PoliLine)) {
			return false;
		}
		
		PoliLine poliLine = (PoliLine) o;
		
		if (poliLine.size() != 3) {
			return false;
		}
		
		for (Point p : poliLine) {
			if (! this.contains(p)) {
				return false;
			}
		}
		return true;
	}
	
	public BigDecimal area() {
		BigDecimal a = this.getP1().getX().subtract(this.getP3().getX(), DECIMAL128);
		BigDecimal b = this.getP1().getY().subtract(this.getP3().getY(), DECIMAL128);
		BigDecimal c = this.getP2().getX().subtract(this.getP3().getX(), DECIMAL128);
		BigDecimal d = this.getP2().getY().subtract(this.getP3().getY(), DECIMAL128);
		
		BigDecimal area = (a.multiply(d, DECIMAL128).subtract(b.multiply(c, DECIMAL128))).abs().multiply(new BigDecimal(0.5), DECIMAL128);
		return area.setScale(MY_SCALE, MY_RND);
	}
	
	
	public BigDecimal perimeter() {

		if (perimeter == null) {
			BigDecimal a = this.getE1().length();
			BigDecimal b = this.getE2().length();
			BigDecimal c = this.getE3().length();
			perimeter = a.add(b, DECIMAL128).add(c, DECIMAL128);
		}
		return perimeter;
	}
	
	
	/**
	 * http://en.wikipedia.org/wiki/Incenter#Coordinates_of_the_incenter
	 * @param t
	 * @return
	 */
	public Point incenter() {
		
		if (incenter == null) {
			BigDecimal perimeter = perimeter();
			
			BigDecimal x1 = this.getE1().length().multiply(this.getP1().getX(), DECIMAL128);
			BigDecimal x2 = this.getE2().length().multiply(this.getP2().getX(), DECIMAL128);
			BigDecimal x3 = this.getE3().length().multiply(this.getP3().getX(), DECIMAL128);
			
			BigDecimal y1 = this.getE1().length().multiply(this.getP1().getY(), DECIMAL128);
			BigDecimal y2 = this.getE2().length().multiply(this.getP2().getY(), DECIMAL128);
			BigDecimal y3 = this.getE3().length().multiply(this.getP3().getY(), DECIMAL128);
			
			BigDecimal x = x1.add(x2, DECIMAL128).add(x3, DECIMAL128).divide(perimeter, DECIMAL128);
			BigDecimal y = y1.add(y2, DECIMAL128).add(y3, DECIMAL128).divide(perimeter, DECIMAL128);
			
			incenter = new Point(x, y);
		}
		
		return incenter; 
	}

	public BigDecimal perfectness() {

		try {
			BigDecimal a = this.getE1().length();
			BigDecimal b = this.getE2().length();
			BigDecimal c = this.getE3().length();
			
			BigDecimal x = TWO.multiply(a, DECIMAL128).multiply(b, DECIMAL128).multiply(c, DECIMAL128);
			
			BigDecimal t1 = b.add(c, DECIMAL128).subtract(a, DECIMAL128);
			BigDecimal t2 = c.add(a, DECIMAL128).subtract(b, DECIMAL128);
			BigDecimal t3 = a.add(b, DECIMAL128).subtract(c, DECIMAL128);
			BigDecimal y = t1.multiply(t2, DECIMAL128).multiply(t3, DECIMAL128);
			
			BigDecimal rez = x.divide(y, DECIMAL128);
			rez = rez.setScale(MY_SCALE, MY_RND);
			return rez;
		} catch (ArithmeticException e) {
			System.out.println("AritmeticProblems for " + this.toString());
			throw e;
		}
	}
}
