/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal memory tag structures.
 * 
 * @since 2023/02/04
 */

#ifndef SQUIRRELJME_MEMTAGINTERNAL_H
#define SQUIRRELJME_MEMTAGINTERNAL_H

#include "memio/memtag.h"
#include "memio/atomic.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MEMTAGINTERNAL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Lower bit protection for the size. */
#define SJME_MEMIO_NEW_TAGGED_PROTECT_LOW INT32_C(0x40000000)

/**
 * Internal tagged memory representation.
 *
 * @since 2022/12/20
 */
typedef struct sjme_memTagInternal
{
	/** The group this tag is a part of. */
	sjme_memIo_tagGroupInternal* inGroup;

	/** The free function for this tag, is called on free. */
	sjme_memIo_tagFreeFuncType* freeFunc;

	/** The allocation size used currently. */
	sjme_jsize allocSize;

	/** The key used to check if a tag is valid. */
	sjme_jint checkKey;

	/** The pointer to the actual data in the tag. */
	sjme_memIo_atomicPointer ptr;
} sjme_memTagInternal;

struct sjme_memIo_tagGroupInternal
{
	/** The parent tag group, if there is one. */
	sjme_memIo_tagGroupInternal* parent;

	/** Estimated memory used in total. */
	sjme_jlong estimatedUsedSize;
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MEMTAGINTERNAL_H
}
		#undef SJME_CXX_SQUIRRELJME_MEMTAGINTERNAL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MEMTAGINTERNAL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MEMTAGINTERNAL_H */
