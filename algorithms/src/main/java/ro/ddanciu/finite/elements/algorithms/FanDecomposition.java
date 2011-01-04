package ro.ddanciu.finite.elements.algorithms;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.PoliLine;
import ro.ddanciu.finite.elements.api.Segment;
import ro.ddanciu.finite.elements.api.Triangle;

/**
 * @author dan
 */
public class FanDecomposition extends AbstractSweepAlgorithm {

	private PoliLine poligon;
	private PoliLine leftover;
	private HashSet<Triangle> result;

	private FanDecomposition(PoliLine poligon) {
		this.poligon = poligon;
		this.leftover = poligon.clone();
		this.result = new HashSet<Triangle>();
	}

	public static Set<Triangle> decompose(PoliLine poligon) {
		FanDecomposition algorithm = new FanDecomposition(poligon);
		algorithm.run();
		
		return algorithm.result;
	}

	@Override
	protected void sweep(Vertice current) {

		Iterator<Point> cursor = leftover.iterator();
		
		if (leftover.size() < 3) {
			return;
		}

		Point[] triangle = new Point[3];
		int i = 0;
		boolean reset = true;
		boolean started = false;
		while(cursor.hasNext()) {
			Point x = cursor.next();
			
			if (x.equals(current)) {
				started = true;
			}
			
			if (started) {
				triangle[i] = x;
				
				if (i == 1) {
					cursor.remove();	
				} else if (i == 2) {
					break;
				}
				
				i++;
			}
			
			if (reset && !cursor.hasNext() && i < 3) {
				cursor = leftover.iterator();
				reset = false;
			}
		}
		
		if (i == 2) {
			result.add(new Triangle(triangle[0], triangle[1], triangle[2]));
		}
	}

	@Override
	protected Iterable<Segment> segments() {
		return poligon.segments();
	}
}
