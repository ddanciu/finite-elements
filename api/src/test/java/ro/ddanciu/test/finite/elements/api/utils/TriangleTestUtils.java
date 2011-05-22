package ro.ddanciu.test.finite.elements.api.utils;


import static java.lang.String.format;

import java.math.BigDecimal;

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

	@Test
	public void circumcenterRightAngleTrinagles() {
		
		Point c0 = TriangleUtils.circumcenter(new Triangle(new Point(1, 1), new Point(-1, 1), new Point(0, 0)));
		Assert.assertEquals("Center of 90 angle triangle failed", new Point(0, 1), c0);
		
	}
	

	@Test
	public void circumcenterEchilateralTrinagles() {
		
		Point c0 = TriangleUtils.circumcenter(new Triangle(
				new Point(new BigDecimal("0"),		new BigDecimal("1.73205081")), 
				new Point(new BigDecimal("-1.5"), 	new BigDecimal("-0.86602540")),
				new Point(new BigDecimal("1.5"), 	new BigDecimal("-0.86602540"))));
		
		System.out.println(format("c = %s", c0));
		System.out.println(format("cx = %f", c0.getX().doubleValue()));
		System.out.println(format("cy = %f", c0.getY().doubleValue()));
		
		Assert.assertEquals("Center of 90 angle triangle failed", new Point(0, 0), c0);
		
	}
	
	
}
