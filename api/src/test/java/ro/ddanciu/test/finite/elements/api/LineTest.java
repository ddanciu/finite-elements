package ro.ddanciu.test.finite.elements.api;

import static java.lang.String.format;
import static org.junit.Assert.assertTrue;
import static ro.ddanciu.finite.elements.api.Constants.MY_CNTX;
import static ro.ddanciu.finite.elements.api.Constants.MY_RND;
import static ro.ddanciu.finite.elements.api.Constants.MY_SCALE;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

import ro.ddanciu.finite.elements.api.Line;
import ro.ddanciu.finite.elements.api.Point;

public class LineTest {
	
	@Test
	public void distance() {
		assertDistance(new Point(0, 0), new Point(1, 0), new Point(2, 0), 0);
		
		assertDistance(new Point(0, 0), new Point(1, 0), new Point(0.5, 1), 1);
		assertDistance(new Point(0, 0), new Point(1, 0), new Point(2.5, 1), 1);
		assertDistance(new Point(0, 0), new Point(1, 0), new Point(-3.5, 1), 1);

		assertDistance(new Point(0, 0), new Point(0, 1), new Point(0, 1), 0);
		assertDistance(new Point(0, 0), new Point(0, 1), new Point(-1, 1), 1);
		assertDistance(new Point(0, 0), new Point(0, 1), new Point(7.56, 1), 7.56);

		assertDistance(new Point(1, 1), new Point(3, 3), new Point(2, 2), 0);
		assertDistance(new Point(1, 1), new Point(3, 3), new Point(2, 0), Math.sqrt(2));
		assertDistance(new Point(0, 0), new Point(1, 2), new Point(1, 0), 
				new BigDecimal(2).divide(new BigDecimal(Math.sqrt(5)), MathContext.DECIMAL32).round(MY_CNTX).doubleValue());
	}
	
	private void assertDistance(Point d1, Point d2, Point to, double dist) {
		
		BigDecimal computed = Line.defineByPoints(d1, d2).distance(to);
		BigDecimal expected = new BigDecimal(dist).setScale(MY_SCALE, MY_RND);
		
		assertTrue(format("Distance not OK; expected: %s, computed %s", expected, computed), expected.compareTo(computed) == 0);
		
	}

}
