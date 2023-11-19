/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

/* Include Valgrind if it is available? */
#if defined(SJME_CONFIG_HAS_VALGRIND)
	#include <valgrind.h>
	#include <memcheck.h>
#endif

#include "sjme/alloc.h"
#include "sjme/debug.h"

/** The minimum size permitted for allocation pools. */
#define SJME_ALLOC_MIN_SIZE (((SJME_SIZEOF_ALLOC_POOL(0) + \
	(SJME_SIZEOF_ALLOC_LINK(0) * 3)) | 0x1FF))

jboolean sjme_alloc_poolMalloc(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInPositive jint size)
{
	void* result;
	jint useSize;
	
	useSize = SJME_SIZEOF_ALLOC_POOL(size);
	if (outPool == NULL || size <= SJME_ALLOC_MIN_SIZE || useSize <= 0 ||
		size > useSize)
		return JNI_FALSE;
	
	/* Attempt allocation. */
	result = malloc(useSize);
	if (!result)
		return JNI_FALSE;
	
	/* Use static pool initializer to setup structures. */
	return sjme_alloc_poolStatic(outPool, result, useSize);
}

jboolean sjme_alloc_poolStatic(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInNotNull void* baseAddr,
	sjme_attrInPositive jint size)
{
	sjme_alloc_pool* result;
	sjme_alloc_link* frontLink;
	sjme_alloc_link* midLink;
	sjme_alloc_link* backLink;
	
	if (outPool == NULL || baseAddr == NULL || size <= SJME_ALLOC_MIN_SIZE)
		return JNI_FALSE;
	
	/* Initialize memory to nothing. */
	memset(baseAddr, 0, size);
	
	/* Setup initial pool structure. */
	result = baseAddr;
	result->size = size & (~7);
	
	/* Setup front link. */
	frontLink = (void*)&result->block[0];
	result->frontLink = frontLink;
	
	/* Setup back link. */
	backLink = (void*)&result->block[result->size - SJME_SIZEOF_ALLOC_LINK(0)];
	result->backLink = backLink;
	
	/* Setup middle link, which is between the two. */
	midLink = (void*)&frontLink->block[0];
	midLink->prev = frontLink;
	frontLink->next = midLink;
	midLink->next = backLink;
	backLink->prev = midLink;
	
	/* Determine size of the middle link, which is free space. */
	midLink->blockSize = (jint)((uintptr_t)backLink -
		(uintptr_t)&midLink->block[0]);
	
	/* Determine size that can and cannot be used. */
	result->space[SJME_ALLOC_POOL_SPACE_FREE].reserved =
		(SJME_SIZEOF_ALLOC_LINK(0) * 3);
	result->space[SJME_ALLOC_POOL_SPACE_FREE].usable = midLink->blockSize;
	
#if defined(SJME_CONFIG_HAS_VALGRIND)
	/* Reserve front side in Valgrind. */
	VALGRIND_MAKE_MEM_NOACCESS(baseAddr,
		((uintptr_t)&midLink->block[0] - (uintptr_t)baseAddr));
		
	/* Reserve back side in Valgrind. */
	VALGRIND_MAKE_MEM_NOACCESS(backLink,
		(SJME_SIZEOF_ALLOC_LINK(0)));
#endif
	
	/* Use the pool. */
	*outPool = result;
	return JNI_TRUE;
}

jboolean sjme_alloc(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInPositive jint size,
	sjme_attrOutNotNull void** outAddr)
{
	sjme_todo("Implement this?");
	return JNI_FALSE;
}

jboolean sjme_allocFree(
	sjme_attrInNotNull void* addr)
{
	sjme_todo("Implement this?");
	return JNI_FALSE;
}
