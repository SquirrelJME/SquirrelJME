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

jboolean configNvmLocalPopInteger(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull const sjme_elevatorRunCurrent* inCurrent)
{
	sjme_todo("Implement this?");
}

/** Elevator set for test. */
static const sjme_elevatorSet elevatorNvmLocalPopInteger =
{
	configNvmLocalPopInteger,
	0,

	/* Elevator calls. */
	{
		sjme_elevatorDoInit,
		NULL
	}
};

sjme_testResult testNvmLocalPopInteger(sjme_test* test)
{
	sjme_elevatorState state;
	
	/* Perform the elevator. */
	memset(&state, 0, sizeof(state));
	if (!sjme_elevatorAct(&state, &elevatorNvmLocalPopInteger))
		sjme_die("Invalid elevator");

	sjme_todo("Implement %s", __func__);
	
	return SJME_TEST_RESULT_FAIL;
}
