/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Streaming of a linear piece of data.
 * 
 * @since 2021/11/11
 */

#ifndef SQUIRRELJME_STREAM_H
#define SQUIRRELJME_STREAM_H

#include "atomic.h"
#include "counter.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_STREAM_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * This represents an individual data stream.
 * 
 * @since 2021/11/13
 */
typedef struct sjme_dataStream
{
	/** The number of bytes which have so far been read. */
	sjme_atomicInt readBytes;
	
	/** Counter for garbage collection. */
	sjme_counter count;
} sjme_dataStream;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_STREAM_H
}
#undef SJME_CXX_SQUIRRELJME_STREAM_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_STREAM_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_STREAM_H */
