package ro.ddanciu.finite.elements.api;

import java.math.BigDecimal;

/**
 * 
 * @author dan
 *
 * ax  + by + c = 0
 */
public class Line2 {
	
	public class Factory {
		
		public Line2 byPoints(Point p1, Point p2) {
			return null;
		}
	}
	
	private BigDecimal a;
	
	private BigDecimal b;
	
	private BigDecimal c;

}
