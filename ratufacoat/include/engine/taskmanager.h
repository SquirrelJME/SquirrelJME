/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Handles tasks and anything related to them within the engine.
 * 
 * @since 2022/01/08
 */

#ifndef SQUIRRELJME_TASKMANAGER_H
#define SQUIRRELJME_TASKMANAGER_H

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_TASKMANAGER_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/
 
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

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_TASKMANAGER_H
}
#undef SJME_CXX_SQUIRRELJME_TASKMANAGER_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_TASKMANAGER_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_TASKMANAGER_H */
