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
#include "proto.h"
#include "sjme/debug.h"
#include "sjme/nvm/nvmFunc.h"
#include "test.h"
#include "unit.h"

#define TEST_NUM_OBJECT_IDS 3

typedef struct testHookResult
{
	/** The number of GCed objects. */
	sjme_jint count;
	
	/** The GCed objects. */
	sjme_jobject gc[SJME_MOCK_MAX_OBJECTS];
} testHookResult;

static sjme_jboolean hookGcNvmLocalPopReference(sjme_nvm_frame frame,
	sjme_jobject instance)
{
	sjme_mock* mock;
	testHookResult* hookResult;
	
	/* Debug. */
	sjme_message("GC of %p...", instance);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#if 0
	/* Mock must be set. */
	mock = frame->inThread->inState->common.frontEnd.data;
	if (mock == NULL)
		return SJME_JNI_FALSE;
	
	/* There must be a hook result. */
	hookResult = mock->special;
	if (hookResult == NULL)
		return SJME_JNI_FALSE;
	
	/* Track it, within reason. */
	if (hookResult->count < SJME_MOCK_MAX_OBJECTS)
		hookResult->gc[hookResult->count++] = instance;
	
	/* Success! */
	return SJME_JNI_TRUE;
#endif
}
 
const sjme_nvm_stateHooks hooksNvmLocalPopReference =
{
	.gc = hookGcNvmLocalPopReference,
};

sjme_jboolean configNvmLocalPopReference(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWork* inCurrent)
{
	sjme_mock_configDataNvmState* state;
	sjme_mock_configDataNvmFrame* frame;
	
	/* Check. */
	if (inState == NULL || inCurrent == NULL)
		return SJME_JNI_FALSE;
	
	/* Quick access. */
	state = &inCurrent->data.nvmState;
	frame = &inCurrent->data.nvmFrame;

	/* Configure. */
	switch (inCurrent->type)
	{
		case SJME_MOCK_DO_TYPE_NVM_STATE:
			state->hooks = &hooksNvmLocalPopReference;
			break;
		
		case SJME_MOCK_DO_TYPE_NVM_FRAME:
			frame->maxLocals = 1;
			frame->maxStack = 1;
			frame->treads[SJME_JAVA_TYPE_ID_OBJECT].max = 2;
			frame->treads[SJME_JAVA_TYPE_ID_OBJECT].stackBaseIndex = 1;
			break;
	}
	
	return SJME_JNI_TRUE;
}

/** Mock set for test. */
static const sjme_mock_configSet mockNvmLocalPopReference =
	{
		configNvmLocalPopReference,
		0,

		/* Mock calls. */
		{
			sjme_mock_doNvmState,
			sjme_mock_doNvmThread,
			sjme_mock_doNvmFrame,
			sjme_mock_doNvmObject,
			sjme_mock_doNvmObject,
			NULL
		}
};

SJME_TEST_DECLARE(testNvmLocalPopReference)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#if 0
	sjme_jbyte firstId, secondId;
	sjme_mock state;
	sjme_nvm_frame frame;
	sjme_jint oldNumStack;
	sjme_nvm_frameTread* objectsTread;
	sjme_nvm_frameStack* stack;
	testHookResult hookResult;
	
	/* Test all possible combination of objects: [a, b, NULl]. */
	/* This is for testing that reference counting works in this case. */
	for (firstId = 0; firstId < TEST_NUM_OBJECT_IDS; firstId++)
		for (secondId = 0; secondId < TEST_NUM_OBJECT_IDS; secondId++)
		{
			/* Perform the mock. */
			memset(&state, 0, sizeof(state));
			if (!sjme_mock_act(test, &state,
					&mockNvmLocalPopReference,
					firstId + (secondId * TEST_NUM_OBJECT_IDS)))
				sjme_die("Invalid mock");
			
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
				return sjme_unit_fail(test, "Failed to pop local reference.");
			
			/* Only a specific object should be GCed and only in a certain */
			/* circumstance. */
			if (state.objects[secondId] != NULL &&
				state.objects[secondId] != state.objects[firstId])
			{
				sjme_unit_equalL(test,
					hookResult.gc[0], state.objects[secondId],
					"Old local was not what should have been GCed?");
				sjme_unit_equalI(test,
					hookResult.count, 1,
					"Different old local not GCed?");
			}
	
			/* New stack should be lower. */
			sjme_unit_equalI(test, stack->count, oldNumStack - 1,
				"Items in stack not lower?");
	
			/* Check that the value was moved over. */
			sjme_unit_equalL(test, state.objects[firstId],
				objectsTread->values.jobjects[0],
				"Popped stack into local was not the correct value.");
		
			/* And the stack value was cleared. */
			sjme_unit_equalL(test, NULL, objectsTread->values.jobjects[1],
				"Stack value did not get cleared.");
		}
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
#endif
}

