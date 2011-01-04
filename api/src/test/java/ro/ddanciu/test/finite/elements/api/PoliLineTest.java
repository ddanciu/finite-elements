package ro.ddanciu.test.finite.elements.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Test;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.PoliLine;
import ro.ddanciu.finite.elements.api.Segment;

/**
 * @author dan
 *
 */
public class PoliLineTest {
	
	private PoliLine triangle = new PoliLine(new Point(0, 0), new Point(0, 1), new Point(1, 0));
	private PoliLine square = new PoliLine(new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 0));

	@Test
	public void createBySegments() {
		
		PoliLine newTriangle;
		
		newTriangle = new PoliLine(
					new Segment(new Point(0, 0), new Point(0, 1)),
					new Segment(new Point(0, 1), new Point(1, 0)),
					new Segment(new Point(1, 0), new Point(0, 0))
				);
		
		assertEquals("Basic create failed!", triangle, newTriangle);
		

		newTriangle = new PoliLine(
					new Segment(new Point(0, 1), new Point(0, 0)),
					new Segment(new Point(0, 1), new Point(1, 0)),
					new Segment(new Point(1, 0), new Point(0, 0))
				);
		
		assertEquals("Mixed start failed!", triangle, newTriangle);
		

		newTriangle = new PoliLine(
					new Segment(new Point(0, 0), new Point(0, 1)),
					new Segment(new Point(1, 0), new Point(0, 1)),
					new Segment(new Point(1, 0), new Point(0, 0))
				);
		
		assertEquals("Mixed points failed!", triangle, newTriangle);
	}
	

	@Test
	public void segmentsOfTriangle() {
		
		Segment[] triangleSegments = new Segment[3];
		int i = 0;
		for (Segment s : triangle.segments()) {
			triangleSegments[i++] = s;
		}
		
		assertEquals("Segments failed: " + triangleSegments, triangle, new PoliLine(triangleSegments));
		
	}

	@Test
	public void segmentsOfSquare() {
		
		Segment[] squareSegments = new Segment[4];
		int i = 0;
		for (Segment s : square.segments()) {
			assertNotNull(s);
			squareSegments[i++] = s;
		}
		assertEquals("Not all segments received: " + Arrays.toString(squareSegments), 4, i);
		assertEquals("Segments failed: " + Arrays.toString(squareSegments), square, new PoliLine(squareSegments));
		
	}

	@Test
	public void testContainsSegment() {
		assertTrue(triangle.containsSegment(new Segment(new Point(1, 0), new Point(0, 1))));
		assertFalse(triangle.containsSegment(new Segment(new Point(1, 2), new Point(0, 1))));
	}
	
}