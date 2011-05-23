/**
 * 
 */
package ro.ddanciu.finite.elements.api;

import static ro.ddanciu.finite.elements.api.Constants.MY_RND;
import static ro.ddanciu.finite.elements.api.Constants.MY_SCALE;

import java.math.BigDecimal;

/**
 * @author dan
 *
 */
public class Point {
	
	private final BigDecimal x;
	
	private final BigDecimal y;
	
	public Point(BigDecimal x, BigDecimal y) {
		this.x = x.setScale(MY_SCALE, MY_RND);
		this.y = y.setScale(MY_SCALE, MY_RND);
	}

	public Point(double x, double y) {
		this(new BigDecimal(x), new BigDecimal(y));
	}

	public BigDecimal getX() {
		return x;
	}

	public BigDecimal getY() {
		return y;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x.hashCode();
		result = prime * result + y.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof Point))
			return false;
		Point other = (Point) obj;
		return x.compareTo(other.x) == 0 && y.compareTo(other.y) == 0;
	}

	@Override
	public String toString() {
		return "(" + x.toString() + ", " + y.toString() + ")";
	}
}
