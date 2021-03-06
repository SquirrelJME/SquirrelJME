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

/** Size of the test handle. */
#define HANDLE_SIZE 8

/**
 * Tests accessing memory handles.
 * 
 * @since 2021/03/06 
 */
SJME_TEST_PROTOTYPE(testMemHandleAccess)
{
	sjme_memHandles* handles = NULL;
	sjme_memHandle* handle = NULL;
	
	/* Initialize handles. */
	if (sjme_memHandlesInit(&handles, &shim->error))
		return FAIL_TEST(1);
	
	/* Setup a handle. */
	if (sjme_memHandleNew(handles, &handle,
		SJME_MEMHANDLE_KIND_OBJECT_INSTANCE, HANDLE_SIZE,
		&shim->error))
		return FAIL_TEST(2);
	
	/* In bounds. */
	if (sjme_memHandleInBounds(handle, 0, HANDLE_SIZE, &shim->error))
		return FAIL_TEST(3);
	
	/* Negative offset. */
	sjme_clearError(&shim->error);
	if (!sjme_memHandleInBounds(handle, -1, HANDLE_SIZE, &shim->error))
		return FAIL_TEST(4);
	
	/* Missing error? */
	if (!sjme_hasError(&shim->error))
		return FAIL_TEST(5);
	
	/* Negative size. */
	sjme_clearError(&shim->error);
	if (!sjme_memHandleInBounds(handle, 0, -1, &shim->error))
		return FAIL_TEST(6);
		
	/* Missing error? */
	if (!sjme_hasError(&shim->error))
		return FAIL_TEST(7);
	
	/* Off right side. */
	sjme_clearError(&shim->error);
	if (!sjme_memHandleInBounds(handle, 4, HANDLE_SIZE, &shim->error))
		return FAIL_TEST(8);
		
	/* Missing error? */
	if (!sjme_hasError(&shim->error))
		return FAIL_TEST(9);
	
	/* Then immediately destroy them. */
	if (sjme_memHandlesDestroy(handles, &shim->error))
		return FAIL_TEST(10);
	
	return PASS_TEST();
}
