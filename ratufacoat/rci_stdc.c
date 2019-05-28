/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * Standard C entry point for RatufaCoat.
 *
 * @since 2019/05/28
 */

#include "ratufac.h"

/**
 * Main entry point.
 * 
 * @param argc Argument count.
 * @param argv Arguments.
 * @since 2019/05/28
 */
int main(int argc, char** argv)
{
	ratufacoat_machine_t* machine;
	ratufacoat_native_t* native;
	
	// Allocate native functions
	native = calloc(1, sizeof(*native));
	if (native == NULL)
	{
		fprintf(stderr, "Could not allocate native handler!\n");
		return EXIT_FAILURE;
	}
	
	// Create the machine
	machine = ratufacoat_createmachine(native, argc, argv);
	if (machine == NULL)
	{
		free(native);
		
		fprintf(stderr, "Could not create RatufaCoat machine!\n");
		return EXIT_FAILURE;
	}
	
	return EXIT_SUCCESS;
}
