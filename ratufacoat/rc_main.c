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
ratufacoat_machine_t* ratufacoat_createmachine(ratufacoat_boot_t* boot)
{
	ratufacoat_machine_t* rv;
	
	// This needs to be passed, if not then it shall fail
	if (boot == NULL || boot->native == NULL)
		return NULL;
	
	// Try to allocate the machine data
	rv = ratufacoat_memalloc(sizeof(*rv));
	if (rv == NULL)
		return NULL;
	
	// Copy some basic boot parameters
	rv->native = boot->native;
	rv->args = boot->args;
	
	fprintf(stderr, "Hello!\n");
	
	ratufacoat_todo();
	
	return rv;
}

/** Fails with a ToDo message. */
void ratufacoat_todo(void)
{
	fprintf(stderr, "TODO hit!\n");
	raise(SIGABRT);
	exit(7);
}

