/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/fixed.h"
#include "sjme/debug.h"

sjme_fixed sjme_fixed_abs(
	sjme_attrInValue sjme_jint v)
{
	if (v < 0)
		return -v;
	return v;
}

sjme_fixed sjme_fixed_ceil(
	sjme_attrInValue sjme_jint v)
{
	sjme_fixed z;
	
	z = v & (~SJME_FIXED_MASK);
	if ((v & SJME_FIXED_MASK) != 0)
		return z + SJME_FIXED_ONE;
	return z;
}

sjme_fixed sjme_fixed_cos(
	sjme_attrInValue sjme_fixed radAngle)
{
	return sjme_fixed_sin(radAngle + SJME_FIXED_RAD_90);
}

sjme_fixed sjme_fixed_degToRad(
	sjme_attrInValue sjme_fixed degAngle)
{
	return sjme_fixed_mul(degAngle, SJME_FIXED_RAD_1);
}

sjme_fixed sjme_fixed_div(
	sjme_attrInValue sjme_fixed num,
	sjme_attrInValue sjme_fixed den)
{
	if (den == 0)
		return 0;
	
#if !defined(SJME_CONFIG_HAS_NO_JULONG_NATIVE)
	return (sjme_fixed)((((int64_t)num) << SJME_FIXED_SHIFT) / den);
#else
	sjme_todo("Impl?");
	return 0;
#endif
}

sjme_fixed sjme_fixed_floor(
	sjme_attrInValue sjme_jint v)
{
	return v & (~SJME_FIXED_MASK);
}

sjme_fixed sjme_fixed_fraction(
	sjme_attrInValue sjme_jint num,
	sjme_attrInValue sjme_jint den)
{
	if (den == 0)
		return 0;
	
	return sjme_fixed_div(sjme_fixed_hi(num),
		sjme_fixed_hi(den));
}

sjme_fixed sjme_fixed_hi(
	sjme_attrInValue sjme_jint val)
{
	return val << SJME_FIXED_SHIFT;
}

sjme_jint sjme_fixed_int(
	sjme_attrInValue sjme_fixed val)
{
	return val >> SJME_FIXED_SHIFT;
}

sjme_jint sjme_fixed_intClip(
	sjme_attrInValue sjme_jint lo,
	sjme_attrInValue sjme_fixed val,
	sjme_attrInValue sjme_jint hi)
{
	sjme_jint v;
	
	/* Convert value first. */
	v = val >> SJME_FIXED_SHIFT;
	
	/* Then clip. */
	if (v < lo)
		return lo;
	else if (v >= hi)
		return hi;
	return v;
}

sjme_fixed sjme_fixed_invSqrt(
	sjme_attrInValue sjme_fixed val)
{
	/* This uses Newton-Raphson to return the inverse square root, and sqrt */
	/* is a function of this because muls are faster than divs on most CPUs */
	/* (sans some like Apple's M-Series where they perform about the same). */
	sjme_fixed half, y, y_sq;
	sjme_jint i;
	
	if (val <= 0)
		return 0;
	
	y = (val > SJME_FIXED_SQRT2) ? (SJME_FIXED_ONE >> 1) : SJME_FIXED_ONE;
	half = sjme_fixed_mul(val, SJME_FIXED_HALF);
	
	/* Being overly cautious with 3 Newton-Raphson steps here, if these FP */
	/* libs are mostly going to be used for screen rendering, 2 steps offer */
	/* more than enough precision. The equation for each iteration is: */
	/* 'y = y * (1.5 - (half * y * y))' */
	for (i = 0; i < 3; i++)
	{
		y_sq = sjme_fixed_mul(y, y);
		y = sjme_fixed_mul(y, SJME_FIXED_ONE_HALF -
			sjme_fixed_mul(half, y_sq));
	}
	
	return y;
}

sjme_jint sjme_fixed_max(
	sjme_attrInValue sjme_fixed a,
	sjme_attrInValue sjme_fixed b)
{
	if (a > b)
		return a;
	return b;
}
	
sjme_jint sjme_fixed_min(
	sjme_attrInValue sjme_fixed a,
	sjme_attrInValue sjme_fixed b)
{
	if (a < b)
		return a;
	return b;
}

sjme_fixed sjme_fixed_neg(
	sjme_attrInValue sjme_fixed v)
{
	return -v;
}

sjme_fixed sjme_fixed_mul(
	sjme_attrInValue sjme_fixed a,
	sjme_attrInValue sjme_fixed b)
{
#if !defined(SJME_CONFIG_HAS_NO_JULONG_NATIVE)
	return (sjme_fixed)(((int64_t)a) * ((int64_t)b) >> SJME_FIXED_SHIFT);
#else
	sjme_todo("Impl?");
	return 0;
#endif
}

sjme_fixed sjme_fixed_part(
	sjme_attrInValue sjme_fixed v)
{
	if (v < 0)
		return v | SJME_FIXED_MASK_HI;
	return v & SJME_FIXED_MASK;
}

sjme_fixed sjme_fixed_radToDeg(
	sjme_attrInValue sjme_fixed radAngle)
{
	return sjme_fixed_mul(radAngle, SJME_FIXED_DEG_1);
}

sjme_fixed sjme_fixed_round(
	sjme_attrInValue sjme_jint v)
{
	if (((v & SJME_FIXED_ROUND_MASK) != 0) == (v < 0))
		return sjme_fixed_ceil(v);
	return sjme_fixed_floor(v);
}

sjme_fixed sjme_fixed_signum(
	sjme_attrInValue sjme_jint v)
{
	if (v == 0)
		return 0;
	else if (v > 0)
		return SJME_FIXED_ONE;
	return -SJME_FIXED_ONE;
}

sjme_fixed sjme_fixed_sin(
	sjme_attrInValue sjme_fixed radAngle)
{
	/* Based on https://www.coranac.com/2009/07/sines/ Section 2. */
	sjme_jboolean negval;
	sjme_fixed x, x2, x3, x5, term2, term3, res;
	
	/* We will work on the [0,2*PI] range since sine is periodic. */
	/* Multiplying by the reciprocal of 2*PI could be a good speedup rather */
	/* than using a modulo of all things, but needs a Q32 reciprocal and */
	/* extra manipulations that may not be worth it at this point in time. */
	radAngle %= SJME_FIXED_RAD_360;
	
	if (radAngle < 0)
		radAngle += SJME_FIXED_RAD_360;
	
	/* Fold this to the [0, PI] range, also extract the sign bit if the */
	/* value currently sits in quadrants 3 or 4 ([PI, 2*PI] interval). That */
	/* way we only need to do a single negation on the resulting value. */
	negval = SJME_JNI_FALSE;
	if (radAngle >= SJME_FIXED_RAD_180)
	{
		radAngle -= SJME_FIXED_RAD_180;
		negval = SJME_JNI_TRUE;
	}
	
	/* Fold this even further to the [0, PI/2] range, the less sign math */
	/* involved in the Taylor series, the easier it is for us because we no */
	/* longer need to care for which quadrant this is in. Another property */
	/* of Taylor series is that they center around 0 so this also gives us */
	/* more accuracy below as well. */
	if (radAngle > SJME_FIXED_RAD_90)
		radAngle = SJME_FIXED_RAD_180 - radAngle;
	
	/* Bog-standard Taylor Series. */
	/* Some implementations use a seventh-order term (x7 = x^7 / 5040) for */
	/* slightly better precision, but doing that in Q16.16 would be a bad */
	/* idea, as 1/5040 for "term4" in Q16 would result in a value of 13, */
	/* which is far too erratic here. */
	x = radAngle;
	x2 = sjme_fixed_mul(x, x);
	x3 = sjme_fixed_mul(x2, x);
	x5 = sjme_fixed_mul(x3, x2);
	
	/* Coefficients scaled to Q16.16 notation: */
	/* 1/6 in is 10923 */
	/* 1/120 in is 546 */
	/* If used, "term4" for the seventh-order term in Q16.16 would be 13. */
	term2 = sjme_fixed_mul(x3, SJME_FIXED_FRAC_1_6);
	term3 = sjme_fixed_mul(x5, SJME_FIXED_FRAC_1_120);
	
	/* 'sin(x) = x - (x^3 / 6) + (x^5 / 120);' */
	res = x - term2 + term3;
	
	/* Clamping because it's very easy for this to overflow and become a */
	/* multiplier. In fact, that's what happens in BreakQuest Mobile and */
	/* SolaRola... this is made even worse by DDA being used to accelerate */
	/* drawArc and fillArc rather than computing sine/cos for every step. */
	if (res > SJME_FIXED_ONE)
		res = SJME_FIXED_ONE;
	if (res < -SJME_FIXED_ONE)
		res = -SJME_FIXED_ONE;
	
	return (negval ? -res : res);
}

sjme_fixed sjme_fixed_sqrt(
	sjme_attrInValue sjme_fixed val)
{
	if (val <= 0)
		return 0;
	
	return sjme_fixed_mul(val, sjme_fixed_invSqrt(val));
}
