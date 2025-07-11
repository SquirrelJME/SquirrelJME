/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Core dump support.
 * 
 * @since 2025/07/11
 */

#ifndef SJME_C_WALKCOREDUMP_H
#define SJME_C_WALKCOREDUMP_H

#include "sjme/nvm/walk.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_WALKCOREDUMP_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * A single pointer link, to deduplicate objects.
 *
 * @since 2025/07/11
 */
typedef struct sjme_nvm_walk_pointerLink
{
	/** The actual pointer for this item. */
	sjme_pointer pointer;
	
	/** The item ID of this pointer. */
	sjme_jint itemId;

	/** The type ID of this pointer. */
	sjme_jint typeId;
} sjme_nvm_walk_pointerLink;

/**
 * Represents a chain of @c sjme_nvm_walk_pointerLink .
 *
 * @since 2025/07/11
 */
typedef struct sjme_nvm_walk_pointerChain sjme_nvm_walk_pointerChain;

/** The number of pointers in a single chain. */
#define SJME_NVM_WALK_CHAIN_SIZE 64
	
struct sjme_nvm_walk_pointerChain
{
	/** The links which make up this chain. */
	sjme_nvm_walk_pointerLink links[SJME_NVM_WALK_CHAIN_SIZE];

	/** The previous link. */
	sjme_nvm_walk_pointerChain* prev;

	/** The next link. */
	sjme_nvm_walk_pointerChain* next;
};

/**
 * Stores the state of the core dump as walking progresses.
 *
 * @since 2025/07/11
 */
typedef struct sjme_nvm_walk_coreState
{
	/** The stream to write to. */
	sjme_stream_output out;

	/** Allocation pool to use for allocations. */
	sjme_alloc_pool allocPool;

	/** The current item id. */
	sjme_jint itemId;

	/** The first link of the pointer chain. */
	sjme_nvm_walk_pointerChain* chain;

	/** Was the elements array opened? */
	sjme_jboolean openElements;

	/** The last depth, to detect structure fallouts. */
	sjme_jint lastDepth;
} sjme_nvm_walk_coreState;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_WALKCOREDUMP_H
}
#undef SJME_CXX_WALKCOREDUMP_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_WALKCOREDUMP_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_WALKCOREDUMP_H */
