/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Cleaning functions.
 * 
 * @since 2024/08/09
 */

#ifndef SQUIRRELJME_CLEANUP_H
#define SQUIRRELJME_CLEANUP_H

#include "sjme/nvm.h"
#include "sjme/alloc.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CLEANUP_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Special enqueue data that represents this is an identity. */
#define SJME_NVM_ENQUEUE_IDENTITY ((sjme_pointer)(0x1234))

/**
 * Handler for any weak references which have been enqueued.
 * 
 * @param weak The weak reference.
 * @param data The data for the enqueue.
 * @param isBlockFree Is this a block free or a weak free?
 * @return Any resultant error, if any.
 * @since 2024/08/08
 */
sjme_errorCode sjme_nvm_enqueueHandler(
	sjme_attrInNotNull sjme_alloc_weak weak,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInValue sjme_jboolean isBlockFree);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CLEANUP_H
}
		#undef SJME_CXX_SQUIRRELJME_CLEANUP_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLEANUP_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CLEANUP_H */
