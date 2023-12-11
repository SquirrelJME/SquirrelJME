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
 * Allocation link chain, each is a chain between each allocation.
 * 
 * @since 2023/11/18
 */
typedef struct sjme_alloc_link sjme_alloc_link;

/**
 * Allocation pool and link space types.
 * 
 * @since 2023/11/18
 */
typedef enum sjme_alloc_poolSpace
{
	/** Free space. */
	SJME_ALLOC_POOL_SPACE_FREE,
	
	/** Used space. */
	SJME_ALLOC_POOL_SPACE_USED,
	
	/** the number of possible spaces. */
	SJME_NUM_ALLOC_POOL_SPACE
} sjme_alloc_poolSpace;

struct sjme_alloc_link
{
	/** The pool this is in. */
	sjme_alloc_pool* pool;
	
	/** Previous link. */
	sjme_alloc_link* prev;
	
	/** Next link. */
	sjme_alloc_link* next;
	
	/** The space this is in. */
	sjme_alloc_poolSpace space;
	
	/** The previous free link. */
	sjme_alloc_link* freePrev;
	
	/** The next free link. */
	sjme_alloc_link* freeNext;
	
	/** The allocation size of the link, @code{allocSize <= blockSize}. */
	sjme_jint allocSize;
	
	/** The size of the data area of this block. */
	sjme_jint blockSize;
	
	/** The memory block. */
	sjme_jubyte block[sjme_flexibleArrayCount];
};

/**
 * Calculates the size of the pool link.
 * 
 * @param size The size to use for the pool link.
 * @return The size of the given pool link.
 * @since 2023/11/16
 */
#define SJME_SIZEOF_ALLOC_LINK(size) \
	(sizeof(sjme_alloc_link) + (((size_t)(size)) * \
	sizeof(sjme_jubyte)))

/**
 * Structure which stores the pooled memory allocator.
 * 
 * @since 2023/11/18
 */
struct sjme_alloc_pool
{
	/** The front end wrapped type. */
	sjme_frontEndWrapper* frontEndWrapper;

	/** The size of the allocation pool. */
	sjme_jint size;
	
	/** Free and used space information. */
	struct
	{
		/** Space that can be used. */
		sjme_jint usable;
		
		/** Space that is actually reserved due to overhead. */
		sjme_jint reserved;
	} space[SJME_NUM_ALLOC_POOL_SPACE];
	
	/** The front chain link. */
	sjme_alloc_link* frontLink;
	
	/** The back chain link. */
	sjme_alloc_link* backLink;
		
	/** The first free link in the chain. */
	sjme_alloc_link* freeFirstLink;
		
	/** The last free link in the chain. */
	sjme_alloc_link* freeLastLink;
	
	/** The memory block. */
	sjme_jubyte block[sjme_flexibleArrayCount];
};

/**
 * Calculates the size of the allocation pool.
 * 
 * @param size The size to use for the allocation pool.
 * @return The size of the given allocation pool.
 * @since 2023/11/16
 */
#define SJME_SIZEOF_ALLOC_POOL(size) \
	(sizeof(sjme_alloc_pool) + (((size_t)(size)) * \
	sizeof(sjme_jubyte)))

/**
 * Allocates a pool that is based on @c malloc() .
 * 
 * @param outPool The resultant pool. 
 * @param size The requested pool size.
 * @return Returns an error code.
 * @since 2023/11/18
 */
sjme_errorCode sjme_alloc_poolInitMalloc(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInPositive sjme_jint size);

/**
 * Allocates a pool that is based on a static region of memory.
 * 
 * @param outPool The resultant pool.
 * @param baseAddr The base address of the block. 
 * @param size The size of the block.
 * @return Returns an error code.
 * @since 2023/11/18
 */
sjme_errorCode sjme_alloc_poolInitStatic(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInNotNull void* baseAddr,
	sjme_attrInPositive sjme_jint size);

/**
 * Returns the total space that is available within the pool, includes both
 * free and used spaces.
 *
 * @param pool The pool to get the information of.
 * @param outTotal The total space of the pool, will be @c outReserved plus
 * the value of @c outUsable .
 * @param outReserved The total reserved space within the pool.
 * @param outUsable The total usable space within the pool.
 * @return Any error or otherwise success.
 * @since 2023/12/11
 */
sjme_errorCode sjme_alloc_poolSpaceTotalSize(
	sjme_attrInNotNull const sjme_alloc_pool* pool,
	sjme_attrOutNullable sjme_jint* outTotal,
	sjme_attrOutNullable sjme_jint* outReserved,
	sjme_attrOutNullable sjme_jint* outUsable);

/**
 * Allocates memory within the given pool.
 * 
 * @param pool The pool to allocate within.
 * @param size The number of bytes to allocate.
 * @param outAddr The output address.
 * @return Returns an error code.
 * @since 2023/11/19
 */
sjme_errorCode sjme_alloc(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInPositiveNonZero sjme_jint size,
	sjme_attrOutNotNull void** outAddr); 

/**
 * Frees memory.
 * 
 * @param addr The memory to free. 
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/11/19
 */
sjme_errorCode sjme_allocFree(
	sjme_attrInNotNull void* addr);
	
/**
 * Returns the link of the given memory block
 * 
 * @param addr The pointer to get the link from.
 * @param outLink The resultant link.
 * @return Returns an error code.
 * @since 2023/11/19
 */
sjme_errorCode sjme_allocLink(
	sjme_attrInNotNull void* addr,
	sjme_attrOutNotNull sjme_alloc_link** outLink);

/**
 * Reallocates memory, either growing it or shrinking... the pointer will be
 * adjusted accordingly.
 * 
 * @param inOutAddr The address to reallocate.
 * @param newSize The new size of the allocation, if @c 0 then the pointer
 * is freed instead.
 * @return Returns an error code.
 * @since 2023/11/28
 */
sjme_errorCode sjme_allocRealloc(
	sjme_attrInOutNotNull void** inOutAddr,
	sjme_attrInPositive sjme_jint newSize);

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
