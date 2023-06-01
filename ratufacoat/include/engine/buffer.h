/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Implements a thread safe buffer which can be read from and written to, may
 * be used as a byte queue or by task/thread communication.
 *
 * Everything here must be thread safe.
 * 
 * @since 2022/12/09
 */

#ifndef SQUIRRELJME_BUFFER_H
#define SQUIRRELJME_BUFFER_H

#include "sjmerc.h"
#include "error.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_BUFFER_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Represents a buffer that can be read and written to as a queue of bytes,
 * when it is written to those bytes at the start will be able to be read
 * from in a linear fashion.
 *
 * @since 2022/12/09
 */
typedef struct sjme_buffer sjme_buffer;

/**
 * Allocates a new buffer which can be used to read and write from in a thread
 * safe manner.
 *
 * @param outBuffer The output buffer pointer.
 * @param length The length of the buffer to allocate, may be @c -1 if the
 * size of the buffer does not matter and a default will be used.
 * @param error Any error state resulting in the creation of the buffer.
 * @return If the allocation of the buffer was successful.
 * @since 2022/12/10
 */
sjme_jboolean sjme_bufferNew(sjme_buffer** outBuffer, sjme_jint length,
	sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_BUFFER_H
}
		#undef SJME_CXX_SQUIRRELJME_BUFFER_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_BUFFER_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_BUFFER_H */
