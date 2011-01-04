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
		return e2.getP1();
	}

	public Point getP2() {
		return e1.getP1();
	}

	public Point getP3() {
		return e1.getP2();
	}
}
