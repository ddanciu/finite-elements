package ro.ddanciu.test.finite.elements.algorithms;

import static junit.framework.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ro.ddanciu.finite.elements.algorithms.CornerFanDecomposition;
import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.PoliLine;
import ro.ddanciu.finite.elements.api.Triangle;

public class CornerFanDecompositionTest {
	
	@Test
	public void triangle() {

		Triangle triangle = new Triangle(
				new Point(0, 0), 
				new Point(1, 1), 
				new Point(1, 2));
		
		CornerFanDecomposition algorithm = new CornerFanDecomposition();
		algorithm.setPoliLine(triangle);
		
		Set<Triangle> results;
		
		results= algorithm.decompose(0);
		assertEquals("Not as many as expected!", 1, results.size());
		assertEquals("Triangle not returned back", triangle, results.iterator().next());
		
		results = algorithm.decompose(1);
		assertEquals("Not as many as expected!", 1, results.size());
		assertEquals("Triangle not returned back", triangle, results.iterator().next());
		
		results = algorithm.decompose(2);
		assertEquals("Not as many as expected!", 1, results.size());
		assertEquals("Triangle not returned back", triangle, results.iterator().next());
	}

	
	@Test
	public void diamond() {
		PoliLine diamond = new PoliLine(
				new Point(1, 0), new Point(0, 1), 
				new Point(1, 3), new Point(2, 2));
		
		CornerFanDecomposition algorithm = new CornerFanDecomposition();
		algorithm.setPoliLine(diamond);

		Set<Triangle> results;
		Set<Triangle> expected;
		
		results= algorithm.decompose(0);
		expected = new HashSet<Triangle>();
		expected.add(new Triangle(new Point(1, 0), new Point(0, 1), new Point(1, 3)));
		expected.add(new Triangle(new Point(2, 2), new Point(1, 0), new Point(1, 3)));
		assertEquals("Decomposition failed!", expected, results);
		
		results= algorithm.decompose(1);
		expected = new HashSet<Triangle>();
		expected.add(new Triangle(new Point(0, 1), new Point(1, 3), new Point(2, 2)));
		expected.add(new Triangle(new Point(0, 1), new Point(2, 2), new Point(1, 0)));
		assertEquals("Decomposition failed!", expected, results);
		
		results= algorithm.decompose(2);
		expected = new HashSet<Triangle>();
		expected.add(new Triangle(new Point(1, 0), new Point(0, 1), new Point(1, 3)));
		expected.add(new Triangle(new Point(2, 2), new Point(1, 0), new Point(1, 3)));
		assertEquals("Decomposition failed!", expected, results);
		
		results= algorithm.decompose(3);
		expected = new HashSet<Triangle>();
		expected.add(new Triangle(new Point(0, 1), new Point(1, 3), new Point(2, 2)));
		expected.add(new Triangle(new Point(0, 1), new Point(2, 2), new Point(1, 0)));
		assertEquals("Decomposition failed!", expected, results);
	}
	

	@Test
	public void complex() {
		PoliLine diamond = new PoliLine(
				new Point(2, 0), new Point(1, 1), new Point(0, 3), 
				new Point(3, 5), new Point(5, 4));
		
		CornerFanDecomposition algorithm = new CornerFanDecomposition();
		algorithm.setPoliLine(diamond);

		Set<Triangle> results;
		Set<Triangle> expected;
		
		results= algorithm.decompose(1);
		
		expected = new HashSet<Triangle>();
		expected.add(new Triangle(new Point(1, 1), new Point(0, 3), new Point(3, 5))); 
		expected.add(new Triangle(new Point(1, 1), new Point(3, 5), new Point(5, 4)));
		expected.add(new Triangle(new Point(1, 1), new Point(5, 4), new Point(2, 0)));
		
		assertEquals("Decomposition failed!", expected, results);
	}
}
