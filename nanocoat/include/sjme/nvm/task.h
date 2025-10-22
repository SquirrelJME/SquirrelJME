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
 * @file
 * @since 2023/07/29
 */

#ifndef SJME_C_TASK_H
#define SJME_C_TASK_H

#include "sjme/list.h"
#include "sjme/util.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/rom.h"
#include "sjme/nvm/classyVm.h"
#include "sjme/nvm/mleConst.h"
#include "sjme/nvm/mleBrackets.h"

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
 * Represents the starting state of a thread.
 *
 * @since 2025/01/04
 */
typedef enum sjme_nvm_thread_startType
{
	/** Thread was never started. */
	SJME_NVM_THREAD_START_NEVER = 0,

	/** Standard thread start. */
	SJME_NVM_THREAD_START_STANDARD = 1,

	/** Thread is in the finishing state, it is stopping execution. */
	SJME_NVM_THREAD_START_FINISHING = 2,

	/** Thread finished execution. */
	SJME_NVM_THREAD_START_FINISHED = 3,

	/** Callback thread which can reach zero frames and not be finished. */
	SJME_NVM_THREAD_START_CALLBACK = 4,

	/** The number of thread start types. */
	SJME_NVM_NUM_THREAD_START_TYPES = 5,
} sjme_nvm_thread_startType;
	
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
 * Interned task strings.
 *
 * @since 2025/01/25
 */
typedef struct sjme_nvm_taskStringsBase sjme_nvm_taskStringsBase;

/**
 * Interned task strings.
 *
 * @since 2025/01/25
 */
typedef sjme_nvm_taskStringsBase* sjme_nvm_taskStrings;
	
/**
 * Contains information on all the thread stack frames.
 *
 * @since 2025/02/10
 */
typedef struct sjme_frame_threadStacks sjme_frame_threadStacks;
	
/**
 * Contains information on the stack framing information.
 *
 * @since 2025/01/27
 */
typedef struct sjme_frame_frameStack sjme_frame_frameStack;
	
/**
 * Contains information on all the frame stack information. 
 *
 * @since 2025/02/10
 */
typedef struct sjme_frame_frameStacks sjme_frame_frameStacks;

/**
 * This is called when a frame is waiting for a condition to be met.
 *
 * @param inFrame The frame that is waiting on the condition.
 * @param condition The condition value that was passed in, this may be
 * anything.
 * @param stackPush If the condition should push any value to the stack,
 * then this should be set to a value other than void.
 * @return Any resultant error, if
 * any, @link SJME_ERROR_NOT_MATCHED @endlink means that the condition has not
 * been met yet.
 * @since 2025/10/02
 */
typedef sjme_errorCode (*sjme_nvm_frame_conditionFunc)(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInValue sjme_intPointer condition,
	sjme_attrOutNotNull sjme_jvalueTyped* stackPush);
	
struct sjme_frame_threadStacks
{
	/** The storage for the stack. */
	sjme_pointer storage;

	/** The number of bytes used for storage. */
	sjme_jint storageLen;

	/** The address top of the stack top. */
	sjme_jint storageTop;
};
	
struct sjme_frame_frameStack
{
	/** The top of the stack frame. */
	sjme_jint top;

	/** The front of the stack, anything before are local variables. */
	sjme_jint front;

	/** The length of this stack. */
	sjme_jint length;
	
	/** Pointer bases for the type on the frame. */
	sjme_alignPointer union
	{
		/** Pointer base. */
		sjme_pointer base;
		
		/** Integer values. */
		sjme_jint* i;

		/** Long values. */
		sjme_jlong* j;

		/** Float values. */
		sjme_jfloat* f;

		/** Double values. */
		sjme_jdouble* d;

		/** Object values. */
		sjme_jobject* l;
	} base;
};

/** The object check stack type. */
#define SJME_NVM_STACK_OBJECT_CHECK_ID SJME_NUM_JAVA_TYPE_IDS

/** Final stack indicator. */
#define SJME_NVM_STACK_FINAL_ID (SJME_NVM_STACK_OBJECT_CHECK_ID + 1)

struct sjme_frame_frameStacks
{
	/** The number of bytes claimed for this frame. */
	sjme_jint storageClaim;
	
	/** Stack framing information. */
	sjme_frame_frameStack stack[SJME_NVM_STACK_FINAL_ID];

	/** The order of the stack. */
	sjme_javaTypeId* order;

	/** The front of the stack, anything before this are local variables. */
	sjme_jint orderFront;

	/** The top of the order stack. */
	sjme_jint orderTop;

	/** The maximum size of the order. */
	sjme_jint orderLength;
};

/** The number of items to store in a current GC commit. */
#define SJME_NVM_FRAME_NUM_GC_COMMIT 4

/**
 * Garbage collection commit for stack popping and otherwise.
 *
 * @since 2025/07/20
 */
typedef struct sjme_nvm_frame_gcCommit sjme_nvm_frame_gcCommit;

struct sjme_nvm_frame_gcCommit
{
	/** The objects waiting to be garbage collected. */
	struct
	{
		/** The object that is waiting. */
		sjme_jobject l;

		/** The number of times it should be garbage collected. */
		sjme_jint count;
	} objects[SJME_NVM_FRAME_NUM_GC_COMMIT];

	/** Is this a dynamically allocated commit? */
	sjme_jboolean isDynamic;

	/** The previous commit in the chain. */
	sjme_nvm_frame_gcCommit* prev;

	/** The next commit in the chain if there are more items. */
	sjme_nvm_frame_gcCommit* next;
};

/**
 * The type of GC consideration to make.
 *
 * @since 2025/07/20
 */
typedef enum sjme_nvm_frame_considerGc
{
	/** Do not garbage collect. */
	SJME_NVM_FRAME_CONSIDER_GC_NONE,

	/** Always GC. */
	SJME_NVM_FRAME_CONSIDER_GC_ALWAYS,

	/** Use commit based GC. */
	SJME_NVM_FRAME_CONSIDER_GC_COMMIT,
} sjme_nvm_frame_considerGc;

/**
 * Frame state flags.
 *
 * @since 2025/10/21
 */
typedef enum sjme_nvm_frame_stateFlags
{
	/** Is this a static initializer? */
	SJME_NVM_FRAME_STATE_INIT_STATIC = SJME_NVM_CLASS_INIT_STATIC,

	/** Is this an instance initializer? */
	SJME_NVM_FRAME_STATE_INIT_INSTANCE = SJME_NVM_CLASS_INIT_INSTANCE,

	/** Is this any static initializer? */
	SJME_NVM_FRAME_STATE_INIT_ANY = SJME_NVM_CLASS_INIT_ANY,
	
	/** Enter synchronization was performed. */
	SJME_NVM_FRAME_STATE_SYNC_ENTER = INT8_C(0x4),

	/** Exit synchronization was performed. */
	SJME_NVM_FRAME_STATE_SYNC_EXIT = INT8_C(0x8),
} sjme_nvm_frame_stateFlags;

/**
 * Checks if the given bits set a flag state for a frame.
 * 
 * @param bits The bits to check.
 * @param x The check to make.
 * @return Boolean of whether the given bit is set.
 * @since 2025/10/21
 */
#define SJME_NVM_FRAME_STATE_IS(bits, x) \
	(((bits) & SJME_TOKEN_PASTE_PP(SJME_NVM_FRAME_STATE_, x)) != 0)

struct sjme_nvm_frameBase
{
	/** Common virtual machine structure. */
	sjme_nvm_commonBase common;

	/** The state this frame is in. */
	sjme_phantom(sjme_nvm) inState;

	/** The thread this frame is in. */
	sjme_phantom(sjme_nvm_thread) inThread;

	/** The task this is in. */
	sjme_phantom(sjme_nvm_task) inTask;

	/** The parent frame. */
	sjme_phantom(sjme_nvm_frame) parent;
	
	/** The current program counter. */
	sjme_pcAddr pc;
	
	/** This class this is currently in. */
	sjme_jclass inClass;

	/** The method ID of the current method. */
	sjme_jmethodID inMethod;
	
	/** The constant pool of this class. */
	sjme_nvm_class_poolInfo pool;

	/** The code this is executing within. */
	sjme_nvm_class_codeInfo inCode;

	/** Stack information for the frame. */
	sjme_frame_frameStacks stack;

	/** The instance object or class. */
	sjme_jobject instance;
	
	/** The last PC address. */
	sjme_jint lastPc;

	/** The last IV. */
	sjme_byteCode lastIv;

	/** The ID of this frame, used for JDWP and tracing. */
	sjme_jint id;

	/** Phantom tracepoint reference, for recycling. */
	sjme_phantom(sjme_jbracketTrace) phantomTracePoint;

	/** The index of this frame. */
	sjme_jint index;

	/** Waiting condition. */
	struct
	{
		/** The function to call for the condition. */
		sjme_nvm_frame_conditionFunc function;
	} condition;

	/** Frame state flags. */
	sjme_nvm_frame_stateFlags flags;
};

/** List of stack frames. */
SJME_LIST_DECLARE(sjme_nvm_frame, 0);

struct sjme_nvm_task_taskNewConfig
{
	/** Redirection for standard output. */
	sjme_nvm_task_pipeRedirectType stdOut;

	/** Redirection for standard error. */
	sjme_nvm_task_pipeRedirectType stdErr;

	/** The class path to use. */
	sjme_list(sjme_nvm_rom_library)* classPath;

	/** Main class to start in. */
	sjme_lpcstr mainClass;

	/** Main arguments. */
	const sjme_list(sjme_lpcstr)* mainArgs;

	/** System properties. */
	const sjme_list(sjme_lpcstr)* sysProps;
	
	/** The class loader for this task. */
	sjme_nvm_vmClass_loader classLoader;

	/** The classpath is a strong reference. */
	sjme_jboolean strongClassPath;

	/** The belay for the task. */
	sjme_nvm_bootBelayType belay;

	/** Do not optimize. */
	sjme_jboolean noOptimize;
};

struct sjme_nvm_taskStringsBase
{
	/** Common structure details. */
	sjme_nvm_commonBase common;

	/** The interned strings. */
	sjme_list(sjme_jstring)* interns;
};

/**
 * A class that is very commonly used.
 *
 * @since 2025/03/20
 */
typedef enum sjme_nvm_task_commonClassId
{
	/** Null class. */
	SJME_NVM_TASK_COMMON_CLASS_NULL,
	
	/** @c java.lang.Class . */
	SJME_NVM_TASK_COMMON_CLASS_CLASS,

	/** @c java.lang.ClassCastException. */
	SJME_NVM_TASK_COMMON_CLASS_EXCEPTION_CLASS_CAST,

	/** @c java.lang.LinkageError . */
	SJME_NVM_TASK_COMMON_CLASS_EXCEPTION_LINKAGE_ERROR,

	/** @c cc.squirreljme.jvm.mle.brackets.JarPackageBracket . */
	SJME_NVM_TASK_COMMON_CLASS_JAR_PACKAGE,
	
	/** @c java.lang.Object . */
	SJME_NVM_TASK_COMMON_CLASS_OBJECT,

	/** @c cc.squirreljme.jvm.mle.brackets.PipeBracket . */
	SJME_NVM_TASK_COMMON_CLASS_PIPE,
	
	/** @c boolean . */
	SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_BOOLEAN,
	
	/** @c byte . */
	SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_BYTE,
	
	/** @c char . */
	SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_CHARACTER,
	
	/** @c double . */
	SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_DOUBLE,
	
	/** @c float . */
	SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_FLOAT,
	
	/** @c int . */
	SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_INTEGER,
	
	/** @c long . */
	SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_LONG,
	
	/** @c short . */
	SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_SHORT,
	
	/** @c void . */
	SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_VOID,

	/** @c java.lang.ref.PhantomReference . */
	SJME_NVM_TASK_COMMON_CLASS_REFERENCE_PHANTOM,

	/** @c java.lang.ref.SoftReference . */
	SJME_NVM_TASK_COMMON_CLASS_REFERENCE_SOFT,

	/** @c java.lang.ref.WeakReference . */
	SJME_NVM_TASK_COMMON_CLASS_REFERENCE_WEAK,
	
	/** @c java.lang.String . */
	SJME_NVM_TASK_COMMON_CLASS_STRING,

	/** @c java.lang.Thread . */
	SJME_NVM_TASK_COMMON_CLASS_THREAD,

	/** @c java.lang.Throwable . */
	SJME_NVM_TASK_COMMON_CLASS_THROWABLE,

	/** @c cc.squirreljme.jvm.mle.brackets.TracePointBracket . */
	SJME_NVM_TASK_COMMON_CLASS_TRACE_POINT,

	/** The number of common classes. */
	SJME_NVM_TASK_NUM_COMMON_CLASS
} sjme_nvm_task_commonClassId;

/** A list of Jar package brackets. */ 
SJME_LIST_DECLARE(sjme_jbracketJarPackage, 0);
	
/**
 * Globals for the task.
 *
 * @since 2025/02/23
 */
typedef struct sjme_nvm_task_globals
{
	/** The lock for global access. */
	sjme_thread_spinLock lock;

	/** The standard pipes for standard IO. */
	sjme_jbracketPipe stdPipes[SJME_NVM_MLE_NUM_STD_PIPES];

	/** Main class name to start in. */
	sjme_jstring mainClassName;

	/** Main arguments, as objects. */
	sjme_list(sjme_jstring)* mainArgs;

	/** Common classes. */
	sjme_atomic(sjme_jclass) commonClasses[SJME_NVM_TASK_NUM_COMMON_CLASS];

	/** The default accessor for fields. */
	sjme_nvm_jfieldAccessFunc accessor;

	/** Cached @link sjme_jbracketJarPackage @endlink for libraries. */
	sjme_list(sjme_jbracketJarPackage)* jarBrackets;
	
	/** The main thread. */
	sjme_atomic(sjme_nvm_thread) mainThread;

	/** No optimization? */
	sjme_jboolean noOptimize;
} sjme_nvm_task_globals;

typedef enum sjme_nvm_task_threadCountType
{
	/** All. */
	SJME_NVM_THREAD_COUNT_ALL = 0,
	
	/** Non-daemon. */
	SJME_NVM_THREAD_COUNT_NORMAL = 1,
	
	/** Daemon. */
	SJME_NVM_THREAD_COUNT_DAEMON = 2,
	
	/** Await terminate. */
	SJME_NVM_THREAD_COUNT_AWAIT_CLEANUP = 3,

	/** Count for the main thread. */
	SJME_NVM_THREAD_COUNT_MAIN = 4,

	/** The number of thread counts. */
	SJME_NVM_THREAD_NUM_COUNT_TYPE = 5,
} sjme_nvm_task_threadCountType;
	
struct sjme_nvm_taskBase
{
	/** The base object for the task. */
	sjme_jobjectBase object;
	
	/** The identifier of this task. */
	sjme_jint id;
	
	/** The state machine which owns this task. */
	sjme_phantom(sjme_nvm) inState;
	
	/** The exit code of the task. */
	sjme_atomic(sjme_jint) exitCode;
	
	/** The current task status. */
	sjme_nvm_task_statusType status;

	/** Task @link sjme_nvm_terminateLevel @endlink level. */
	sjme_atomic(sjme_jint) terminate;

	/** The number of threads based on the count. */
	sjme_atomic(sjme_jint) numThreads[SJME_NVM_THREAD_NUM_COUNT_TYPE];
	
	/** The threads within the current task. */
	sjme_list(sjme_nvm_thread)* threads;
	
	/** The class loader for this specific task. */
	sjme_nvm_vmClass_loader classLoader;

	/** Internal strings for the task. */
	sjme_nvm_taskStrings strings;

	/** Globals for the task. */
	sjme_nvm_task_globals globals;

	/** The next frame ID for this task, used for JDWP and debugging. */
	sjme_atomic(sjme_jint) nextFrameId;

	/** The task initialization configuration. */
	const sjme_nvm_task_taskNewConfig* initConfig;

	/** The identity hashcode generator. */
	sjme_random idHash;

	/** Is this the main task. */
	sjme_jboolean isMain;
};

struct sjme_nvm_threadBase
{
	/** The base object for the thread. */
	sjme_jobjectBase object;
	
	/** The VM state this thread is in. */
	sjme_phantom(sjme_nvm) inState;
	
	/** The owning task. */
	sjme_phantom(sjme_nvm_task) inTask;

	/** The @link sjme_nvm_thread_startType @endlink of the thread. */
	sjme_atomic(sjme_jint) start;
	
	/** The current thread status. */
	sjme_nvm_thread_statusType status;
	
	/** The wrapper in the front end. */
	sjme_frontEnd frontEnd;

	/** The native thread, if applicable. */
	sjme_atomic(sjme_thread) nativeThread;
	
	/** The thread ID. */
	sjme_jint threadId;

	/** Is this the main thread? */
	sjme_jboolean isMain;

	/** The @c java.lang.Thread this is bound to. */
	sjme_jobject vmObject;
	
	/** The number of valid frames. */
	sjme_jint numFrames;
	
	/** The stack frames. */
	sjme_list(sjme_nvm_frame)* frames;

	/** The stack information for the entire thread. */
	sjme_frame_threadStacks stack;

	/** What is the @link sjme_nvm_threadScheduleMode @endlink of this thread? */
	sjme_atomic(sjme_jint) scheduleMode;

	/** A @c Throwable which has been thrown. */
	sjme_atomic(sjme_jobject) tossed;

	/** The current frame level that the throwable was tossed at. */
	sjme_atomic(sjme_jint) tossedLevel;

	/** If this thread is interrupted. */
	sjme_atomic(sjme_jint) interrupted;

	/** Thread specific flags. */
	struct
	{
		/** Is this a daemon thread? */
		sjme_jboolean isDaemon : sjme_booleanBit;
	} flags;
};

/**
 * Stores the state for printing stack traces.
 *
 * @since 2025/07/05
 */
typedef struct sjme_nvm_task_stackTraceState
{
	/** The current index. */
	sjme_jint i;

	/** The instruction ID. */
	sjme_jint instructionId;

	/** The PC index. */
	sjme_jint pc;

	/** The last class. */
	sjme_jclass lastClass;

	/** The current class. */
	sjme_jclass nowClass;

	/** The current code. */
	sjme_nvm_class_codeInfo nowCode;

	/** The current method. */
	sjme_nvm_class_methodInfo nowMethod;
} sjme_nvm_task_stackTraceState;

/**
 * Returns the @c JarPackageBracket for the given library.
 * 
 * @param contextThread The context thread.
 * @param inLibrary The library to map.
 * @param outBracket The resultant bracket.
 * @return Any resultant error, if any.
 * @since 2025/07/06
 */
sjme_errorCode sjme_nvm_task_bracketJarPackage(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_jbracketJarPackage* outBracket);
	
/**
 * Loads a cached common class.
 * 
 * @param contextThread The context thread.
 * @param commonId The common class ID.
 * @param outClass The resultant class.
 * @param doInit Initialize the target class?
 * @return Any resultant error, if any.
 * @since 2025/03/20
 */
sjme_errorCode sjme_nvm_task_commonClass(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInValue sjme_nvm_task_commonClassId commonId,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInValue sjme_jboolean doInit);

/**
 * Loads a cached common class.
 * 
 * @param contextThread The context thread.
 * @param commonId The common class ID.
 * @return The resultant class.
 * @since 2025/03/20
 */
sjme_jclass sjme_nvm_task_commonClassR(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_TASK_NUM_COMMON_CLASS)
		sjme_nvm_task_commonClassId commonId);

/**
 * Commits any pending garbage collection.
 * 
 * @param inFrame The frame to commit within.
 * @param commit Any pending garbage collection actions to be committed.
 * @return Any resultant error, if any.
 * @since 2025/09/06
 */
sjme_errorCode sjme_nvm_task_frameCommit(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_frame_gcCommit* commit);

/**
 * Pushes an object to be commited for later garbage collection.
 * 
 * @param inFrame The frame to push the commit within.
 * @param commit The commit to push into.
 * @param pushObject The object to be pushed.
 * @return Any resultant error, if any.
 * @since 2025/09/06
 */
sjme_errorCode sjme_nvm_task_frameCommitPush(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_frame_gcCommit* commit,
	sjme_attrInNotNull sjme_jobject pushObject);

/**
 * Locates the exception handler to use for exceptions.
 * 
 * @param inFrame The frame to find the handler for.
 * @param tossed The tossed throwable.
 * @param handled If this is handled.
 * @param pcNew The PC handler.
 * @return Any resultant error, if any.
 * @since 2025/06/29
 */
sjme_errorCode sjme_nvm_task_frameHandler(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_jobject tossed,
	sjme_attrInOutNotNull sjme_jboolean* handled,
	sjme_attrInOutNotNull sjme_nvm_byteCode_pcNew* pcNew);

/**
 * Returns the direct address to the local variable.
 * 
 * @param inFrame The thread frame.
 * @param localType The type of local to access.
 * @param localIndex The local index.
 * @param outAddr The direct address to the local value.
 * @return Any resultant error, if any.
 * @since 2025/03/02
 */
sjme_errorCode sjme_nvm_task_frameLocalAddr(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId localType,
	sjme_attrInPositive sjme_jint localIndex,
	sjme_attrOutNotNull sjme_jvalue** outAddr);

/**
 * Clears the entire set of locals for a frame.
 * 
 * @param inFrame The frame to clear.
 * @return Any resultant error, if any.
 * @since 2025/07/10
 */
sjme_errorCode sjme_nvm_task_frameLocalClear(
	sjme_attrInNotNull sjme_nvm_frame inFrame);

/**
 * Returns the value of a local variable.
 * 
 * @param inFrame The frame to read the local from.
 * @param typeId The type to read.
 * @param localIndex The index of the local.
 * @param outValue The output value.
 * @return Any resultant error, if any.
 * @since 2025/07/18
 */
sjme_errorCode sjme_nvm_task_frameLocalGet(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId typeId,
	sjme_attrInPositive sjme_jint localIndex,
	sjme_attrInNotNull sjme_jvalueTyped* outValue);

/**
 * Pushes the specified local to the stack.
 * 
 * @param inFrame The frame to push the local to the stack from.
 * @param typeId The type of local to push.
 * @param localIndex The index of the local.
 * @return Any resultant error, if any.
 * @since 2025/02/12
 */
sjme_errorCode sjme_nvm_task_frameLocalPush(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInValue sjme_javaTypeId typeId,
	sjme_attrInPositive sjme_jint localIndex);
	
/**
 * Sets the value of a local variable within a frame using the local variable
 * index, which is the same as the Java index.
 * 
 * @param inFrame The frame to set the value in.
 * @param localIndex The local index to set.
 * @param inValue The value to set.
 * @return Any resultant error, if any.
 * @since 2025/01/04
 */
sjme_errorCode sjme_nvm_task_frameLocalSetL(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositive sjme_jint localIndex,
	sjme_attrInNotNull const sjme_jvalueTyped* inValue);
	
/**
 * Obtains the given frame pool.
 * 
 * @param inFrame The frame to get a pool entry from.
 * @param poolIndex The pool index to read.
 * @param outEntry The resultant entry.
 * @param inType The first type to obtain, @c 0 is the end.
 * @param inTypeB The second type to obtain, @c 0 is the end.
 * @param ... The subsequent types to obtain, @c 0 is the end.
 * @return Any resultant error, if any.
 * @since 2025/01/10
 */
sjme_errorCode sjme_nvm_task_framePool(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositiveNonZero sjme_jint poolIndex,
	sjme_attrOutNotNull sjme_nvm_class_poolEntry** outEntry,
	sjme_attrInRange(0, SJME_NUM_CLASS_POOL_TYPE)
		sjme_nvm_class_poolType inType,
	sjme_attrInRange(0, SJME_NUM_CLASS_POOL_TYPE)
		sjme_nvm_class_poolType inTypeB,
	...);

/**
 * Clears the entire stack for a frame.
 * 
 * @param inFrame The frame to clear.
 * @return Any resultant error, if any.
 * @since 2025/06/29
 */
sjme_errorCode sjme_nvm_task_frameStackClear(
	sjme_attrInNotNull sjme_nvm_frame inFrame);
	
/**
 * Peeks a single value from the top of the stack.
 * 
 * @param inFrame The frame to pop from.
 * @param typeId The type ID to pop, if this is @link SJME_NUM_JAVA_TYPE_IDS
 * then this will disregard the type.
 * @param outValue The resultant value.
 * @param copiedElsewhere Is this value copied elsewhere? That is if this is
 * true, then this will be reference counted.
 * @return Any resultant error, if any.
 * @since 2025/02/17
 */
sjme_errorCode sjme_nvm_task_frameStackPeek(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId typeId,
	sjme_attrInNotNull sjme_jvalueTyped* outValue,
	sjme_attrInValue sjme_jboolean copiedElsewhere);

/**
 * Pops a value from the top of the stack.
 * 
 * @param inFrame The frame to pop from.
 * @param typeId The type ID to pop.
 * @param commit The commit to the garbage collector when the value is no
 * longer needed.
 * @param outValue The resultant value.
 * @return Any resultant error, if any.
 * @since 2025/02/16
 */
sjme_errorCode sjme_nvm_task_frameStackPop(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId typeId,
	sjme_attrInNotNull sjme_nvm_frame_gcCommit* commit,
	sjme_attrInNotNull sjme_jvalueTyped* outValue);

/**
 * Pops multiple values from the stack and places their values into the given
 * typed values.
 * 
 * @param inFrame The frame to pop from.
 * @param copiedElsewhere Are these values copied elsewhere?
 * @param commit The commit to the garbage collector when the value is no
 * longer needed.
 * @param argC The number of values to pop.
 * @param argT The types of values to pop.
 * @param argV The resultant values which were popped.
 * @return Any resultant errors, if any.
 * @since 2025/02/13
 */
sjme_errorCode sjme_nvm_task_frameStackPopA(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInValue sjme_jboolean copiedElsewhere,
	sjme_attrInNotNull sjme_nvm_frame_gcCommit* commit,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNotNullBuf(argC) sjme_javaTypeId* argT,
	sjme_attrInNotNullBuf(argC) sjme_jvalueTyped* argV);
	
/**
 * Pushes the given value to the stack.
 * 
 * @param inFrame The frame to push to.
 * @param inValue The value being pushed.
 * @return Any resultant error, if any.
 * @since 2025/01/11
 */
sjme_errorCode sjme_nvm_task_frameStackPush(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_jvalueTyped* inValue);
	
/**
 * Pushes the given class, named by the pool string, to the stack.
 * 
 * @param inFrame The frame to push to.
 * @param inClassName The name of the class to push.
 * @return Any resultant error, if any.
 * @since 2025/01/11
 */
sjme_errorCode sjme_nvm_task_frameStackPushClassPD(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_stringPool_string inClassName);
	
/**
 * Pushes the given string pool string to the stack.
 * 
 * @param inFrame The frame to push into the stack for. 
 * @param inString The string value being pushed.
 * @return Any resultant error, if any.
 * @since 2025/01/11
 */
sjme_errorCode sjme_nvm_task_frameStackPushStringP(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_stringPool_string inString);

/**
 * Returns the top of the stack, its type and value.
 * 
 * @param inFrame The frame to get the top of.
 * @param depth The depth from the stack top.
 * @param outValue The resultant value.
 * @param copiedElsewhere Is this value copied elsewhere? That is if this is
 * true, then this will be reference counted.
 * @return Any resultant error, if any.
 * @since 2025/02/24
 */
sjme_errorCode sjme_nvm_task_frameStackTop(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositive sjme_jint depth,
	sjme_attrOutNotNull sjme_jvalueTyped* outValue,
	sjme_attrInValue sjme_jboolean copiedElsewhere);

/**
 * Returns the direct address to a tread value.
 * 
 * @param inFrame The input stack frame.
 * @param typeId The type.
 * @param typeIndex The index into the tread.
 * @param outAddr The resultant address of the value.
 * @param outCheck The output check value if an object.
 * @param outConsiderGc How to consider garbage collection.
 * @return Any resultant value, if any.
 * @since 2025/03/02
 */
sjme_errorCode sjme_nvm_task_frameTreadAddr(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId typeId,
	sjme_attrInPositive sjme_jint typeIndex,
	sjme_attrOutNotNull sjme_jvalue** outAddr,
	sjme_attrOutNotNull sjme_jint** outCheck,
	sjme_attrOutNullable sjme_nvm_frame_considerGc* outConsiderGc);

/**
 * Gets the value of a variable within a frame using the typed index
 * which is placed within its own frame set.
 * 
 * @param inFrame The frame to set the value in.
 * @param typeId The type to read.
 * @param typeIndex The type index to set.
 * @param outValue The resultant value.
 * @param eraseOld Erase the old value in the slot?
 * @return Any resultant error, if any.
 * @since 2025/02/16
 */
sjme_errorCode sjme_nvm_task_frameTreadGetT(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId typeId,
	sjme_attrInPositive sjme_jint typeIndex,
	sjme_attrOutNotNull sjme_jvalueTyped* outValue,
	sjme_attrInValue sjme_jboolean eraseOld);
	
/**
 * Sets the value of a variable within a frame using the typed index
 * which is placed within its own frame set.
 * 
 * @param inFrame The frame to set the value in.
 * @param typeIndex The type index to set.
 * @param inValue The value to set.
 * @param oldValue The old value that was in this tread slot.
 * @return Any resultant error, if any.
 * @since 2025/01/04
 */
sjme_errorCode sjme_nvm_task_frameTreadSetT(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositive sjme_jint typeIndex,
	sjme_attrInNotNull const sjme_jvalueTyped* inValue,
	sjme_attrOutNotNull sjme_jvalueTyped* oldValue);

/**
 * Specifies that the given frame should wait for the given condition to be
 * met before execution can continue.
 * 
 * @param inFrame The frame that is waiting for the condition.
 * @param conditionFunc The condition function to wait on.
 * @param timeout The timeout before the condition will expire, if this
 * is @c -1 then this will wait forever.
 * @param value The condition parameter, this may be anything. 
 * @return Any resultant error, if any.
 * @since 2025/10/02
 */
sjme_errorCode sjme_nvm_task_frameWaitFor(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_frame_conditionFunc conditionFunc,
	sjme_attrInNegativeOnePositive sjme_jint timeout,
	sjme_attrInValue sjme_intPointer value);
	
/**
 * Prints the stack trace for a thread using the standard compact SquirrelJME
 * style stack traces.
 * 
 * @param inThread The thread to print the trace for.
 * @return Any resultant error, if any.
 * @since 2025/02/16
 */
sjme_errorCode sjme_nvm_task_stackTraceThread(
	sjme_attrInNotNull sjme_nvm_thread inThread);

/**
 * Prints the stack trace for a throwable using the standard compact
 * SquirrelJME style stack traces.
 *
 * @param contextThread The context thread.
 * @param inThrowable The throwable to print the trace for.
 * @return Any resultant error, if any.
 * @since 2025/07/05
 */
sjme_errorCode sjme_nvm_task_stackTraceThrowable(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jthrowable inThrowable);

/**
 * Enters the actual main for this task.
 * 
 * @param inTask The task to enter main for.
 * @param outThread The optional output thread.
 * @return Any resultant error, if any.
 * @since 2025/07/15
 */
sjme_errorCode sjme_nvm_task_taskEnterMain(
	sjme_attrInNotNull sjme_nvm_task inTask,
	sjme_attrOutNullable sjme_nvm_thread* outThread);
	
/**
 * Starts the task.
 *
 * @param inState The input state.
 * @param initConfig The start configuration for this task.
 * @param outTask The resultant task.
 * @return Any error state.
 * @since 2023/12/17
 */
sjme_errorCode sjme_nvm_task_taskNew(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull const sjme_nvm_task_taskNewConfig* initConfig,
	sjme_attrOutNullable sjme_nvm_task* outTask);

/**
 * Deletes the given thread from the schedule, this does not place it in
 * unscheduled.
 * 
 * @param inState The task to delete from.
 * @param inThread The thread to delete from the schedule.
 * @return Any resultant error, if any.
 * @since 2025/06/29
 */
sjme_errorCode sjme_nvm_task_taskScheduleDelete(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread);
	
/**
 * Schedules the given thread for execution.
 * 
 * @param inState The virtual machine state.
 * @param inThread The thread to schedule.
 * @return Any resultant error.
 * @since 2025/01/06
 */
sjme_errorCode sjme_nvm_task_taskScheduleIn(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread);

/**
 * Returns the next scheduled thread.
 * 
 * @param inState The input state.
 * @param runThread The resultant thread.
 * @param isTerminated Is the virtual machine terminated?
 * @return Any resultant error, if any.
 * @since 2025/06/29
 */
sjme_errorCode sjme_nvm_task_taskScheduleNext(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_nvm_thread* runThread,
	sjme_attrOutNotNull sjme_jboolean* isTerminated);
	
/**
 * Un-schedules the given thread so that it does not execute but enters
 * a resting mode.
 * 
 * @param inState The virtual machine state.
 * @param inThread The thread to un-schedule.
 * @param msResting The time to spend resting at the minimum, if zero then
 * this is just a yield and the thread will wake back up as soon as possible.
 * @return Any resultant error, if any.
 * @since 2025/06/29
 */
sjme_errorCode sjme_nvm_task_taskScheduleOut(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInPositive sjme_jint msResting);

/**
 * Determines if the given thread can be scheduled.
 * 
 * @param inState The input state.
 * @param inThread If the thread can be scheduled.
 * @param isRunning Is this running?
 * @return Any resultant error, if any.
 * @since 2025/06/29
 */
sjme_errorCode sjme_nvm_task_taskScheduleYes(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_jboolean* isRunning);

/**
 * Emits an exception with the given message.
 * 
 * @param inThread The thread to emit within.
 * @param commonClass The commit class to emit.
 * @param cause The cause of this exception, this is optional.
 * @param message The message to use for the message.
 * @param ... Any formatted parameters to the message.
 * @return Any resultant error, if any.
 * @since 2025/09/06
 */
sjme_errorCode sjme_nvm_task_threadEmit(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInValue sjme_nvm_task_commonClassId commonClass,
	sjme_attrInNullable sjme_jthrowable cause,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message,
	...) sjme_attrFormatOuter(3, 4);

/**
 * Enters a frame for the given exact method within the thread.
 * 
 * @param inThread The thread to enter within.
 * @param outFrame The resultant frame.
 * @param inMethod The method being called.
 * @param callType The type of call to perform.
 * @param argC The argument count.
 * @param argV Argument values to the call.
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_task_threadEnter(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_jmethodID inMethod,
	sjme_attrInRange(0, SJME_NVM_NUM_METHOD_CALL_TYPE)
		sjme_nvm_methodCallType callType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV);

/**
 * Enters a frame within the thread.
 * 
 * @param inThread The thread to enter within.
 * @param outFrame The resultant frame.
 * @param inClass The class to execute within.
 * @param instanceType The instance type of the method.
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
	sjme_attrInRange(0, SJME_ERROR_INVALID_ARGUMENT)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNotNull sjme_lpcstr inType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV);

/**
 * Enters a frame within the thread.
 * 
 * @param inThread The thread to enter within.
 * @param outFrame The resultant frame.
 * @param inClass The class to execute within.
 * @param instanceType The instance type of the method.
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
	sjme_attrInRange(0, SJME_ERROR_INVALID_ARGUMENT)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInNotNull sjme_charSeq inName,
	sjme_attrInNotNull sjme_charSeq inType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV);
	
/**
 * Returns the next empty frame at the top of the stack, this does not
 * increment the frame count.
 * 
 * @param inThread The thread to get the next frame for.
 * @param outFrame The resultant frame.
 * @return Any resultant error, if any.
 * @since 2025/01/04
 */
sjme_errorCode sjme_nvm_task_threadFrameNext(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame);

/**
 * Interrupts the given thread.
 * 
 * @param inThread The thread to interrupt.
 * @return Any resultant error, if any.
 * @since 2025/10/02
 */
sjme_errorCode sjme_nvm_task_threadInterrupt(
	sjme_attrInNotNull sjme_nvm_thread inThread);

/**
 * Checks if the given thread is in the interrupt state, then optionally
 * clears it.
 * 
 * @param inThread The thread to check if interrupted.
 * @param clear If the interrupt signal should be cleared.
 * @return Any resultant error, if any, interrupted threads
 * will be @link SJME_ERROR_INTERRUPTED.
 * @since 2025/10/02
 */
sjme_errorCode sjme_nvm_task_threadInterruptCheck(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInValue sjme_jboolean clear);
	
/**
 * Leaves a frame of execution.
 * 
 * @param inThread The thread which is leaving a thread.
 * @return Any resultant error, if any.
 * @since 2025/02/24
 */
sjme_errorCode sjme_nvm_task_threadLeave(
	sjme_attrInNotNull sjme_nvm_thread inThread);
	
/**
 * Creates a new thread within the given task.
 * 
 * @param inTask The task to create the thread in.
 * @param outThread The resultant thread.
 * @param threadName The name of the new thread.
 * @param isMain Is this the main thread? If a main thread already exists
 * then this parameter will have no effect.
 * @return On any errors, if any.
 * @since 2024/10/15
 */
sjme_errorCode sjme_nvm_task_threadNew(
	sjme_attrInNotNull sjme_nvm_task inTask,
	sjme_attrOutNotNull sjme_nvm_thread* outThread,
	sjme_attrInNotNull sjme_lpcstr threadName,
	sjme_attrInValue sjme_jboolean isMain);

/**
 * Starts the specified thread.
 * 
 * @param inThread The thread to start. 
 * @return Any resultant error, if any.
 * @since 2024/10/15
 */
sjme_errorCode sjme_nvm_task_threadStart(
	sjme_attrInNotNull sjme_nvm_thread inThread);

/**
 * Loads the given character sequence as a string object.
 * 
 * @param inThread The context thread to load as the string requires
 * initialization.
 * @param outString The resultant string object.
 * @param isIntern Should this be interned?
 * @param inSeq The input sequence.
 * @return Any resultant error, if any.
 * @since 2025/01/25
 */
sjme_errorCode sjme_nvm_task_threadStringValueOfCS(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_jstring* outString,
	sjme_attrInValue sjme_jboolean isIntern,
	sjme_attrInNotNull sjme_charSeq inSeq);
	
/**
 * Loads the given string pool string as a string object.
 * 
 * @param inThread The context thread to load as the string requires
 * initialization.
 * @param outString The resultant string object.
 * @param inPool The pooled string to load.
 * @return Any resultant error.
 * @since 2025/01/11
 */
sjme_errorCode sjme_nvm_task_threadStringValueOfP(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_jstring* outString,
	sjme_attrInNotNull sjme_nvm_stringPool_string inPool);
	
/**
 * Loads the given UTF string as a string object.
 * 
 * @param inThread The context thread to load as the string requires
 * initialization.
 * @param outString The resultant string object.
 * @param isIntern Should this be interned?
 * @param inUtf The input UTF string.
 * @return Any resultant error, if any.
 * @since 2025/03/07
 */
sjme_errorCode sjme_nvm_task_threadStringValueOfUtf(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_jstring* outString,
	sjme_attrInValue sjme_jboolean isIntern,
	sjme_attrInNotNull sjme_lpcstr inUtf);

/** Frame thread. */
#define SJME_F_T(frame) \
	sjme_atomic_g(sjme_nvm_thread, &(frame)->inThread)

/** Frame task. */
#define SJME_F_K(frame) \
	sjme_atomic_g(sjme_nvm_task, &(frame)->inTask)

/** Frame classloader. */
#define SJME_F_CL(frame) \
	(sjme_atomic_g(sjme_nvm_task, &(frame)->inTask)->classLoader)

/** Frame state. */
#define SJME_F_S(frame) \
	sjme_atomic_g(sjme_nvm, &SJME_F_K(frame)->inState)

/** Thread state. */
#define SJME_T_S(thread) \
	sjme_atomic_g(sjme_nvm, &(thread)->inState)

/** Thread task. */
#define SJME_T_K(thread) \
	sjme_atomic_g(sjme_nvm_task, &(thread)->inTask)

/** Thread classloader. */
#define SJME_T_CL(thread) \
	(sjme_atomic_g(sjme_nvm_task, &(thread)->inTask)->classLoader)
	
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
