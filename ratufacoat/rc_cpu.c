/* ---------------------------------------------------------------------------
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

/** Atomically decrements a memory address and gets the value. */
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

/**
 * Decodes a single unsigned byte value.
 *
 * @param pc The PC address.
 * @return The resulting value.
 * @since 2019/06/01
 */
static uint32_t ratufacoat_decodeubyte(void** pc)
{
	int32_t rv;
	uint8_t* xpc;
	
	// Get current PC pointer
	xpc = *pc;
	
	// Read single byte value
	rv = (*xpc++) & 0xFF;
	
	// Set next
	*pc = xpc;
	
	// Return the decoded value
	return rv;
}
/**
 * Decodes a single unsigned short value.
 *
 * @param pc The PC address.
 * @return The resulting value.
 * @since 2019/06/01
 */
static uint32_t ratufacoat_decodeushort(void** pc)
{
	int32_t rv;
	uint8_t* xpc;
	
	// Get current PC pointer
	xpc = *pc;
	
	// Read in both values
	rv = (*xpc++) & 0xFF;
	rv <<= 8;
	rv |= (*xpc++) & 0xFF;
	
	// Set next
	*pc = xpc;
	
	// Return the decoded value
	return rv;
}

/**
 * Decodes a variable unsigned integer.
 * 
 * @param pc The PC pointer.
 * @since 2019/05/31
 */
static uint32_t ratuacoat_decodevuint(void** pc)
{
	int32_t rv;
	uint8_t* xpc;
	
	// Get current PC pointer
	xpc = *pc;
	
	// Read single byte value, check if it is wide
	rv = (*xpc++) & 0xFF;
	if (rv & 0x80)
	{
		// Read remaining part
		rv = (rv & 0x7F) << 8;
		rv |= (*xpc++) & 0xFF;
	}
	
	// Set next
	*pc = xpc;
	
	// Return the decoded value
	return rv;
}

/** Executes the specified CPU. */
void ratufacoat_cpuexec(ratufacoat_cpu_t* cpu)
{
	uint8_t op;
	void* pc;
	void* nextpc;
	int32_t* r;
	int32_t* oldr;
	int32_t ia, ib, ic, id, i, j;
	uint32_t ua, ub, uc, ud;
	uint32_t* pool;
	ratufacoat_cpustatelink_t* link;
	
	// Do nothing if no CPU was specified
	if (cpu == NULL)
		return;
	
	// Get CPU registers
	r = cpu->state.r;
	
	// CPU runs within an infinite loop!
	for (pc = (void*)((uintptr_t)cpu->state.pc);; pc = nextpc)
	{	
		// Read the operation to be performed
		op = *((uint8_t*)pc);
		
		// Seed next address, it is +1 for no-argument values
		nextpc = (void*)((uintptr_t)pc + 1);
		
		// Depends on the operation
		switch (op)
		{
				// Entry point into method
			case RATUFACOAT_OP_DEBUG_ENTRY:
				{
					// Load indexes into the pool
					ua = ratuacoat_decodevuint(&nextpc);
					ub = ratuacoat_decodevuint(&nextpc);
					uc = ratuacoat_decodevuint(&nextpc);
					
					// Need the pool to load the values from
					pool = (uint32_t*)((uintptr_t)r[RATUFACOAT_POOL_REGISTER]);
					
					// Load pool values
					cpu->state.debuginclass =
						(char*)((uintptr_t)pool[ua] + 2);
					cpu->state.debuginname =
						(char*)((uintptr_t)pool[ub] + 2);
					cpu->state.debugintype =
						(char*)((uintptr_t)pool[uc] + 2);
				}
				break;
			
				// Single point in the method
			case RATUFACOAT_OP_DEBUG_POINT:
				{
					// Load location information
					cpu->state.debugline = ratuacoat_decodevuint(&nextpc);
					cpu->state.debugjop = ratuacoat_decodevuint(&nextpc);
					cpu->state.debugjpc = ratuacoat_decodevuint(&nextpc);
				}
				break;
				
				// Invoke pointer
			case RATUFACOAT_OP_INVOKE:
				{
					// Create new link, to load values into
					link = ratufacoat_memalloc(sizeof(*link));
					if (link == NULL)
					{
						ratufacoat_log("Could not allocate CPU link!");
						return;
					}
					
					// Store our old state in this link
					link->state = cpu->state;
					link->next = cpu->links;
					
					// Old registers needed for copy
					oldr = cpu->state.r;
					
					// Load new PC address
					ua = r[ratuacoat_decodevuint(&nextpc)];
					
					// Base output register
					j = RATUFACOAT_ARGUMENT_REGISTER_BASE;
					
					// Wide register list
					ub = ratufacoat_decodeubyte(&nextpc);
					if ((ub & 0x80) != 0)
					{
						// Read next byte to get the true count
						ub = ((ub & 0x7F) << 8);
						ub |= ratufacoat_decodeubyte(&nextpc);
						
						// Read register list
						for (i = 0; i < ub; i++, j++)
							r[j] = oldr[ratufacoat_decodeushort(&nextpc)];
					}
					
					// Narrow register list
					else
					{
						// Read register list
						for (i = 0; i < ub; i++, j++)
							r[j] = oldr[ratufacoat_decodeubyte(&nextpc)];
					}
					
					// Set remaining registers to zero
					for (; j < RATUFACOAT_MAX_REGISTERS; j++)
						r[j] = 0;
					
					// Copy next pool to pool
					r[RATUFACOAT_POOL_REGISTER] =
						oldr[RATUFACOAT_NEXT_POOL_REGISTER];
					
					// Store our old PC address and switch ours
					link->state.pc = nextpc;
					nextpc = (void*)((uintptr_t)ua);
					
					// Link in state
					link->next = cpu->links;
					cpu->links = link;
				}
				break;
				
				// Load from constant pool
			case RATUFACOAT_OP_LOAD_POOL:
				{
					// Read pool index and destination register
					ua = ratuacoat_decodevuint(&nextpc);
					ub = ratuacoat_decodevuint(&nextpc);
					
					// Need the pool to load the values from
					pool = (uint32_t*)((uintptr_t)r[RATUFACOAT_POOL_REGISTER]);
					
					// Set destination register to the value in the pool
					r[ub] = pool[ua];
				}
				break;
				
				// Invalid operation
			default:
				ratufacoat_log("Invalid operation (%d/0x%02X) @ %p!",
					(int)op, (int)op, pc);
				ratufacoat_log("In %s::%s:%s (L%d / J%d@%d)",
					cpu->state.debuginclass, cpu->state.debuginname,
					cpu->state.debugintype, (int)cpu->state.debugline,
					(int)cpu->state.debugjop, (int)cpu->state.debugjpc);
				return;
		}
	}
}
