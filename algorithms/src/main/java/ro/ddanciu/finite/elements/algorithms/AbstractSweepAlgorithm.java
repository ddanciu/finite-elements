package ro.ddanciu.finite.elements.algorithms;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.Segment;

public abstract class AbstractSweepAlgorithm {

	protected static final Comparator<Point> COMPARATOR_BY_X = new Comparator<Point>() {
			@Override
			public int compare(Point p1, Point p2) {
				return p1.getX().compareTo(p2.getX());
			}
		};
		

	public void run() {
		Iterable<Vertice> sweep = getPointsInSweepOrder();
		for (Vertice current : sweep) {
			sweep(current);
		}
	}

	protected abstract void sweep(Vertice current);

	private static final Comparator<Point> COMPARATOR_BY_Y = new Comparator<Point>() {
			@Override
			public int compare(Point p1, Point p2) {
				return p1.getY().compareTo(p2.getY());
			}
		};

	protected final static class Vertice extends Point {
			
			protected Segment segment1;
			protected Segment segment2;
			
			protected Point p1;
			protected Point p2;
			
			private Vertice(Point point, Segment segment1, Segment segment2) {
				super(point.getX(), point.getY());
				this.segment1 = segment1;
				this.segment2 = segment2;
				
				this.p1 = segment1.getP1().equals(point) ? segment1.getP2() : segment1.getP1();
				this.p2 = segment2.getP1().equals(point) ? segment2.getP2() : segment2.getP1();
			}
	
	
	
			public static Vertice instance(Segment s1, Segment s2) {
				Point point;
				if (s1.getP1().equals(s2.getP1()) || s1.getP1().equals(s2.getP2())) {
					point = s1.getP1();
				} else if (s1.getP2().equals(s2.getP1()) || s1.getP2().equals(s2.getP2())) {
					point = s1.getP2();
				} else {
					throw new IllegalArgumentException(
							"Illegal vertice, segments don't have common end point: " + s1 + ", " + s2);
				}
				
				return new Vertice(point, s1, s2);
			}
		}

	protected Iterable<Vertice> getPointsInSweepOrder() {
		Set<Vertice> points = new TreeSet<Vertice>(COMPARATOR_BY_Y);
		
		Iterator<Segment> segments = segments().iterator();
		
		Segment first = null;
		Segment prior = null;
		while (segments.hasNext()) {
			Segment current = segments.next();
			if (first == null) {
				first = current;
			} else {
				points.add(Vertice.instance(prior, current));
			}
			prior = current;
		}
		points.add(Vertice.instance(prior, first));
		return points;
	}

	protected abstract Iterable<Segment> segments();
	
}