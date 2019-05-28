/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * Main entry point for the RatufaCoat VM.
 *
 * @since 2019/05/28
 */

#include "ratufac.h"

/** Creates RatufaCoat machine. */
ratufacoat_machine_t* ratufacoat_createmachine(ratufacoat_native_t* native,
	int argc, char** argv)
{
	// This needs to be passed, if not then it shall fail
	if (native == NULL)
		return NULL;
	
	ratufacoat_todo();
	return NULL;
}

/** Fails with a ToDo message. */
void ratufacoat_todo(void)
{
	fprintf(stderr, "TODO hit!\n");
	exit(7);
}

