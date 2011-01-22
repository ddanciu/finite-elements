package ro.ddanciu.finite.elements.algorithms;

import static ro.ddanciu.finite.elements.api.Utils.max;
import static ro.ddanciu.finite.elements.api.Utils.min;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import ro.ddanciu.finite.elements.api.Line;
import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.PoliLine;
import ro.ddanciu.finite.elements.api.Segment;

/**
 * Algorithm for decomposing a closed poliline into trapezes.
 * 
 * Note: not thread safe!!!		
 * @author dan
 */
public class SeidelTrapezoidation extends AbstractSweepAlgorithm {

	private PoliLine outer;

	@SuppressWarnings("unused")
	private PoliLine[] holes;
	
	private Pool pool;
	private PoliLine leftover;
	Set<PoliLine> discretization;

	private Vertice prior;

	public Set<PoliLine> decompose(PoliLine outer, PoliLine... holes) {
		this.outer = outer;
		this.holes = holes;

		pool = new Pool();
		leftover = outer.clone();
		discretization = new HashSet<PoliLine>();
		
		run();
		return discretization;
		
	}
	
	@Override
	public void run() {
		super.run();
		discretization.add(leftover);
	}
	
	@Override
	protected Iterable<Segment> segments() {
		return outer.segments();
	}

	@Override
	protected void sweep(Vertice current) {
		
		if (pool.valid(current)) {
			
			Segment newSegment = new Segment(current, prior);
			if (! outer.containsSegment(newSegment)) {
				System.out.println(newSegment);
				PoliLine x;
				if (current.getX().compareTo(prior.getX()) > 0) { //gt
					x = pop(leftover, current, prior);
				} else {
					x = pop(leftover, prior, current);
				}
				discretization.add(x);
			}
			
		}
		
		pool.enhance(current);
		prior = current;
	}
	
	protected PoliLine pop(PoliLine master, Point first, Point last) {
		
		Iterator<Point> cursor = master.iterator();
		LinkedList<Point> poppedPoints = new LinkedList<Point>();
		poppedPoints.add(first);
		boolean started = false;
		while (cursor.hasNext()) {
			Point x = cursor.next();
			if (started && x.equals(last)) {
				break;
			} else if (started) {
				poppedPoints.add(x);
				cursor.remove();
			} else if (x.equals(first)) {
				started = true;
			} /* else {
				continue;
			} */
			if (!cursor.hasNext() && started) {
				cursor = master.iterator();
			}
			
		}
		poppedPoints.add(last);
		
		return new PoliLine(poppedPoints.toArray(new Point[poppedPoints.size()]));
	}

	@SuppressWarnings("serial")
	private final class Pool extends HashSet<Segment> {
		
		public void enhance(Vertice v) {
			
			if (v.p1.getY().compareTo(v.getY()) > 0){ //gt
				add(v.segment1);
			} else {
				remove(v.segment1);
			}
				
			if (v.p2.getY().compareTo(v.getY()) > 0){ //gt
				add(v.segment2);
			} else {
				remove(v.segment2);
			}

			
		}

		public boolean valid(Vertice current) {
			
			Line l = Line.defineByPoints(current, new Point(current.getX().add(BigDecimal.ONE), current.getY()));
			
			LinkedList<Point> valid = new LinkedList<Point>();
			for (Segment s : this) {
				Point p = l.intersects(s.getLine());
				if (p != null
						&& p.getX().compareTo(max(s.getP1().getX(), s.getP2().getX())) < 0
						&& p.getX().compareTo(min(s.getP1().getX(), s.getP2().getX())) > 0) {
					
					valid.add(p);
				}
			}

			if (valid.isEmpty()) { 
				return false;
			}
			
			valid.add(current);
			Collections.sort(valid, COMPARATOR_BY_X);
			
			
			Iterator<Point> it = valid.iterator();
			boolean found = false;
			int before = 0;
			int after = 0;
			while(it.hasNext()) {
				Point p = it.next();
				if (p.getX().compareTo(current.getX()) == 0) {
					found = true;
					continue;
				}
				
				if (found) {
					after++;
				} else {
					before++;
				}
				
			}
			return (before % 2 == 1 || after % 2 == 1);
		}
		
	}
}
