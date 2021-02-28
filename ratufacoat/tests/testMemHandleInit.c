/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

#include "tests.h"
#include "handles.h"

SJME_TEST_PROTOTYPE(testMemHandleInit)
{
	sjme_error error;
	sjme_memHandles* handles = NULL;
	
	/* Initialize handles. */
	if (sjme_initMemHandles(&handles, &error))
		return EXIT_FAILURE;
	
	/* Then immediately destroy them. */
	if (sjme_destroyMemHandles(handles, &error))
		return EXIT_FAILURE;
	
	return EXIT_SUCCESS;
}
