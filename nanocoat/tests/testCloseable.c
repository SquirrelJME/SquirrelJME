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

sjme_errorCode testCloseable_new(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_closeable* outCloseable,
	sjme_attrInNotNull sjme_closeable_closeHandlerFunc handlerFunc,
	sjme_attrInValue sjme_jboolean isRefCounted)
{
	sjme_errorCode error;
	sjme_closeable result;
	
	if (inPool == NULL || outCloseable == NULL || handlerFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(
		inPool, sizeof(*result), sjme_closeable_autoEnqueue, NULL,
		(sjme_pointer*)&result, NULL)) || result == NULL)
		return sjme_error_default(error);
	
	/* Initialize fields. */
	result->closeHandler = handlerFunc;
	result->refCounting = isRefCounted;
	
	/* Success! */
	*outCloseable = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode testHandler(
	sjme_attrInNotNull sjme_closeable closeable)
{
	return SJME_ERROR_NONE;
}

/**
 * Generic closeable test.
 *  
 * @since 2024/09/28 
 */
SJME_TEST_DECLARE(testCloseable)
{
	sjme_closeable closing;
	
	/* Make closeable. */
	if (sjme_error_is(test->error = testCloseable_new(test->pool,
		&closing, testHandler,
		SJME_JNI_FALSE)))
		return sjme_unit_fail(test, "Could not make closeable?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
