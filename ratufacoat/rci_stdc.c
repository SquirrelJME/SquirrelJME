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
	ratufacoat_boot_t* boot;
	ratufacoat_native_t* native;
	ratufacoat_args_t* args;
	
	// Allocate boot argument
	args = calloc(1, sizeof(*args));
	if (args == NULL)
	{
		fprintf(stderr, "Could not allocate arguments!\n");
		return EXIT_FAILURE;
	}
	
	// Set arguments
	args->argc = argc;
	args->argv = argv;
	
	// Allocate native functions
	native = calloc(1, sizeof(*native));
	if (native == NULL)
	{
		free(args);
		
		fprintf(stderr, "Could not allocate native handler!\n");
		return EXIT_FAILURE;
	}
	
	// Allocate boot settings
	boot = calloc(1, sizeof(*boot));
	if (boot == NULL)
	{
		free(args);
		free(native);
		
		fprintf(stderr, "Could not allocate boot parameters!\n");
		return EXIT_FAILURE;
	}
	
	// Set boot parameters
	boot->native = native;
	boot->args = args;
	
	// Create the machine
	machine = ratufacoat_createmachine(boot);
	if (machine == NULL)
	{
		free(args);
		free(boot);
		free(native);
		
		fprintf(stderr, "Could not create RatufaCoat machine!\n");
		return EXIT_FAILURE;
	}
	
	return EXIT_SUCCESS;
}
