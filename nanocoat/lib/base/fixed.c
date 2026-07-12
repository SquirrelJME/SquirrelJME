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
	sjme_todo("Impl?");
	return 0;
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

sjme_fixed sjme_fixed_radToDeg(
	sjme_attrInValue sjme_fixed radAngle)
{
	sjme_todo("Impl?");
	return 0;
}

sjme_fixed sjme_fixed_round(
	sjme_attrInValue sjme_jint v)
{
	if (((v & SJME_FIXED_ROUND_MASK) != 0) == (v < 0))
		return sjme_fixed_ceil(v);
	return sjme_fixed_floor(v);
}

sjme_fixed sjme_fixed_sin(
	sjme_attrInValue sjme_fixed radAngle)
{
	sjme_todo("Impl?");
	return 0;
}
