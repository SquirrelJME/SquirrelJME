/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal direct memory structures.
 * 
 * @since 2023/01/04
 */

#ifndef SQUIRRELJME_MEMDIRECTINTERNAL_H
#define SQUIRRELJME_MEMDIRECTINTERNAL_H

#include "memio/memdirect.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MEMDIRECTINTERNAL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Direct header protector value. */
#define SJME_MEMIO_DIRECT_CHUNK_MAGIC INT32_C(0xB002CE77)

/**
 * Structure for internal direct memory representation.
 *
 * @since 2023/01/04
 */
typedef struct sjme_memIo_directChunk
{
	/** The size of this chunk. */
	sjme_jsize size;

	/** Magic value. */
	sjme_jsize magic;

	/** Data within the chunk, dynamically sized. */
	SJME_ALIGN_POINTER sjme_jbyte data[SJME_ZERO_SIZE_ARRAY];
} sjme_memIo_directChunk;

/**
 * Obtains the direct memory chunk.
 *
 * @param ptr The pointer to get the chunk of.
 * @param outChunk The output chunk result.
 * @param error On any errors.
 * @return If the call was successful.
 * @since 2023/01/04
 */
sjme_jboolean sjme_memIo_directGetChunk(void* ptr,
	sjme_memIo_directChunk** outChunk, sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MEMDIRECTINTERNAL_H
}
		#undef SJME_CXX_SQUIRRELJME_MEMDIRECTINTERNAL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MEMDIRECTINTERNAL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MEMDIRECTINTERNAL_H */
