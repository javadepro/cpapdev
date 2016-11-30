/**
 * 
 */
package com.esofa.crm.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author JHa
 *
 */
public class MathUtils {

	private static final BigDecimal CENT_1 = new BigDecimal(0.01).setScale(2,BigDecimal.ROUND_HALF_UP);
	private static final BigDecimal CENT_2 = new BigDecimal(0.02).setScale(2,BigDecimal.ROUND_HALF_UP);
	
	private static final BigDecimal CENT_6 = new BigDecimal(0.06).setScale(2,BigDecimal.ROUND_HALF_UP);
	private static final BigDecimal CENT_7 = new BigDecimal(0.07).setScale(2,BigDecimal.ROUND_HALF_UP);		
	private static final BigDecimal CENT_3 = new BigDecimal(0.03).setScale(2,BigDecimal.ROUND_HALF_UP);
	private static final BigDecimal CENT_4 = new BigDecimal(0.04).setScale(2,BigDecimal.ROUND_HALF_UP);
	
	private static final BigDecimal CENT_8 = new BigDecimal(0.08).setScale(2,BigDecimal.ROUND_HALF_UP);
	private static final BigDecimal CENT_9 = new BigDecimal(0.09).setScale(2,BigDecimal.ROUND_HALF_UP);
	
	private static final BigDecimal CENT_5 = new BigDecimal(0.05).setScale(2,BigDecimal.ROUND_HALF_UP);
	
	private static final BigDecimal CENT_10 = new BigDecimal (0.10).setScale(2,BigDecimal.ROUND_HALF_UP);
	public static final BigDecimal ZERO = new BigDecimal (0.00).setScale(2,BigDecimal.ROUND_HALF_UP);

	public static final int NUM_SCALE =2;
	public static final int ROUND_MODE = BigDecimal.ROUND_HALF_UP;
	
	
	public static BigDecimal pennyRound(BigDecimal value) {
		
		boolean isNegative = value.signum() == -1;
		BigDecimal tenth = value.abs().movePointRight(1);
		tenth= tenth.setScale(2,BigDecimal.ROUND_HALF_UP);
		
		BigDecimal tenth_noCent = new BigDecimal(tenth.toBigInteger());
		
		BigDecimal cent = tenth.subtract(tenth_noCent).movePointLeft(1).setScale(2);
		
		//0.01, 0.02 down.  0.06 0.07 down
		//0.03, 0.04 up.  0.08, 0.09 up.
		
		if (CENT_1.compareTo(cent) ==0 || CENT_2.compareTo(cent) ==0 || ZERO.setScale(2).compareTo(cent) ==0) {
			
			//round down to zero.
			cent = ZERO;
			
		} else if (CENT_3.compareTo(cent) == 0 || CENT_4.compareTo(cent) == 0 || CENT_5.compareTo(cent) == 0
				|| CENT_6.compareTo(cent) ==0 || CENT_7.compareTo(cent) ==0) {
			
			cent = CENT_5;
			
		} else {
		
	
			cent = CENT_10;
		}

		
		BigDecimal retValB = tenth_noCent.movePointLeft(1).add(cent).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		if (isNegative) { retValB = retValB.negate(); }
		
		return retValB;
		
	}
	
	public static boolean acceptableCashAmt(BigDecimal value) {
		
		boolean result = false;
		
		if (value == null) {
			return false;
		} 

		BigDecimal wholeNumber = value.scaleByPowerOfTen(2).setScale(0);		
		BigDecimal remainder = wholeNumber.remainder(new BigDecimal(5));
		
		return (remainder.equals(BigDecimal.ZERO));
	}

	public static BigDecimal setBDScale(BigDecimal value) {
		
		if (value == null) { return value;}
		return value.setScale(NUM_SCALE,RoundingMode.HALF_UP);
		
	}

	public static BigDecimal toBD(Float f) {
		
		if (f == null) { return null;}		
		return setBDScale(new BigDecimal(f.floatValue()));
	}

	//is zero as far as money is concerned
	public static boolean isZero(BigDecimal value) {
		
		if (ZERO.equals(value)) {
			
			return true;
		} else if (ZERO.equals(value.setScale(2, BigDecimal.ROUND_HALF_UP))) {
			
			return true;
		}
		
		return false;
	}
}
