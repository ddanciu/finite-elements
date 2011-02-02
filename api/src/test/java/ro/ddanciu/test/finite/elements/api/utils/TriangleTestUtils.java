package ro.ddanciu.test.finite.elements.api.utils;


import org.junit.Assert;
import org.junit.Test;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.Segment;
import ro.ddanciu.finite.elements.api.Triangle;
import ro.ddanciu.finite.elements.api.utils.TriangleUtils;

public class TriangleTestUtils {
	private Triangle t1 = new Triangle(new Point(1, 0), new Point(0, 1), new Point(1, 1));
	private Triangle t2 = new Triangle(new Point(0, 1), new Point(1, 1), new Point(1, 2));
	private Triangle t3 = new Triangle(new Point(1, 0), new Point(2, 0), new Point(2, 1));
	
	@Test
	public void disjunct() {
		Assert.assertNull("Disjunct triangles failed!", TriangleUtils.segmentInCommon(t2, t3));
	}
	
	@Test
	public void commonPoint() {
		Assert.assertNull("Common point failed!", TriangleUtils.segmentInCommon(t1, t3));
	}
	
	@Test
	public void segment() {
		Segment segmentInCommon = TriangleUtils.segmentInCommon(t1, t2);
		Assert.assertNotNull("Disjunct triangles failed!", segmentInCommon);
		Assert.assertEquals("Segment in common wrong!", new Segment(new Point(0, 1), new Point(1, 1)), segmentInCommon);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void same() {
		TriangleUtils.segmentInCommon(t1, t1);
	}
}
