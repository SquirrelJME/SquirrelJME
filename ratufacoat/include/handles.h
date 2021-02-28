/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Memory handle support, used to have potentially loosely bound regions
 * of memory.
 * 
 * @since 2021/02/27
 */

#ifndef SQUIRRELJME_HANDLES_H
#define SQUIRRELJME_HANDLES_H

#include "sjmerc.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_HANDLES_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Storage for all memory handles. */
typedef struct sjme_memHandles sjme_memHandles;

/** A single memory handle. */
typedef struct sjme_memHandle sjme_memHandle;

/**
 * Initializes the state of memory handles.
 * 
 * @param out The output where handles will go.
 * @param error The error state, if any.
 * @return The failure state.
 * @since 2021/02/27
 */
sjme_returnFail sjme_initMemHandles(sjme_memHandles** out, sjme_error* error);

/**
 * Destroys and deallocates the state of memory handles.
 * 
 * @param in The handles to destroy.
 * @param error The error state.
 * @return If this failed or not.
 * @since 2021/02/28
 */
sjme_returnFail sjme_destroyMemHandles(sjme_memHandles* in, sjme_error* error);

//sjme_memHandle* sjme_memHandleNew(

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_HANDLES_H
}
#undef SJME_CXX_SQUIRRELJME_HANDLES_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_HANDLES_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_HANDLES_H */
