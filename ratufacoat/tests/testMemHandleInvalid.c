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

/**
 * Test that invalid actions on handles fail.
 * 
 * @since 2021/02/28 
 */
SJME_TEST_PROTOTYPE(testMemHandleInvalid)
{
	sjme_error error;
	sjme_memHandles* handles = NULL;
	sjme_memHandle* handle = NULL;
	
	/* Initialize handles. */
	if (sjme_memHandlesInit(&handles, &error))
		return EXIT_FAILURE;
	
	/* Negative size. */
	if (!sjme_memHandleNew(handles, &handle,
		SJME_MEMHANDLE_KIND_OBJECT_INSTANCE, -127, &error))
		return EXIT_FAILURE;
	
	/* Then immediately destroy them. */
	if (sjme_memHandlesDestroy(handles, &error))
		return EXIT_FAILURE;
	
	return EXIT_SUCCESS;
}
