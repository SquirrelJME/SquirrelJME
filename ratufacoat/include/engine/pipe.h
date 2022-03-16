/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Pipe access and otherwise.
 * 
 * @since 2022/01/08
 */

#ifndef SQUIRRELJME_PIPE_H
#define SQUIRRELJME_PIPE_H

#include "sjmerc.h"
#include "error.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_PIPE_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Standard pipe descriptor identifiers.
 *
 * @since 2020/06/14
 */
typedef enum sjme_standardPipeType
{
	/** Standard input. */
	SJME_STANDARD_PIPE_STDIN = 0,
	
	/** Standard output. */
	SJME_STANDARD_PIPE_STDOUT = 1,
	
	/** Standard error. */
	SJME_STANDARD_PIPE_STDERR = 2,
	
	/** The number of standard pipes. */
	NUM_SJME_STANDARD_PIPES = 3,
} sjme_standardPipeType;

/**
 * Represents the direction of flow for a task.
 * 
 * @since 2022/03/15
 */
typedef enum sjme_taskPipeDirection
{
	/** The direction that is from a task, on the client side. */
	SJME_TASK_PIPE_DIRECTION_FROM_TASK,
	
	/** The direction that is from the spawner of a tax, external. */
	SJME_TASK_PIPE_DIRECTION_FROM_SPAWNER,
	
	/** A forced operation that does not check the direction. */
	SJME_TASK_PIPE_DIRECTION_FORCED,
	
	/** The number of directions. */
	NUM_SJME_TASK_PIPE_DIRECTION,
} sjme_taskPipeDirection;

/**
 * This represents the types of redirects that may occur for a launched task
 * when it is given a pipe.
 *
 * @since 2020/07/02
 */
typedef enum sjme_taskPipeRedirectType
{
	/** Discard all program output. */
	SJME_TASK_PIPE_REDIRECT_DISCARD = 0,
	
	/** Buffer the resultant program's output. */
	SJME_TASK_PIPE_REDIRECT_BUFFER = 1,
	
	/** Send the output to the virtual machine's terminal output. */
	SJME_TASK_PIPE_REDIRECT_TERMINAL = 2,
	
	/** The number of redirect types. */
	NUM_SJME_TASK_PIPE_REDIRECTS = 3
} sjme_taskPipeRedirectType;

/**
 * This represents a single instance of a pipe.
 * 
 * @since 2022/03/15
 */
typedef struct sjme_pipeInstance sjme_pipeInstance;

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
	 * @param fd The file descriptor of the pipe, this can be used as a hint
	 * to determine the action of a given pipe.
	 * @param isInput Is this an input pipe? An input pipe is one that is
	 * meant to be read from the task that is within.
	 * @param error If the pipe could not be created.
	 * @return If the instance was successfully created.
	 * @since 2022/03/15
	 */
	sjme_jboolean (*newInstance)(sjme_pipeInstance** outPipe,
		sjme_jint fd, sjme_jboolean isInput, sjme_error* error);
	
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
		sjme_taskPipeDirection direction, sjme_jbyte* buf,
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
		sjme_taskPipeDirection direction, sjme_jbyte* buf,
		sjme_jint off, sjme_jint len, sjme_error* error);
} sjme_pipeFunction;

/**
 * This is the set of pipe functions that implement a given pipe behavior.
 * 
 * @since 2022/03/15
 */
extern const sjme_pipeFunction sjme_pipeFunctions
	[NUM_SJME_TASK_PIPE_REDIRECTS];

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_PIPE_H
}
#undef SJME_CXX_SQUIRRELJME_PIPE_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_PIPE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_PIPE_H */
