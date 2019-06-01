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

/** Math (Register): Add. */
#define RATUFACOAT_OP_MATH_REG_INT_ADD UINT8_C(0x00)

/** Math (Register): Subtract. */
#define RATUFACOAT_OP_MATH_REG_INT_SUB UINT8_C(0x01)

/** Math (Register): Multiply. */
#define RATUFACOAT_OP_MATH_REG_INT_MUL UINT8_C(0x02)

/** Math (Register): Divide. */
#define RATUFACOAT_OP_MATH_REG_INT_DIV UINT8_C(0x03)

/** Math (Register): Remainder. */
#define RATUFACOAT_OP_MATH_REG_INT_REM UINT8_C(0x04)

/** Math (Register): Negative. */
#define RATUFACOAT_OP_MATH_REG_INT_NEG UINT8_C(0x05)

/** Math (Register): Shift left. */
#define RATUFACOAT_OP_MATH_REG_INT_SHL UINT8_C(0x06)

/** Math (Register): Shift right. */
#define RATUFACOAT_OP_MATH_REG_INT_SHR UINT8_C(0x07)

/** Math (Register): Unsigned shift right. */
#define RATUFACOAT_OP_MATH_REG_INT_USHR UINT8_C(0x08)

/** Math (Register): And. */
#define RATUFACOAT_OP_MATH_REG_INT_AND UINT8_C(0x09)

/** Math (Register): Or. */
#define RATUFACOAT_OP_MATH_REG_INT_OR UINT8_C(0x0A)

/** Math (Register): XOr. */
#define RATUFACOAT_OP_MATH_REG_INT_XOR UINT8_C(0x0B)

/** Math (Register): Compare (Lower). */
#define RATUFACOAT_OP_MATH_REG_INT_CMPL UINT8_C(0x0C)

/** Math (Register): Compare (Higher). */
#define RATUFACOAT_OP_MATH_REG_INT_CMPG UINT8_C(0x0D)

/** Math (Register): Sign x8. */
#define RATUFACOAT_OP_MATH_REG_INT_SIGNX8 UINT8_C(0x0E)

/** Math (Register): Sign x16. */
#define RATUFACOAT_OP_MATH_REG_INT_SIGNX16 UINT8_C(0x0F)

/** Math (Constant): Add. */
#define RATUFACOAT_OP_MATH_CON_INT_ADD UINT8_C(0x80)

/** Math (Constant): Subtract. */
#define RATUFACOAT_OP_MATH_CON_INT_SUB UINT8_C(0x81)

/** Math (Constant): Multiply. */
#define RATUFACOAT_OP_MATH_CON_INT_MUL UINT8_C(0x82)

/** Math (Constant): Divide. */
#define RATUFACOAT_OP_MATH_CON_INT_DIV UINT8_C(0x83)

/** Math (Constant): Remainder. */
#define RATUFACOAT_OP_MATH_CON_INT_REM UINT8_C(0x84)

/** Math (Constant): Negative. */
#define RATUFACOAT_OP_MATH_CON_INT_NEG UINT8_C(0x85)

/** Math (Constant): Shift left. */
#define RATUFACOAT_OP_MATH_CON_INT_SHL UINT8_C(0x86)

/** Math (Constant): Shift right. */
#define RATUFACOAT_OP_MATH_CON_INT_SHR UINT8_C(0x87)

/** Math (Constant): Unsigned shift right. */
#define RATUFACOAT_OP_MATH_CON_INT_USHR UINT8_C(0x88)

/** Math (Constant): And. */
#define RATUFACOAT_OP_MATH_CON_INT_AND UINT8_C(0x89)

/** Math (Constant): Or. */
#define RATUFACOAT_OP_MATH_CON_INT_OR UINT8_C(0x8A)

/** Math (Constant): XOr. */
#define RATUFACOAT_OP_MATH_CON_INT_XOR UINT8_C(0x8B)

/** Math (Constant): Compare (Lower). */
#define RATUFACOAT_OP_MATH_CON_INT_CMPL UINT8_C(0x8C)

/** Math (Constant): Compare (Higher). */
#define RATUFACOAT_OP_MATH_CON_INT_CMPG UINT8_C(0x8D)

/** Math (Constant): Sign x8. */
#define RATUFACOAT_OP_MATH_CON_INT_SIGNX8 UINT8_C(0x8E)

/** Math (Constant): Sign x16. */
#define RATUFACOAT_OP_MATH_CON_INT_SIGNX16 UINT8_C(0x8F)

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
	uint8_t op;
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
		
		// Seed next address, it is +1 for no-argument values
		nextpc = (void*)((uintptr_t)pc + 1);
		
		// Depends on the operation
		switch (op)
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
				
				// Add
			case RATUFACOAT_OP_MATH_REG_INT_ADD:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia + ib);
				}
				break;
				
				// Subtract
			case RATUFACOAT_OP_MATH_REG_INT_SUB:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia - ib);
				}
				break;
				
				// Multiply
			case RATUFACOAT_OP_MATH_REG_INT_MUL:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia * ib);
				}
				break;
				
				// Divide
			case RATUFACOAT_OP_MATH_REG_INT_DIV:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia / ib);
				}
				break;
				
				// Remainder
			case RATUFACOAT_OP_MATH_REG_INT_REM:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia % ib);
				}
				break;
				
				// Negative
			case RATUFACOAT_OP_MATH_REG_INT_NEG:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (-ia);
				}
				break;
				
				// Shift left
			case RATUFACOAT_OP_MATH_REG_INT_SHL:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia << (ib & 0x1F));
				}
				break;
				
				// Shift right
			case RATUFACOAT_OP_MATH_REG_INT_SHR:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia >> (ib & 0x1F));
				}
				break;
				
				// Unsigned shift right
			case RATUFACOAT_OP_MATH_REG_INT_USHR:
				{
					ua = ((uint32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ub = ((uint32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ua >> (ub & 0x1F));
				}
				break;
				
			case RATUFACOAT_OP_MATH_REG_INT_AND:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia & ib);
				}
				break;
				
				// OR
			case RATUFACOAT_OP_MATH_REG_INT_OR:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia | ib);
				}
				break;
				
				// XOR
			case RATUFACOAT_OP_MATH_REG_INT_XOR:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia ^ ib);
				}
				break;
				
				// Compare
			case RATUFACOAT_OP_MATH_REG_INT_CMPL:
			case RATUFACOAT_OP_MATH_REG_INT_CMPG:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia < ib ? -1 :
						(ia > ib ? 1 : 0));
				}
				break;
				
				// Sign 8-bit
			case RATUFACOAT_OP_MATH_REG_INT_SIGNX8:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					if ((ia & 0x80) != 0)
						ia |= RATUFACOAT_REGISTER_C(0xFFFFFF00);
					r[ratufacoat_decodevuint(&nextpc)] = ia;
				}
				break;
				
				// Sign 16-bit
			case RATUFACOAT_OP_MATH_REG_INT_SIGNX16:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					if ((ia & 0x8000) != 0)
						ia |= RATUFACOAT_REGISTER_C(0xFFFF0000);
					r[ratufacoat_decodevuint(&nextpc)] = ia;
				}
				break;

				// Add
			case RATUFACOAT_OP_MATH_CON_INT_ADD:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia + ib);
				}
				break;
				
				// Subtract
			case RATUFACOAT_OP_MATH_CON_INT_SUB:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia - ib);
				}
				break;
				
				// Multiply
			case RATUFACOAT_OP_MATH_CON_INT_MUL:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia * ib);
				}
				break;
				
				// Divide
			case RATUFACOAT_OP_MATH_CON_INT_DIV:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia / ib);
				}
				break;
				
				// Remainder
			case RATUFACOAT_OP_MATH_CON_INT_REM:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia % ib);
				}
				break;
				
				// Negative
			case RATUFACOAT_OP_MATH_CON_INT_NEG:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (-ia);
				}
				break;
				
				// Shift left
			case RATUFACOAT_OP_MATH_CON_INT_SHL:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia << (ib & 0x1F));
				}
				break;
				
				// Shift Right
			case RATUFACOAT_OP_MATH_CON_INT_SHR:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia >> (ib & 0x1F));
				}
				break;
				
				// Unsigned shift right
			case RATUFACOAT_OP_MATH_CON_INT_USHR:
				{
					ua = ((uint32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ub = ((uint32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ua >> (ub & 0x1F));
				}
				break;
				
				// AND
			case RATUFACOAT_OP_MATH_CON_INT_AND:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia & ib);
				}
				break;
				
				// OR
			case RATUFACOAT_OP_MATH_CON_INT_OR:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia | ib);
				}
				break;
				
				// XOR
			case RATUFACOAT_OP_MATH_CON_INT_XOR:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia ^ ib);
				}
				break;
				
				// Compare
			case RATUFACOAT_OP_MATH_CON_INT_CMPL:
			case RATUFACOAT_OP_MATH_CON_INT_CMPG:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					r[ratufacoat_decodevuint(&nextpc)] = (ia < ib ? -1 :
						(ia > ib ? 1 : 0));
				}
				break;
				
				// Sign 8-bit
			case RATUFACOAT_OP_MATH_CON_INT_SIGNX8:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					if ((ia & 0x80) != 0)
						ia |= RATUFACOAT_REGISTER_C(0xFFFFFF00);
					r[ratufacoat_decodevuint(&nextpc)] = ia;
				}
				break;
				
				// Sign 16-bit
			case RATUFACOAT_OP_MATH_CON_INT_SIGNX16:
				{
					ia = ((int32_t)r[ratufacoat_decodevuint(&nextpc)]);
					ib = ((int32_t)r[ratufacoat_decodeint(&nextpc)]);
					if ((ia & 0x8000) != 0)
						ia |= RATUFACOAT_REGISTER_C(0xFFFF0000);
					r[ratufacoat_decodevuint(&nextpc)] = ia;
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
