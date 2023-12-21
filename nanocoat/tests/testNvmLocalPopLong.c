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

sjme_jboolean configNvmLocalPopLong(
	sjme_attrInNotNull sjme_mockState* inState,
	sjme_attrInNotNull sjme_mockRunCurrent* inCurrent)
{
	/* Check. */
	if (inState == NULL || inCurrent == NULL)
		return SJME_JNI_FALSE;
	
	/* Configure. */
	switch (inCurrent->type)
	{
		case SJME_MOCK_DO_TYPE_MAKE_FRAME:
			inCurrent->data.frame.maxLocals = 1;
			inCurrent->data.frame.maxStack = 1;
			inCurrent->data.frame.treads[SJME_JAVA_TYPE_ID_LONG]
				.max = 2;
			inCurrent->data.frame.treads[SJME_JAVA_TYPE_ID_LONG]
				.stackBaseIndex = 1;
			break;
	}
	
	return SJME_JNI_TRUE;
}

/** Mock set for test. */
static const sjme_mockSet mockNvmLocalPopLong =
{
	configNvmLocalPopLong,
	0,

	/* Mock calls. */
	{
		sjme_mockDoInit,
		sjme_mockDoMakeThread,
		sjme_mockDoMakeFrame,
		NULL
	}
};

sjme_attrUnused SJME_TEST_DECLARE(testNvmLocalPopLong)
{
	sjme_mockState state;
	sjme_nvm_frame* frame;
	sjme_jint oldNumStack;
	sjme_nvm_frameTread* longsTread;
	sjme_nvm_frameStack* stack;
	
	/* Perform the mock. */
	memset(&state, 0, sizeof(state));
	if (!sjme_mockAct(&state,
		&mockNvmLocalPopLong, 0))
		sjme_die("Invalid mock");
		
	/* Get initialize frame size. */
	frame = state.threads[0].nvmThread->top;
	
	/* Setup integer values. */
	longsTread = frame->treads[SJME_JAVA_TYPE_ID_LONG];
	stack = frame->stack;
	longsTread->values.jlongs[1].hi = 0x12345678;
	longsTread->values.jlongs[1].lo = 0x9ABCDEF0;
	longsTread->count = longsTread->stackBaseIndex + 1;
	stack->count = 1;
	stack->order[0] = SJME_JAVA_TYPE_ID_LONG;
	
	/* Pop integer from the stack to the first local. */
	oldNumStack = stack->count;
	if (!sjme_nvm_localPopLong(frame, 0))
		return sjme_unitFail(test, "Failed to pop local long.");
	
	/* New stack should be lowered. */
	sjme_unitEqualI(test, stack->count, oldNumStack - 1,
		"Items in stack not lower?");
	
	/* Check that the value was moved over. */
	sjme_unitEqualI(test, 0x12345678, longsTread->values.jlongs[0].hi,
		"Popped stack into local was not the correct value.");
	sjme_unitEqualI(test, 0x9ABCDEF0, longsTread->values.jlongs[0].lo,
		"Popped stack into local was not the correct value.");
		
	/* And the stack value was cleared. */
	sjme_unitEqualI(test, 0, longsTread->values.jlongs[1].hi,
		"Stack value did not get cleared.");
	sjme_unitEqualI(test, 0, longsTread->values.jlongs[1].lo,
		"Stack value did not get cleared.");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}

