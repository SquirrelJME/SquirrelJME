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
#include "sjme/debug.h"
#include "sjme/atomic.h"

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

/**
 * Special link flags.
 * 
 * @since 2024/02/08
 */
typedef enum sjme_alloc_linkFlag
{
	/** Nested allocation pool. */
	SJME_ALLOC_LINK_FLAG_NESTED_POOL = 1,
} sjme_alloc_linkFlag;

/**
 * Object for referenced counted weak pointer references.
 * 
 * @since 2024/07/01
 */
typedef struct sjme_alloc_weak sjme_alloc_weak;

typedef sjme_errorCode (*sjme_alloc_weakEnqueueFunc)(
	sjme_attrInNotNull sjme_alloc_weak* weak,
	sjme_attrInNullable sjme_pointer data);

struct sjme_alloc_weak
{
	/** The link this points to, @c NULL if freed. */
	sjme_alloc_link* link;
	
	/** The count for this weak reference, zero will free this reference. */
	sjme_atomic_sjme_jint count;
	
	/** The pointer this points to, @c NULL if freed. */
	sjme_pointer pointer;
	
	/** The data to call for when this is removed. */
	sjme_alloc_weakEnqueueFunc enqueue;
	
	/** The pointer for enqueue. */
	sjme_pointer enqueueData;
};

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
	
	/** The weak reference this is attached to. */
	sjme_alloc_weak* weak;
	
	/** The allocation size of the link, @code{allocSize <= blockSize}. */
	sjme_jint allocSize;
	
	/** The size of the data area of this block. */
	sjme_jint blockSize;
	
	/** Link flags. */
	sjme_jint flags;

#if defined(SJME_CONFIG_DEBUG)
	/** The file of this allocation. */
	sjme_lpcstr debugFile;
	
	/** The line of this allocation. */
	sjme_jint debugLine;
	
	/** The function of this allocation. */
	sjme_lpcstr debugFunction;
#endif
	
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
	sjme_frontEnd frontEnd;

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
	
	/** Previous pool in multi-pool chain allocation. */
	sjme_alloc_pool* prevPool;
	
	/** Next pool in multi-pool chain allocation. */
	sjme_alloc_pool* nextPool;
	
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
sjme_errorCode SJME_DEBUG_IDENTIFIER(sjme_alloc)(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInPositiveNonZero sjme_jint size,
	sjme_attrOutNotNull void** outAddr
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL);

/**
 * Allocates a copy of the given data.
 *
 * @param pool The pool to allocate within.
 * @param size The allocation size.
 * @param outAddr The output address.
 * @param inAddr The input address.
 * @return Returns an error code.
 * @since 2023/12/13
 */
sjme_errorCode SJME_DEBUG_IDENTIFIER(sjme_alloc_copy)(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInPositiveNonZero sjme_jint size,
	sjme_attrOutNotNull void** outAddr,
	sjme_attrInNotNull void* inAddr
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL);

/**
 * Allocates a formatted string.
 *
 * @param inPool The pool to allocate within.
 * @param outString The output string.
 * @param format The format string.
 * @param ...
 * @return Any resultant error.
 * @since 2023/12/22
 */
sjme_errorCode SJME_DEBUG_IDENTIFIER(sjme_alloc_format)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_lpstr* outString,
	SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL SJME_DEBUG_ONLY_COMMA
	sjme_attrInNotNull sjme_attrFormatArg const char* format,
	...) SJME_DEBUG_TERNARY(sjme_attrFormatOuter(5, 6),
		sjme_attrFormatOuter(2, 3));

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
sjme_errorCode SJME_DEBUG_IDENTIFIER(sjme_alloc_realloc)(
	sjme_attrInOutNotNull void** inOutAddr,
	sjme_attrInPositive sjme_jint newSize
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL);

#if defined(SJME_CONFIG_DEBUG)

/**
 * Allocates memory within the given pool.
 * 
 * @param pool The pool to allocate within.
 * @param size The number of bytes to allocate.
 * @param outAddr The output address.
 * @return Returns an error code.
 * @since 2023/11/19
 */
#define sjme_alloc(pool, size, outAddr) \
	sjme_allocR((pool), (size), (outAddr), SJME_DEBUG_FILE_LINE_FUNC)

/**
 * Allocates a copy of the given data.
 *
 * @param pool The pool to allocate within.
 * @param size The allocation size.
 * @param outAddr The output address.
 * @param inAddr The input address.
 * @return Returns an error code.
 * @since 2023/12/13
 */
#define sjme_alloc_copy(pool, size, outAddr, inAddr) \
	sjme_alloc_copyR((pool), (size), (outAddr), (inAddr), \
		SJME_DEBUG_FILE_LINE_FUNC)

/**
 * Allocates a formatted string.
 *
 * @param inPool The pool to allocate within.
 * @param outString The output string.
 * @param format The format string.
 * @param ...
 * @return Any resultant error.
 * @since 2023/12/22
 */
#define sjme_alloc_format(inPool, outString, ...) \
	sjme_alloc_formatR((inPool), (outString), SJME_DEBUG_FILE_LINE_FUNC, \
		__VA_ARGS__)

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
#define sjme_alloc_realloc(inOutAddr, newSize) \
	sjme_alloc_reallocR((inOutAddr), (newSize), SJME_DEBUG_FILE_LINE_FUNC)

#endif

/**
 * Frees memory.
 * 
 * @param addr The memory to free. 
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/11/19
 */
sjme_errorCode sjme_alloc_free(
	sjme_attrInNotNull void* addr);

/**
 * Returns the link of the given memory block
 * 
 * @param addr The pointer to get the link from.
 * @param outLink The resultant link.
 * @return Returns an error code.
 * @since 2023/11/19
 */
sjme_errorCode sjme_alloc_getLink(
	sjme_attrInNotNull void* addr,
	sjme_attrOutNotNull sjme_alloc_link** outLink);

/**
 * Deletes a weak reference by un-counting it, if the count reaches zero
 * then this weak will be freed if that was requested.
 * 
 * @param inOutWeak The weak reference to delete, if the count reaches
 * zero then the pointer will be set to @c NULL .
 * @return Any resultant error, if any.
 * @since 2024/07/01
 */
sjme_errorCode sjme_alloc_weakDelete(
	sjme_attrInOutNotNull sjme_alloc_weak** inOutWeak);

/**
 * Gets the pointer pointed to by the given weak reference, if this returns
 * the value @c NULL then @c sjme_alloc_weakDelete should be called to
 * remove any stale weak references.
 * 
 * @param inWeak The weak reference to get from.
 * @param outPointer The pointer to the referenced memory, if it has been
 * freed then this will return @c NULL .
 * @return Any resultant error, if any.
 * @since 2024/07/01
 */
sjme_errorCode sjme_alloc_weakGet(
	sjme_attrInNotNull sjme_alloc_weak* inWeak,
	sjme_attrOutNotNull sjme_pointer* outPointer);

/**
 * Creates or returns a weak reference to the given block. If the reference
 * already exists, then it will be incremented.
 * 
 * @param addr The address to reference.
 * @param outWeak The resultant weak reference for the type.
 * @param inEnqueue The optional function to call when this reference is
 * enqueued.
 * @param inEnqueueData Optional data to pass to @c inEnqueue .
 * @return Any resultant error, if any.
 * @since 2024/07/01
 */
sjme_errorCode sjme_alloc_weakRef(
	sjme_attrInNotNull void* addr,
	sjme_attrOutNotNull sjme_alloc_weak** outWeak,
	sjme_attrInNullable sjme_alloc_weakEnqueueFunc inEnqueue,
	sjme_attrInNullable sjme_pointer inEnqueueData);

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
