/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * This is the CPU execution code.
 *
 * @since 2019/05/31
 */

#include "ratufac.h"

/** Executes the specified CPU. */
void ratufacoat_cpuexec(ratufacoat_cpu_t* cpu)
{
	uint8_t op;
	void* pc;
	int32_t* r;
	
	// Do nothing if no CPU was specified
	if (cpu == NULL)
		return;
	
	// Get CPU registers
	r = cpu->r;
	
	// CPU runs within an infinite loop!
	for (;;)
	{
		// Get CPU PC address
		pc = (void*)((uintptr_t)cpu->pc);
		
		// Read the operation to be performed
		op = *((uint8_t*)pc);
		
		// Depends on the operation
		switch (op)
		{
				// Invalid operation
			default:
				ratufacoat_log("Invalid operation (%d/%02x)!",
					(int)op, (int)op);
				return;
		}
	}
}
