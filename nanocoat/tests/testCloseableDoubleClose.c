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
#include "mock.h"
#include "unit.h"
#include "testCloseable.h"

static sjme_jint closeCount = 0;

static sjme_errorCode testHandler(
	sjme_attrInNotNull sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Bump up count. */
	closeCount++;
	
	return SJME_ERROR_NONE;
}

/**
 * Tests double close of closeable.
 *  
 * @since 2024/09/28 
 */
SJME_TEST_DECLARE(testCloseableDoubleClose)
{
	sjme_closeable closing;
	
	/* Make closeable. */
	if (sjme_error_is(test->error = testCloseable_new(test->pool,
		&closing, testHandler,
		SJME_JNI_TRUE)))
		return sjme_unit_fail(test, "Could not make closeable?");
	
	/* Should not be closed. */
	sjme_unit_equalI(test, 0, closeCount,
		"Was closed???");
	
	/* Close it. */
	if (sjme_error_is(test->error = sjme_closeable_close(closing)))
		return sjme_unit_fail(test, "Failed closing once?");
	
	/* Only once. */
	sjme_unit_equalI(test, 1, closeCount,
		"Not closed?");
	
	/* Close it, again! */
	if (sjme_error_is(test->error = sjme_closeable_close(closing)))
		return sjme_unit_fail(test, "Failed closing twice?");
	
	/* Still only once. */
	sjme_unit_equalI(test, 1, closeCount,
		"Close was called multiple times?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
