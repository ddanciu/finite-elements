package ro.ddanciu.test.finite.elements.api;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.PoliLine;

/**
 * @author dan
 */
public class PoliLineEqualsTest {

	@Test
	public void aLine() {
		PoliLine one = new PoliLine(new Point(0, 0), new Point(1, 1));
		PoliLine two= new PoliLine(new Point(1, 1), new Point(0, 0));
		
		assertEquals("PoliLines of a segment don't match!", one, two);
	}

	@Test
	public void aTriangleSqued() {
		PoliLine one = new PoliLine(new Point(0, 0), new Point(0, 1), new Point(1, 0));
		PoliLine two= new PoliLine(new Point(0, 1), new Point(1, 0), new Point(0, 0));
		
		assertEquals("PoliLines of a triangle don't match!", one, two);
	}

	@Test
	public void aTriangle() {
		PoliLine one = new PoliLine(new Point(0, 0), new Point(0, 1), new Point(1, 0));
		PoliLine two = new PoliLine(new Point(0, 0), new Point(0, 1), new Point(1, 0));
		
		assertEquals("PoliLines of a triangle don't match!", one, two);
	}

	@Test
	public void anotherTriangle() {
		PoliLine one = new PoliLine(new Point(2, 2), new Point(0, 1), new Point(1, 3));
		PoliLine two= new PoliLine(new Point(0, 1), new Point(1, 3), new Point(2, 2));
		
		assertEquals("PoliLines of a triangle don't match!", one, two);
	}

	@Test
	public void anotherTriangleNot() {
		PoliLine one = new PoliLine(new Point(2, 2), new Point(1, 0), new Point(0, 1));
		PoliLine two= new PoliLine(new Point(0, 1), new Point(1, 3), new Point(2, 2));

		assertFalse("PoliLines match, but shoudn't!", one.equals(two));
	}

	@Test
	public void a4PointsPoliLine() {
		PoliLine one = new PoliLine(new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 0));
		PoliLine two= new PoliLine(new Point(1, 1), new Point(1, 0), new Point(0, 0), new Point(0, 1));
		
		assertEquals("PoliLines of a segment don't match!", one, two);
	}

	@Test
	public void a4PointsPoliLineNot() {
		PoliLine one = new PoliLine(new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 0));
		PoliLine two= new PoliLine(new Point(1, 1), new Point(0, 0), new Point(1, 0), new Point(0, 1));
		
		assertFalse("PoliLines match, but shoudn't!", one.equals(two));
	}
}
