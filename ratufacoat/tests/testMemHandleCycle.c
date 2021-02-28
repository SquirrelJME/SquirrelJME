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

/** Handle test size. */
#define TEST_HANDLE_SIZE 128

/**
 * Tests a full cycle for memory handles, everything about them.
 * 
 * @since 2021/02/28 
 */
SJME_TEST_PROTOTYPE(testMemHandleCycle)
{
	sjme_error error;
	sjme_memHandles* handles = NULL;
	sjme_memHandle* handle = NULL;
	
	/* Initialize handles. */
	if (sjme_memHandlesInit(&handles, &error))
		return EXIT_FAILURE;
	
	/* Create a new handle. */
	if (sjme_memHandleNew(handles, &handle,
		SJME_MEMHANDLE_KIND_OBJECT_INSTANCE, TEST_HANDLE_SIZE, &error))
		return EXIT_FAILURE;
	
	/* Then immediately destroy them. */
	if (sjme_memHandlesDestroy(handles, &error))
		return EXIT_FAILURE;
	
	return EXIT_SUCCESS;
}
