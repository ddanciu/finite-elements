package ro.ddanciu.test.finite.elements.api;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.Segment;

public class PointTest {
	
	@Test
	public void test() {
		Segment segment = new Segment(new Point(new BigDecimal("895.1250"), 	new BigDecimal("1233.0000")),
				new Point(new BigDecimal("1568.5625"), 	new BigDecimal("998.4375")));
		
		Assert.assertEquals("Wrong length", new BigDecimal("713.1182"), segment.length());
	}

}
