/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SquirrelJME allocator.
 * 
 * @since 2023/11/18
 */

#ifndef SQUIRRELJME_ALLOC_H
#define SQUIRRELJME_ALLOC_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ALLOC_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Structure which stores the pooled memory allocator.
 * 
 * @since 2023/11/18
 */
typedef struct sjme_alloc_pool sjme_alloc_pool;

/**
 * Allocation link chain, each is a chain between each allocation.
 * 
 * @since 2023/11/18
 */
typedef struct sjme_alloc_link sjme_alloc_link;

struct sjme_alloc_link
{
	/** The pool this is in. */
	sjme_alloc_pool* pool;
	
	/** Previous link. */
	sjme_alloc_link* prev;
	
	/** Next link. */
	sjme_alloc_link* next;
	
	/** The number of bytes in this block. */
	jint size;
	
	/** The memory block. */
	jubyte block[sjme_flexibleArrayCount];
}; 

/**
 * Structure which stores the pooled memory allocator.
 * 
 * @since 2023/11/18
 */
struct sjme_alloc_pool
{
	/** The size of the allocation pool. */
	jint size;
	
	/** The first chain link. */
	sjme_alloc_link* firstLink;
	
	/** The last chain link. */
	sjme_alloc_link* lastLink;
	
	/** The memory block. */
	jubyte block[sjme_flexibleArrayCount];
};

jboolean sjme_alloc_poolMalloc(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInPositive jint size);

jboolean sjme_alloc_poolStatic(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInNotNull void* baseAddr,
	sjme_attrInPositive jint size);

jboolean sjme_alloc(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInPositive jint size,
	sjme_attrOutNotNull void** outAddr); 

jboolean sjme_allocFree(
	sjme_attrInNotNull void* addr);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ALLOC_H
}
		#undef SJME_CXX_SQUIRRELJME_ALLOC_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ALLOC_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ALLOC_H */
