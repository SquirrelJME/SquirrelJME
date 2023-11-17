/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "test.h"
#include "proto.h"

#define TEST_NUM_OBJECT_IDS 3

sjme_testResult testNvmLocalPopReference(sjme_test* test)
{
	jbyte firstId, secondId;
	
	/* Test all possible combination of objects, NULL, same, different, etc. */
	for (firstId = 0; firstId < TEST_NUM_OBJECT_IDS; firstId++)
		for (secondId = 0; secondId < TEST_NUM_OBJECT_IDS; secondId++)
		{
			sjme_todo("Implement %s", __func__);
			return SJME_TEST_RESULT_FAIL;
		}
	
	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}

