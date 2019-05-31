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
	void* ram;
	void* rom;
	uint32_t ramsize, romsize;
	
	// This needs to be passed, if not then it shall fail
	if (boot == NULL || boot->native == NULL)
		return NULL;
	
	// Entry point banner
	ratufacoat_log("SquirrelJME RatufaCoat");
	ratufacoat_log("Copyright (C) Stephanie Gawroriski");
	ratufacoat_log("https://squirreljme.cc/");
	
	// Try to allocate the machine data
	rv = ratufacoat_memalloc(sizeof(*rv));
	if (rv == NULL)
		return NULL;
	
	// Copy some basic boot parameters
	rv->native = boot->native;
	rv->args = boot->args;
	
	// Allocate RAM (default to a given RAM size)
	ramsize = boot->ramsize;
	if (ramsize == 0)
		ramsize = RATUFACOAT_DEFAULT_MEMORY_SIZE;
	ram = ratufacoat_memalloc(ramsize);
	if (ram == NULL)
	{
		ratufacoat_memfree(rv);
		return NULL;
	}
	
	// Note
	ratufacoat_log("RAM @ %p (%d bytes)", ram, (int)ramsize);
	
	// Set the machine RAM space
	rv->ram = ram;
	rv->ramsize = ramsize;
	
	// Set and note ROM address
	rom = boot->rom;
	romsize = boot->romsize;
	rv->rom = rom;
	rv->romsize = romsize;
	
	// Note it
	ratufacoat_log("ROM @ %p (%d bytes)", rom, (int)romsize);
	
	ratufacoat_todo();
	
	return rv;
}

/** Logs a message. */
void ratufacoat_log(char* fmt, ...)
{
	va_list args;
	int len;
	
	// Load arguments
	va_start(args, fmt);
	
	// Forward
	vfprintf(stderr, fmt, args);
	
	// End
	va_end(args);
	
	// Always include the ending newline if it was missing
	len = strlen(fmt);
	if (len > 0 && fmt[len - 1] != '\n')
		fprintf(stderr, "\n");
}

/** Fails with a ToDo message. */
void ratufacoat_todo(void)
{
	ratufacoat_log("TODO hit!");
	raise(SIGABRT);
	exit(7);
}

