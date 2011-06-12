/**
 * 
 */
package ro.ddanciu.finite.elements.api;

import static java.math.MathContext.DECIMAL128;
import static ro.ddanciu.finite.elements.api.utils.MathUtils.max;
import static ro.ddanciu.finite.elements.api.utils.MathUtils.min;

import java.math.BigDecimal;

/**
 * @author dan
 *
 */
public class Segment {

	private static final BigDecimal TWO = new BigDecimal("2");
	
	protected final Point p1;
	
	protected final Point p2;
	
	protected transient Line line;
	
	protected transient BigDecimal length;

	public Segment(Point p1, Point p2) {
		
		if (p1 == null || p2 == null) {
			throw new IllegalArgumentException("A segment cannot have null endpoints: (" + p1 + ", " + p2 + ")");
		}
		
		this.p1 = p1;
		this.p2 = p2;
	}

	public Point getP1() {
		return p1;
	}

	public Point getP2() {
		return p2;
	}

	public Line getLine() {
		if (this.line == null) {
			this.line = Line.defineByPoints(p1, p2);
		}
		return line;
	}
	
	public BigDecimal length() {
		if (this.length == null) {
			this.length = p1.distance(p2);
		}
		
		return this.length;
	}
	
	public Point getMiddlePoint() {
		BigDecimal xm = p1.getX().add(p2.getX(), DECIMAL128).divide(TWO, DECIMAL128);
		BigDecimal ym = p1.getY().add(p2.getY(), DECIMAL128).divide(TWO, DECIMAL128);
		return new Point(xm, ym );
	}

	public boolean intersects(Segment other) {
		/*
		| Ax-Cx  Bx-Cx |    | Ax-Dx  Bx-Dx |
		| Ay-Cy  By-Cy |    | Ay-Dy  By-Dy |
		
		| Cx-Ax  Dx-Ax |    | Cx-Bx  Dx-Bx |
		| Cy-Ay  Dy-Ay |    | Cy-By  Dy-By |
		 */
		int d1 = p1.getX().subtract(other.p1.getX(), DECIMAL128).multiply(p2.getY().subtract(other.p1.getY(), DECIMAL128))
		.subtract(p1.getY().subtract(other.p1.getY(), DECIMAL128).multiply(p2.getX().subtract(other.p1.getX(), DECIMAL128))).signum();

		int d2 = p1.getX().subtract(other.p2.getX(), DECIMAL128).multiply(p2.getY().subtract(other.p2.getY(), DECIMAL128))
		.subtract(p1.getY().subtract(other.p2.getY(), DECIMAL128).multiply(p2.getX().subtract(other.p2.getX(), DECIMAL128))).signum();

		int d3 = other.p1.getX().subtract(p1.getX(), DECIMAL128).multiply(other.p2.getY().subtract(p1.getY(), DECIMAL128))
		.subtract(other.p1.getY().subtract(p1.getY(), DECIMAL128).multiply(other.p2.getX().subtract(p1.getX(), DECIMAL128))).signum();

		int d4 = other.p1.getX().subtract(p2.getX(), DECIMAL128).multiply(other.p2.getY().subtract(p2.getY(), DECIMAL128))
		.subtract(other.p1.getY().subtract(p2.getY(), DECIMAL128).multiply(other.p2.getX().subtract(p2.getX(), DECIMAL128))).signum();

		
		if (d1 == 0 && d2 == 0 && d3 == 0 && d4 == 0) {
			//overlap
			boolean o1 = 
				   (max(this.p1.getX(), this.p2.getX()).compareTo(min(other.p1.getX(), other.p2.getX()))) >= 0
				&& (min(this.p1.getX(), this.p2.getX()).compareTo(min(other.p1.getX(), other.p2.getX()))) <= 0
				&& (max(this.p1.getY(), this.p2.getY()).compareTo(min(other.p1.getY(), other.p2.getY()))) >= 0
				&& (min(this.p1.getY(), this.p2.getY()).compareTo(min(other.p1.getY(), other.p2.getY()))) <= 0;
			boolean o2 = 
				   (max(other.p1.getX(), other.p2.getX()).compareTo(min(this.p1.getX(), this.p2.getX()))) >= 0
				&& (min(other.p1.getX(), other.p2.getX()).compareTo(min(this.p1.getX(), this.p2.getX()))) <= 0
				&& (max(other.p1.getY(), other.p2.getY()).compareTo(min(this.p1.getY(), this.p2.getY()))) >= 0
				&& (min(other.p1.getY(), other.p2.getY()).compareTo(min(this.p1.getY(), this.p2.getY()))) <= 0;
			//contains	
			boolean c1 = 
				   (max(this.p1.getX(), this.p2.getX()).compareTo(max(other.p1.getX(), other.p2.getX()))) >= 0
				&& (min(this.p1.getX(), this.p2.getX()).compareTo(min(other.p1.getX(), other.p2.getX()))) <= 0
				&& (max(this.p1.getY(), this.p2.getY()).compareTo(max(other.p1.getY(), other.p2.getY()))) >= 0
				&& (min(this.p1.getY(), this.p2.getY()).compareTo(min(other.p1.getY(), other.p2.getY()))) <= 0;
			boolean c2 = 
				   (max(this.p1.getX(), this.p2.getX()).compareTo(max(other.p1.getX(), other.p2.getX()))) <= 0
				&& (min(this.p1.getX(), this.p2.getX()).compareTo(min(other.p1.getX(), other.p2.getX()))) >= 0
				&& (max(this.p1.getY(), this.p2.getY()).compareTo(max(other.p1.getY(), other.p2.getY()))) <= 0
				&& (min(this.p1.getY(), this.p2.getY()).compareTo(min(other.p1.getY(), other.p2.getY()))) >= 0;

			return 
				   o1
				|| o2
				|| c1
				|| c2;
		} else {
			return (d1 * d2 <= 0) && (d3 * d4 <= 0);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
		result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else  if (getClass() != obj.getClass()) {
			return false;
		}
		
		Segment other = (Segment) obj;
		if (p1.equals(other.p1)
				&& p2.equals(other.p2)) {
			return true;
		} else if (p1.equals(other.p2)
				&& p2.equals(other.p1)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "[" + p1 + ", " + p2 + "]";
	}
	
	
	
	
}
