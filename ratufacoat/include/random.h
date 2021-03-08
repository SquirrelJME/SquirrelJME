/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Random number generation, compatible with Java's @c Random.
 * 
 * @since 2021/03/07
 */

#ifndef SQUIRRELJME_RANDOM_H
#define SQUIRRELJME_RANDOM_H

#include "softmath.h"
#include "error.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_RANDOM_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Random state.
 * 
 * @since 2021/03/07
 */
typedef struct sjme_randomState
{
	/** The seed used. */
	sjme_jlong seed;
	
	/** The current value for upcoming values. */
	sjme_jlong current;
} sjme_randomState;

/**
 * Seeds the RNG.
 * 
 * @param random The RNG to seed.
 * @param seed The seed to seed with.
 * @param error The error state.
 * @return If the RNG could not be seeded.
 * @since 2021/03/07
 */
sjme_returnFail sjme_randomSeed(sjme_randomState* random, sjme_jlong seed,
	sjme_error* error);

/**
 * Returns the next boolean from the RNG.
 * 
 * @param random The random state.
 * @return The random boolean.
 * @since 2021/03/07
 */
sjme_jboolean sjme_randomNextBoolean(sjme_randomState* random);

/**
 * Returns the next integer from the RNG.
 * 
 * @param random The random state.
 * @return The random integer.
 * @since 2021/03/07
 */
sjme_jint sjme_randomNextInt(sjme_randomState* random);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_RANDOM_H
}
#undef SJME_CXX_SQUIRRELJME_RANDOM_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_RANDOM_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_RANDOM_H */
