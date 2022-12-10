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
#include "frontend/frontdef.h"
#include "file.h"
#include "engine/buffer.h"

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
	SJME_NUM_STANDARD_PIPES = 3,
} sjme_standardPipeType;

/**
 * Represents the direction of flow for a task.
 *
 * @deprecated Pipe direction not needed anymore.
 * @since 2022/03/15
 */
typedef enum SJME_DEPRECATED(sjme_pipeDirection)
{
	/** The direction that is from a task, on the client side. */
	SJME_PIPE_DIRECTION_FROM_TASK,
	
	/** The direction that is from the spawner of a tax, external. */
	SJME_PIPE_DIRECTION_FROM_SPAWNER,
	
	/** A forced operation that does not check the direction. */
	SJME_PIPE_DIRECTION_FORCED,
	
	/** The number of directions. */
	NUM_SJME_PIPE_DIRECTIONS,
} SJME_DEPRECATED(sjme_pipeDirection);

/**
 * This represents a single instance of a pipe.
 * 
 * @since 2022/03/15
 */
typedef struct sjme_pipeInstance sjme_pipeInstance;

/**
 * Deletes the given pipe.
 *
 * @param inPipe The pipe to delete.
 * @param error Any errors that may occur.
 * @return If the pipe deletion was successful.
 * @since 2022/12/09
 */
sjme_jboolean sjme_pipeDelete(sjme_pipeInstance* inPipe,
	sjme_error* error);

/**
 * Creates a pipe that may read and/or write into the given buffer.
 *
 * @param buffer The buffer that will be used for pipe communication.
 * @param isReadable Can the buffer be read from via this pipe?
 * @param isWritable Can the buffer be written to via this pipe?
 * @param outPipe The resultant pipe.
 * @param error The error indicating why the pipe could not be created.
 * @return If the pipe was successfully created or not.
 * @since 2022/12/09
 */
sjme_jboolean sjme_pipeNewFromBuffer(sjme_buffer* buffer,
	sjme_jboolean isReadable, sjme_jboolean isWritable,
	sjme_pipeInstance** outPipe, sjme_error* error);

/**
 * Creates a new pipe that may read and/or write to the given specified file.
 *
 * @param file The file used to setup the pipe with, capability to read and/or
 * write the pipe depends on the file.
 * @param outPipe The resultant pipe.
 * @param error The error indicating why the pipe could not be created.
 * @return If the pipe was successfully created or not.
 * @since 2022/12/09
 */
sjme_jboolean sjme_pipeNewFromFile(sjme_file* file,
	sjme_pipeInstance** outPipe, sjme_error* error);

/**
 * Creates a new null pipe which does not contain any data nor keeps any
 * information that is written to it.
 *
 * @param isReadable Should the pipe be readable, always producing EOF?
 * @param isWritable Should the pipe be writable, always discarding data?
 * @param outPipe The resultant pipe.
 * @param error The error indicating why the pipe could not be created.
 * @return If the pipe was successfully created or not.
 * @since 2022/12/09
 */
sjme_jboolean sjme_pipeNewNull(sjme_jboolean isReadable,
	sjme_jboolean isWritable, sjme_pipeInstance** outPipe, sjme_error* error);

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
