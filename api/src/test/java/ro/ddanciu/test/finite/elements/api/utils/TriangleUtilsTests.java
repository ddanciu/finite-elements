package ro.ddanciu.test.finite.elements.api.utils;


import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.Segment;
import ro.ddanciu.finite.elements.api.Triangle;
import ro.ddanciu.finite.elements.api.utils.TriangleUtils;

public class TriangleUtilsTests {
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
	public void centerRightAngleTrinagles() {
		
		Triangle rightAngleTriangle = new Triangle(new Point(1, 1), new Point(-1, 1), new Point(0, 0));
		Point ic = rightAngleTriangle.incenter();
		Assert.assertEquals("Incenter of 90 angle triangle failed", new Point(0, 0.5858), ic);
		
	}


	@Test
	public void centerEchilateralTrinagles() {
		
		Triangle echilateral = new Triangle(
				new Point(new BigDecimal("0"),		new BigDecimal("1.73205081")), 
				new Point(new BigDecimal("-1.5"), 	new BigDecimal("-0.86602540")),
				new Point(new BigDecimal("1.5"), 	new BigDecimal("-0.86602540")));
		
		Point ic = echilateral.incenter();
		
		Assert.assertEquals("Incenter of echilateral triangle failed", new Point(0, 0), ic);
		
	}


	@Test
	public void centerRandomTrinagles() {
		
		Triangle triangle = new Triangle(
				new Point(new BigDecimal("1231.8750"),	new BigDecimal("1516.0000")), 
				new Point(new BigDecimal("895.1250"), 	new BigDecimal("1233.0000")),
				new Point(new BigDecimal("1568.5625"), 	new BigDecimal("998.4375")));
		
		Point ic = triangle.incenter();
		
		Assert.assertEquals("Incenter of echilateral triangle failed", 
				new Point(new BigDecimal("1198.0855"), new BigDecimal("1288.7121")), ic);
		
	}
	
	
}
