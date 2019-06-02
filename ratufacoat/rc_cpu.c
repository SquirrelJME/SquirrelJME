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
#include "rc_cpu.h"

/**
 * Decodes a single unsigned byte value.
 *
 * @param pc The PC address.
 * @return The resulting value.
 * @since 2019/06/01
 */
static int32_t sjme_decodeint(void** pc)
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
static uint32_t sjme_decodeubyte(void** pc)
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
static uint32_t sjme_decodeushort(void** pc)
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
static uint32_t sjme_decodevuint(void** pc)
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
void sjme_cpuexec(sjme_cpu_t* cpu)
{
	uint8_t op, en;
	void* pc;
	void* nextpc;
	void* ma;
	sjme_register_t* r;
	sjme_register_t* oldr;
	int32_t ia, ib, ic, id, i, j;
	sjme_register_t ra, rb, rc, rd;
	uint32_t ua, ub, uc, ud;
	uint32_t* pool;
	sjme_cpustatelink_t* link;
	
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
		en = (op >= SJME_ENC_SPECIAL_A ? op : op & SJME_ENC_MASK);
		
		// Seed next address, it is +1 for no-argument values
		nextpc = (void*)((uintptr_t)pc + 1);
		
		// Register zero is always zero!
		r[0] = 0;
		
		// Depends on the operation
		switch (en)
		{
				// Copy value in one register to another
			case SJME_OP_COPY:
				{
					// Source and destination
					ua = sjme_decodevuint(&nextpc);
					ub = sjme_decodevuint(&nextpc);
					
					// Copy the values
					r[ub] = r[ua];
				}
				break;
				
				// Entry point into method
			case SJME_OP_DEBUG_ENTRY:
				{
					// Load indexes into the pool
					ua = sjme_decodevuint(&nextpc);
					ub = sjme_decodevuint(&nextpc);
					uc = sjme_decodevuint(&nextpc);
					
					// Need the pool to load the values from
					pool = (uint32_t*)((uintptr_t)r[SJME_POOL_REGISTER]);
					
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
			case SJME_OP_DEBUG_POINT:
				{
					// Load location information
					cpu->state.debugline = sjme_decodevuint(&nextpc);
					cpu->state.debugjop = sjme_decodevuint(&nextpc);
					cpu->state.debugjpc = sjme_decodevuint(&nextpc);
				}
				break;
				
				// Invoke pointer
			case SJME_OP_INVOKE:
				{
					// Create new link, to load values into
					link = sjme_memalloc(sizeof(*link));
					if (link == NULL)
					{
						sjme_log("Could not allocate CPU link!");
						return;
					}
					
					// Store our old state in this link
					link->state = cpu->state;
					link->next = cpu->links;
					
					// Old registers needed for copy
					oldr = &cpu->state.r;
					
					// Load new PC address
					ua = r[sjme_decodevuint(&nextpc)];
					
					// Base output register
					j = SJME_ARGUMENT_REGISTER_BASE;
					
					// Wide register list
					ub = sjme_decodeubyte(&nextpc);
					if ((ub & 0x80) != 0)
					{
						// Read next byte to get the true count
						ub = ((ub & 0x7F) << 8);
						ub |= sjme_decodeubyte(&nextpc);
						
						// Read register list
						for (i = 0; i < ub; i++, j++)
							r[j] = oldr[sjme_decodeushort(&nextpc)];
					}
					
					// Narrow register list
					else
					{
						// Read register list
						for (i = 0; i < ub; i++, j++)
							r[j] = oldr[sjme_decodeubyte(&nextpc)];
					}
					
					// Set remaining registers to zero
					for (; j < SJME_MAX_REGISTERS; j++)
						r[j] = 0;
					
					// Copy next pool to pool
					r[SJME_POOL_REGISTER] =
						oldr[SJME_NEXT_POOL_REGISTER];
					
					// Store our old PC address and switch ours
					link->state.pc = nextpc;
					nextpc = (void*)((uintptr_t)ua);
					
					// Link in state
					link->next = cpu->links;
					cpu->links = link;
				}
				break;
				
				// Load from constant pool
			case SJME_OP_LOAD_POOL:
				{
					// Read pool index and destination register
					ua = sjme_decodevuint(&nextpc);
					ub = sjme_decodevuint(&nextpc);
					
					// Need the pool to load the values from
					pool = (uint32_t*)((uintptr_t)r[SJME_POOL_REGISTER]);
					
					// Set destination register to the value in the pool
					r[ub] = pool[ua];
				}
				break;
				
				// Math operations
			case SJME_ENC_MATH_REG_INT:
			case SJME_ENC_MATH_CONST_INT:
				{
					// Read parameters
					ia = ((int32_t)r[sjme_decodevuint(&nextpc)]);
					ib = (en == SJME_ENC_MATH_CONST_INT ?
						((int32_t)r[sjme_decodeint(&nextpc)]) :
						((int32_t)r[sjme_decodevuint(&nextpc)]));
					
					// Do the math
					switch (op & SJME_ENC_MATH_MASK)
					{
						case SJME_MATH_ADD:
							ic = ia + ib;
							break;
							
						case SJME_MATH_SUB:
							ic = ia - ib;
							break;
							
						case SJME_MATH_MUL:
							ic = ia * ib;
							break;
							
						case SJME_MATH_DIV:
							ic = ia / ib;
							break;
							
						case SJME_MATH_REM:
							ic = ia % ib;
							break;
							
						case SJME_MATH_NEG:
							ic = -ia;
							break;
							
						case SJME_MATH_SHL:
							ic = ia << (ib & 0x1F);
							break;
							
						case SJME_MATH_SHR:
							ic = ia >> (ib & 0x1F);
							break;
							
						case SJME_MATH_USHR:
							ic = (int32_t)(
								(uint32_t)ia >> ((uint32_t)ib & 0x1F));
							break;
							
						case SJME_MATH_AND:
							ic = ia & ib;
							break;
							
						case SJME_MATH_OR:
							ic = ia | ib;
							break;
							
						case SJME_MATH_XOR:
							ic = ia ^ ib;
							break;
							
						case SJME_MATH_CMPL:
						case SJME_MATH_CMPG:
							ic = (ia < ib ? -1 : (ia > ib ? 1 : 0));
							break;
						
						case SJME_MATH_SIGNX8:
							if ((ia & 0x80) != 0)
								ic = SJME_REGISTER_C(0xFFFFFF00);
							else
								ic = ia;
							break;
							
						case SJME_MATH_SIGNX16:
							if ((ia & 0x8000) != 0)
								ic = SJME_REGISTER_C(0xFFFF0000);
							else
								ic = ia;
							break;
					}
					
					// Store value
					r[sjme_decodevuint(&nextpc)] = ic;
				}
				break;
				
				// Memory Access
			case SJME_ENC_MEMORY_OFF_REG:
			case SJME_ENC_MEMORY_OFF_REG_JAVA:
			case SJME_ENC_MEMORY_OFF_ICONST:
			case SJME_ENC_MEMORY_OFF_ICONST_JAVA:
				{
					// Source/Destination register
					rc = sjme_decodevuint(&nextpc);
					
					// Base address and offset
					ia = r[sjme_decodevuint(&nextpc)];
					ib = (en >= 0x80 ? sjme_decodeint(&nextpc) :
						r[sjme_decodevuint(&nextpc)]);
					
					// Is this a Java type? The lowest bit in the
					// encoding is used to indicate this
					j = (op & 0x10);
					
					// Calculate actual address
					ma = (void*)((uintptr_t)((uint32_t)(ia + ib)));
					
					// Load
					if ((op & SJME_MEM_LOAD_MASK) != 0)
					{
						// Depends on the type
						switch (op & SJME_MEM_DATATYPE_MASK)
						{
							case SJME_DATATYPE_OBJECT:
							case SJME_DATATYPE_INTEGER:
							case SJME_DATATYPE_FLOAT:
								if (j)
									r[rc] = sjme_memreadjint(ma, 0);
								else
									r[rc] = *((int32_t*)ma);
								break;
								
							case SJME_DATATYPE_BYTE:
								r[rc] = *((int8_t*)ma);
								break;
								
							case SJME_DATATYPE_SHORT:
								if (j)
									r[rc] = sjme_memreadjshort(ma, 0);
								else
									r[rc] = *((int16_t*)ma);
								break;
								
							case SJME_DATATYPE_CHARACTER:
								if (j)
									r[rc] = (uint16_t)sjme_memreadjshort(
										ma, 0);
								else
									r[rc] = *((uint16_t*)ma);
								break;
						}
					}
					
					// Store
					else
					{
						// Depends on the type
						switch (op & SJME_MEM_DATATYPE_MASK)
						{
							case SJME_DATATYPE_OBJECT:
							case SJME_DATATYPE_INTEGER:
							case SJME_DATATYPE_FLOAT:
								*((int32_t*)ma) = r[rc];
								break;
								
							case SJME_DATATYPE_BYTE:
								*((int8_t*)ma) = r[rc];
								break;
								
							case SJME_DATATYPE_SHORT:
							case SJME_DATATYPE_CHARACTER:
								*((int16_t*)ma) = r[rc];
								break;
						}
					}
				}
				break;
				
				// Invalid operation
			default:
				sjme_log("Invalid operation (%d/0x%02X) @ %p!",
					(int)op, (int)op, pc);
				sjme_log("In %s::%s:%s (L%d / J%d@%d)",
					cpu->state.debuginclass, cpu->state.debuginname,
					cpu->state.debugintype, (int)cpu->state.debugline,
					(int)cpu->state.debugjop, (int)cpu->state.debugjpc);
				return;
		}
	}
}
