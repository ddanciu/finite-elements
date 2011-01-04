package ro.ddanciu.test.finite.elements.api;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.Test;

/**
 * @author dan
 *
 */
public class BigDecimalTest {
	
	@Test
	public void imporatant() {
		int divisor = 3;
		BigDecimal x = BigDecimal.ONE.divide(new BigDecimal(divisor), MathContext.DECIMAL128);
		BigDecimal oneABe = x.multiply(new BigDecimal(divisor), MathContext.DECIMAL128);
		oneABe = oneABe.setScale(4, RoundingMode.HALF_EVEN);
		assertTrue("Precision lost! 1'a be: " + oneABe, BigDecimal.ONE.compareTo(oneABe) == 0);
	}
	
	@Test
	public void lessImportant() {
		int divisor = 13;
		double x = BigDecimal.ONE.divide(new BigDecimal(divisor), MathContext.DECIMAL128).doubleValue();
		BigDecimal oneABe = new BigDecimal(x).multiply(new BigDecimal(divisor), MathContext.DECIMAL128);
		oneABe = oneABe.setScale(4, RoundingMode.FLOOR);
		assertTrue("Precision lost! 1'a be: " + oneABe, BigDecimal.ONE.compareTo(oneABe) == 0);
	}

}
