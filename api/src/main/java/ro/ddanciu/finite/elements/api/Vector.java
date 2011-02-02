/**
 * 
 */
package ro.ddanciu.finite.elements.api;

/**
 * A vector is a segment for which the order
 * @author dan
 *
 */
public class Vector {
	
	private Segment magnitude;
	private boolean p1ToP2 = true;

	public Vector(Point p1, Point p2) {
		magnitude =  new Segment(p1, p2);
	}
	
	public Vector(Segment s, boolean direction) {
		magnitude = s;
		p1ToP2 = direction;
	}
	
	public Segment getMagnitude() {
		return magnitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (p1ToP2) {
			result = prime * result + magnitude.p1.hashCode();
			result = prime * result + magnitude.p2.hashCode();
		} else {
			result = prime * result + magnitude.p2.hashCode();
			result = prime * result + magnitude.p1.hashCode();
		}
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
		
		Vector other = (Vector) obj;
		if (p1ToP2) {
			return (magnitude.getP1().equals(other.magnitude.getP1()) 
					&& magnitude.getP2().equals(other.magnitude.getP2()));
		} else {
			return (magnitude.getP1().equals(other.magnitude.getP2()) 
					&& magnitude.getP2().equals(other.magnitude.getP1()));
		}
	}

	public Vector invert() {
		Vector inverted = new Vector(magnitude, ! p1ToP2);
		return inverted;
	}

	@Override
	public String toString() {
		Object[] args;
		if (p1ToP2) {
			args = new Object[] {magnitude.getP1(), magnitude.getP2()};
		} else {
			args = new Object[] {magnitude.getP2(), magnitude.getP1()};
		}
		return String.format("(%s => %s)", args);
	}
	
	
	

}
