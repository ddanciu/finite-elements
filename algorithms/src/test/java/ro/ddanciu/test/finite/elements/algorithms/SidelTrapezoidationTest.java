package ro.ddanciu.test.finite.elements.algorithms;


import static junit.framework.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ro.ddanciu.finite.elements.algorithms.SeidelTrapezoidation;
import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.PoliLine;
import ro.ddanciu.finite.elements.api.Triangle;

/**
 * @author dan
 *
 */
public class SidelTrapezoidationTest {

	@Test
	public void triangle() {
		
		Triangle triangle = new Triangle(
				new Point(0, 0), 
				new Point(1, 1), 
				new Point(1, 2));
		
		Set<PoliLine> results = SeidelTrapezoidation.run(triangle);

		assertEquals("Not as many as expected!", 1, results.size());
		assertEquals("Triangle not returned", triangle, results.iterator().next());
	}

	@Test
	public void diamond() {
		PoliLine diamond = new PoliLine(
				new Point(1, 0), new Point(0, 1), 
				new Point(1, 3), new Point(2, 2));
		
		Set<PoliLine> results = SeidelTrapezoidation.run(diamond);
		
		Set<PoliLine> expected = new HashSet<PoliLine>();
		expected.add(new PoliLine(new Point(1, 0), new Point(0, 1), new Point(2, 2)));
		expected.add(new PoliLine(new Point(2, 2), new Point(0, 1), new Point(1, 3)));

		assertEquals("Trapezoidation failed!", expected, results);
	}
	
	@Test
	public void hart() {
		PoliLine hart = new PoliLine(
				new Point(1, 0), new Point(0, 3), new Point(1, 6), 
				new Point(4, 4), new Point(3, 1), new Point(2, 2));
		
		Set<PoliLine> results = SeidelTrapezoidation.run(hart);
		
		Set<PoliLine> expected = new HashSet<PoliLine>();
		expected.add(new PoliLine(new Point(2, 2), new Point(1, 0), new Point(0, 3)));
		expected.add(new PoliLine(new Point(4, 4), new Point(3, 1), new Point(2, 2), new Point(0, 3)));
		expected.add(new PoliLine(new Point(4, 4), new Point(0, 3), new Point(1, 6)));

		assertEquals("Trapezoidation failed!", expected, results);
	}
	
}
