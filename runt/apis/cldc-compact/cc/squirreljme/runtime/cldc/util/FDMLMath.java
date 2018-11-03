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
 * Copyright (C) 1993 by Sun Microsystems, Inc. All rights reserved.
 *
 * Developed at SunSoft, a Sun Microsystems, Inc. business.
 * Permission to use, copy, modify, and distribute this
 * software is freely granted, provided that this notice 
 * is preserved.
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
	
	/** The one value. */
	private static final double _ONE =
		1.0;
	
	/** The tiniest value. */
	private static final double _TINY =
		1.0e-300;
	
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
		double hfsq, f, s, z, R, w, t1, t2, dk;
		int k, hx, i, j;
		unsigned lx;

		// high and low word of __v
		hx = __hi(__v);
		lx = __lo(__v);

		k=0;
		
		// x < 2**-1022
		if (hx < 0x00100000)
		{
			// log(+-0)=-inf
			if (((hx & 0x7fffffff) | lx) == 0) 
				return -two54 / zero;
			
			// log(-#) = NaN
			if (hx < 0)
				return (__v - __v) / zero;
			
			// subnormal number, scale up __v
			k -= 54;
			__v *= two54;
			
			// high word of __v
			hx = __HI(__v);
		} 
		
		if (hx >= 0x7ff00000)
			return __v + __v;
		
		k += (hx >> 20) - 1023;
		hx &= 0x000fffff;
		i = (hx + 0x95f64) & 0x100000;
		
		// normalize x or x/2
		__HI(x) = hx | (i^0x3ff00000);
		k += (i >> 20);
		f = __v - 1.0;
		
		// |f| < 2**-20
		if ((0x000fffff & (2 + hx)) < 3)
		{
			if (f == zero)
				if (k == 0)
					return zero;
				else
				{
					dk = (double)k;
					return dk * ln2_hi + dk * ln2_lo;
				}
			
			R = f * f * (0.5 - 0.33333333333333333 * f);
			if (k == 0)
				return f - R;
			else
			{
				dk = (double)k;
				return dk * ln2_hi - ((R - dk * ln2_lo) - f);
			}
		}
		
		s = f / (2.0 + f); 
		dk = (double)k;
		z = s * s;
		i = hx - 0x6147a;
		w = z * z;
		j = 0x6b851 - hx;
		t1 = w * (Lg2 + w * (Lg4 + w * Lg6)); 
		t2 = z * (Lg1 + w * (Lg3 + w * (Lg5 + w * Lg7))); 
		i |= j;
		R = t2 + t1;
		
		if (i > 0)
		{
			hfsq = 0.5 * f * f;
			if (k == 0)
				return f - (hfsq - s * (hfsq + R));
			else
				return dk * ln2_hi -
					((hfsq - (s * (hfsq + R) + dk * ln2_lo)) - f);
		}
		else
		{
			if (k == 0)
				return f - s * (f - R);
			else
				return dk * ln2_hi - (( s * (f - R) - dk * ln2_lo) - f);
		}
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
		int uur, uut1, uus1, uuix1, uuq1;
		int ix0, s0, q, m, t, i;

		// high and low word of __v
		ix0 = __hi(__v);
		uuix1 = __lo(__v);

		// Take care of Inf and NaN
		if ((ix0 & 0x7FF00000) == 0x7FF00000)
		{
			// sqrt(NaN)=NaN, sqrt(+inf)=+inf
			// sqrt(-inf)=sNaN
			return __v * __v + __v;
		}
		
		// take care of zero
		if (ix0 <= 0)
		{
			// sqrt(+-0) = +-0
			if (((ix0 & (~_SIGN)) | uuix1) == 0)
				return __v;
			
			// sqrt(-ve) = sNaN
			else if (ix0 < 0)
				return (__v - __v) / (__v - __v);
		}
		
		// normalize
		m = (ix0 >> 20);
		
		// subnormal __v
		if (m == 0)
		{
			while (ix0 == 0)
			{
				m -= 21;
				ix0 |= (uuix1 >>> 11);
				uuix1 <<= 21;
			}
			
			for (i = 0; (ix0 & 0x00100000) == 0; i++)
				ix0 <<= 1;
			
			m -= i - 1;
			ix0 |= (uuix1 >>> (32 - i));
			uuix1 <<= i;
		}
		
		// unbias exponent
		m -= 1023;
		ix0 = (ix0 & 0x000FFFFF) | 0x00100000;
		
		// odd m, double __v to make it even
		if ((m & 1) != 0)
		{
			ix0 += ix0 + ((uuix1 & _SIGN) >>> 31);
			uuix1 += uuix1;
		}
		
		// m = [m/2]
		m >>= 1;

		// generate sqrt(__v) bit by bit
		ix0 += ix0 + ((uuix1 & _SIGN) >>> 31);
		uuix1 += uuix1;
		
		// [q,q1] = sqrt(__v)
		q = uuq1 = s0 = uus1 = 0;
		
		// r = moving bit from right to left
		uur = 0x00200000;
		while (uur != 0)
		{
			t = s0 + uur;
			if (t <= ix0)
			{
				s0 = t + uur;
				ix0 -= t;
				q += uur;
			}
			ix0 += ix0 + ((uuix1 & _SIGN) >>> 31);
			uuix1 += uuix1;
			uur >>>= 1;
		}

		uur = _SIGN;
		while (uur != 0)
		{
			uut1 = uus1 + uur;
			t = s0;
			
			if ((t < ix0) || ((t == ix0) &&
				UnsignedInteger.compareSignedUnsigned(uut1, uuix1) <= 0))
			{
				uus1 = uut1 + uur;
				
				if (((uut1 & _SIGN) == _SIGN) && (uus1 & _SIGN) == 0)
					s0 += 1;
				
				ix0 -= t;
				
				if (UnsignedInteger.compareUnsigned(uuix1, uut1) < 0)
					ix0 -= 1;
				
				uuix1 -= uut1;
				uuq1 += uur;
			}
			
			ix0 += ix0 + ((uuix1 & _SIGN) >>> 31);
			uuix1 += uuix1;
			uur >>>= 1;
		}

		// use floating add to find out rounding direction
		if ((ix0 | uuix1) != 0)
		{
			// trigger inexact flag
			z = _ONE - _TINY;
			
			if (z >= _ONE)
			{
				z = _ONE + _TINY;
				if (uuq1 == 0xFFFFFFFF)
				{
					uuq1 = 0;
					q += 1;
				}
				else if (z > _ONE)
				{
					if (uuq1 == 0xFFFFFFFE)
						q += 1;
					uuq1 += 2;
				}
				else
					uuq1 += (uuq1 & 1);
			}
		}
		
		ix0 = (q >> 1) + 0x3FE00000;
		uuix1 = uuq1 >>> 1;
		
		if ((q & 1) == 1)
			uuix1 |= _SIGN;
		
		ix0 += (m << 20);
		
		return __compose(ix0, uuix1);
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

