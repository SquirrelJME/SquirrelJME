/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/fixed.h"

/** The number of bits to shift for fractions. */
#define SJME_FIXED_SHIFT 16

/** The number of bits in an entire fixed value. */
#define SJME_FIXED_FULL_BITS 32

/** The masked for shifted values. */
#define SJME_FIXED_MASK 0xFFFF

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

sjme_fixed sjme_fixed_mul(
	sjme_attrInValue sjme_fixed a,
	sjme_attrInValue sjme_fixed b)
{
	return (sjme_fixed)(((int64_t)a) * ((int64_t)b) >> SJME_FIXED_SHIFT);
}
