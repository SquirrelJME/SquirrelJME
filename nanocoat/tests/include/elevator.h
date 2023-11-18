/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Test elevator.
 * 
 * @since 2023/11/03
 */

#ifndef SQUIRRELJME_ELEVATOR_H
#define SQUIRRELJME_ELEVATOR_H

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "test.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ELEVATOR_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The elevator instruction type.
 * 
 * @since 2023/11/11
 */
typedef enum sjme_elevatorDoType
{
	/** Unknown. */
	SJME_ELEVATOR_DO_TYPE_UNKNOWN,
	
	/** Initialize. */
	SJME_ELEVATOR_DO_TYPE_INIT,
	
	/** Make thread. */
	SJME_ELEVATOR_DO_TYPE_MAKE_THREAD,
	
	/** Make Object. */
	SJME_ELEVATOR_DO_TYPE_MAKE_OBJECT,
	
	/** Make frame. */
	SJME_ELEVATOR_DO_TYPE_MAKE_FRAME,

	/** The number of do types. */
	SJME_NUM_ELEVATOR_DO_TYPES
} sjme_elevatorDoType;

/** The maximum number of threads supported in the elevator for testing. */
#define SJME_ELEVATOR_MAX_THREADS 16

/** The maximum number of objects that can be created. */
#define SJME_ELEVATOR_MAX_OBJECTS 32

/**
 * Represents the state of the elevator.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_elevatorState
{
	/** The virtual machine state. */
	sjme_nvm_state* nvmState;
	
	/** The number of active threads. */
	jint numThreads;
	
	/** Set of threads. */
	struct
	{
		/** The actual native thread. */
		sjme_nvm_thread* nvmThread;
	} threads[SJME_ELEVATOR_MAX_THREADS];
	
	/** The number of objects which were created. */
	jint numObjects;
	
	/** Objects that were created. */
	jobject objects[SJME_ELEVATOR_MAX_OBJECTS];
	
	/** Special data, if needed. */
	void* special;
} sjme_elevatorState;

/**
 * The current run item in the elevator.
 * 
 * @since 2023/11/11
 */
typedef struct sjme_elevatorRunCurrent
{
	/** The current index of all. */
	jint indexAll;
	
	/** The current index of this type. */
	jint indexType;
	
	/** The current type. */
	sjme_elevatorDoType type;
	
	/** Special value, for alternative configuration potentially. */
	jint special;
	
	/** Data for the initialization step. */
	union
	{
		/** State information. */
		struct
		{
			/** State hooks to use. */
			const sjme_nvm_stateHooks* hooks;
		} state;
		
		/** Frame creation information. */
		struct
		{
			/** The thread index to create the frame in. */
			jint threadIndex;
			
			/** The maximum number of locals. */
			jint maxLocals;
			
			/** The maximum number of stack entries. */
			jint maxStack;
			
			/** The treads within the frame. */
			struct
			{
				/** Maximum size of this tread. */
				jint max;
				
				/** The stack pivot point. */
				jint stackBaseIndex;
			} treads[SJME_NUM_JAVA_TYPE_IDS];
		} frame;
		
		/** Object information. */
		struct
		{
			/** Implement. */
			int todo;
		} object;
	} data;
} sjme_elevatorRunCurrent;

/**
 * Data for the entire elevator un.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_elevatorRunData sjme_elevatorRunData;

/**
 * Configuration function for the elevator.
 * 
 * @param inState The input state.
 * @param inCurrent The current run item for the elevator.
 * @return Returns @c JNI_TRUE when successful.
 * @since 2023/11/11
 */
typedef jboolean (*sjme_elevatorConfigFunc)(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunCurrent* inCurrent);

/**
 * Represents an elevator initialization function.
 * 
 * @return Will return @c JNI_TRUE when successful.
 * @since 2023/11/03
 */
typedef jboolean (*sjme_elevatorDoFunc)(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData);

/**
 * Structure which contains the elevator instructions to use for initializing
 * a test virtual machine.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_elevatorSet
{
	/** The configuration function. */
	sjme_elevatorConfigFunc config;

	/** Flags for elevator. */
	jint flags;
	
	/** Elevator function order. */
	sjme_elevatorDoFunc order[sjme_flexibleArrayCount];
} sjme_elevatorSet;

/**
 * Performs the elevator action.
 * 
 * @param inState The input state.
 * @param inSet The set to act on.
 * @param special Special value, optional.
 * @return Returns @c JNI_TRUE on success.
 * @since 2023/1/111
 */
jboolean sjme_elevatorAct(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull const sjme_elevatorSet* inSet,
	sjme_attrInValue jint special);

/**
 * Allocates memory
 * 
 * @param inState The input state. 
 * @param inLen The number of bytes to allocate.
 * @return The allocated buffer, returns @c NULL if allocation failed.
 * @since 2023/11/11
 */
void* sjme_elevatorAlloc(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInPositiveNonZero size_t inLen);

/**
 * Initial virtual machine initialization state.
 * 
 * @param inState The elevator state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/11/03
 */
jboolean sjme_elevatorDoInit(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData);
	
/**
 * Makes a frame within the virtual machine.
 * 
 * @param inState The elevator state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/11/11
 */
jboolean sjme_elevatorDoMakeFrame(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData);

/**
 * Creates a new object.
 * 
 * @param inState The input state.
 * @param inData The input data.
 * @return Returns @c JNI_TRUE on success.
 * @since 2023/11/17 
 */
jboolean sjme_elevatorDoMakeObject(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData);

/**
 * Makes a thread within the virtual machine.
 * 
 * @param inState The elevator state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/11/11
 */
jboolean sjme_elevatorDoMakeThread(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ELEVATOR_H
}
		#undef SJME_CXX_SQUIRRELJME_ELEVATOR_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ELEVATOR_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ELEVATOR_H */
