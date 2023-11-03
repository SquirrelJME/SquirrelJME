/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "test.h"
#include "elevator.h"
#include "proto.h"

/** Elevator set for test. */
static const sjme_elevatorSet elevatorNvmLocalPopInteger[] =
{
	0,
	{
			{
				beep,
			},
			{
			}
	}
};

sjme_testResult testNvmLocalPopInteger(sjme_test* test)
{
	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}


#pragma clang diagnostic pop
