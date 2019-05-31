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
	ratufacoat_cpu_t* cpu;
	FILE* romfile;
	void* romdata;
	size_t romsize,
		readsize,
		count;
	
	// Open ROM file
	romfile = fopen("squirreljme.sqc", "rb");
	if (romfile == NULL)
	{
		ratufacoat_log("Could not open the ROM file.");
		return EXIT_FAILURE;
	}
	
	// Read size of file
	fseek(romfile, 0, SEEK_END);
	romsize = ftell(romfile);
	fseek(romfile, 0, SEEK_SET);
	
	// Allocate ROM data
	romdata = ratufacoat_memalloc(romsize);
	if (romdata == NULL)
	{
		fclose(romfile);
		
		ratufacoat_log("Could not allocate the ROM file!");
		return EXIT_FAILURE;
	}
	
	// Read the entire ROM
	for (readsize = 0; readsize < romsize;)
	{
		// Read the data
		count = fread((void*)((uintptr_t)romdata + readsize),
			1, romsize - readsize, romfile);
		
		// EOF?
		if (count <= 0)
		{
			// Error?
			if (ferror(romfile))
			{
				fclose(romfile);
				ratufacoat_memfree(romdata);
				
				ratufacoat_log("Could not read the ROM! (%s)",
					strerror(errno));
				return EXIT_FAILURE;
			}
			
			// Okay!
			break;
		}
		
		// Read size goes up!
		readsize += count;
	}
	
	// Close ROM
	fclose(romfile);
	
	// Allocate all the structures the JVM needs
	args = ratufacoat_memalloc(sizeof(*args));
	native = ratufacoat_memalloc(sizeof(*native));
	boot = ratufacoat_memalloc(sizeof(*boot));
	if (args == NULL || native == NULL || boot == NULL)
	{
		ratufacoat_memfree(args);
		ratufacoat_memfree(boot);
		ratufacoat_memfree(native);
		ratufacoat_memfree(romdata);
		
		ratufacoat_log("Could not allocate structure memory!");
		return EXIT_FAILURE;
	}
	
	// Set arguments
	args->argc = argc;
	args->argv = argv;
	
	// Set boot parameters
	boot->native = native;
	boot->args = args;
	boot->rom = romdata;
	boot->romsize = romsize;
	
	// Create the machine
	machine = ratufacoat_createmachine(boot, &cpu);
	if (machine == NULL)
	{
		ratufacoat_memfree(args);
		ratufacoat_memfree(boot);
		ratufacoat_memfree(native);
		ratufacoat_memfree(romdata);
		
		ratufacoat_log("Could not create RatufaCoat machine!");
		return EXIT_FAILURE;
	}
	
	return EXIT_SUCCESS;
}
