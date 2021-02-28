/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Software Math Support.
 * 
 * @since 2021/02/27
 */

#ifndef SQUIRRELJME_SOFTMATH_H
#define SQUIRRELJME_SOFTMATH_H

#include "sjmerc.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SOFTMATH_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Long return value result
 *
 * @since 2019/12/07
 */
typedef struct sjme_jlong_combine
{
	/** Low. */
	sjme_jint lo;
	
	/** High. */
	sjme_jint hi;
} sjme_jlong_combine;

/**
 * Division result.
 *
 * @since 2019/06/20
 */
typedef struct sjme_jint_div
{
	/** Quotient. */
	sjme_jint quot;

	/** Remainder. */
	sjme_jint rem;
} sjme_jint_div;

/**
 * Performs software division.
 * 
 * @param anum The numerator.
 * @param aden The denominator.
 * @return The result of the operation.
 * @since 2021/02/27
 */
sjme_jint_div sjme_div(sjme_jint anum, sjme_jint aden);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SOFTMATH_H
}
#undef SJME_CXX_SQUIRRELJME_SOFTMATH_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SOFTMATH_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SOFTMATH_H */
