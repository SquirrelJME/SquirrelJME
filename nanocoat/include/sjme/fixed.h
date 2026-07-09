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
 * @file
 * @since 2024/06/27
 */

#ifndef SJME_C_FIXED_H
#define SJME_C_FIXED_H

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

/** The number of bits to shift for fractions. */
#define SJME_FIXED_SHIFT INT32_C(16)

/** The number of bits in an entire fixed value. */
#define SJME_FIXED_FULL_BITS INT32_C(32)

/** The value one. */
#define SJME_FIXED_ONE INT32_C(0x10000)

/** The masked for shifted values. */
#define SJME_FIXED_MASK INT32_C(0xFFFF)
	
/** 1 Degree in radians. */
#define SJME_FIXED_RAD_1 INT32_C(1144)
	
/** 45 Degree in radians. */
#define SJME_FIXED_RAD_45 INT32_C(51472)
	
/** 90 Degrees in radians. */
#define SJME_FIXED_RAD_90 INT32_C(102944)
	
/** 135 Degrees in radians. */
#define SJME_FIXED_RAD_135 INT32_C(154415)
	
/** 180 Degrees in radians. */
#define SJME_FIXED_RAD_180 INT32_C(205887)
	
/** 225 Degrees in radians. */
#define SJME_FIXED_RAD_225 INT32_C(257359)
	
/** 270 Degrees in radians. */
#define SJME_FIXED_RAD_270 INT32_C(308831)
	
/** 315 Degrees in radians. */
#define SJME_FIXED_RAD_315 INT32_C(360302)
	
/** 360 Degrees in radians. */
#define SJME_FIXED_RAD_360 INT32_C(411774)

/** The masked for rounding values. */
#define SJME_FIXED_ROUND_MASK INT32_C(0x800)

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
 * Calculates @code cos(radAngle) @endcode.
 * 
 * @param radAngle The angle in radians.
 * @return The @code cos(radAngle) @endcode.
 * @since 2026/07/08
 */
sjme_fixed sjme_fixed_cos(
	sjme_attrInValue sjme_fixed radAngle);
	
/**
 * Converts an angle in degrees to radians.
 * 
 * @param degAngle The angle in degrees to convert.
 * @return The angle in radians.
 * @since 2026/07/08
 */
sjme_fixed sjme_fixed_degToRad(
	sjme_attrInValue sjme_fixed degAngle);

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
 * Converts a fixed to an integer value, with clipping.
 * 
 * @param lo The low value.
 * @param val The input value to convert.
 * @param hi The high value.
 * @return The resultant integer value.
 * @since 2025/12/22
 */
sjme_jint sjme_fixed_intClip(
	sjme_attrInValue sjme_jint lo,
	sjme_attrInValue sjme_fixed val,
	sjme_attrInValue sjme_jint hi);

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
 * Converts an angle in radians to degrees.
 * 
 * @param radAngle The angle in radians to convert.
 * @return The angle in degrees.
 * @since 2026/07/08
 */
sjme_fixed sjme_fixed_radToDeg(
	sjme_attrInValue sjme_fixed radAngle);

/**
 * Rounds a fixed point number, removing any fractional value.
 * 
 * @param v The value to round.
 * @return The resultant rounded value.
 * @since 2024/07/10
 */
sjme_fixed sjme_fixed_round(
	sjme_attrInValue sjme_jint v);
	
/**
 * Calculates @code sin(radAngle) @endcode.
 * 
 * @param radAngle The angle in radians.
 * @return The @code sin(radAngle) @endcode.
 * @since 2026/07/08
 */
sjme_fixed sjme_fixed_sin(
	sjme_attrInValue sjme_fixed radAngle);

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
