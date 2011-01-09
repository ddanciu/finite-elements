package ro.ddanciu.test.finite.elements.api;


import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.Triangle;

public class TriangleTest {
	
	
	@Test
	public void equals() {
		
		Triangle one = new Triangle(new Point(0, 1), new Point(1, 0), new Point(1, 1));
		Triangle two = new Triangle(new Point(1, 0), new Point(0, 1), new Point(1, 1));
		
		assertTrue("Triangle equals fail!", one.equals(two));
		assertTrue("Triangle equals fail!", two.equals(one));
		
	}

}
