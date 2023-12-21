/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Test mock.
 * 
 * @since 2023/11/03
 */

#ifndef SQUIRRELJME_MOCK_H
#define SQUIRRELJME_MOCK_H

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "sjme/alloc.h"
#include "test.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MOCK_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The mock instruction type.
 * 
 * @since 2023/11/11
 */
typedef enum sjme_mockDoType
{
	/** Unknown. */
	SJME_MOCK_DO_TYPE_UNKNOWN,
	
	/** Initialize. */
	SJME_MOCK_DO_TYPE_MAKE_STATE,
	
	/** Make thread. */
	SJME_MOCK_DO_TYPE_MAKE_THREAD,
	
	/** Make Object. */
	SJME_MOCK_DO_TYPE_MAKE_OBJECT,
	
	/** Make frame. */
	SJME_MOCK_DO_TYPE_MAKE_FRAME,

	/** The number of do types. */
	SJME_NUM_MOCK_DO_TYPES
} sjme_mockDoType;

/** The maximum number of threads supported in the mock for testing. */
#define SJME_MOCK_MAX_THREADS 16

/** The maximum number of objects that can be created. */
#define SJME_MOCK_MAX_OBJECTS 32

/**
 * Represents the state of the mock.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_mockState
{
	/** Allocated memory pool. */
	sjme_alloc_pool* allocPool;

	/** The virtual machine state. */
	sjme_nvm_state* nvmState;
	
	/** The number of active threads. */
	sjme_jint numThreads;
	
	/** Set of threads. */
	struct
	{
		/** The actual native thread. */
		sjme_nvm_thread* nvmThread;
	} threads[SJME_MOCK_MAX_THREADS];
	
	/** The number of objects which were created. */
	sjme_jint numObjects;
	
	/** Objects that were created. */
	sjme_jobject objects[SJME_MOCK_MAX_OBJECTS];
	
	/** Special data, if needed. */
	void* special;
} sjme_mockState;

/**
 * The current run item in the mock.
 * 
 * @since 2023/11/11
 */
typedef struct sjme_mockRunCurrent
{
	/** The current index of all. */
	sjme_jint indexAll;
	
	/** The current index of this type. */
	sjme_jint indexType;
	
	/** The current type. */
	sjme_mockDoType type;
	
	/** Special value, for alternative configuration potentially. */
	sjme_jint special;
	
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
			sjme_jint threadIndex;
			
			/** The maximum number of locals. */
			sjme_jint maxLocals;
			
			/** The maximum number of stack entries. */
			sjme_jint maxStack;
			
			/** The treads within the frame. */
			struct
			{
				/** Maximum size of this tread. */
				sjme_jint max;
				
				/** The stack pivot point. */
				sjme_jint stackBaseIndex;
			} treads[SJME_NUM_JAVA_TYPE_IDS];
		} frame;
		
		/** Object information. */
		struct
		{
			/** Implement. */
			int todo;
		} object;
	} data;
} sjme_mockRunCurrent;

/**
 * Data for the entire mock un.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_mockRunData sjme_mockRunData;

/**
 * Configuration function for the mock.
 * 
 * @param inState The input state.
 * @param inCurrent The current run item for the mock.
 * @return Returns @c SJME_JNI_TRUE when successful.
 * @since 2023/11/11
 */
typedef sjme_jboolean (*sjme_mockConfigFunc)(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunCurrent* inCurrent);

/**
 * Represents an mock initialization function.
 * 
 * @return Will return @c SJME_JNI_TRUE when successful.
 * @since 2023/11/03
 */
typedef sjme_jboolean (*sjme_mockDoFunc)(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunData* inData);

/**
 * Structure which contains the mock instructions to use for initializing
 * a test virtual machine.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_mockSet
{
	/** The configuration function. */
	sjme_mockConfigFunc config;

	/** Flags for mock. */
	sjme_jint flags;
	
	/** Mock function order. */
	sjme_mockDoFunc order[sjme_flexibleArrayCount];
} sjme_mockSet;

/**
 * Performs the mock action.
 * 
 * @param inState The input state.
 * @param inSet The set to act on.
 * @param special Special value, optional.
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/1/111
 */
sjme_jboolean sjme_mockAct(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull const sjme_mockSet* inSet,
	sjme_attrInValue sjme_jint special);

/**
 * Allocates memory
 * 
 * @param inState The input state. 
 * @param inLen The number of bytes to allocate.
 * @return The allocated buffer, returns @c NULL if allocation failed.
 * @since 2023/11/11
 */
void* sjme_mockAlloc(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInPositiveNonZero size_t inLen);
	
/**
 * Makes a frame within the virtual machine.
 * 
 * @param inState The mock state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/11/11
 */
sjme_jboolean sjme_mockDoMakeFrame(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunData* inData);

/**
 * Creates a new object.
 * 
 * @param inState The input state.
 * @param inData The input data.
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/11/17 
 */
sjme_jboolean sjme_mockDoMakeObject(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunData* inData);

/**
 * Initial virtual machine initialization state.
 *
 * @param inState The mock state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/11/03
 */
sjme_jboolean sjme_mockDoMakeState(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunData* inData);

/**
 * Makes a thread within the virtual machine.
 * 
 * @param inState The mock state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/11/11
 */
sjme_jboolean sjme_mockDoMakeThread(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunData* inData);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MOCK_H
}
		#undef SJME_CXX_SQUIRRELJME_MOCK_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MOCK_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MOCK_H */
