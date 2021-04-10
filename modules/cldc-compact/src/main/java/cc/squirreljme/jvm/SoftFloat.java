// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.runtime.cldc.util.UnsignedInteger;

/**
 * Software math operations on 32-bit floats.
 * 
 * This source file uses parts of the Berkeley SoftFloat Release 3e library,
 * converted into Java. See the 3rd party licenses documentation.
 *
 * @since 2019/05/24
 */
@SuppressWarnings({"CommentedOutCode", "MagicNumber"})
public final class SoftFloat
{
	/** The sign mask. */
	public static final int SIGN_MASK =
		0b1000_0000_0000_0000__0000_0000_0000_0000;
	
	/** Exponent Mask. */
	public static final int EXPONENT_MASK =
		0b0111_1111_1000_0000__0000_0000_0000_0000;
	
	/** Fraction Mask. */
	public static final int FRACTION_MASK =
		0b0000_0000_0111_1111__1111_1111_1111_1111;
	
	/** The mask for NaN values. */
	public static final int NAN_MASK =
		0b0111_1111_1000_0000__0000_0000_0000_0000;
	
	/** Exponent shift. */
	private static final byte _EXP_SHIFT = 
		23;
	
	/** Default NaN value. */
	public static final int DEFAULT_NAN =
		0xFFC0_0000;
	
	/**
	 * Not used.
	 *
	 * @since 2019/05/24
	 */
	private SoftFloat()
	{
	}
	
	/**
	 * Adds two values.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float add(int __a, int __b)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two values, NaN returns {@code -1}.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SuppressWarnings("SpellCheckingInspection")
	public static int cmpl(int __a, int __b)
	{
		if (SoftFloat.isNaN(__a) || SoftFloat.isNaN(__b))
			return -1;
		
		return SoftFloat.__cmp(__a, __b);
	}
	
	/**
	 * Compares two values, NaN returns {@code 1}.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SuppressWarnings("SpellCheckingInspection")
	public static int cmpg(int __a, int __b)
	{
		if (SoftFloat.isNaN(__a) || SoftFloat.isNaN(__b))
			return 1;
		
		return SoftFloat.__cmp(__a, __b);
	}
	
	/**
	 * Divides two values.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float div(int __a, int __b)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Is this Not a Number?
	 * 
	 * @param __a The value to check.
	 * @return If this is not a number.
	 * @since 2021/04/07
	 */
	public static boolean isNaN(int __a)
	{
		return SoftFloat.NAN_MASK == (__a & SoftFloat.NAN_MASK);
	}
	
	/**
	 * Multiplies two values.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float mul(int __a, int __b)
	{
		// First value
		boolean signA = SoftFloat.__signF32UI(__a);
		int expA = SoftFloat.__expF32UI(__a);
		int sigA = SoftFloat.__fracF32UI(__a);
		
		// Second value
		boolean signB = SoftFloat.__signF32UI(__b);
		int expB = SoftFloat.__expF32UI(__b);
		int sigB = SoftFloat.__fracF32UI(__b);
		
		// Will this result in a negative value?
		boolean signZ = signA ^ signB;
		
		boolean returnInfinite = false;
		int magBits = 0;
		if (expA == 0xFF)
		{
			// if ( sigA || ((expB == 0xFF) && sigB) )
			if (sigA != 0 || ((expB == 0xFF) && sigB != 0))
				return Float.intBitsToFloat(
					SoftFloat.__propagateNaNF32UI(__a, __b));
			
			magBits = expB | sigB;
			returnInfinite = true;
		}
		
		if (!returnInfinite && expB == 0xFF)
		{
			// if ( sigB )
			if (sigB != 0)
				return Float.intBitsToFloat(
					SoftFloat.__propagateNaNF32UI(__a, __b));
			
			magBits = expA | sigA;
			returnInfinite = true;
		}
		
		// Returning infinite value?
		if (returnInfinite)
		{
			// if ( ! magBits )
			if (magBits == 0)
				return SoftFloat.DEFAULT_NAN;
			return SoftFloat.__packToF32UI(signZ, 0xFF, 0);
		}
		
		// if ( ! expA )
		if (expA == 0)
		{
			// if ( ! sigA )
			if (sigA == 0)
				return SoftFloat.__packToF32UI(signZ, 0, 0);
				
			long normExpSig = SoftFloat.__normSubnormalF32Sig(sigA);
			expA = Assembly.longUnpackHigh(normExpSig);
			sigA = Assembly.longUnpackLow(normExpSig);
		}
		
		// if ( ! expB )
		if (expB == 0)
		{
			// if ( ! sigB )
			if (sigB == 0)
				return SoftFloat.__packToF32UI(signZ, 0, 0);
				
			long normExpSig = SoftFloat.__normSubnormalF32Sig(sigB);
			expB = Assembly.longUnpackHigh(normExpSig);
			sigB = Assembly.longUnpackLow(normExpSig);
		}
		
		int expZ = expA + expB - 0x7F;
		sigA = (sigA | 0x00800000) << 7;
		sigB = (sigB | 0x00800000) << 8;
		
		// sigZ = softfloat_shortShiftRightJam64(
		//     (uint_fast64_t)sigA * sigB, 32);
		int sigZ = (int)SoftFloat.__shortShiftRightJam64(
			(long)sigA * sigB, 32);
		
		// if ( sigZ < 0x40000000 )
		if (UnsignedInteger.compareUnsigned(sigZ, 0x40000000) < 0)
		{
			--expZ;
			sigZ <<= 1;
		}
		
		return SoftFloat.__roundPackToF32(signZ, expZ, sigZ);
	}
	
	/**
	 * Negates a value.
	 *
	 * @param __a A.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float neg(int __a)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Ors a value, used for constant loading.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float or(int __a, int __b)
	{
		return Assembly.intBitsToFloat(__a | __b);
	}
	
	/**
	 * Remainders a value.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float rem(int __a, int __b)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Subtracts values.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float sub(int __a, int __b)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to double.
	 *
	 * @param __a A.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static double toDouble(int __a)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to integer.
	 *
	 * @param __a A.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static int toInteger(int __a)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to long.
	 *
	 * @param __a A.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long toLong(int __a)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two values.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2021/04/07
	 */
	private static int __cmp(int __a, int __b)
	{
		// Equality, note second means -0 == 0
		// return (uiA == uiB) || ! (uint32_t) ((uiA | uiB)<<1);
		if (__a == __b || ((__a | __b) << 1) == 0)
			return 0;
		
		// Less than
		// (signA != signB) ? signA && ((uint32_t) ((uiA | uiB)<<1) != 0)
		// : (uiA != uiB) && (signA ^ (uiA < uiB));
		boolean signA = (0 != (__a & SoftFloat.SIGN_MASK));
		boolean signB = (0 != (__b & SoftFloat.SIGN_MASK));
		if (signA != signB)
		{
			// signA && ((uint32_t) ((uiA | uiB)<<1) != 0)
			if (signA && ((__a | __b) << 1) != 0)
				return -1;
		}
		
		// (uiA != uiB) && (signA ^ (uiA < uiB))
		// ^^^ const ^^
		else if (signA ^ (UnsignedInteger.compareUnsigned(__a, __b) < 0))
			return -1;
		
		// Anything else assume greater than
		return 1;
	}
	
	/**
	 * Returns the exponent.
	 * 
	 * @param __a The float to read from.
	 * @return The exponent.
	 * @since 2021/04/10
	 */
	private static int __expF32UI(int __a)
	{
		return (((__a) >> SoftFloat._EXP_SHIFT) & 0xFF);
	}
	
	/**
	 * Returns the fraction/significand from the floating point value.
	 * 
	 * @param __a The float to read from.
	 * @return The fraction/significand.
	 * @since 2021/04/10
	 */
	private static int __fracF32UI(int __a)
	{
		return (__a & SoftFloat.FRACTION_MASK);
	}
	
	/**
	 * Gets if this is a NaN.
	 * 
	 * @param __a The value to check.
	 * @return If this is a NaN.
	 * @since 2021/04/10
	 */
	private static boolean __isNaNF32UI(int __a)
	{
		return ((~(__a) & 0x7F800000) == 0) &&
			((__a) & 0x007FFFFF) != 0;
	}
	
	/**
	 * Gets if this is a signaling NaN.
	 * 
	 * @param __a The value to check.
	 * @return If this is a signaling NaN.
	 * @since 2021/04/10
	 */
	private static boolean __isSigNaNF32UI(int __a)
	{
		return ((__a & 0x7FC00000) == 0x7F800000) &&
			(__a & 0x003FFFFF) != 0;
	}
	
	/**
	 * Normalized round packed to 32-bit float.
	 * 
	 * @param __sign The sign.
	 * @param __exp The exponent.
	 * @param __sig The significand.
	 * @return The resultant value.
	 * @since 2021/04/08
	 */
	protected static int __normRoundPackToF32(boolean __sign, int __exp,
		int __sig)
	{
		int shiftDist;
		
		// shiftDist = softfloat_countLeadingZeros32( __sig ) - 1;
		shiftDist = Integer.numberOfLeadingZeros(__sig) - 1;
		__exp -= shiftDist;
		
		// if ( (7 <= shiftDist) && ((unsigned int) exp < 0xFD) ) {
		if (7 <= shiftDist &&
			UnsignedInteger.compareUnsigned(__exp, 0xFD) < 0)
		{
			// uZ.ui = packToF32UI( sign, sig ? exp : 0, sig<<(shiftDist - 7));
			return SoftFloat.__packToF32UI(__sign,
				(__sig != 0 ? __exp : 0),
				__sig << (shiftDist - 7));
		}
		
		// return softfloat_roundPackToF32( sign, exp, sig<<shiftDist );
		return SoftFloat.__roundPackToF32(__sign, __exp,
			__sig << shiftDist);
	}
	
	/**
	 * Normalizes a subnormal 32-bit float significand. 
	 * 
	 * @param __sig The significand.
	 * @return The normalized value, the exponent is the high value and
	 * the significand is the low value.
	 * @since 2021/04/10
	 */
	private static long __normSubnormalF32Sig(int __sig)
	{
		// softfloat_countLeadingZeros32( sig ) - 8;
		int shiftDist = Integer.numberOfLeadingZeros(__sig) - 8;
		
		// struct exp16_sig32 { int_fast16_t exp; uint_fast32_t sig; };
		// exp = 1 - shiftDist ,, sig = sig<<shiftDist
		return Assembly.longPack(1 - shiftDist, __sig << shiftDist);
	}
	
	/**
	 * Packs value to an unsigned integer.
	 * 
	 * @param __sign Sign bit.
	 * @param __exp Exponent.
	 * @param __sig Significand.
	 * @return The packed value.
	 * @since 2021/04/08
	 */
	protected static int __packToF32UI(boolean __sign, int __exp, int __sig)
	{
		// (((uint32_t) (sign)<<31) + ((uint32_t) (exp)<<23) + (sig))
		return (__sign ? SoftFloat.SIGN_MASK : 0) + ((__exp) << 23) + (__sig);
	}
	
	/**
	 * Propagates the given NaN values.
	 * 
	 * @param __a The first value.
	 * @param __b The second value.
	 * @return The propagated NaN.
	 * @since 2021/04/10
	 */
	private static int __propagateNaNF32UI(int __a, int __b)
	{
		boolean isSigNaNA = SoftFloat.__isSigNaNF32UI(__a);
		boolean isSigNaNB = SoftFloat.__isSigNaNF32UI(__b);
		
		// Make NaNs non-signaling.
		int uiNonSigA = __a | 0x00400000;
		int uiNonSigB = __b | 0x00400000;
		
		// Are either of these signaling?
		if (isSigNaNA | isSigNaNB)
		{
			if (isSigNaNA)
			{
				if (!isSigNaNB)
					return SoftFloat.__isNaNF32UI(__b) ? uiNonSigB : uiNonSigA;
			}
			else
				return SoftFloat.__isNaNF32UI(__a) ? uiNonSigA : uiNonSigB;
		}
		
		int uiMagA = __a & 0x7FFFFFFF;
		int uiMagB = __b & 0x7FFFFFFF;
		
		if (UnsignedInteger.compareUnsigned(uiMagA, uiMagB) < 0)
			return uiNonSigB;
		
		if (UnsignedInteger.compareUnsigned(uiMagB, uiMagA) < 0)
			return uiNonSigA;
		
		// return (uiNonSigA < uiNonSigB) ? uiNonSigA : uiNonSigB;
		if (UnsignedInteger.compareUnsigned(uiNonSigA, uiNonSigB) < 0)
			return uiNonSigA;
		return uiNonSigB;
	}
	
	/**
	 * Round and pack to float.
	 * 
	 * @param __sign The sign.
	 * @param __exp The exponent.
	 * @param __sig The significand.
	 * @return The resultant value.
	 * @since 2021/04/08
	 */
	private static int __roundPackToF32(boolean __sign, int __exp, int __sig)
	{
		int roundIncrement = 0x40;
		int roundBits = __sig & 0x7F;
		
		// if ( 0xFD <= (unsigned int) exp )
		if (UnsignedInteger.compareUnsigned(0xFD, __exp) <= 0)
		{
			// Negative exponent?
			if (__exp < 0)
			{
				__sig = SoftFloat.__shiftRightJam32(__sig, -__exp);
				__exp = 0;
				roundBits = __sig & 0x7F;
			}
			
			// else if ((0xFD < exp) || (0x80000000 <= sig + roundIncrement))
			else if (0xFD < __exp ||
				UnsignedInteger.compareUnsigned(0x8000_0000,
					__sig + roundIncrement) <= 0)
			{
				// uiZ = packToF32UI(__sign, 0xFF, 0) - !roundIncrement;
				return SoftFloat.__packToF32UI(__sign, 0xFF, 0);
			}
		}
		
		// sig = (sig + roundIncrement)>>7;
		__sig = (__sig + roundIncrement) >>> 7;
		
		// sig &= ~(uint_fast32_t) (! (roundBits ^ 0x40) & roundNearEven);
		__sig &= ~(((roundBits ^ 0x40) == 0 ? 1 : 0) & 1);
		
		// if ( ! sig ) exp = 0;
		if (__sig == 0)
			__exp = 0;
		
		// uiZ = packToF32UI( sign, exp, sig );
		return SoftFloat.__packToF32UI(__sign, __exp, __sig);
	}
	
	/**
	 * Shift right and jam float.
	 * 
	 * @param __v The value.
	 * @param __uDist The distance.
	 * @return The jammed value.
	 * @since 2021/04/08
	 */
	private static int __shiftRightJam32(int __v, int __uDist)
	{
		// uint_fast16_t dist
		// (dist < 31) ? a>>dist | ((uint32_t) (a<<(-dist & 31)) != 0)
		if (UnsignedInteger.compareUnsigned(__uDist, 31) < 0)
			return __v >> __uDist | (((__v << (-__uDist & 31)) != 0) ? 1 : 0);
		
		// (a != 0)
		return (__v != 0 ? 1 : 0);
	}
	
	/**
	 * Short shift right jam at 64-bits.
	 * 
	 * @param __a The value to jam.
	 * @param __dist The distance.
	 * @return The jammed value.
	 * @since 2021/04/10
	 */
	private static long __shortShiftRightJam64(long __a, int __dist)
	{
		// return a>>dist | ((a & (((uint_fast64_t) 1<<dist) - 1)) != 0);
    	return __a >>> __dist |
    		((__a & (((((1L << __dist) - 1)) != 0) ? 1 : 0)));
	}
	
	/**
	 * Returns whether the sign bit is set.
	 * 
	 * @param __a The float to read from.
	 * @return If the sign bit is set.
	 * @since 2021/04/10
	 */
	private static boolean __signF32UI(int __a)
	{
		return (__a & SoftFloat.SIGN_MASK) != 0;
	}
}
