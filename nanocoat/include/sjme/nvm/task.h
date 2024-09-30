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

#include "sjme/list.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/rom.h"
#include "sjme/nvm/classyVm.h"

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
	SJME_TASK_NUM_PIPE_REDIRECT_TYPES
} sjme_task_pipeRedirectType;

/**
 * The current task status.
 * 
 * @since 2024/09/30
 */
typedef enum sjme_task_statusType
{
	/** Task has exited. */
	SJME_TASK_STATUS_EXITED = 0,
	
	/** Task is alive. */
	SJME_TASK_STATUS_ALIVE = 1,
	
	/** The number of task statuses. */
	SJME_TASK_NUM_STATUS_TYPES
} sjme_task_statusType;

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
	
	/** The class loader for this task. */
	sjme_vmClass_loader classLoader;
} sjme_task_startConfig;

struct sjme_nvm_taskBase
{
	/** Common structure details. */
	sjme_nvm_commonBase common;
	
	/** The identifier of this task. */
	sjme_jint id;
	
	/** The state machine which owns this task. */
	sjme_nvm inState;
	
	/** The exit code of the task. */
	sjme_jint exitCode;
	
	/** The current task status. */
	sjme_task_statusType status;
	
	/** The threads within the current task. */
	sjme_list_sjme_nvm_thread* threads;
};

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
	sjme_attrInNotNull sjme_nvm inState,
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
