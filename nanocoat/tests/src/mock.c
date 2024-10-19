/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "mock.h"
#include "mock.jar.h"
#include "hello.txt.h"
#include "MockMain.class.h"

struct sjme_mock_configWorkData
{
	/** The index type counts. */
	sjme_jint indexTypeCount[SJME_NUM_MOCK_DO_TYPES];
	
	/** The current run. */
	sjme_mock_configWork current;
	
	/** The next thread ID. */
	sjme_jint nextThreadId;
};

/**
 * Mock function to do type.
 * 
 * @since 2023/11/11
 */
struct
{
	sjme_mock_doFunc func;
	sjme_mock_doType type;
} sjme_mockFuncToType[SJME_NUM_MOCK_DO_TYPES] =
{
	{sjme_mock_doNvmFrame,
		SJME_MOCK_DO_TYPE_NVM_FRAME},
	{sjme_mock_doNvmObject,
		SJME_MOCK_DO_TYPE_NVM_OBJECT},
	{sjme_mock_doNvmState,
		SJME_MOCK_DO_TYPE_NVM_STATE},
	{sjme_mock_doNvmThread,
		SJME_MOCK_DO_TYPE_NVM_THREAD},
	{sjme_mock_doRomLibrary,
		SJME_MOCK_DO_TYPE_ROM_LIBRARY},
	{sjme_mock_doRomMockLibrary,
		SJME_MOCK_DO_TYPE_ROM_MOCK_LIBRARY},
	{sjme_mock_doRomSuite,
		SJME_MOCK_DO_TYPE_ROM_SUITE},
		
	/* End. */
	{NULL, SJME_MOCK_DO_TYPE_UNKNOWN}
};

static sjme_errorCode sjme_mock_defaultRomLibraryRawData(
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint srcPos,
	sjme_attrInPositive sjme_jint length)
{
	sjme_mock_configDataRomLibrary* mock;

	if (inLibrary == NULL || dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (srcPos < 0 || length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Recover mock. */
	mock = inLibrary->cache.common.frontEnd.data;

	/* Double check size. */
	if (srcPos + length < 0 || srcPos + length > mock->length)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* There is no actual data here? */
	if (mock->data == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Just do a normal copy over. */
	memmove(dest, (sjme_pointer)(((uintptr_t)mock->data) + srcPos), length);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_mock_defaultRomLibraryRawSize(
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_jint* outSize)
{
	sjme_mock_configDataRomLibrary* mock;

	if (inLibrary == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover mock. */
	mock = inLibrary->cache.common.frontEnd.data;

	/* Is a simple set operation. */
	*outSize = mock->length;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_mock_defaultRomMockLibraryResourceStream(
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_lpcstr resourceName)
{
	sjme_alloc_pool* pool;
	sjme_cpointer data;
	sjme_jint len;

	if (inLibrary == NULL || resourceName == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Which pool to allocate within? */
	pool = inLibrary->cache.common.allocPool;

	/* Debug. */
	sjme_message("Looking for resource %s...", resourceName);

	/* Depends on the resource name. */
	if (0 == strcmp(resourceName, "hello.txt"))
	{
		data = hello_txt__bin;
		len = hello_txt__len;
	}
	else if (0 == strcmp(resourceName, "MockMain.class"))
	{
		data = mockmain_class__bin;
		len = mockmain_class__len;
	}
	
	/* Not found. */
	else
		return SJME_ERROR_RESOURCE_NOT_FOUND;

	/* Open the stream. */
	return sjme_stream_inputOpenMemory(pool, outStream,
		data, len);
}

sjme_jboolean sjme_mock_act(
	sjme_attrInNotNull sjme_test* inTest,
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull const sjme_mock_configSet* inSet,
	sjme_attrInValue sjme_jint special)
{
	sjme_jint dx, i;
	sjme_mock_configWorkData data;
	sjme_mock_doType doType;
	
	/* Check. */
	if (inState == NULL || inSet == NULL)
		return sjme_die("Null arguments.");

	/* Use the testing pool. */
	inState->allocPool = inTest->pool;
	
	/* Initialize base data. */
	memset(&data, 0, sizeof(data));

	/* Go through each entry, stop at NULl. */
	for (dx = 0; inSet->order[dx] != NULL; dx++)
	{
		/* Always wipe the current data so it is fresh. */
		memset(&data.current, 0, sizeof(data.current));
		
		/* This index is just a straight through. */
		data.current.indexAll = dx;
		
		/* Find the type for this function. */
		doType = SJME_MOCK_DO_TYPE_UNKNOWN;
		for (i = 0; i < SJME_NUM_MOCK_DO_TYPES; i++)
			if (inSet->order[dx] == sjme_mockFuncToType[i].func)
			{
				doType = sjme_mockFuncToType[i].type;
				break;
			}
		
		/* Not found? */
		if (doType == SJME_MOCK_DO_TYPE_UNKNOWN)
			return sjme_die("Could not find the type for do function.");
		
		/* Increment up the index for this. */
		data.current.type = doType;
		data.current.indexType = data.indexTypeCount[doType]++;
		data.current.special = special;
		
		/* Run configuration function to initialize the data set. */
		if (inSet->config != NULL)
			if (!inSet->config(inState, &data.current))
				return sjme_die("Configuration step failed at %d.", dx);
		
		/* Call do function to perform whatever test initialization. */
		if (!inSet->order[dx](inState, &data))
			return sjme_die("Do failed at %d.", dx);
	}
	
	/* Successful. */
	return SJME_JNI_TRUE;
}

sjme_pointer sjme_mock_alloc(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInPositiveNonZero size_t inLen)
{
	sjme_pointer rv;
	
	/* Check. */
	if (inState == NULL)
		return sjme_dieP("No input state.");
	
	rv = NULL;
	if (sjme_error_is(sjme_alloc(inState->allocPool, inLen,
		&rv)))
		return sjme_dieP("Could not allocate pointer in test pool.");
	
	return rv;
}

sjme_jboolean sjme_mock_doNvmState(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData)
{
	sjme_nvm newState;
	
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Allocate virtual machine state. */
	newState = sjme_mock_alloc(inState,
		sizeof(*inState->nvmState));
	inState->nvmState = newState;
	
	/* Store test state, as required for some tests. */
	newState->common.frontEnd.data = inState;
	
	/* Register any hooks? */
	if (inData->current.data.nvmState.hooks != NULL)
		newState->hooks = inData->current.data.nvmState.hooks;
	
	/* Done. */
	return SJME_JNI_TRUE;
}

sjme_jboolean sjme_mock_doNvmFrame(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#if 0
	sjme_jint threadIndex, treadMax, tallyLocals, stackBase, desireMaxLocals;
	sjme_jint tallyStack, desireMaxStack, localIndex;
	sjme_nvm_thread thread;
	sjme_nvm_frame newFrame;
	sjme_basicTypeId typeId;
	sjme_nvm_frameTread* tread;
	sjme_nvm_frameStack* stack;
	sjme_nvm_frameLocalMap* localMap;
	sjme_jbyte baseLocalAt[SJME_NUM_JAVA_TYPE_IDS];
	
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Make sure the requested thread index is valid. */
	threadIndex = inData->current.data.nvmFrame.threadIndex;
	if (threadIndex < 0 || threadIndex >= SJME_MOCK_MAX_THREADS ||
		inState->threads[threadIndex].nvmThread == NULL)
		return sjme_die("Invalid thread index %d.", threadIndex);
	
	/* Get the actual thread. */
	thread = inState->threads[threadIndex].nvmThread;
	
	/* Allocate new frame. */
	newFrame = sjme_mock_alloc(inState, sizeof(*newFrame));
	if (newFrame == NULL)
		return sjme_die("Could not allocate frame.");
	
	/* Correlate the frame index to the thread. */
	newFrame->frameIndex = thread->numFrames;
	thread->numFrames++;
	
	/* Link in frame to the thread. */
	newFrame->inThread = thread;
	newFrame->parent = thread->top;
	thread->top = newFrame;
	
	/* Track tally of locals and stack for consistency. */
	tallyLocals = 0;
	tallyStack = 0;
	
	/* Setup locals mapping. */
	desireMaxLocals = inData->current.data.nvmFrame.maxLocals;
	localMap = sjme_mock_alloc(inState,
		SJME_SIZEOF_FRAME_LOCAL_MAP(desireMaxLocals));
	localMap->max = desireMaxLocals;
	
	/* Setup stack information. */
	desireMaxStack = inData->current.data.nvmFrame.maxStack;
	stack = sjme_mock_alloc(inState,
		SJME_SIZEOF_FRAME_STACK(desireMaxStack));
	newFrame->stack = stack;
	stack->limit = desireMaxStack;
	
	/* Remember to set the local mapping in the frame. */
	newFrame->localMap = localMap;
	
	/* Clear base local map set trackers. */
	memset(baseLocalAt, 0, sizeof(baseLocalAt));
	
	/* Need to initialize frame locals and stack? */
	for (typeId = 0; typeId < SJME_NUM_JAVA_TYPE_IDS; typeId++)
	{
		/* Ignore if empty. */
		treadMax = inData->current.data.nvmFrame.treads[typeId].max;
		if (treadMax <= 0)
			continue;
		
		/* Allocate target tread. */
		tread = sjme_mock_alloc(inState,
			SJME_SIZEOF_FRAME_TREAD_VAR(typeId, treadMax));
		newFrame->treads[typeId] = tread;
		
		/* Setup stack base. */
		stackBase = inData->current.data.nvmFrame.treads[typeId]
			.stackBaseIndex;
		if (stackBase < 0 || stackBase > treadMax)
			return sjme_die("Invalid test stack base %d, outside range %d.",
				stackBase, treadMax);
		
		/* Local tally goes up by the stack base. */
		tallyLocals += stackBase;
		
		/* Tally number of stack items. */
		tallyStack += treadMax - stackBase;
		
		/* Setup other tread details. */
		tread->stackBaseIndex = stackBase;
		tread->count = stackBase;
		tread->max = treadMax;
		
		/* Fill in local mappings for a given tread. */
		for (localIndex = 0; localIndex < stackBase; localIndex++)
			localMap->maps[localIndex].to[typeId] = (sjme_jbyte)localIndex;
		
#if 0
		/* Store the type onto the stack. */
		stack->order[stack->count] = typeId;
		stack->count++;
#endif
	}
	
	/* Consistency check. */
	if (tallyLocals != desireMaxLocals)
		return sjme_die("Calculated and desired locals invalid: %d != %d.",
			tallyLocals, desireMaxLocals);
	
	if (tallyStack != desireMaxStack)
		return sjme_die("Calculated and desired stack invalid: %d != %d.",
			tallyStack, desireMaxStack);
	
	/* Done. */
	return SJME_JNI_TRUE;
#endif
}

sjme_jboolean sjme_mock_doNvmObject(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData)
{
	sjme_jobject newObject;
	
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Too many objects? */
	if (inState->numObjects >= SJME_MOCK_MAX_OBJECTS)
		sjme_die("Too many mock objects.");
	
	/* Allocate new object. */
	newObject = sjme_mock_alloc(inState, sizeof(*newObject));
	inState->objects[inState->numObjects++] = newObject;
	
	/* Initialize object details. */
	newObject->refCount = 1;
	
	/* Success. */
	return SJME_JNI_TRUE;
}

sjme_jboolean sjme_mock_doNvmThread(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData)
{
	sjme_jint threadIndex;
	sjme_nvm_thread newThread;
	
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Mock has a limited set of threads for testing purposes. */
	threadIndex = inState->numThreads;
	if (threadIndex >= SJME_MOCK_MAX_THREADS)
		return sjme_die("Too make mock threads.");
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#if 0
	/* Allocate thread. */
	newThread = sjme_mock_alloc(inState, sizeof(*newThread));
	if (newThread == NULL)
		return sjme_die("Could not allocate thread.");
	
	/* Store in thread and bump up. */
	newThread->threadId = ++inData->nextThreadId;
	newThread->inState = inState->nvmState;
	inState->threads[threadIndex].nvmThread = newThread;
	
	/* Done. */
	return SJME_JNI_TRUE;
#endif
}

sjme_jboolean sjme_mock_doRomLibrary(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData)
{
	sjme_jint libraryIndex;
	sjme_nvm_rom_libraryBase* library;
	sjme_mock_configDataRomLibrary* data;
	sjme_nvm_rom_libraryFunctions* functions;

	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");

	/* Index of the resultant library. */
	libraryIndex = inState->numRomLibraries;
	if (libraryIndex >= SJME_MOCK_MAX_ROM_LIBRARIES)
		return sjme_die("Too many libraries.");

	/* Allocate library. */
	library = NULL;
	if (sjme_error_is(sjme_alloc(inState->allocPool,
		sizeof(*library), (void**)&library)) ||
		library == NULL)
		return sjme_die("Could not allocate library.");

	/* Make a copy of the input data to be used as front end specific data. */
	library->cache.common.frontEnd.data = NULL;
	if (sjme_error_is(sjme_alloc_copy(inState->allocPool,
		sizeof(inData->current.data.romLibrary),
		&library->cache.common.frontEnd.data,
		&inData->current.data.romLibrary)) ||
		library->cache.common.frontEnd.data == NULL)
		return sjme_die("Could not copy data.");

	/* Make sure the pool is set, otherwise other functions will not work. */
	library->cache.common.allocPool = inState->allocPool;

	/* Use the copied data instead. */
	data = library->cache.common.frontEnd.data;

	/* Setup baseline mock functions, if none are set for some. */
	library->functions = &data->functions;
	functions = &data->functions;

	/* Generic raw data access? */
	if (functions->rawData == NULL || functions->rawSize == NULL)
	{
		functions->rawData = sjme_mock_defaultRomLibraryRawData;
		functions->rawSize = sjme_mock_defaultRomLibraryRawSize;
	}

	/* ID of the library. */
	if (data->id != 0)
		library->id = data->id;
	else
		library->id = inData->current.indexType + 1;

	/* Name of the library. */
	if (data->name != NULL)
		library->name = data->name;
	else
	{
		if (sjme_error_is(sjme_alloc_format(inState->allocPool,
			(sjme_lpstr*)&library->name,
			"unnamed%d.jar", (int)(inData->current.indexType + 1))) ||
			library->name == NULL)
			return sjme_die("Could not set default library name.");
	}

	/* Store in mock data. */
	inState->romLibraries[inState->numRomLibraries++] = library;

	/* Success! */
	return SJME_JNI_TRUE;
}

sjme_jboolean sjme_mock_doRomMockLibrary(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData)
{
	sjme_errorCode error;
	sjme_jint libraryIndex;
	sjme_mock_configDataRomLibrary* data;
	sjme_jboolean isJar;
	sjme_nvm_rom_library result;

	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");

	/* Is this a JAR or not? */
	isJar = inData->current.data.romMockLibrary.isJar;

	/* Using an actual JAR file? Just open an actual library. */
	if (isJar)
	{
		/* Index of the resultant library. */
		libraryIndex = inState->numRomLibraries;
		if (libraryIndex >= SJME_MOCK_MAX_ROM_LIBRARIES)
			return sjme_die("Too many libraries.");

		/* Open it. */
		result = NULL;
		if (sjme_error_is(error = sjme_nvm_rom_libraryFromZipMemory(
			inState->allocPool, &result, "mock.jar",
			mock_jar__bin, mock_jar__len)) || result == NULL)
			return sjme_die("Could not open library: %d", error);

		/* Register it. */
		inState->romLibraries[inState->numRomLibraries++] = result;

		/* Success! */
		return SJME_JNI_TRUE;
	}

	/* Synthetic resource access. */
	else
	{
		/* Clear existing settings. */
		data = &inData->current.data.romLibrary;
		memset(data, 0, sizeof(*data));

		/* Setup aliased mock library. */
		data->name = "mock.jar";

		data->functions.resourceStream =
			sjme_mock_defaultRomMockLibraryResourceStream;

		/* This is just an alias so call the other accordingly. */
		return sjme_mock_doRomLibrary(inState, inData);
	}
}

sjme_jboolean sjme_mock_doRomSuite(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWorkData* inData)
{
	sjme_jint suiteIndex;
	sjme_nvm_rom_suite suite;
	sjme_nvm_rom_suiteFunctions* writeFunctions;
	sjme_mock_configDataRomSuite* suiteData;

	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");

	/* Too many suites declared already? */
	suiteIndex = inState->numRomSuites;
	if (suiteIndex >= SJME_MOCK_MAX_ROM_SUITES)
		return sjme_die("Too many ROM suites.");

	/* Allocate suite. */
	suite = NULL;
	if (sjme_error_is(sjme_alloc(inState->allocPool,
		sizeof(*suite), (void**)&suite)) || suite == NULL)
		return sjme_die("Could not allocate suite.");

	/* Quicker this way... */
	suiteData = &inData->current.data.romSuite;

	/* Seed front end data. */
	suite->cache.common.frontEnd.data = inState;

	/* Copy suite functions. */
	suite->functions = NULL;
	if (sjme_error_is(sjme_alloc_copy(inState->allocPool,
		sizeof(*suite->functions),
		(void**)&suite->functions,
		&suiteData->functions)) ||
		suite->functions == NULL)
		return sjme_die("Could not copy functions.");

	/* Set front end to the test state. */
	writeFunctions = (sjme_nvm_rom_suiteFunctions*)suite->functions;

	/* If there is no cache init, just initialize it to something... */
	if (writeFunctions->init == NULL)
		memset(&suite->cache, 0, sizeof(suite->cache));

	/* Otherwise call the initializer. */
	else
	{
		if (sjme_error_is(writeFunctions->init(
			suite, NULL)))
			return sjme_die("Could not initialize suite via cache init.");
	}

	/* Set the allocation pool to use if not set. */
	if (suite->cache.common.allocPool == NULL)
		suite->cache.common.allocPool = inState->allocPool;

	/* Is there a pre-cache used for libraries? */
	if (suiteData->cacheLibraries != NULL)
		suite->cache.libraries = suiteData->cacheLibraries;

	/* Place finalized suite down. */
	inState->romSuites[inState->numRomSuites] = suite;

	/* Continue on. */
	return SJME_JNI_TRUE;
}
