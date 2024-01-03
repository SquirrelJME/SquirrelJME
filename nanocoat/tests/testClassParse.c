/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "mock.h"
#include "proto.h"
#include "test.h"
#include "unit.h"

/**
 * Tests parsing of classes.
 *  
 * @since 2024/01/01 
 */
SJME_TEST_DECLARE(testClassParse)
{
	sjme_stream_input stream;

#if 0
	if (SJME_IS_ERROR(sjme_stream_inputOpenMemory()))
		return sjme_unitFail(test, "Could not open class input stream.");
#endif

	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
