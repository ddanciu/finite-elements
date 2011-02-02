package ro.ddanciu.finite.elements.api.utils;

import java.math.BigDecimal;

public class MathUtils {
	
	public static BigDecimal max(BigDecimal... in) {
		BigDecimal max = null;
		for (BigDecimal c : in) {
			if (max == null) {
				max = c;
				continue;
			} else if (max.compareTo(c) < 0) {
				max = c;
			}
		}
		return max;
	}
	
	public static BigDecimal min(BigDecimal... in) {
		BigDecimal min = null;
		for (BigDecimal c : in) {
			if (min == null) {
				min = c;
				continue;
			} else if (min.compareTo(c) > 0) {
				min = c;
			}
		}
		return min;
	}

}
