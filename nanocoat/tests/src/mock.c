/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/except.h"
#include "mock.h"

struct sjme_mockRunData
{
	/** The index type counts. */
	sjme_jint indexTypeCount[SJME_NUM_MOCK_DO_TYPES];
	
	/** The current run. */
	sjme_mockRunCurrent current;
	
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
	sjme_mockDoFunc func;
	sjme_mockDoType type;
} sjme_mockFuncToType[SJME_NUM_MOCK_DO_TYPES] =
{
	{sjme_mockDoNvmFrame,
		SJME_MOCK_DO_TYPE_NVM_FRAME},
	{sjme_mockDoNvmObject,
		SJME_MOCK_DO_TYPE_NVM_OBJECT},
	{sjme_mockDoNvmState,
		SJME_MOCK_DO_TYPE_NVM_STATE},
	{sjme_mockDoNvmThread,
		SJME_MOCK_DO_TYPE_NVM_THREAD},
	{sjme_mockDoRomSuite,
		SJME_MOCK_DO_TYPE_ROM_SUITE},
		
	/* End. */
	{NULL, SJME_MOCK_DO_TYPE_UNKNOWN}
};

sjme_jboolean sjme_mockAct(
	sjme_attrInNotNull sjme_test* inTest,
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull const sjme_mockSet* inSet,
	sjme_attrInValue sjme_jint special)
{
	sjme_jint dx, i;
	sjme_mockRunData data;
	sjme_mockDoType doType;
	
	/* Check. */
	if (inState == NULL || inSet == NULL)
		return sjme_die("Null arguments.");
	
	/* Confirm that the set is valid. */
	if (inSet->config == NULL)
		return sjme_die("Invalid configuration.");
		
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
		if (!inSet->config(inState, &data.current))
			return sjme_die("Configuration step failed at %d.", dx);
		
		/* Call do function to perform whatever test initialization. */
		if (!inSet->order[dx](inState, &data))
			return sjme_die("Do failed at %d.", dx);
	}
	
	/* Successful. */
	return SJME_JNI_TRUE;
}

void* sjme_mockAlloc(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInPositiveNonZero size_t inLen)
{
	void* rv;
	
	/* Check. */
	if (inState == NULL)
		return sjme_dieP("No input state.");
	
	rv = NULL;
	if (SJME_IS_ERROR(sjme_alloc(inState->allocPool, inLen,
		&rv)))
		return sjme_dieP("Could not allocate pointer in test pool.");
	
	return rv;
}

sjme_jboolean sjme_mockDoNvmState(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunData* inData)
{
	sjme_nvm_state* newState;
	
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Allocate virtual machine state. */
	newState = sjme_mockAlloc(inState,
		sizeof(*inState->nvmState));
	inState->nvmState = newState;
	
	/* Store test state, as required for some tests. */
	newState->frontEnd.data = inState;
	
	/* Register any hooks? */
	if (inData->current.data.nvmState.hooks != NULL)
		newState->hooks = inData->current.data.nvmState.hooks;
	
	/* Done. */
	return SJME_JNI_TRUE;
}

sjme_jboolean sjme_mockDoNvmFrame(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunData* inData)
{
	sjme_jint threadIndex, treadMax, tallyLocals, stackBase, desireMaxLocals;
	sjme_jint tallyStack, desireMaxStack, localIndex;
	sjme_nvm_thread* thread;
	sjme_nvm_frame* newFrame;
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
	newFrame = sjme_mockAlloc(inState, sizeof(*newFrame));
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
	localMap = sjme_mockAlloc(inState,
		SJME_SIZEOF_FRAME_LOCAL_MAP(desireMaxLocals));
	localMap->max = desireMaxLocals;
	
	/* Setup stack information. */
	desireMaxStack = inData->current.data.nvmFrame.maxStack;
	stack = sjme_mockAlloc(inState,
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
		tread = sjme_mockAlloc(inState,
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
}

sjme_jboolean sjme_mockDoNvmObject(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunData* inData)
{
	sjme_jobject newObject;
	
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Too many objects? */
	if (inState->numObjects >= SJME_MOCK_MAX_OBJECTS)
		sjme_die("Too many mock objects.");
	
	/* Allocate new object. */
	newObject = sjme_mockAlloc(inState, sizeof(*newObject));
	inState->objects[inState->numObjects++] = newObject;
	
	/* Initialize object details. */
	newObject->refCount = 1;
	
	/* Success. */
	return SJME_JNI_TRUE;
}

sjme_jboolean sjme_mockDoNvmThread(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunData* inData)
{
	sjme_jint threadIndex;
	sjme_nvm_thread* newThread;
	
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Mock has a limited set of threads for testing purposes. */
	threadIndex = inState->numThreads;
	if (threadIndex >= SJME_MOCK_MAX_THREADS)
		return sjme_die("Too make mock threads.");
	
	/* Allocate thread. */
	newThread = sjme_mockAlloc(inState, sizeof(*newThread));
	if (newThread == NULL)
		return sjme_die("Could not allocate thread.");
	
	/* Store in thread and bump up. */
	newThread->threadId = ++inData->nextThreadId;
	newThread->inState = inState->nvmState;
	inState->threads[threadIndex].nvmThread = newThread;
	
	/* Done. */
	return SJME_JNI_TRUE;
}

sjme_jboolean sjme_mockDoRomSuite(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunData* inData)
{
	sjme_jint suiteIndex;
	sjme_rom_suite* suite;
	sjme_rom_suiteFunctions* writeFunctions;

	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");

	/* Too many suites declared already? */
	suiteIndex = inState->numRomSuites;
	if (suiteIndex >= SJME_MOCK_MAX_ROM_SUITES)
		return sjme_die("Too many ROM suites.");

	/* Allocate suite. */
	suite = NULL;
	if (SJME_IS_ERROR(sjme_alloc(inState->allocPool,
		sizeof(*suite), &suite)) || suite == NULL)
		return sjme_die("Could not allocate suite.");

	/* Copy suite functions. */
	suite->functions = NULL;
	if (SJME_IS_ERROR(sjme_alloc_copy(inState->allocPool,
		sizeof(*suite->functions),
		&suite->functions,
		&inData->current.data.romSuite.functions)) ||
		suite->functions == NULL)
		return sjme_die("Could not copy functions.");

	/* Set front end to the test state. */
	writeFunctions = (sjme_rom_suiteFunctions*)suite->functions;
	writeFunctions->frontEnd.data = inState;

	/* If there is no cache init, just initialize it to something... */
	if (writeFunctions->initCache == NULL)
	{
		/* Just allocate one... */
		suite->cache = NULL;
		if (SJME_IS_ERROR(sjme_alloc(inState->allocPool,
				SJME_SIZEOF_SUITE_CACHE(int),
				(void**)&suite->cache)) || suite->cache == NULL)
			return sjme_die("Could not fake init cache.");
	}

	/* Continue on. */
	return SJME_JNI_TRUE;
}
