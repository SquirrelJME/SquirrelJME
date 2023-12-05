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
#include "elevator.h"

struct sjme_elevatorRunData
{
	/** The index type counts. */
	jint indexTypeCount[SJME_NUM_ELEVATOR_DO_TYPES];
	
	/** The current run. */
	sjme_elevatorRunCurrent current;
	
	/** The next thread ID. */
	jint nextThreadId;
};

/**
 * Elevator function to do type.
 * 
 * @since 2023/11/11
 */
struct
{
	sjme_elevatorDoFunc func;
	sjme_elevatorDoType type;
} sjme_elevatorFuncToType[SJME_NUM_ELEVATOR_DO_TYPES] =
{
	{sjme_elevatorDoInit,
		SJME_ELEVATOR_DO_TYPE_INIT},
	{sjme_elevatorDoMakeThread,
		SJME_ELEVATOR_DO_TYPE_MAKE_THREAD},
	{sjme_elevatorDoMakeObject,
		SJME_ELEVATOR_DO_TYPE_MAKE_OBJECT},
	{sjme_elevatorDoMakeFrame,
		SJME_ELEVATOR_DO_TYPE_MAKE_FRAME},
		
	/* End. */
	{NULL, SJME_ELEVATOR_DO_TYPE_UNKNOWN}
};

jboolean sjme_elevatorAct(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull const sjme_elevatorSet* inSet,
	sjme_attrInValue jint special)
{
	jint dx, i;
	sjme_elevatorRunData data;
	sjme_elevatorDoType doType;
	
	/* Check. */
	if (inState == NULL || inSet == NULL)
		return sjme_die("Null arguments.");
	
	/* Confirm that the set is valid. */
	if (inSet->config == NULL)
		return sjme_die("Invalid configuration.");
		
	/* Allocate main memory pool. */
	if (!sjme_alloc_poolMalloc(&inState->allocPool, 1024 * 1024))
		return sjme_die("Could not allocate main memory pool.");
	
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
		doType = SJME_ELEVATOR_DO_TYPE_UNKNOWN;
		for (i = 0; i < SJME_NUM_ELEVATOR_DO_TYPES; i++)
			if (inSet->order[dx] == sjme_elevatorFuncToType[i].func)
			{
				doType = sjme_elevatorFuncToType[i].type;
				break;
			}
		
		/* Not found? */
		if (doType == SJME_ELEVATOR_DO_TYPE_UNKNOWN)
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
	return JNI_TRUE;
}

void* sjme_elevatorAlloc(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInPositiveNonZero size_t inLen)
{
	void* rv;
	
	/* Check. */
	if (inState == NULL)
		return sjme_dieP("No input state.");
	
	rv = NULL;
	if (!sjme_alloc(inState->allocPool, inLen, &rv))
		return sjme_dieP("Could not allocate pointer in test pool.");
	
	return rv;
}

jboolean sjme_elevatorDoInit(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData)
{
	sjme_nvm_state* newState;
	
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Allocate virtual machine state. */
	newState = sjme_elevatorAlloc(inState,
		sizeof(*inState->nvmState));
	inState->nvmState = newState;
	
	/* Store test state, as required for some tests. */
	newState->special = inState;
	
	/* Register any hooks? */
	if (inData->current.data.state.hooks != NULL)
		newState->hooks = inData->current.data.state.hooks;
	
	/* Done. */
	return JNI_TRUE;
}

jboolean sjme_elevatorDoMakeFrame(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData)
{
	jint threadIndex, treadMax, tallyLocals, stackBase, desireMaxLocals;
	jint tallyStack, desireMaxStack, localIndex;
	sjme_nvm_thread* thread;
	sjme_nvm_frame* newFrame;
	sjme_basicTypeId typeId;
	sjme_nvm_frameTread* tread;
	sjme_nvm_frameStack* stack;
	sjme_nvm_frameLocalMap* localMap;
	jbyte baseLocalAt[SJME_NUM_JAVA_TYPE_IDS];
	
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Make sure the requested thread index is valid. */
	threadIndex = inData->current.data.frame.threadIndex;
	if (threadIndex < 0 || threadIndex >= SJME_ELEVATOR_MAX_THREADS ||
		inState->threads[threadIndex].nvmThread == NULL)
		return sjme_die("Invalid thread index %d.", threadIndex);
	
	/* Get the actual thread. */
	thread = inState->threads[threadIndex].nvmThread;
	
	/* Allocate new frame. */
	newFrame = sjme_elevatorAlloc(inState, sizeof(*newFrame));
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
	desireMaxLocals = inData->current.data.frame.maxLocals;
	localMap = sjme_elevatorAlloc(inState,
		SJME_SIZEOF_FRAME_LOCAL_MAP(desireMaxLocals));
	localMap->max = desireMaxLocals;
	
	/* Setup stack information. */
	desireMaxStack = inData->current.data.frame.maxStack;
	stack = sjme_elevatorAlloc(inState,
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
		treadMax = inData->current.data.frame.treads[typeId].max;
		if (treadMax <= 0)
			continue;
		
		/* Allocate target tread. */
		tread = sjme_elevatorAlloc(inState,
			SJME_SIZEOF_FRAME_TREAD_VAR(typeId, treadMax));
		newFrame->treads[typeId] = tread;
		
		/* Setup stack base. */
		stackBase = inData->current.data.frame.treads[typeId].stackBaseIndex;
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
			localMap->maps[localIndex].to[typeId] = (jbyte)localIndex;
		
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
	return JNI_TRUE;
}

jboolean sjme_elevatorDoMakeObject(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData)
{
	jobject newObject;
	
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Too many objects? */
	if (inState->numObjects >= SJME_ELEVATOR_MAX_OBJECTS)
		sjme_die("Too many elevator objects.");
	
	/* Allocate new object. */
	newObject = sjme_elevatorAlloc(inState, sizeof(*newObject));
	inState->objects[inState->numObjects++] = newObject;
	
	/* Initialize object details. */
	newObject->refCount = 1;
	
	/* Success. */
	return JNI_TRUE;
}

jboolean sjme_elevatorDoMakeThread(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData)
{
	jint threadIndex;
	sjme_nvm_thread* newThread;
	
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Elevator has a limited set of threads for testing purposes. */
	threadIndex = inState->numThreads;
	if (threadIndex >= SJME_ELEVATOR_MAX_THREADS)
		return sjme_die("Too make elevator threads.");
	
	/* Allocate thread. */
	newThread = sjme_elevatorAlloc(inState, sizeof(*newThread));
	if (newThread == NULL)
		return sjme_die("Could not allocate thread.");
	
	/* Store in thread and bump up. */
	newThread->threadId = ++inData->nextThreadId;
	newThread->inState = inState->nvmState;
	inState->threads[threadIndex].nvmThread = newThread;
	
	/* Done. */
	return JNI_TRUE;
}
