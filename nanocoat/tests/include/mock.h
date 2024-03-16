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

#include "classBuilder.h"
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
typedef enum sjme_mock_doType
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

	/** ROM Library. */
	SJME_MOCK_DO_TYPE_ROM_LIBRARY,

	/** Mocked ROM Library. */
	SJME_MOCK_DO_TYPE_ROM_MOCK_LIBRARY,

	/** ROM Suite. */
	SJME_MOCK_DO_TYPE_ROM_SUITE,

	/** The number of do types. */
	SJME_NUM_MOCK_DO_TYPES
} sjme_mock_doType;

/** The maximum number of threads supported in the mock for testing. */
#define SJME_MOCK_MAX_THREADS 16

/** The maximum number of objects that can be created. */
#define SJME_MOCK_MAX_OBJECTS 32

/** The maximum number of suites that can be created. */
#define SJME_MOCK_MAX_ROM_SUITES 4

/** The maximum number of libraries that can be created. */
#define SJME_MOCK_MAX_ROM_LIBRARIES 8

/**
 * Represents the state of the mock.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_mock
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
	sjme_rom_suite romSuites[SJME_MOCK_MAX_ROM_SUITES];

	/** The number of created ROM libraries. */
	sjme_jint numRomLibraries;

	/** ROM libraries. */
	sjme_rom_library romLibraries[SJME_MOCK_MAX_ROM_LIBRARIES];
	
	/** Special data, if needed. */
	void* special;
} sjme_mock;

/**
 * Mocked frame data.
 *
 * @since 2023/12/21
 */
typedef struct sjme_mock_configDataNvmFrame
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
} sjme_mock_configDataNvmFrame;

/**
 * Java object mocked data.
 *
 * @since 2023/12/21
 */
typedef struct sjme_mock_configDataNvmObject
{
	/** Implement. */
	int todo;
} sjme_mock_configDataNvmObject;

/**
 * Virtual machine state mock data.
 *
 * @since 2023/12/21
 */
typedef struct sjme_mock_configDataNvmState
{
	/** State hooks to use. */
	const sjme_nvm_stateHooks* hooks;
} sjme_mock_configDataNvmState;

/**
 * Mocking data for ROM libraries.
 *
 * @since 2023/12/21
 */
typedef struct sjme_mock_configDataRomLibrary
{
	/** Functions to utilize for the library. */
	sjme_rom_libraryFunctions functions;

	/** The ID of the library. */
	sjme_jint id;

	/** The name of the library. */
	sjme_lpcstr name;

	/** The backing data for the library. */
	const sjme_jubyte* data;

	/** The length of the data. */
	sjme_jint length;
} sjme_mock_configDataRomLibrary;

/**
 * Mocked ROM library.
 *
 * @since 2023/12/30
 */
typedef struct sjme_mock_configDataRomMockLibrary
{
	/** Should this load @c mock.jar or not? */
	sjme_jboolean isJar;
} sjme_mock_configDataRomMockLibrary;

/**
 * Mocking data for ROM suites.
 *
 * @since 2023/12/21
 */
typedef struct sjme_mock_configDataRomSuite
{
	/** Functions to utilize for the suite. */
	sjme_rom_suiteFunctions functions;

	/** Pre-made library cache set. */
	sjme_list_sjme_rom_library* cacheLibraries;
} sjme_mock_configDataRomSuite;

/**
 * Data for a given mock.
 *
 * @since 2023/12/21
 */
typedef union sjme_mock_configData
{
	/** Frame creation information. */
	sjme_mock_configDataNvmFrame nvmFrame;

	/** Object information. */
	sjme_mock_configDataNvmObject nvmObject;

	/** State information. */
	sjme_mock_configDataNvmState nvmState;

	/** ROM Library. */
	sjme_mock_configDataRomLibrary romLibrary;

	/** Mocked ROM library. */
	sjme_mock_configDataRomMockLibrary romMockLibrary;

	/** ROM suites. */
	sjme_mock_configDataRomSuite romSuite;
} sjme_mock_configData;

/**
 * The current run item in the mock.
 * 
 * @since 2023/11/11
 */
typedef struct sjme_mock_configWork
{
	/** The current index of all. */
	sjme_jint indexAll;
	
	/** The current index of this type. */
	sjme_jint indexType;
	
	/** The current type. */
	sjme_mock_doType type;
	
	/** Special value, for alternative configuration potentially. */
	sjme_jint special;
	
	/** Data for the initialization step. */
	sjme_mock_configData data;
} sjme_mock_configWork;

/**
 * Data for the entire mock un.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_mock_configWorkData sjme_mock_configWorkData;

/**
 * Configuration function for the mock.
 * 
 * @param inState The input state.
 * @param inCurrent The current run item for the mock.
 * @return Returns @c SJME_JNI_TRUE when successful.
 * @since 2023/11/11
 */
typedef sjme_jboolean (*sjme_mock_configFunc)(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWork* inCurrent);

/**
 * Represents an mock initialization function.
 * 
 * @return Will return @c SJME_JNI_TRUE when successful.
 * @since 2023/11/03
 */
typedef sjme_jboolean (*sjme_mock_doFunc)(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData);

/** The maximum number mock functions permitted. */
#define SJME_MAX_MOCK_FUNCTIONS 64

/**
 * Structure which contains the mock instructions to use for initializing
 * a test virtual machine.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_mock_configSet
{
	/** The configuration function. */
	sjme_mock_configFunc config;

	/** Flags for mock. */
	sjme_jint flags;
	
	/** Mock function order. */
	sjme_mock_doFunc order[SJME_MAX_MOCK_FUNCTIONS];
} sjme_mock_configSet;

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
sjme_jboolean sjme_mock_act(
	sjme_attrInNotNull sjme_test* inTest,
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull const sjme_mock_configSet* inSet,
	sjme_attrInValue sjme_jint special);

/**
 * Allocates memory
 * 
 * @param inState The input state. 
 * @param inLen The number of bytes to allocate.
 * @return The allocated buffer, returns @c NULL if allocation failed.
 * @since 2023/11/11
 */
void* sjme_mock_alloc(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInPositiveNonZero size_t inLen);
	
/**
 * Makes a frame within the virtual machine.
 * 
 * @param inState The mock state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/11/11
 */
sjme_jboolean sjme_mock_doNvmFrame(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData);

/**
 * Creates a new object.
 * 
 * @param inState The input state.
 * @param inData The input data.
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/11/17 
 */
sjme_jboolean sjme_mock_doNvmObject(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData);

/**
 * Initial virtual machine initialization state.
 *
 * @param inState The mock state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/11/03
 */
sjme_jboolean sjme_mock_doNvmState(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData);

/**
 * Makes a thread within the virtual machine.
 * 
 * @param inState The mock state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/11/11
 */
sjme_jboolean sjme_mock_doNvmThread(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData);

/**
 * Initializes a ROM suite.
 *
 * @param inState The mock state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/12/21
 */
sjme_jboolean sjme_mock_doRomLibrary(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData);

/**
 * Initializes a library that contains a basic setup with regards to resources
 * and otherwise.
 *
 * @param inState The input state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/12/30
 */
sjme_jboolean sjme_mock_doRomMockLibrary(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData);

/**
 * Initializes a ROM library.
 *
 * @param inState The mock state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/12/21
 */
sjme_jboolean sjme_mock_doRomSuite(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData);

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
