/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/fixed.h"

sjme_fixed sjme_fixed_ceil(
	sjme_attrInValue sjme_jint v)
{
	sjme_fixed z;
	
	z = v & (~SJME_FIXED_MASK);
	if ((v & SJME_FIXED_MASK) != 0)
		return z + SJME_FIXED_ONE;
	return z;
}

sjme_fixed sjme_fixed_div(
	sjme_attrInValue sjme_fixed num,
	sjme_attrInValue sjme_fixed den)
{
	if (den == 0)
		return 0;
	
	return (sjme_fixed)((((int64_t)num) << SJME_FIXED_SHIFT) / den);
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
	return (sjme_fixed)(((int64_t)a) * ((int64_t)b) >> SJME_FIXED_SHIFT);
}

sjme_fixed sjme_fixed_round(
	sjme_attrInValue sjme_jint v)
{
	if (((v & SJME_FIXED_ROUND_MASK) != 0) == (v < 0))
		return sjme_fixed_ceil(v);
	return sjme_fixed_floor(v);
}
