/**
 * 
 */
package ro.ddanciu.finite.elements.api;

import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author dan
 *
 */
public final class Constants {
	
	public static final RoundingMode MY_RND = RoundingMode.HALF_EVEN;

	public static final int MY_SCALE = 4;

	public static final MathContext MY_CNTX = new MathContext(MY_SCALE, MY_RND);

}
