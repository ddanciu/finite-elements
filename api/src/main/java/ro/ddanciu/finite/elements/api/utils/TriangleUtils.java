package ro.ddanciu.finite.elements.api.utils;

import static java.lang.String.format;
import ro.ddanciu.finite.elements.api.Point;
import ro.ddanciu.finite.elements.api.Segment;
import ro.ddanciu.finite.elements.api.Triangle;

public class TriangleUtils {


	public static Segment segmentInCommon(Triangle a, Triangle b) {

		Point[] pointsInCommon = new Point[2];
		int i = 0;
		try {
			for (Point x : a) {
				for (Point y : b) {
					if (x.equals(y)) {
						pointsInCommon[i++] = x;
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException(format("The given triangles are equal! <%s,%s>", a, b));
		}
		
		Segment segment;
		if (i == 2) {
			segment = new Segment(pointsInCommon[0], pointsInCommon[1]);
		} else {
			segment = null;
		}
		return segment;
	}
}
