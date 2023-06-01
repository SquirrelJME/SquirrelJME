/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Pipe internals.
 * 
 * @since 2022/03/26
 */

#ifndef SQUIRRELJME_PIPEINTERN_H
#define SQUIRRELJME_PIPEINTERN_H

/* Anti-C++. */
#include "pipe.h"
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_PIPEINTERN_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Used to implement pipe functions within the engine that modify where the
 * result goes and otherwise.
 * 
 * @since 2022/03/15
 */
typedef struct sjme_pipeFunction
{
	/**
	 * Closes the given pipe, meaning it cannot be read anymore.
	 * 
	 * @param pipe The pipe to close.
	 * @param error If the pipe could not be closed.
	 * @return If the close was a success or not.
	 * @since 2022/03/15
	 */
	sjme_jboolean (*close)(sjme_pipeInstance* pipe, sjme_error* error);
	
	/**
	 * Flushes the given pipe.
	 * 
	 * @param pipe The pipe to flush.
	 * @param error If the pipe could not be flushed.
	 * @return If the flush was a success or not.
	 * @since 2022/03/15
	 */
	sjme_jboolean (*flush)(sjme_pipeInstance* pipe, sjme_error* error);
	
	/**
	 * Creates a new instance of a given pipe.
	 * 
	 * @param outPipe The output pipe.
	 * @param file The file descriptor of the pipe, this can be used as a hint
	 * to determine the action of a given pipe.
	 * @param isInput Is this an input pipe? An input pipe is one that is
	 * meant to be read from the task that is within.
	 * @param error If the pipe could not be created.
	 * @return If the instance was successfully created.
	 * @deprecated Do not use this.
	 * @since 2022/03/15
	 */
	SJME_DEPRECATED(sjme_jboolean (*newInstance)(sjme_pipeInstance* outPipe,
		sjme_file* file, sjme_jboolean isInput, sjme_error* error));
	
	/**
	 * Reads from the given pipe, anything that is waiting within.
	 * 
	 * @param pipe The output pipe to write to.
	 * @param direction The direction of the pipe read.
	 * @param buf The buffer to read into.
	 * @param off The offset into the buffer.
	 * @param len The number of bytes to read.
	 * @param result The result of the read, which will contain the number
	 * of bytes read or @c -1 on end of file.
	 * @param error If the write was not successful.
	 * @return If the read was successful.
	 * @since 2022/03/15
	 */
	sjme_jboolean (*read)(sjme_pipeInstance* pipe,
		sjme_pipeDirection direction, sjme_jbyte* buf,
		sjme_jint off, sjme_jint len, sjme_jint* result, sjme_error* error);
	
	/**
	 * Writes to the given pipe.
	 * 
	 * @param pipe The output pipe to write to.
	 * @param direction The direction of the pipe write.
	 * @param buf The buffer to write.
	 * @param off The offset into the buffer.
	 * @param len The number of bytes to write.
	 * @param error If the write was not successful.
	 * @return If the write was successful.
	 * @since 2022/03/15
	 */
	sjme_jboolean (*write)(sjme_pipeInstance* pipe,
		sjme_pipeDirection direction, sjme_jbyte* buf,
		sjme_jint off, sjme_jint len, sjme_error* error);
} sjme_pipeFunction;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_PIPEINTERN_H
}
		#undef SJME_CXX_SQUIRRELJME_PIPEINTERN_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_PIPEINTERN_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_PIPEINTERN_H */
