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

/** Math type mask. */
#define RATUFACOAT_ENC_MATH_MASK UINT8_C(0x0F)

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

/** Add. */
#define RATUFACOAT_MATH_ADD UINT8_C(0)

/** Subtract. */
#define RATUFACOAT_MATH_SUB UINT8_C(1)

/** Multiply. */
#define RATUFACOAT_MATH_MUL UINT8_C(2)

/** Divide. */
#define RATUFACOAT_MATH_DIV UINT8_C(3)

/** Remainder. */
#define RATUFACOAT_MATH_REM UINT8_C(4)

/** Negate. */
#define RATUFACOAT_MATH_NEG UINT8_C(5)

/** Shift left. */
#define RATUFACOAT_MATH_SHL UINT8_C(6)

/** Shift right. */
#define RATUFACOAT_MATH_SHR UINT8_C(7)

/** Unsigned shift right. */
#define RATUFACOAT_MATH_USHR UINT8_C(8)

/** And. */
#define RATUFACOAT_MATH_AND UINT8_C(9)

/** Or. */
#define RATUFACOAT_MATH_OR UINT8_C(10)

/** Xor. */
#define RATUFACOAT_MATH_XOR UINT8_C(11)

/** Compare (Less). */
#define RATUFACOAT_MATH_CMPL UINT8_C(12)

/** Compare (Greater). */
#define RATUFACOAT_MATH_CMPG UINT8_C(13)

/** Sign 8-bit. */
#define RATUFACOAT_MATH_SIGNX8 UINT8_C(14)

/** Sign 16-bit. */
#define RATUFACOAT_MATH_SIGNX16 UINT8_C(15)

/**
 * Decodes a single unsigned byte value.
 *
 * @param pc The PC address.
 * @return The resulting value.
 * @since 2019/06/01
 */
static int32_t ratufacoat_decodeint(void** pc)
{
	int32_t rv;
	uint8_t* xpc;
	
	// Get current PC pointer
	xpc = *pc;
	
	// Read byte values
	rv = ((*xpc++) & 0xFF) << 24;
	rv |= ((*xpc++) & 0xFF) << 16;
	rv |= ((*xpc++) & 0xFF) << 8;
	rv |= (*xpc++) & 0xFF;
	
	// Set next
	*pc = xpc;
	
	// Return the decoded value
	return rv;
}

/**
 * Decodes a single unsigned byte value.
 *
 * @param pc The PC address.
 * @return The resulting value.
 * @since 2019/06/01
 */
static uint32_t ratufacoat_decodeubyte(void** pc)
{
	uint32_t rv;
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
	uint32_t rv;
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
static uint32_t ratufacoat_decodevuint(void** pc)
{
	uint32_t rv;
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
	uint8_t op, en;
	void* pc;
	void* nextpc;
	ratufacoat_register_t* r;
	ratufacoat_register_t* oldr;
	int32_t ia, ib, ic, id, i, j;
	ratufacoat_register_t ra, rb, rc, rd;
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
		en = (op >= RATUFACOAT_ENC_SPECIAL_A ? op : op & RATUFACOAT_ENC_MASK);
		
		// Seed next address, it is +1 for no-argument values
		nextpc = (void*)((uintptr_t)pc + 1);
		
		// Depends on the operation
		switch (en)
		{
				// Copy value in one register to another
			case RATUFACOAT_OP_COPY:
				{
					// Source and destination
					ua = ratufacoat_decodevuint(&nextpc);
					ub = ratufacoat_decodevuint(&nextpc);
					
					// Copy the values
					r[ub] = r[ua];
				}
				break;
				
				// Entry point into method
			case RATUFACOAT_OP_DEBUG_ENTRY:
				{
					// Load indexes into the pool
					ua = ratufacoat_decodevuint(&nextpc);
					ub = ratufacoat_decodevuint(&nextpc);
					uc = ratufacoat_decodevuint(&nextpc);
					
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
					cpu->state.debugline = ratufacoat_decodevuint(&nextpc);
					cpu->state.debugjop = ratufacoat_decodevuint(&nextpc);
					cpu->state.debugjpc = ratufacoat_decodevuint(&nextpc);
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
					ua = r[ratufacoat_decodevuint(&nextpc)];
					
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
					ua = ratufacoat_decodevuint(&nextpc);
					ub = ratufacoat_decodevuint(&nextpc);
					
					// Need the pool to load the values from
					pool = (uint32_t*)((uintptr_t)r[RATUFACOAT_POOL_REGISTER]);
					
					// Set destination register to the value in the pool
					r[ub] = pool[ua];
				}
				break;
				
				// Math operations
			case RATUFACOAT_ENC_MATH_REG_INT:
			case RATUFACOAT_ENC_MATH_CONST_INT:
				{
					// Read parameters
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = (en == RATUFACOAT_ENC_MATH_CONST_INT ?
						((int32_t)r[ratufacoat_decodeint(&nextpc)]) :
						((int32_t)r[ratufacoat_decodevuint(&nextpc)]));
					
					// Do the math
					switch (op & RATUFACOAT_ENC_MATH_MASK)
					{
						case RATUFACOAT_MATH_ADD:
							ic = ia + ib;
							break;
							
						case RATUFACOAT_MATH_SUB:
							ic = ia - ib;
							break;
							
						case RATUFACOAT_MATH_MUL:
							ic = ia * ib;
							break;
							
						case RATUFACOAT_MATH_DIV:
							ic = ia / ib;
							break;
							
						case RATUFACOAT_MATH_REM:
							ic = ia % ib;
							break;
							
						case RATUFACOAT_MATH_NEG:
							ic = -ia;
							break;
							
						case RATUFACOAT_MATH_SHL:
							ic = ia << (ib & 0x1F);
							break;
							
						case RATUFACOAT_MATH_SHR:
							ic = ia >> (ib & 0x1F);
							break;
							
						case RATUFACOAT_MATH_USHR:
							ic = (int32_t)(
								(uint32_t)ia >> ((uint32_t)ib & 0x1F));
							break;
							
						case RATUFACOAT_MATH_AND:
							ic = ia & ib;
							break;
							
						case RATUFACOAT_MATH_OR:
							ic = ia | ib;
							break;
							
						case RATUFACOAT_MATH_XOR:
							ic = ia ^ ib;
							break;
							
						case RATUFACOAT_MATH_CMPL:
						case RATUFACOAT_MATH_CMPG:
							ic = (ia < ib ? -1 : (ia > ib ? 1 : 0));
							break;
						
						case RATUFACOAT_MATH_SIGNX8:
							if ((ia & 0x80) != 0)
								ic = RATUFACOAT_REGISTER_C(0xFFFFFF00);
							else
								ic = ia;
							break;
							
						case RATUFACOAT_MATH_SIGNX16:
							if ((ia & 0x8000) != 0)
								ic = RATUFACOAT_REGISTER_C(0xFFFF0000);
							else
								ic = ia;
							break;
					}
					
					// Store value
					r[ratufacoat_decodevuint(&nextpc)] = ic;
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
