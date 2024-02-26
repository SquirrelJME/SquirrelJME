/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Tasks support.
 * 
 * @since 2023/07/29
 */

#ifndef SQUIRRELJME_TASK_H
#define SQUIRRELJME_TASK_H

#include "list.h"
#include "sjme/nvm.h"
#include "sjme/rom.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_TASK_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The type of redirection to use for pipes.
 *
 * @since 2023/12/17
 */
typedef enum sjme_task_pipeRedirectType
{
	/** Discard everything. */
	SJME_TASK_PIPE_REDIRECT_TYPE_DISCARD = 0,

	/** Store everything into a buffer. */
	SJME_TASK_PIPE_REDIRECT_TYPE_BUFFER = 1,

	/** Send everything to the terminal. */
	SJME_TASK_PIPE_REDIRECT_TYPE_TERMINAL = 2,

	/** The number of redirect types. */
	SJME_NUM_TASK_PIPE_REDIRECT_TYPES
} sjme_task_pipeRedirectType;

/**
 * The configuration that stores the information needed for starting the task.
 *
 * @since 2023/12/17
 */
typedef struct sjme_task_startConfig
{
	/** Redirection for standard output. */
	sjme_task_pipeRedirectType stdOut;

	/** Redirection for standard error. */
	sjme_task_pipeRedirectType stdErr;

	/** The class path to use. */
	sjme_list_sjme_rom_library* classPath;

	/** Main class to start in. */
	sjme_lpcstr mainClass;

	/** Main arguments. */
	sjme_list_sjme_lpcstr* mainArgs;

	/** System properties. */
	sjme_list_sjme_lpcstr* sysProps;
} sjme_task_startConfig;

/**
 * Starts the task.
 *
 * @param inState The input state.
 * @param startConfig The start configuration for this task.
 * @param outTask The resultant task.
 * @return Any error state.
 * @since 2023/12/17
 */
sjme_errorCode sjme_task_start(
	sjme_attrInNotNull sjme_nvm_state* inState,
	sjme_attrInNotNull const sjme_task_startConfig* startConfig,
	sjme_attrOutNullable sjme_nvm_task* outTask);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_TASK_H
}
		#undef SJME_CXX_SQUIRRELJME_TASK_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_TASK_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_TASK_H */
