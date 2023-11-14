/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "test.h"
#include "elevator.h"
#include "proto.h"
#include "unit.h"

jboolean configNvmLocalPopInteger(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunCurrent* inCurrent)
{
	/* Check. */
	if (inState == NULL || inCurrent == NULL)
		return JNI_FALSE;
	
	/* Configure. */
	switch (inCurrent->indexAll)
	{
	}
	
	return JNI_TRUE;
}

/** Elevator set for test. */
static const sjme_elevatorSet elevatorNvmLocalPopInteger =
{
	configNvmLocalPopInteger,
	0,

	/* Elevator calls. */
	{
		sjme_elevatorDoInit,
		sjme_elevatorDoMakeThread,
		sjme_elevatorDoMakeFrame,
		NULL
	}
};

sjme_testResult testNvmLocalPopInteger(sjme_test* test)
{
	sjme_elevatorState state;
	sjme_nvm_frame* frame;
	jint oldNumStack, intVal;
	
	/* Perform the elevator. */
	memset(&state, 0, sizeof(state));
	if (!sjme_elevatorAct(&state, &elevatorNvmLocalPopInteger))
		sjme_die("Invalid elevator");
		
	/* Get initialize frame size. */
	frame = state.threads[0].nvmThread->top;
	
	/* Setup integer value. */
	frame->numInStack = 1;
	if (JNI_TRUE)
		sjme_todo("Implement set of first stack value??");
	
	/* Pop integer from the stack to the first local. */
	oldNumStack = frame->numInStack;
	if (!sjme_nvm_localPopInteger(frame, 0))
		return sjme_unitFail(test, "Failed to pop local integer.");
	
	/* New stack should be lower. */
	sjme_unitEqualI(test, frame->numInStack, oldNumStack - 1,
		"Items in stack not lower?");
	
	/* Read in value. */
	intVal = -1;
	if (!sjme_nvm_localReadInteger(frame, 0, &intVal))
		return sjme_unitFail(test, "Could not read value from stack.");
	sjme_unitEqualI(test, 1234, intVal,
		"Popped stack into local was not the correct value.");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
