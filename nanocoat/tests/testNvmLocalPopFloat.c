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
#include "sjme/nvmFunc.h"
#include "test.h"
#include "unit.h"

sjme_jboolean configNvmLocalPopFloat(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunCurrent* inCurrent)
{
	sjme_mockDataNvmFrame* frame;

	/* Check. */
	if (inState == NULL || inCurrent == NULL)
		return SJME_JNI_FALSE;

	/* Quick access. */
	frame = &inCurrent->data.nvmFrame;

	/* Configure. */
	switch (inCurrent->type)
	{
		case SJME_MOCK_DO_TYPE_NVM_FRAME:
			frame->maxLocals = 1;
			frame->maxStack = 1;
			frame->treads[SJME_JAVA_TYPE_ID_FLOAT].max = 2;
			frame->treads[SJME_JAVA_TYPE_ID_FLOAT].stackBaseIndex = 1;
			break;
	}
	
	return SJME_JNI_TRUE;
}

/** Mock set for test. */
static const sjme_mockSet mockNvmLocalPopFloat =
	{
		configNvmLocalPopFloat,
		0,

		/* Mock calls. */
		{
			sjme_mockDoNvmState,
			sjme_mockDoNvmThread,
			sjme_mockDoNvmFrame,
			NULL
		}
};

sjme_attrUnused SJME_TEST_DECLARE(testNvmLocalPopFloat)
{
	sjme_mockState state;
	sjme_nvm_frame* frame;
	sjme_jint oldNumStack;
	sjme_nvm_frameTread* intsTread;
	sjme_nvm_frameStack* stack;
	
	/* Perform the mock. */
	memset(&state, 0, sizeof(state));
	if (!sjme_mockAct(test,  &state,
		&mockNvmLocalPopFloat, 0))
		sjme_die("Invalid mock");
		
	/* Get initialize frame size. */
	frame = state.threads[0].nvmThread->top;
	
	/* Setup integer values. */
	intsTread = frame->treads[SJME_JAVA_TYPE_ID_FLOAT];
	stack = frame->stack;
	intsTread->values.jfloats[1].value = 0x12345678;
	intsTread->count = intsTread->stackBaseIndex + 1;
	stack->count = 1;
	stack->order[0] = SJME_JAVA_TYPE_ID_FLOAT;
	
	/* Pop integer from the stack to the first local. */
	oldNumStack = stack->count;
	if (!sjme_nvm_localPopFloat(frame, 0))
		return sjme_unitFail(test, "Failed to pop local float.");
	
	/* New stack should be lower. */
	sjme_unitEqualI(test, stack->count, oldNumStack - 1,
		"Items in stack not lower?");
	
	/* Check that the value was moved over. */
	sjme_unitEqualI(test, 0x12345678, intsTread->values.jfloats[0].value,
		"Popped stack into local was not the correct value.");
		
	/* And the stack value was cleared. */
	sjme_unitEqualI(test, 0, intsTread->values.jfloats[1].value,
		"Stack value did not get cleared.");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}

