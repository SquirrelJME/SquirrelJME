// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;

/**
 * This class contains math methods which are derived from the Freely
 * Distributed Math Library, which is written in C which is then translated
 * into Java. The conversion process is copy and pasted from the origin sources
 * and adapted to Java.
 *
 * The library is located at http://www.netlib.org/fdlibm/ and was
 * developed at Sun Microsystems, Inc.
 *
 * @since 2018/11/02
 */
@ImplementationNote("This code is derived from the Freely " +
	"Distributable Math Library (http://www.netlib.org/fdlibm/).")
public final class FDMLMath
{
	/** The sign bit value. */
	private static final int _SIGN =
		0x80000000;

	/**
	 * Not used.
	 *
	 * @since 2018/11/02
	 */
	private FDMLMath()
	{
	}

	/**
	 * Logarithm of a number.
	 *
	 * @param __v The input value.
	 * @return The resulting logarithm value.
	 * @since 2018/11/02
	 */
	@ImplementationNote("Source http://www.netlib.org/fdlibm/e_log.c")
	public static double log(double __v)
	{
		throw new todo.TODO();
	}

	/**
	 * Square root of a number.
	 *
	 * @param __v The input value.
	 * @return The resulting square root value.
	 * @since 2018/11/02
	 */
	@ImplementationNote("Source: http://www.netlib.org/fdlibm/e_sqrt.c")
	public static double sqrt(double __v)
	{
		double z;
		unsigned r, t1, s1, ix1, q1;
		int ix0, s0, q, m, t, i;

		// high and low word of x
		ix0 = __hi(x);
		ix1 = __lo(x);

		// Take care of Inf and NaN
		if ((ix0 & 0x7ff00000) == 0x7ff00000)
		{
			// sqrt(NaN)=NaN, sqrt(+inf)=+inf
			// sqrt(-inf)=sNaN
			return x * x + x;
		}
		
		// take care of zero
		if (ix0 <= 0)
		{
			// sqrt(+-0) = +-0
			if (((ix0 & (~sign)) | ix1) == 0)
				return x;
			
			// sqrt(-ve) = sNaN
			else if (ix0 < 0)
				return (x - x) / (x - x);
		}
		
		// normalize
		m = (ix0 >> 20);
		
		// subnormal x
		if (m == 0)
		{
			while (ix0 == 0)
			{
				m -= 21;
				ix0 |= (ix1 >> 11);
				ix1 <<= 21;
			}
			
			for (i = 0; (ix0 & 0x00100000) == 0; i++)
				ix0 <<= 1;
			
			m -= i - 1;
			ix0 |= (ix1 >> (32 - i));
			ix1 <<= i;
		}
		
		// unbias exponent
		m -= 1023;
		ix0 = (ix0 & 0x000fffff) | 0x00100000;
		
		// odd m, double x to make it even
		if(m&1)
		{
			ix0 += ix0 + ((ix1 & sign) >> 31);
			ix1 += ix1;
		}
		
		// m = [m/2]
		m >>= 1;

		// generate sqrt(x) bit by bit
		ix0 += ix0 + ((ix1 & sign) >> 31);
		ix1 += ix1;
		
		// [q,q1] = sqrt(x)
		q = q1 = s0 = s1 = 0;
		
		// r = moving bit from right to left
		r = 0x00200000;
		while (r!=0)
		{
			t = s0 + r;
			if (t <= ix0)
			{
				s0 = t+r;
				ix0 -= t;
				q += r;
			}
			ix0 += ix0 + ((ix1 & sign) >> 31);
			ix1 += ix1;
			r >>= 1;
		}

		r = sign;
		while (r != 0)
		{
			t1 = s1+r;
			t = s0;
			
			if ((t < ix0) || ((t == ix0) && (t1 <= ix1)))
			{
				s1 = t1+r;
				if (((t1 & sign) == sign) && (s1 & sign) ==0)
					s0 += 1;
				ix0 -= t;
				if (ix1 < t1)
					ix0 -= 1;
				ix1 -= t1;
				q1 += r;
			}
			
			ix0 += ix0 + ((ix1 & sign) >> 31);
			ix1 += ix1;
			r>>=1;
		}

		// use floating add to find out rounding direction
		if ((ix0 | ix1) != 0)
		{
			// trigger inexact flag
			z = one - tiny;
			
			if (z >= one)
			{
				z = one + tiny;
				if (q1 == (unsigned)0xffffffff)
				{
					q1 = 0;
					q += 1;
				}
				else if (z > one)
				{
					if (q1 == (unsigned)0xfffffffe)
						q += 1;
					q1 += 2;
				}
				else
					q1 += (q1 & 1);
			}
		}
		
		ix0 = (q >> 1) + 0x3fe00000;
		ix1 = q1 >> 1;
		
		if ((q & 1) == 1)
			ix1 |= sign;
		
		ix0 += (m << 20);
		
		return __compose(ix0, ix1);
	}
	
	/**
	 * Compose high and low value to double.
	 *
	 * @param __hi The high value.
	 * @param __lo The low value.
	 * @return The double value.
	 * @since 2018/11/03
	 */
	private static final double __compose(int __hi, int __lo)
	{
		return Double.longBitsToDouble(
			(((long)__hi & 0xFFFFFFFFL) << 32) |
			((long)__lo & 0xFFFFFFFFL));
	}

	/**
	 * Returns the high word of the double.
	 *
	 * @param __v The input double.
	 * @return The high word of the double.
	 * @since 2018/11/03
	 */
	private static final int __hi(double __v)
	{
		return (int)(Double.doubleToRawLongBits(__v) >>> 32);
	}

	/**
	 * Returns the low word of the double.
	 *
	 * @param __v The input double.
	 * @return The low word of the double.
	 * @since 2018/11/03
	 */
	private static final int __lo(double __v)
	{
		return (int)(Double.doubleToRawLongBits(__v));
	}
}

