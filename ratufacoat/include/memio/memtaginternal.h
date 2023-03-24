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

/** The tag check key. */
#define SJME_MEMIO_TAG_CHECK_KEY SJME_JSIZE(0xCAFEF00D)

/** The group check key. */
#define SJME_MEMIO_GROUP_CHECK_KEY SJME_JSIZE(0xFA57FA17)

/**
 * Macro to undo @c sizeof() equivalent for memory tags.
 *
 * @param size The size to undo.
 * @since 2023/03/24
 */
#define sjme_memIo_taggedNewUnSizeOf(size) \
	(((sjme_jsize)(size)) ^ SJME_MEMIO_NEW_TAGGED_PROTECT)

/**
 * Internal tagged memory representation.
 *
 * @since 2022/12/20
 */
typedef struct sjme_memTagInternal
{
	/** The pointer to the data. */
	sjme_memIo_atomicPointer pointer;

	/** The allocation size used currently. */
	sjme_jsize allocSize;

	/** The group this tag is a part of. */
	sjme_memIo_tagGroupInternal* inGroup;

	/** The free function for this tag, is called on free. */
	sjme_memIo_taggedFreeFuncType freeFunc;

	/** The key used to check if a tag is valid. */
	sjme_jsize checkKey;

	/** Data within the tag itself. */
	SJME_ALIGN_POINTER sjme_jbyte data[SJME_ZERO_SIZE_ARRAY];
} sjme_memTagInternal;

struct sjme_memIo_tagGroupInternal
{
	/** The parent tag group, if there is one. */
	sjme_memIo_tagGroupInternal* parent;

	/** The free function for any tag in this group. */
	sjme_memIo_taggedFreeFuncType freeFunc;

	/** Magical group number, for valid group check. */
	sjme_jsize checkKey;

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
