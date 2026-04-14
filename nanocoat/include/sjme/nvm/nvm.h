/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SquirrelJME NanoCoat Virtual Machine Header Definitions.
 * 
 * @since 2023/07/25
 */

#ifndef SJME_C_NVM_H
#define SJME_C_NVM_H

#include "sjme/closeable.h"
#include "sjme/config.h"
#include "sjme/stdTypes.h"
#include "sjme/tokenUtils.h"
#include "sjme/alloc.h"
#include "sjme/list.h"
#include "sjme/atomic.h"
#include "sjme/native.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_NVM_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Program counter address.
 * 
 * @since 2023/07/25
 */
typedef sjme_jint sjme_pcAddr;

/**
 * Static linkage type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jint sjme_staticLinkageType;

/** Single byte code storage type. */
typedef sjme_jubyte sjme_byteCode;

typedef union sjme_anyData
{
	/** Integer. */
	sjme_jint jint;
	
	/** Object. */
	sjme_jobject jobject;
	
	/** Temporary index. */
	sjme_tempIndex tempIndex;
} sjme_anyData;

typedef struct sjme_any
{
	/** Data type used. */
	sjme_basicTypeId type;

	/** Data stored within. */
	sjme_anyData data;
} sjme_any;

/**
 * Represents an identifier to an interface.
 * 
 * @since 2025/03/26
 */
typedef struct sjme_jinterfaceIDBase sjme_jinterfaceIDBase;

/**
 * Represents an identifier to an interface.
 * 
 * @since 2025/03/26
 */
typedef sjme_jinterfaceIDBase* sjme_jinterfaceID;

/**
 * Represents an identifier to a member.
 * 
 * @since 2025/02/26
 */
typedef struct sjme_jmemberIDBase sjme_jmemberIDBase;

/**
 * Represents an identifier to a member.
 * 
 * @since 2025/02/26
 */
typedef sjme_jmemberIDBase* sjme_jmemberID;

/**
 * Represents an identifier to a method.
 * 
 * @since 2024/10/19
 */
typedef struct sjme_jmethodIDBase sjme_jmethodIDBase;

/**
 * Represents an identifier to a method.
 * 
 * @since 2024/10/19
 */
typedef sjme_jmethodIDBase* sjme_jmethodID;

/**
 * Represents an identifier to a field.
 * 
 * @since 2025/02/26
 */
typedef struct sjme_jfieldIDBase sjme_jfieldIDBase;

/**
 * Represents an identifier to a field.
 * 
 * @since 2025/02/26
 */
typedef sjme_jfieldIDBase* sjme_jfieldID;

/** List of fields. */
SJME_LIST_DECLARE(sjme_jfieldID, 0);

/**
 * The type of structure a type is.
 * 
 * @since 2024/08/09
 */
typedef enum sjme_nvm_structType
{
	/** Unknown. */
	SJME_NVM_STRUCT_UNKNOWN,

	/** Array instance. */
	SJME_NVM_STRUCT_ARRAY_INSTANCE,

	/** A Jar package instance pointer object. */
	SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE,

	/** Pipe bracket. */
	SJME_NVM_STRUCT_BRACKET_PIPE_INSTANCE,

	/** A trace point instance pointer object. */
	SJME_NVM_STRUCT_BRACKET_TRACE_INSTANCE,
	
	/** Class information. */
	SJME_NVM_STRUCT_CLASS_INFO,
	
	/** A single class instance. */
	SJME_NVM_STRUCT_CLASS_INSTANCE,
	
	/** Method code. */
	SJME_NVM_STRUCT_CODE,

	/** Field identifier. */
	SJME_NVM_STRUCT_FIELD_ID,
	
	/** Field information. */
	SJME_NVM_STRUCT_FIELD_INFO,
	
	/** Thread frame. */
	SJME_NVM_STRUCT_FRAME,
	
	/** Identifier. */
	SJME_NVM_STRUCT_IDENTIFIER,

	/** Interface binding. */ 
	SJME_NVM_STRUCT_INTERFACE_ID,
	
	/** The classes a class is. */
	SJME_NVM_STRUCT_IS_CLASSES,
	
	/** Method binding. */
	SJME_NVM_STRUCT_METHOD_ID,
	
	/** Method information. */
	SJME_NVM_STRUCT_METHOD_INFO,
	
	/** Object Instance. */
	SJME_NVM_STRUCT_OBJECT_INSTANCE,
	
	/** Rom Library. */
	SJME_NVM_STRUCT_ROM_LIBRARY,
	
	/** Rom Suite. */
	SJME_NVM_STRUCT_ROM_SUITE,
	
	/** Constant pool. */
	SJME_NVM_STRUCT_POOL,
	
	/** NanoCoat state. */
	SJME_NVM_STRUCT_STATE,

	/** A string instance. */
	SJME_NVM_STRUCT_STRING_INSTANCE,
	
	/** A string pool. */
	SJME_NVM_STRUCT_STRING_POOL,
	
	/** A string in the string pool. */
	SJME_NVM_STRUCT_STRING_POOL_STRING,
	
	/** A single task. */
	SJME_NVM_STRUCT_TASK,

	/** Task intern strings. */
	SJME_NVM_STRUCT_TASK_STRINGS,
	
	/** A single thread. */
	SJME_NVM_STRUCT_THREAD_INSTANCE,
	
	/** Class loader. */
	SJME_NVM_STRUCT_VM_CLASS_LOADER,

	/** An instance of a @c Reference . */
	SJME_NVM_STRUCT_WEAK_INSTANCE,
	
	/** The number of structure types. */
	SJME_NVM_NUM_STRUCT,

	/** Any object instance. */
	SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE = SJME_NVM_NUM_STRUCT + 3
} sjme_nvm_structType;

/**
 * Common data structure between all NanoCoat types.
 * 
 * @since 2024/08/09
 */
typedef struct sjme_nvm_commonBase sjme_nvm_commonBase;

/**
 * Common data structure pointer.
 * 
 * @since 2024/08/10
 */
typedef sjme_nvm_commonBase* sjme_nvm_common;

/** Cast to array. */
#define SJME_AS_JARRAY(x) ((sjme_jarray)(x))

/** Cast to array pointer. */
#define SJME_AS_JARRAYP(x) ((sjme_jarray*)(x))
	
/** Cast to object. */
#define SJME_AS_JOBJECT(x) ((sjme_jobject)(x))

/** Cast to pointer to object. */
#define SJME_AS_JOBJECTP(x) ((sjme_jobject*)(x))
	
/** Cast to class. */
#define SJME_AS_JCLASS(x) ((sjme_jclass)(x))

/** Cast to pointer to object. */
#define SJME_AS_JCLASSP(x) ((sjme_jclass*)(x))

/** As a member ID. */
#define SJME_AS_JMEMBERID(x) ((sjme_jmemberID)(x))

/** Cast to common type. */
#define SJME_AS_NVM_COMMON(x) ((sjme_nvm_common)(x))

/** Cast to common pointer type. */
#define SJME_AS_NVM_COMMONP(x) ((sjme_nvm_common*)(x))

/**
 * Represents the virtual machine state.
 * 
 * @since 2023/08/08
 */
typedef struct sjme_nvm_stateBase sjme_nvm_stateBase;

/**
 * Represents the virtual machine state.
 * 
 * @since 2023/07/28
 */
typedef sjme_nvm_stateBase* sjme_nvm;

/**
 * Frame of execution within a thread.
 * 
 * @since 2023/08/08
 */
typedef struct sjme_nvm_frameBase sjme_nvm_frameBase;

/**
 * Frame of execution within a thread.
 * 
 * @since 2023/07/25
 */
typedef sjme_nvm_frameBase* sjme_nvm_frame;

/**
 * Base structure for virtual machine threads.
 * 
 * @since 2024/08/08
 */
typedef struct sjme_nvm_threadBase sjme_nvm_threadBase;

/**
 * A thread within SquirrelJME.
 * 
 * @since 2024/08/08
 */
typedef sjme_nvm_threadBase* sjme_nvm_thread;

/** List of threads. */
SJME_LIST_DECLARE(sjme_nvm_thread, 0);

/**
 * Hook for garbage collection detection and/or cancel capability.
 * 
 * @param frame The frame this is garbage collecting in.
 * @param gcWhat what is being garbage collected?
 * @return Returns @c SJME_JNI_TRUE if garbage collection should continue.
 * @since 2023/11/17
 */
typedef sjme_jboolean (*sjme_nvm_stateHookGcFunc)(sjme_nvm_frame frame,
	sjme_jobject gcWhat);

/**
 * Hooks for alternative function.
 * 
 * @since 2023/11/17
 */
typedef struct sjme_nvm_stateHooks
{
	/** Garbage collection. */
	sjme_nvm_stateHookGcFunc gc;
} sjme_nvm_stateHooks;

/**
 * Boot parameters for NanoCoat.
 *
 * @since 2023/07/27
 */
typedef struct sjme_nvm_bootParam sjme_nvm_bootParam;

/**
 * Standard Suite structure.
 *
 * @since 2023/12/12
 */
typedef struct sjme_nvm_rom_suiteBase sjme_nvm_rom_suiteBase;

/**
 * Opaque suite structure type.
 *
 * @since 2023/12/22
 */
typedef sjme_nvm_rom_suiteBase* sjme_nvm_rom_suite;

/**
 * Structure for a single task.
 *
 * @since 2023/12/17
 */
typedef struct sjme_nvm_taskBase sjme_nvm_taskBase;
/**
 * Structure for a single task.
 *
 * @since 2023/12/17
 */
typedef sjme_nvm_taskBase* sjme_nvm_task;
	
/** List of tasks. */
SJME_LIST_DECLARE(sjme_nvm_task, 0);
	
/**
 * The configuration that stores the information needed for starting the task.
 *
 * @since 2023/12/17
 */
typedef struct sjme_nvm_task_taskNewConfig sjme_nvm_task_taskNewConfig;

struct sjme_nvm_commonBase
{
	/** Closeable for this NanoCoat object. */
	sjme_closeableBase closeable;
	
	/** The type of item this is. */
	sjme_nvm_structType type;

	/** The magic number for NVM objects. */
	sjme_jint magic;
	
	/** The wrapper in the front end. */
	sjme_frontEnd frontEnd;
	
	/** The lock to access this common item. */
	sjme_alignPointer sjme_thread_spinLock lock;

	/** Specific close handler. */
	sjme_closeable_closeHandlerFunc specificClose;
};

/**
 * The schedule mode for threads.
 *
 * @since 2025/01/05
 */
typedef enum sjme_nvm_threadScheduleMode
{
	/** Thread is undefined schedule. */
	SJME_NVM_THREAD_UNDEFINED_SCHEDULE = 0,
	
	/** Thread is scheduled. */
	SJME_NVM_THREAD_SCHEDULED = 1,

	/** Thread is unscheduled. */
	SJME_NVM_THREAD_UNSCHEDULED = 2,

	/** The number of scheduled modes. */
	SJME_NVM_THREAD_NUM_SCHEDULE_MODE = 3,
} sjme_nvm_threadScheduleMode;

/**
 * Sub-schedule for thread scheduling.
 *
 * @since 2025/01/06
 */
typedef struct sjme_nvm_threadSubSchedule
{
	/** The number of scheduled threads. */
	sjme_jint count;
		
	/** The list of threads in order. */
	sjme_list_sjme_nvm_thread* order;
} sjme_nvm_threadSubSchedule;
	
/**
 * Thread scheduling system per mode.
 * 
 * @since 2025/01/05
 */
typedef struct sjme_nvm_threadSchedule
{
	/** The thread model in use. */
	sjme_nvm_mle_threadModel model;
	
	/** The lock for scheduling. */
	sjme_thread_spinLock lock;
	
	/** The schedules for each mode. */
	sjme_nvm_threadSubSchedule mode[SJME_NVM_THREAD_NUM_SCHEDULE_MODE];
} sjme_nvm_threadSchedule;

/**
 * The state and/or task termination level.
 *
 * @since 2025/06/29
 */
typedef enum sjme_nvm_terminateLevel
{
	/** Not terminating. */
	SJME_NVM_TERMINATE_NOT = 0,

	/** Entering the cleanup phase. */
	SJME_NVM_TERMINATE_CLEANUP = 1,

	/** Done terminating. */
	SJME_NVM_TERMINATE_COMPLETE = 2,
} sjme_nvm_terminateLevel;
	
/**
 * Determines how initial boot is belayed.
 *
 * @since 2025/07/15
 */
typedef enum sjme_nvm_bootBelayType
{
	/** No belay on boot. */
	SJME_NVM_BOOT_BELAY_NONE = 0,

	/** Belay enter of main entry point. */
	SJME_NVM_BOOT_BELAY_MAIN = 1,

	/** Belay creation of task. */
	SJME_NVM_BOOT_BELAY_TASK = 2,

	/** Belay creation of task and main. */
	SJME_NVM_BOOT_BELAY_TASK_MAIN = 3,
} sjme_nvm_bootBelayType;

/**
 * The clutter level to use.
 *
 * @since 2025/07/15
 */
typedef enum sjme_nvm_bootClutterLevel
{
	/** Release clutter level. */
	SJME_NVM_BOOT_CLUTTER_RELEASE = 0,
	
	/** Debug clutter level. */
	SJME_NVM_BOOT_CLUTTER_DEBUG = 1,
} sjme_nvm_bootClutterLevel;
	
struct sjme_nvm_stateBase
{
	/** Common data. */
	sjme_nvm_commonBase common;
	
	/** The memory pool to use for all allocations. */
	sjme_alloc_pool allocPool;

	/** The copy of the input boot parameters. */
	const sjme_nvm_bootParam* bootParamCopy;
	
	/** Hooks for the state. */
	const sjme_nvm_stateHooks* hooks;

	/** The native abstraction layer to use. */
	const sjme_nal* nal;

	/** The suite containing all the libraries. */
	sjme_nvm_rom_suite suite;
	
	/** The tasks that are currently existing. */
	sjme_list_sjme_nvm_task* tasks;

	/** The number of running tasks. */
	sjme_atomic_sjme_jint numRunningTasks;
	
	/** The next identifier for tasks. */
	sjme_atomic_sjme_jint nextTaskId;
	
	/** The next identifier for tasks. */
	sjme_atomic_sjme_jint nextThreadId;
	
	/** The thread model in use. */
	sjme_nvm_mle_threadModel threadModel;

	/** The thread schedule. */
	sjme_nvm_threadSchedule* schedule;
	
	/** The state @c sjme_nvm_terminateLevel ? */
	sjme_atomic_sjme_jint terminating;

	/** The initial task configuration. */
	sjme_nvm_task_taskNewConfig* initTaskConfig;

	/** The last emitted exit code. */
	sjme_atomic_sjme_jint lastExitCode;
};

/**
 * Specifies how the PC address should be adjusted.
 *
 * @since 2025/01/11
 */
typedef struct sjme_nvm_byteCode_pcNew sjme_nvm_byteCode_pcNew;

/**
 * Standard ROM library structure.
 *
 * @since 2023/12/12
 */
typedef struct sjme_nvm_rom_libraryBase sjme_nvm_rom_libraryBase;

/**
 * Standard ROM library structure.
 *
 * @since 2023/12/12
 */
typedef sjme_nvm_rom_libraryBase* sjme_nvm_rom_library;

/** List of ROM libraries. */
SJME_LIST_DECLARE(sjme_nvm_rom_library, 0);

/** The type ID of ROM libraries. */
#define SJME_TYPEOF_BASIC_sjme_nvm_rom_library SJME_BASIC_TYPE_ID_OBJECT

/** Type size multiplier. */
extern const sjme_jint sjme_nvm_typeMul[SJME_NUM_BASIC_TYPE_IDS];

/** Type promotion. */
extern const sjme_basicTypeId sjme_nvm_typePromote[SJME_NUM_BASIC_TYPE_IDS];

/**
 * Method initialization start.
 *
 * @since 2023/07/25
 */
#define SJME_NANOCOAT_START_CALL ((sjme_pcAddr)-1)

/**
 * Method closing end.
 *
 * @since 2023/07/25
 */
#define SJME_NANOCOAT_END_CALL ((sjme_pcAddr)-2)

/** Casts pointer to a pointer to a @c sjme_jstring . */
#define SJME_AS_JSTRINGP(p) ((sjme_jstring*)(p))
	
/**
 * Allows for optional debug abort when a virtual machine error occurs
 * within the virtual machine.
 *
 * @param vmContext Virtual machine context.
 * @param error The error code to use for the virtual machine error.
 * @return One of the virtual machine error related error codes.
 * @since 2025/02/16
 */
sjme_errorCode sjme_error_vmErrorR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull void* vmContext,
	sjme_attrInValue sjme_errorCode error);

/**
 * Allows for optional debug abort when a virtual machine error occurs
 * within the virtual machine.
 *
 * @param vmContext Virtual machine context.
 * @param error The error code to use for the virtual machine error.
 * @return One of the virtual machine error related error codes.
 * @since 2025/02/16
 */
#define sjme_error_vmError(vmContext, error) \
	sjme_error_vmErrorR(SJME_DEBUG_FILE_LINE_FUNC_ALWAYS, \
	(vmContext), (error))
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_NVM_H
}
		#undef SJME_CXX_SQUIRRELJME_NVM_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_NVM_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_NVM_H */
