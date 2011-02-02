package ro.ddanciu.finite.elements.api;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author dan
 *
 */
public class PoliLine extends LinkedList<Point> implements Cloneable {

	private static final long serialVersionUID = 1L;

	public PoliLine(Point... points) {
		if (points == null || points.length < 2) {
			throw new IllegalArgumentException("The supplied points, do not define a poliline: " + points); 
		}
		
		for (Point p : points) {
			add(p);
		}
	}
	
	public PoliLine(Segment... segments) {
		if (segments == null || segments.length < 3) {
			throw new IllegalArgumentException("The supplied segments, do not define a poliline: " + segments); 
		} 
		Point last;
		if (segments[0].getP2().equals(segments[1].getP1()) 
				|| segments[0].getP1().equals(segments[1].getP1())) {
			last = segments[1].getP1();
		} else if (segments[0].getP2().equals(segments[1].getP2()) 
				|| segments[0].getP1().equals(segments[1].getP2())) {
			last = segments[1].getP2();
		} else {
			throw new IllegalArgumentException("The supplied segments, do not define a poliline: " + segments); 
		}
		add(last);
		for (int i = 1; i < segments.length; i++) {
			if (last.equals(segments[i].getP1())) {
				last = segments[i].getP2();
			} else if (last.equals(segments[i].getP2())) {
				last = segments[i].getP1();
			} else {
				throw new IllegalArgumentException("The supplied segments, do not define a poliline: " + segments); 
			}
			add(last);
		}
	}
	
	protected PoliLine(LinkedList<Point> points) {
		super(points);
	}

	public Iterator<Point> points() {
		return this.iterator();
	}
	
	public Iterable<Segment> segments() {
		final Iterator<Point> points = points();
		final Point start = points.next();
		
		final Iterator<Segment> it = new Iterator<Segment>() {
			private Point current = start;
			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public Segment next() {
				Point previous = current;
				current = points.hasNext() ? points.next() : null;
				return new Segment(previous, current != null ? current : start);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("This iterator is read only!");
				
			}
			
		};
		return new Iterable<Segment>() {
			@Override
			public Iterator<Segment> iterator() {
				return it;
			}
		};
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		for (Point p : this) {
			sb.append(p.toString()).append(",");
		}
		sb.setCharAt(sb.length() -1, '}');
		return sb.toString();
	}

	public boolean containsSegment(Segment s) {
		Iterator<Point> it = this.iterator();
		
		while (it.hasNext()) {
			Point p = it.next();
			if (p.equals(s.getP1())) {
				Point q;
				if (it.hasNext()) {
					q = it.next();
				} else {
					q = this.getFirst();
				}
				if (q.equals(s.getP2())) {
					return true;
				}
			} else if (p.equals(s.getP2())) {
				Point q;
				if (it.hasNext()) {
					q = it.next();
				} else {
					q = this.getFirst();
				}
				if(q.equals(s.getP1())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public PoliLine clone() {
		return new PoliLine((LinkedList<Point>) this);
	}

	@Override
	public boolean equals(Object o) {
		if (! (o instanceof PoliLine)) {
			return false;
		}
		Iterator<Point> other = ((PoliLine) o).iterator();
		
		boolean started = false;
		Iterator<Point> me = this.iterator();
		
		if (! me.hasNext()) {
			return (! other.hasNext());
		}
		
		Point otherFirst = other.next();
		while (other.hasNext()) {
			
			if (! me.hasNext()) {
				if (started) {
					me = iterator();
				} else {
					return false;
				}
			}
			
			Point mine = me.next();
			if (started) {
				if (!other.hasNext() || ! mine.equals(other.next())) {
					return false;
				}
			} else {
				if (mine.equals(otherFirst)) {
					started = true;
				}
			}
		}
		
		return started;
	}

	@Override
	public int hashCode() {
		int i = 0;
		for (Point p : this) {
			i += p.getX().intValue(); 
			i += p.getY().intValue();
		}
		return i;
	}
}
