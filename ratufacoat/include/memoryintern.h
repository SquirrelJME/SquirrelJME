/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal memory structures.
 * 
 * @since 2022/12/11
 */

#ifndef SQUIRRELJME_MEMORYINTERN_H
#define SQUIRRELJME_MEMORYINTERN_H

#include "memory.h"
#include "counter.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MEMORYINTERN_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The protection key. */
#define SJME_MEM_NODE_KEY UINT32_C(0x58657221)

/**
 * This represents a single node within all of the memory that has been
 * allocated and is being managed by SquirrelJME.
 *
 * @since 2022/02/20
 */
struct sjme_memNode
{
	/** The key to check if this is a valid node, is #SJME_MEM_NODE_KEY. */
	sjme_jint key;

	/** The size of this node. */
	sjme_juint nodeSize;

	/** The original allocation size. */
	sjme_juint origSize;

	/** The garbage collection count for this node. */
	sjme_counter gcCount;

	/** The callback used for freeing. */
	sjme_freeCallback freeCallback;

	/** The previous link (a @c sjme_memNode) in the chain. */
	sjme_atomicPointer prev;

	/** The next link (a @c sjme_memNode) in the chain. */
	sjme_atomicPointer next;

	/** The data stored within this node. */
	sjme_jbyte bytes[];
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MEMORYINTERN_H
}
		#undef SJME_CXX_SQUIRRELJME_MEMORYINTERN_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MEMORYINTERN_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MEMORYINTERN_H */
