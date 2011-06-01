/**
 * 
 */
package ro.ddanciu.finite.elements.api;

import static ro.ddanciu.finite.elements.api.Constants.MY_CNTX;
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
			BigDecimal a = p2.getY().subtract(p1.getY(), MY_CNTX).divide(p2.getX().subtract(p1.getX(), MY_CNTX), MathContext.DECIMAL128).round(MY_CNTX);
			BigDecimal b = p1.getY().subtract(a.multiply(p1.getX(), MY_CNTX)).round(MY_CNTX); 
			return new Line(a, b);
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
		BigDecimal x = line.b.subtract(this.b, MY_CNTX).divide(a.subtract(line.a, MY_CNTX), MY_CNTX);
		BigDecimal y = x.multiply(a, MY_CNTX).add(this.b);
		x = x.setScale(MY_SCALE, MY_RND);
		y = y.setScale(MY_SCALE, MY_RND);
		return new Point(x, y);
	}

	public BigDecimal distance(Point p) {
		BigDecimal n = this.a.multiply(p.getX(), MY_CNTX)
			.subtract(p.getY(), MY_CNTX)
			.add(b, MY_CNTX).abs(MY_CNTX);
		
		double d2 = this.a.pow(2, MY_CNTX).add(BigDecimal.ONE, MY_CNTX).doubleValue();
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
			BigDecimal y =  super.a.multiply(line.a, MY_CNTX).add(line.b, MY_CNTX);
			return new Point(super.a, y);
		}

		@Override
		public BigDecimal distance(Point p) {
			return p.getX().abs(MY_CNTX);
		}
		
		
		
	}
	
}
