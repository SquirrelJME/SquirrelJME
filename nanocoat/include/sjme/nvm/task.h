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
typedef enum sjme_nvm_task_pipeRedirectType
{
	/** Discard everything. */
	SJME_NVM_TASK_PIPE_REDIRECT_TYPE_DISCARD = 0,

	/** Store everything into a buffer. */
	SJME_NVM_TASK_PIPE_REDIRECT_TYPE_BUFFER = 1,

	/** Send everything to the terminal. */
	SJME_NVM_TASK_PIPE_REDIRECT_TYPE_TERMINAL = 2,

	/** The number of redirect types. */
	SJME_NVM_TASK_NUM_PIPE_REDIRECT_TYPES
} sjme_nvm_task_pipeRedirectType;

/**
 * The current task status.
 * 
 * @since 2024/09/30
 */
typedef enum sjme_nvm_task_statusType
{
	/** Task has exited. */
	SJME_NVM_TASK_STATUS_EXITED = 0,
	
	/** Task is alive. */
	SJME_NVM_TASK_STATUS_ALIVE = 1,
	
	/** The number of task statuses. */
	SJME_NVM_TASK_NUM_STATUS_TYPES
} sjme_nvm_task_statusType;

/**
 * The type of thread status this is.
 * 
 * @since 2024/10/18
 */
typedef enum sjme_nvm_thread_statusType
{
	/** Running. */
	SJME_NVM_THREAD_STATUS_RUNNING = 0,
	
	/** Sleeping. */
	SJME_NVM_THREAD_STATUS_SLEEPING = 1,
	
	/** Waiting on a monitor. */
	SJME_NVM_THREAD_STATUS_MONITOR_WAIT = 2,
	
	/** The number of thread statuses. */
	SJME_NVM_THREAD_NUM_STATUS_TYPES
} sjme_nvm_thread_statusType;

/**
 * Represents the frame of a stack tread.
 * 
 * @since 2023/11/15
 */
typedef struct sjme_nvm_frameTread
{
	/** The number of items in this tread. */
	sjme_jint count;
	
	/** The base index for the stack index. */
	sjme_jint stackBaseIndex;
	
	/** The maximum size this tread can be. */
	sjme_jint max;
	
	/** Values within the tread. */
	union
	{
		/** Integer values. */
		sjme_jint jints[sjme_flexibleArrayCountUnion];
		
		/** Long values. */
		sjme_jlong jlongs[sjme_flexibleArrayCountUnion];
		
		/** Float values. */
		sjme_jfloat jfloats[sjme_flexibleArrayCountUnion];
		
		/** Double values. */
		sjme_jdouble jdoubles[sjme_flexibleArrayCountUnion];
		
		/** Object references. */
		sjme_jobject jobjects[sjme_flexibleArrayCountUnion];
	} values;
} sjme_nvm_frameTread;

/**
 * Calculates the size of a frame tread for a given type.
 * 
 * @param type The type to get the size for.
 * @param count The number if items to store.
 * @return The size in bytes for the tread.
 * @since 2023/11/15
 */
#define SJME_SIZEOF_FRAME_TREAD(type, count, baseType) \
	(sizeof(sjme_nvm_frameTread) + \
	/* Need to handle cases where values could be aligned up... */ \
	(offsetof(sjme_nvm_frameTread, values.SJME_TOKEN_PASTE(baseType,s)[0]) - \
		offsetof(sjme_nvm_frameTread, values)) + \
	(sizeof(type) * (size_t)(count)))

/**
 * Calculates the size of a frame tread for a given type via variable.
 * 
 * @param typeId The type to get the size for.
 * @param count The number if items to store.
 * @return The size in bytes for the tread.
 * @since 2023/11/15
 */
static sjme_inline sjme_attrArtificial size_t SJME_SIZEOF_FRAME_TREAD_VAR(
	sjme_javaTypeId typeId, sjme_jint count)
{
	switch (typeId)
	{
		case SJME_JAVA_TYPE_ID_INTEGER:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jint, count, jint);
		
		case SJME_JAVA_TYPE_ID_LONG:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jlong, count, jlong);
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jfloat, count, jfloat);
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jdouble, count, jdouble);
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jobject, count, jobject);
	}
	
	/* Invalid. */
	return 0;
}

typedef struct sjme_nvm_frameLocalMap
{
	/** The maximum number of locals. */
	sjme_jint max;
	
	/** Mapping of a specific variable to a given type index. */
	union
	{
		sjme_jbyte to[SJME_NUM_JAVA_TYPE_IDS];
	} maps[sjme_flexibleArrayCount];
} sjme_nvm_frameLocalMap;

struct sjme_nvm_frameBase
{
	/** Common virtual machine structure. */
	sjme_nvm_commonBase common;
	
	/** The current program counter. */
	sjme_pcAddr pc;
	
	/** This class this is currently in. */
	sjme_jclass inClass;
	
	/** Treads for the stack and locals. */
	sjme_nvm_frameTread* treads[SJME_NUM_JAVA_TYPE_IDS];
	
	/** Mapping of local variables to the tread indexes per type. */
	sjme_nvm_frameLocalMap* localMap;
};

/** List of stack frames. */
SJME_LIST_DECLARE(sjme_nvm_frame, 0);

/**
 * The configuration that stores the information needed for starting the task.
 *
 * @since 2023/12/17
 */
typedef struct sjme_nvm_task_startConfig
{
	/** Redirection for standard output. */
	sjme_nvm_task_pipeRedirectType stdOut;

	/** Redirection for standard error. */
	sjme_nvm_task_pipeRedirectType stdErr;

	/** The class path to use. */
	sjme_list_sjme_nvm_rom_library* classPath;

	/** Main class to start in. */
	sjme_lpcstr mainClass;

	/** Main arguments. */
	sjme_list_sjme_lpcstr* mainArgs;

	/** System properties. */
	sjme_list_sjme_lpcstr* sysProps;
	
	/** The class loader for this task. */
	sjme_nvm_vmClass_loader classLoader;
} sjme_nvm_task_startConfig;

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
	sjme_nvm_task_statusType status;
	
	/** The threads within the current task. */
	sjme_list_sjme_nvm_thread* threads;
};

struct sjme_nvm_threadBase
{
	/** The VM state this thread is in. */
	sjme_nvm inState;
	
	/** The current thread status. */
	sjme_nvm_thread_statusType status;
	
	/** The wrapper in the front end. */
	sjme_frontEnd frontEnd;
	
	/** The thread ID. */
	sjme_jint threadId;
	
	/** The number of frames. */
	sjme_jint numFrames;
	
	/** Current stack frames. */
	sjme_list_sjme_nvm_frame* frames;
	
	/** Throwable which has been tossed in the thread. */
	sjme_jobject tossed;
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
sjme_errorCode sjme_nvm_task_start(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull const sjme_nvm_task_startConfig* startConfig,
	sjme_attrOutNullable sjme_nvm_task* outTask);

/**
 * Enters a frame within the thread.
 * 
 * @param inThread The thread to enter within.
 * @param outFrame The resultant frame.
 * @param inMethod The method being called.
 * @param argC The argument count.
 * @param argV Argument values to the call.
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_task_threadEnter(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_jmethodID inMethod,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalue* argV);

/**
 * Enters a frame within the thread.
 * 
 * @param inThread The thread to enter within.
 * @param outFrame The resultant frame.
 * @param inClass The class to execute within.
 * @param inName The name of the method.
 * @param inType The type of the method.
 * @param argC The argument count.
 * @param argV Argument values to the call.
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_task_threadEnterA(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_lpcstr inClass,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNotNull sjme_lpcstr inType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalue* argV);

/**
 * Enters a frame within the thread.
 * 
 * @param inThread The thread to enter within.
 * @param outFrame The resultant frame.
 * @param inClass The class to execute within.
 * @param inName The name of the method.
 * @param inType The type of the method.
 * @param argC The argument count.
 * @param argV Argument values to the call.
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_task_threadEnterC(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNotNull sjme_lpcstr inType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalue* argV);

/**
 * Creates a new thread within the given task.
 * 
 * @param inTask The task to create the thread in.
 * @param outThread The resultant thread.
 * @param threadName The name of the new thread.
 * @return On any errors, if any.
 * @since 2024/10/15
 */
sjme_errorCode sjme_nvm_task_threadNew(
	sjme_attrInNotNull sjme_nvm_task inTask,
	sjme_attrOutNotNull sjme_nvm_thread* outThread,
	sjme_attrInNotNull sjme_lpcstr threadName);

/**
 * Starts the specified thread.
 * 
 * @param inThread The thread to start. 
 * @return Any resultant error, if any.
 * @since 2024/10/15
 */
sjme_errorCode sjme_nvm_task_threadStart(
	sjme_attrInNotNull sjme_nvm_thread inThread);

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
