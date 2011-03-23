/**
 * 
 */
package ro.ddanciu.finite.elements.api;

import java.util.Iterator;

/**
 * @author dan
 *
 */
public class Triangle extends PoliLine {
	
	private static final long serialVersionUID = 1L;

	private final Segment e1;
	
	private final Segment e2;
	
	private final Segment e3;
	
	public Triangle(Point p1, Point p2, Point p3) {
		super(p1, p2, p3);
		Iterator<Segment> segments = super.segments().iterator();
		this.e1 = segments.next();
		this.e2 = segments.next();
		this.e3 = segments.next();
		
		if (getP1().equals(getP2())
				|| getP1().equals(getP3())
				|| getP2().equals(getP3())) {
			System.out.println("Victory!");
		}
	}
	
	public Triangle(Segment e1, Segment e2, Segment e3) {
		super(e1, e2, e3);
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		
		if (getP1().equals(getP2())
				|| getP1().equals(getP3())
				|| getP2().equals(getP3())) {
			System.out.println("Victory!");
		}
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
	
}
