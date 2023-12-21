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

#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/nvm.h"
#include "sjme/romInternal.h"
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

	/** Make frame. */
	SJME_MOCK_DO_TYPE_NVM_FRAME,

	/** Make Object. */
	SJME_MOCK_DO_TYPE_NVM_OBJECT,
	
	/** Initialize. */
	SJME_MOCK_DO_TYPE_NVM_STATE,
	
	/** Make thread. */
	SJME_MOCK_DO_TYPE_NVM_THREAD,

	/** ROM Suite. */
	SJME_MOCK_DO_TYPE_ROM_SUITE,

	/** The number of do types. */
	SJME_NUM_MOCK_DO_TYPES
} sjme_mockDoType;

/** The maximum number of threads supported in the mock for testing. */
#define SJME_MOCK_MAX_THREADS 16

/** The maximum number of objects that can be created. */
#define SJME_MOCK_MAX_OBJECTS 32

/** The maximum number of suites that can be created. */
#define SJME_MOCK_MAX_ROM_SUITES 4

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

	/** The number of created ROM suites. */
	sjme_jint numRomSuites;

	/** ROM Suites. */
	sjme_rom_suite* romSuites[SJME_MOCK_MAX_ROM_SUITES];
	
	/** Special data, if needed. */
	void* special;
} sjme_mockState;

/**
 * Mocked frame data.
 *
 * @since 2023/12/21
 */
typedef struct sjme_mockDataNvmFrame
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
} sjme_mockDataNvmFrame;

/**
 * Java object mocked data.
 *
 * @since 2023/12/21
 */
typedef struct sjme_mockDataNvmObject
{
	/** Implement. */
	int todo;
} sjme_mockDataNvmObject;

/**
 * Virtual machine state mock data.
 *
 * @since 2023/12/21
 */
typedef struct sjme_mockDataNvmState
{
	/** State hooks to use. */
	const sjme_nvm_stateHooks* hooks;
} sjme_mockDataNvmState;

/**
 * Mocking data for ROM suites.
 *
 * @since 2023/12/21
 */
typedef struct sjme_mockDataRomSuite
{
	/** Functions to utilize for the suite. */
	sjme_rom_suiteFunctions functions;
} sjme_mockDataRomSuite;

/**
 * Data for a given mock.
 *
 * @since 2023/12/21
 */
typedef union sjme_mockData
{
	/** Frame creation information. */
	sjme_mockDataNvmFrame nvmFrame;

	/** Object information. */
	sjme_mockDataNvmObject nvmObject;

	/** State information. */
	sjme_mockDataNvmState nvmState;

	/** ROM suites. */
	sjme_mockDataRomSuite romSuite;
} sjme_mockData;

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
	sjme_mockData data;
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
 * @param inTest The test this is in.
 * @param inState The input state.
 * @param inSet The set to act on.
 * @param special Special value, optional.
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/1/111
 */
sjme_jboolean sjme_mockAct(
	sjme_attrInNotNull sjme_test* inTest,
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
sjme_jboolean sjme_mockDoNvmFrame(
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
sjme_jboolean sjme_mockDoNvmObject(
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
sjme_jboolean sjme_mockDoNvmState(
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
sjme_jboolean sjme_mockDoNvmThread(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunData* inData);

/**
 * Initializes a ROM suite.
 *
 * @param inState The mock state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/12/21
 */
sjme_jboolean sjme_mockDoRomSuite(
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
