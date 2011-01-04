package ro.ddanciu.test.finite.elements.api;


import junit.framework.Assert;

import org.junit.Test;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.Segment;

/**
 * @author dan
 */
public class SegmentsIntersectionTest {

	
	@Test
	public void paralels() {
		Segment s1 = new Segment(new Point(0, 0), new Point(0, 1));
		Segment s2 = new Segment(new Point(1, 0), new Point(1, 1));
		
		Assert.assertFalse("Ouch!", s1.intersects(s2));
		Assert.assertFalse("Ouch!", s2.intersects(s1));
	}

	
	@Test
	public void onOx() {
		Segment s1 = new Segment(new Point(0, 0), new Point(0, 1));
		Segment s2 = new Segment(new Point(0, 2), new Point(0, 3));
		
		Assert.assertFalse("Ouch!", s1.intersects(s2));
		Assert.assertFalse("Ouch!", s2.intersects(s1));
	}
	

	
	@Test
	public void onOy() {
		Segment s1 = new Segment(new Point(0, 0), new Point(1, 0));
		Segment s2 = new Segment(new Point(2, 0), new Point(3, 0));
		
		Assert.assertFalse("Ouch!", s1.intersects(s2));
		Assert.assertFalse("Ouch!", s2.intersects(s1));
	}
	

	
	@Test
	public void onSameLine() {
		Segment s1 = new Segment(new Point(0, 0), new Point(1, 1));
		Segment s2 = new Segment(new Point(2, 2), new Point(3, 3));
		
		Assert.assertFalse("Ouch!", s1.intersects(s2));
		Assert.assertFalse("Ouch!", s2.intersects(s1));
	}
	

	@Test
	public void sameSegement() {
		Segment s1 = new Segment(new Point(0, 0), new Point(0, 1));
		Segment s2 = new Segment(new Point(0, 0), new Point(0, 1));
		
		Assert.assertTrue("Ouch!", s1.intersects(s2));
		Assert.assertTrue("Ouch!", s2.intersects(s1));
	}

	
	@Test
	public void overlapOx() {
		Segment s1 = new Segment(new Point(0, 0), new Point(0, 2));
		Segment s2 = new Segment(new Point(0, 1), new Point(0, 3));
		
		Assert.assertTrue("Ouch!", s1.intersects(s2));
		Assert.assertTrue("Ouch!", s2.intersects(s1));
	}

	
	@Test
	public void overlapOy() {
		Segment s1 = new Segment(new Point(0, 0), new Point(2, 0));
		Segment s2 = new Segment(new Point(1, 0), new Point(3, 0));
		
		Assert.assertTrue("Ouch!", s1.intersects(s2));
		Assert.assertTrue("Ouch!", s2.intersects(s1));
	}

	
	@Test
	public void overlap() {
		Segment s1 = new Segment(new Point(0, 0), new Point(2, 2));
		Segment s2 = new Segment(new Point(1, 1), new Point(3, 3));
		
		Assert.assertTrue("Ouch!", s1.intersects(s2));
		Assert.assertTrue("Ouch!", s2.intersects(s1));
	}

}
