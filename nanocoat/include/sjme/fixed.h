/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Fixed point math.
 * 
 * @since 2024/06/27
 */

#ifndef SQUIRRELJME_FIXED_H
#define SQUIRRELJME_FIXED_H

#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_FIXED_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Ceiling a fixed point number, removing any fractional value.
 * 
 * @param v The value to ceiling.
 * @return The resultant ceiling value.
 * @since 2024/07/10
 */
sjme_fixed sjme_fixed_ceil(
	sjme_attrInValue sjme_jint v);
	
/**
 * Divides two fixed values.
 * 
 * @param num The numerator.
 * @param den The denominator.
 * @return The resultant fixed value.
 * @since 2024/06/27
 */
sjme_fixed sjme_fixed_div(
	sjme_attrInValue sjme_fixed num,
	sjme_attrInValue sjme_fixed den);

/**
 * Floors a fixed point number, removing any fractional value.
 * 
 * @param v The value to floor.
 * @return The resultant floored value.
 * @since 2024/07/10
 */
sjme_fixed sjme_fixed_floor(
	sjme_attrInValue sjme_jint v);

/**
 * Calculates a fraction from two integers.
 * 
 * @param num The numerator.
 * @param den The denominator.
 * @return The resultant fixed value.
 * @since 2024/06/27
 */
sjme_fixed sjme_fixed_fraction(
	sjme_attrInValue sjme_jint num,
	sjme_attrInValue sjme_jint den);
	
/**
 * Converts an integer to a fixed value.
 * 
 * @param val The input value to convert. 
 * @return The resultant fixed value.
 * @since 2024/06/27
 */
sjme_fixed sjme_fixed_hi(
	sjme_attrInValue sjme_jint val);

/**
 * Converts a fixed to an integer value.
 * 
 * @param val The input value to convert. 
 * @return The resultant integer value.
 * @since 2024/06/27
 */
sjme_jint sjme_fixed_int(
	sjme_attrInValue sjme_fixed val);

/**
 * Multiplies two fixed values.
 * 
 * @param a The first value. 
 * @param b The second value.
 * @return The resultant fixed value.
 * @since 2024/06/27
 */
sjme_fixed sjme_fixed_mul(
	sjme_attrInValue sjme_fixed a,
	sjme_attrInValue sjme_fixed b);

/**
 * Rounds a fixed point number, removing any fractional value.
 * 
 * @param v The value to round.
 * @return The resultant rounded value.
 * @since 2024/07/10
 */
sjme_fixed sjme_fixed_round(
	sjme_attrInValue sjme_jint v);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_FIXED_H
}
		#undef SJME_CXX_SQUIRRELJME_FIXED_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_FIXED_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_FIXED_H */
