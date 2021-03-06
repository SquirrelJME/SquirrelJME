/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "handles.h"

#define TEST_HANDLE_SIZE 128

/**
 * Test that invalid actions on handles fail.
 * 
 * @since 2021/02/28 
 */
SJME_TEST_PROTOTYPE(testMemHandleInvalid)
{
	sjme_memHandles* handles = NULL;
	sjme_memHandle* handle = NULL;
	
	/* No output handles. */
	if (!sjme_memHandlesInit(NULL, &shim->error))
		return FAIL_TEST(1);
	
	/* Error not set? */
	if (!sjme_hasError(&shim->error))
		return FAIL_TEST(2);
	
	/* Initialize handles. */
	if (sjme_memHandlesInit(&handles, &shim->error))
		return FAIL_TEST(3);
	
	/* Error not set? */
	if (!sjme_hasError(&shim->error))
		return FAIL_TEST(4);
	
	/* Negative size. */
	if (!sjme_memHandleNew(handles, &handle,
		SJME_MEMHANDLE_KIND_OBJECT_INSTANCE, -127, &shim->error))
		return FAIL_TEST(5);
	
	/* Error not set? */
	if (!sjme_hasError(&shim->error))
		return FAIL_TEST(6);
	
	/* Invalid handle (low). */
	if (!sjme_memHandleNew(handles, &handle,
		SJME_MEMHANDLE_KIND_UNDEFINED, TEST_HANDLE_SIZE, &shim->error))
		return FAIL_TEST(7);
	
	/* Error not set? */
	if (!sjme_hasError(&shim->error))
		return FAIL_TEST(8);
	
	/* Invalid handle (high). */
	if (!sjme_memHandleNew(handles, &handle,
		SJME_MEMHANDLE_KIND_NUM_KINDS, TEST_HANDLE_SIZE, &shim->error))
		return FAIL_TEST(9);
	
	/* Error not set? */
	if (!sjme_hasError(&shim->error))
		return FAIL_TEST(10);
	
	/* Delete null handle? */
	if (!sjme_memHandleDelete(handles, NULL, &shim->error))
		return FAIL_TEST(11);
	
	/* Error not set? */
	if (!sjme_hasError(&shim->error))
		return FAIL_TEST(12);
	
	/* Setup actual handle for testing. */
	if (sjme_memHandleNew(handles, &handle,
		SJME_MEMHANDLE_KIND_OBJECT_INSTANCE, TEST_HANDLE_SIZE,
			&shim->error))
		return FAIL_TEST(13);
	
	/* Delete valid handle with no handle placement? */
	if (!sjme_memHandleDelete(NULL, handle, &shim->error))
		return FAIL_TEST(14);
	
	/* Error not set? */
	if (!sjme_hasError(&shim->error))
		return FAIL_TEST(15);
	
	/* Delete valid handle for cleanup */
	if (sjme_memHandleDelete(handles, handle, &shim->error))
		return FAIL_TEST(16);
	
	/* Then immediately destroy them. */
	if (sjme_memHandlesDestroy(handles, &shim->error))
		return FAIL_TEST(17);
	
	return PASS_TEST();
}
