package ro.ddanciu.finite.elements.algorithms;

import static java.lang.String.format;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.PoliLine;
import ro.ddanciu.finite.elements.api.Triangle;

public class CornerFanDecomposition {
	
	
	private PoliLine poliLine;
	
	public void setPoliLine(PoliLine poliLine) {
		this.poliLine = poliLine;
	}

	public Set<Triangle> decompose(int corner) {
		
		
		if (corner < 0 || corner >= poliLine.size()) {
			throw new IllegalArgumentException(
					format("Invalid corner: %d, size of poligon: %d", corner, poliLine.size()));
		}
		
		Set<Triangle> results = new HashSet<Triangle>();
		
		Iterator<Point> iterator = poliLine.iterator();
		int i = 0;
		Point myCorner = null;
		Point previous = null;
		
		boolean reset = true;
		while(iterator.hasNext()) {
			Point cursor = iterator.next();
			if (i == corner) {
				if (reset) {
					myCorner = cursor;
				} else {
					break;
				}
			} else if (myCorner  != null && previous == null) {
				previous = cursor;
			} else if (myCorner  != null && previous != null) {
				results.add(new Triangle(myCorner, previous, cursor));
				previous = cursor;
			}
			i++;
			
			if (reset && !iterator.hasNext()) {
				reset = false;
				iterator = poliLine.iterator();
				i = 0;
			}
		}
		
		return results;
	}

}
