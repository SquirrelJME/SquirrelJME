/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Types for functions.
 * 
 * @since 2022/03/09
 */

#ifndef SQUIRRELJME_FUNCTION_H
#define SQUIRRELJME_FUNCTION_H

#include "sjmerc.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_FUNCTION_H
extern "C" {
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * This represents an integer function which may be called to perform an
 * operation. It is unspecified whether the function produces any side effects
 * and stores values in memory.
 * 
 * @param proxy The proxy value which may be acted upon.
 * @param data The data pointer, which may be anything.
 * @param index The index of the item being touched.
 * @param value The value of the given index.
 * @param error Any resultant error state.
 * @return If the mapping was a complete success.
 * @since 2022/03/09
 */
typedef sjme_jboolean (*sjme_integerFunction)(void* proxy, void* data,
	sjme_jint index, sjme_jint value, sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_FUNCTION_H
}
#undef SJME_CXX_SQUIRRELJME_FUNCTION_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_FUNCTION_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_FUNCTION_H */
