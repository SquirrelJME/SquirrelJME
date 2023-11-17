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

jboolean configNvmLocalPopReference(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunCurrent* inCurrent)
{
	/* Check. */
	if (inState == NULL || inCurrent == NULL)
		return JNI_FALSE;
	
	/* Configure. */
	switch (inCurrent->type)
	{
		case SJME_ELEVATOR_DO_TYPE_MAKE_FRAME:
			inCurrent->data.frame.maxLocals = 1;
			inCurrent->data.frame.maxStack = 1;
			inCurrent->data.frame.treads[SJME_JAVA_TYPE_ID_OBJECT]
				.max = 2;
			inCurrent->data.frame.treads[SJME_JAVA_TYPE_ID_OBJECT]
				.stackBaseIndex = 1;
			break;
	}
	
	return JNI_TRUE;
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

sjme_testResult testNvmLocalPopReference(sjme_test* test)
{
	jbyte firstId, secondId;
	sjme_elevatorState state;
	sjme_nvm_frame* frame;
	jint oldNumStack;
	sjme_nvm_frameTread* objectsTread;
	sjme_nvm_frameStack* stack;
	
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
	
			/* New stack should be lower. */
			sjme_unitEqualI(test, stack->count, oldNumStack - 1,
				"Items in stack not lower?");
	
			sjme_todo("Implement comparisons and GC checks?");
			/* Check that the value was moved over. */
			/*sjme_unitEqualI(test, 0x12345678, intsTread->values.jints[0],
				"Popped stack into local was not the correct value.");*/
		
			/* And the stack value was cleared. */
			/*sjme_unitEqualI(test, 0, intsTread->values.jints[1],
				"Stack value did not get cleared.");*/
		}
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}

