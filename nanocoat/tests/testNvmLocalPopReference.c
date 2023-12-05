/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "proto.h"
#include "elevator.h"
#include "sjme/debug.h"
#include "unit.h"
#include "test.h"

#define TEST_NUM_OBJECT_IDS 3

typedef struct testHookResult
{
	/** The number of GCed objects. */
	sjme_jint count;
	
	/** The GCed objects. */
	sjme_jobject gc[SJME_ELEVATOR_MAX_OBJECTS];
} testHookResult;

static sjme_jboolean hookGcNvmLocalPopReference(sjme_nvm_frame* frame,
	sjme_jobject instance)
{
	sjme_elevatorState* elevator;
	testHookResult* hookResult;
	
	/* Debug. */
	sjme_message("GC of %p...", instance);
	
	/* Elevator must be set. */
	elevator = frame->inThread->inState->special;
	if (elevator == NULL)
		return SJME_JNI_FALSE;
	
	/* There must be a hook result. */
	hookResult = elevator->special;
	if (hookResult == NULL)
		return SJME_JNI_FALSE;
	
	/* Track it, within reason. */
	if (hookResult->count < SJME_ELEVATOR_MAX_OBJECTS)
		hookResult->gc[hookResult->count++] = instance;
	
	/* Success! */
	return SJME_JNI_TRUE;
}
 
const sjme_nvm_stateHooks hooksNvmLocalPopReference =
{
	.gc = hookGcNvmLocalPopReference,
};

sjme_jboolean configNvmLocalPopReference(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunCurrent* inCurrent)
{
	/* Check. */
	if (inState == NULL || inCurrent == NULL)
		return SJME_JNI_FALSE;
	
	/* Configure. */
	switch (inCurrent->type)
	{
		case SJME_ELEVATOR_DO_TYPE_INIT:
			inCurrent->data.state.hooks = &hooksNvmLocalPopReference;
			break;
		
		case SJME_ELEVATOR_DO_TYPE_MAKE_FRAME:
			inCurrent->data.frame.maxLocals = 1;
			inCurrent->data.frame.maxStack = 1;
			inCurrent->data.frame.treads[SJME_JAVA_TYPE_ID_OBJECT]
				.max = 2;
			inCurrent->data.frame.treads[SJME_JAVA_TYPE_ID_OBJECT]
				.stackBaseIndex = 1;
			break;
	}
	
	return SJME_JNI_TRUE;
}

/** Elevator set for test. */
static const sjme_elevatorSet elevatorNvmLocalPopReference =
	{
		configNvmLocalPopReference,
		0,

		/* Elevator calls. */
		{
			sjme_elevatorDoInit,
			sjme_elevatorDoMakeThread,
			sjme_elevatorDoMakeFrame,
			sjme_elevatorDoMakeObject,
			sjme_elevatorDoMakeObject,
			NULL
		}
};

SJME_TEST_DECLARE(testNvmLocalPopReference)
{
	sjme_jbyte firstId, secondId;
	sjme_elevatorState state;
	sjme_nvm_frame* frame;
	sjme_jint oldNumStack;
	sjme_nvm_frameTread* objectsTread;
	sjme_nvm_frameStack* stack;
	testHookResult hookResult;
	
	/* Test all possible combination of objects: [a, b, NULl]. */
	/* This is for testing that reference counting works in this case. */
	for (firstId = 0; firstId < TEST_NUM_OBJECT_IDS; firstId++)
		for (secondId = 0; secondId < TEST_NUM_OBJECT_IDS; secondId++)
		{
			/* Perform the elevator. */
			memset(&state, 0, sizeof(state));
			if (!sjme_elevatorAct(&state,
					&elevatorNvmLocalPopReference,
					firstId + (secondId * TEST_NUM_OBJECT_IDS)))
				sjme_die("Invalid elevator");
			
			/* Set special data for testing. */
			memset(&hookResult, 0, sizeof(hookResult));
			state.special = &hookResult;
		
			/* Get initialize frame size. */
			frame = state.threads[0].nvmThread->top;
	
			/* Setup integer values. */
			objectsTread = frame->treads[SJME_JAVA_TYPE_ID_OBJECT];
			stack = frame->stack;
			objectsTread->values.jobjects[0] = state.objects[secondId];
			objectsTread->values.jobjects[1] = state.objects[firstId];
			objectsTread->count = objectsTread->stackBaseIndex + 1;
			stack->count = 1;
			stack->order[0] = SJME_JAVA_TYPE_ID_OBJECT;
	
			/* Pop integer from the stack to the first local. */
			oldNumStack = stack->count;
			if (!sjme_nvm_localPopReference(frame, 0))
				return sjme_unitFail(test, "Failed to pop local reference.");
			
			/* Only a specific object should be GCed and only in a certain */
			/* circumstance. */
			if (state.objects[secondId] != NULL &&
				state.objects[secondId] != state.objects[firstId])
			{
				sjme_unitEqualL(test,
					hookResult.gc[0], state.objects[secondId],
					"Old local was not what should have been GCed?");
				sjme_unitEqualI(test,
					hookResult.count, 1,
					"Different old local not GCed?");
			}
	
			/* New stack should be lower. */
			sjme_unitEqualI(test, stack->count, oldNumStack - 1,
				"Items in stack not lower?");
	
			/* Check that the value was moved over. */
			sjme_unitEqualL(test, state.objects[firstId],
				objectsTread->values.jobjects[0],
				"Popped stack into local was not the correct value.");
		
			/* And the stack value was cleared. */
			sjme_unitEqualL(test, NULL, objectsTread->values.jobjects[1],
				"Stack value did not get cleared.");
		}
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}

