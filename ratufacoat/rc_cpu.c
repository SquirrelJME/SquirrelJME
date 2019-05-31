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

/** Encoding mask. */
#define RATUFACOAT_ENC_MASK UINT8_C(0xF0)

/** Math, R=RR, Integer. */
#define RATUFACOAT_ENC_MATH_REG_INT UINT8_C(0x00)

/** Int comparison, then maybe jump. */
#define RATUFACOAT_ENC_IF_ICMP UINT8_C(0x10)

/** Memory access, offset is in register. */
#define RATUFACOAT_ENC_MEMORY_OFF_REG UINT8_C(0x20)

/** Memory access to big endian Java format, offset is in register. */
#define RATUFACOAT_ENC_MEMORY_OFF_REG_JAVA UINT8_C(0x30)

/** Math, R=RC, Integer. */
#define RATUFACOAT_ENC_MATH_CONST_INT UINT8_C(0x80)

/** Memory access, offset is a constant. */
#define RATUFACOAT_ENC_MEMORY_OFF_ICONST UINT8_C(0xA0)

/** Memory access to big endian Java format, offset is a constant. */
#define RATUFACOAT_ENC_MEMORY_OFF_ICONST_JAVA UINT8_C(0xB0)

/** Special. */
#define RATUFACOAT_ENC_SPECIAL_A UINT8_C(0xE0)

/** Special.*/
#define RATUFACOAT_ENC_SPECIAL_B UINT8_C(0xF0)

/** If equal to constant. */
#define RATUFACOAT_OP_IFEQ_CONST UINT8_C(0xE6)

/** Debug entry to method. */
#define RATUFACOAT_OP_DEBUG_ENTRY UINT8_C(0xE8)

/** Debug exit from method. */
#define RATUFACOAT_OP_DEBUG_EXIT UINT8_C(0xE9)

/** Debug single point in method. */
#define RATUFACOAT_OP_DEBUG_POINT UINT8_C(0xEA)

/** Return. */
#define RATUFACOAT_OP_RETURN UINT8_C(0xF3)

/** Invoke. */
#define RATUFACOAT_OP_INVOKE UINT8_C(0xF7)

/** Copy value in register. */
#define RATUFACOAT_OP_COPY UINT8_C(0xF8)

/** Atomically decrements a memory addres and gets the value. */
#define RATUFACOAT_OP_ATOMIC_INT_DECREMENT_AND_GET UINT8_C(0xF9)

/** Atomically increments a memory address. */
#define RATUFACOAT_OP_ATOMIC_INT_INCREMENT UINT8_C(0xFA)

/** System call. */
#define RATUFACOAT_OP_SYSTEM_CALL UINT8_C(0xFB)

/** Load from pool, note that at code gen time this is aliased. */
#define RATUFACOAT_OP_LOAD_POOL UINT8_C(0xFD)

/** Load from integer array. */
#define RATUFACOAT_OP_LOAD_FROM_INTARRAY UINT8_C(0xFE)

/** Compare and exchange. */
#define RATUFACOAT_OP_BREAKPOINT UINT8_C(0xFF)

/** Executes the specified CPU. */
void ratufacoat_cpuexec(ratufacoat_cpu_t* cpu)
{
	uint8_t op, xop;
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
		xop = op;
		
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
