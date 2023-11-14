/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "elevator.h"

struct sjme_elevatorRunData
{
	/** The index type counts. */
	jint indexTypeCount[SJME_NUM_ELEVATOR_DO_TYPES];
	
	/** The current run. */
	sjme_elevatorRunCurrent current;
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
	{sjme_elevatorDoMakeFrame,
		SJME_ELEVATOR_DO_TYPE_MAKE_FRAME},
		
	/* End. */
	{NULL, SJME_ELEVATOR_DO_TYPE_UNKNOWN}
};

jboolean sjme_elevatorAct(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull const sjme_elevatorSet* inSet)
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
		
	if (inLen <= 0)
		return sjme_dieP("Invalid length: %d.", (jint)inLen);
	
	/* Attempt allocation. */
	rv = malloc(inLen);
	if (rv == NULL)
		return sjme_dieP("Failed to allocate %d bytes.", (jint)inLen);
	
	/* Initialize memory then return. */
	memset(rv, 0, inLen);
	return rv;
}

jboolean sjme_elevatorDoInit(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData)
{
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Allocate virtual machine state. */
	inState->nvmState = sjme_elevatorAlloc(inState,
		sizeof(*inState->nvmState));
	
	/* Is okay. */
	return JNI_TRUE;
}

jboolean sjme_elevatorDoMakeFrame(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData)
{
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	sjme_todo("Implement this?");
}

jboolean sjme_elevatorDoMakeThread(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData)
{
	jint threadId;
	
	if (inState == NULL || inData == NULL)
		return sjme_die("Null arguments.");
	
	/* Elevator has limited set of threads for testing purposes. */
	threadId = inState->numThreads;
	if (threadId >= SJME_ELEVATOR_MAX_THREADS)
		return sjme_die("Too make elevator threads.");
	
	sjme_todo("Implement this?");
}
