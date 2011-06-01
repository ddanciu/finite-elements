/**
 * 
 */
package ro.ddanciu.finite.elements.api;

import static java.math.MathContext.DECIMAL128;
import static ro.ddanciu.finite.elements.api.Constants.MY_RND;
import static ro.ddanciu.finite.elements.api.Constants.MY_SCALE;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author dan
 * y = a*x + b
 * 
 * TODO: RFC for formula ax + by + c = 0
 */
public class Line {


	protected final BigDecimal a;
	
	protected final BigDecimal b;
	
	public static Line defineByPoints(Point p1, Point p2) {
		
		if (p2.getX().compareTo(p1.getX()) != 0) {
			BigDecimal a = p2.getY().subtract(p1.getY(), DECIMAL128).divide(p2.getX().subtract(p1.getX(), DECIMAL128), MathContext.DECIMAL128);
			BigDecimal b = p1.getY().subtract(a.multiply(p1.getX(), DECIMAL128));
			return new Line(a.setScale(MY_SCALE, MY_RND), b.setScale(MY_SCALE, MY_RND));
		} else {
			return new OyLine(p1.getX());
		}
	}
	
	private Line(BigDecimal a, BigDecimal b) {
		this.a = a.setScale(MY_SCALE, MY_RND);
		this.b = b.setScale(MY_SCALE, MY_RND);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + a.hashCode();
		result = prime * result + b.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		return a.compareTo(other.a) == 0 && b.compareTo(other.b) == 0;
	}

	@Override
	public String toString() {
		return "y = " + a + " * x  + " + b;
	}

	public Point intersects(Line line) {
		if (line == null) {
			throw new IllegalArgumentException("Cannot intersect with a null line!");
		} else if (line instanceof OyLine) {
			return line.intersects(this);
		} else if (this.a == line.a) {
			if (this.b == line.b) {
				throw new IllegalArgumentException("Intersection with same line! Line: " + line);
			} else {
				return null;
			}
		}
		
		// a1*x + b1 = a2*x + b2
		// x = (b2 - b1)/(a1 - a2)
		BigDecimal x = line.b.subtract(this.b, DECIMAL128).divide(a.subtract(line.a, DECIMAL128), DECIMAL128);
		BigDecimal y = x.multiply(a, DECIMAL128).add(this.b);
		
		x = x.setScale(MY_SCALE, MY_RND);
		y = y.setScale(MY_SCALE, MY_RND);
		return new Point(x, y);
	}

	public BigDecimal distance(Point p) {
		BigDecimal n = this.a.multiply(p.getX(), DECIMAL128)
			.subtract(p.getY(), DECIMAL128)
			.add(b, DECIMAL128).abs(DECIMAL128);
		
		double d2 = this.a.pow(2, DECIMAL128).add(BigDecimal.ONE, DECIMAL128).doubleValue();
		BigDecimal d = new BigDecimal(Math.sqrt(d2));
		
		BigDecimal dist = n.divide(d, MathContext.DECIMAL128);
		dist = dist.setScale(MY_SCALE, MY_RND);
		return dist;
	}

	private static final class OyLine extends Line {

		public OyLine(BigDecimal x) {
			super(x, BigDecimal.ZERO);
		}

		@Override
		public String toString() {
			return "x = " + this.a;
		}
		

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true; 
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Line other = (Line) obj;
			return super.a.compareTo(other.a) == 0;
		}

		@Override
		public Point intersects(Line line) {
			// x = a1
			if (line instanceof OyLine) {
				return null;
			}
			// y = a2 * x + b
			// y = a2 * a1 + b
			BigDecimal y =  super.a.multiply(line.a, DECIMAL128).add(line.b, DECIMAL128);
			return new Point(super.a, y);
		}

		@Override
		public BigDecimal distance(Point p) {
			return p.getX().abs(DECIMAL128).setScale(MY_SCALE, MY_RND);
		}
		
		
		
	}
	
}
