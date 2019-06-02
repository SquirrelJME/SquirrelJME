/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * SquirrelJME RatufaCoat CPU support.
 *
 * @since 2019/06/02
 */

/** Header guard. */
#ifndef SJME_hGRATUFACOATRC_CPUHRC_CPUH
#define SJME_hGRATUFACOATRC_CPUHRC_CPUH

/** Include the main header. */
#include "ratufac.h"

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXRATUFACOATRC_CPUHRC_CPUH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

/** Maximum number of CPU registers. */
#define SJME_MAX_REGISTERS SJME_REGISTER_C(64)

/** The zero register. */
#define SJME_ZERO_REGISTER SJME_REGISTER_C(0)

/** The return value register (two slots, 1 + 2). */
#define SJME_RETURN_REGISTER SJME_REGISTER_C(1)

/** Second return register. */
#define SJME_RETURN_TWO_REGISTER SJME_REGISTER_C(2)

/** The exception register. */
#define SJME_EXCEPTION_REGISTER SJME_REGISTER_C(3)

/** The pointer containing static field data. */
#define SJME_STATIC_FIELD_REGISTER SJME_REGISTER_C(4)

/** Register which represents the current thread of execution. */
#define SJME_THREAD_REGISTER SJME_REGISTER_C(5)

/** Base for local registers (locals start here). */
#define SJME_LOCAL_REGISTER_BASE SJME_REGISTER_C(6)

/** The register containing the constant pool. */
#define SJME_POOL_REGISTER SJME_REGISTER_C(6)

/** The register which contains the next pool pointer to use. */
#define SJME_NEXT_POOL_REGISTER SJME_REGISTER_C(7)

/** The register of the first argument. */
#define SJME_ARGUMENT_REGISTER_BASE SJME_REGISTER_C(8)

/** Encoding mask. */
#define SJME_ENC_MASK UINT8_C(0xF0)

/** Math, R=RR, Integer. */
#define SJME_ENC_MATH_REG_INT UINT8_C(0x00)

/** Int comparison, then maybe jump. */
#define SJME_ENC_IF_ICMP UINT8_C(0x10)

/** Math type mask. */
#define SJME_ENC_MATH_MASK UINT8_C(0x0F)

/** Memory access, offset is in register. */
#define SJME_ENC_MEMORY_OFF_REG UINT8_C(0x20)

/** Memory access to big endian Java format, offset is in register. */
#define SJME_ENC_MEMORY_OFF_REG_JAVA UINT8_C(0x30)

/** Math, R=RC, Integer. */
#define SJME_ENC_MATH_CONST_INT UINT8_C(0x80)

/** Memory access, offset is a constant. */
#define SJME_ENC_MEMORY_OFF_ICONST UINT8_C(0xA0)

/** Memory access to big endian Java format, offset is a constant. */
#define SJME_ENC_MEMORY_OFF_ICONST_JAVA UINT8_C(0xB0)

/** Special. */
#define SJME_ENC_SPECIAL_A UINT8_C(0xE0)

/** Special.*/
#define SJME_ENC_SPECIAL_B UINT8_C(0xF0)

/** If equal to constant. */
#define SJME_OP_IFEQ_CONST UINT8_C(0xE6)

/** Debug entry to method. */
#define SJME_OP_DEBUG_ENTRY UINT8_C(0xE8)

/** Debug exit from method. */
#define SJME_OP_DEBUG_EXIT UINT8_C(0xE9)

/** Debug single point in method. */
#define SJME_OP_DEBUG_POINT UINT8_C(0xEA)

/** Return. */
#define SJME_OP_RETURN UINT8_C(0xF3)

/** Invoke. */
#define SJME_OP_INVOKE UINT8_C(0xF7)

/** Copy value in register. */
#define SJME_OP_COPY UINT8_C(0xF8)

/** Atomically decrements a memory address and gets the value. */
#define SJME_OP_ATOMIC_INT_DECREMENT_AND_GET UINT8_C(0xF9)

/** Atomically increments a memory address. */
#define SJME_OP_ATOMIC_INT_INCREMENT UINT8_C(0xFA)

/** System call. */
#define SJME_OP_SYSTEM_CALL UINT8_C(0xFB)

/** Load from pool, note that at code gen time this is aliased. */
#define SJME_OP_LOAD_POOL UINT8_C(0xFD)

/** Load from integer array. */
#define SJME_OP_LOAD_FROM_INTARRAY UINT8_C(0xFE)

/** Compare and exchange. */
#define SJME_OP_BREAKPOINT UINT8_C(0xFF)

/** Add. */
#define SJME_MATH_ADD UINT8_C(0)

/** Subtract. */
#define SJME_MATH_SUB UINT8_C(1)

/** Multiply. */
#define SJME_MATH_MUL UINT8_C(2)

/** Divide. */
#define SJME_MATH_DIV UINT8_C(3)

/** Remainder. */
#define SJME_MATH_REM UINT8_C(4)

/** Negate. */
#define SJME_MATH_NEG UINT8_C(5)

/** Shift left. */
#define SJME_MATH_SHL UINT8_C(6)

/** Shift right. */
#define SJME_MATH_SHR UINT8_C(7)

/** Unsigned shift right. */
#define SJME_MATH_USHR UINT8_C(8)

/** And. */
#define SJME_MATH_AND UINT8_C(9)

/** Or. */
#define SJME_MATH_OR UINT8_C(10)

/** Xor. */
#define SJME_MATH_XOR UINT8_C(11)

/** Compare (Less). */
#define SJME_MATH_CMPL UINT8_C(12)

/** Compare (Greater). */
#define SJME_MATH_CMPG UINT8_C(13)

/** Sign 8-bit. */
#define SJME_MATH_SIGNX8 UINT8_C(14)

/** Sign 16-bit. */
#define SJME_MATH_SIGNX16 UINT8_C(15)

/** Mask for read/write in memory. */
#define SJME_MEM_LOAD_MASK UINT8_C(0x08)

/** Mask for data types in memory. */
#define SJME_MEM_DATATYPE_MASK UINT8_C(0x07)

/** Object. */
#define SJME_DATATYPE_OBJECT UINT8_C(0)

/** Byte. */
#define SJME_DATATYPE_BYTE UINT8_C(1)

/** Short. */
#define SJME_DATATYPE_SHORT UINT8_C(2)

/** Character. */
#define SJME_DATATYPE_CHARACTER UINT8_C(3)

/** Integer. */
#define SJME_DATATYPE_INTEGER UINT8_C(4)

/** Float. */
#define SJME_DATATYPE_FLOAT UINT8_C(5)

/** Long. */
#define SJME_DATATYPE_LONG UINT8_C(6)

/** Double. */
#define SJME_DATATYPE_DOUBLE UINT8_C(7)

/**
 * This contains the CPU state.
 * 
 * @since 2019/06/01
 */
typedef struct sjme_cpustate
{
	/** CPU registers. */
	sjme_jregister r[RATUFACOAT_MAX_REGISTERS];
	
	/** PC address. */
	sjme_jaddress* pc;
	
	/** Debug in class. */
	char* debuginclass;
	
	/** Debug in method name. */
	char* debuginname;
	
	/** Debug in method type. */
	char* debugintype;
	
	/** Debug line. */
	int debugline;
	
	/** Debug Java operation. */
	int debugjop;
	
	/** Debug Java address. */
	int debugjpc;
} sjme_cpustate;

/**
 * Old CPU state chains.
 *
 * @since 2019/06/01
 */
typedef struct sjme_cpustatelink
{
	/** The CPU state. */
	sjme_cpustate state;
	
	/** The next in the chain. */
	struct sjme_cpustatelink* next;
} sjme_cpustatelink;

/**
 * This represents the state of a RatufaCoat CPU.
 * 
 * @since 2019/05/31
 */
typedef struct sjme_cpu
{
	/** The host machine. */
	sjme_machine* machine;
	
	/** Current CPU state. */
	sjme_cpustate state;
	
	/** Old invocation states. */
	sjme_cpustatelink* links;
} sjme_cpu;

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXRATUFACOATRC_CPUHRC_CPUH
}
#undef SJME_cXRATUFACOATRC_CPUHRC_CPUH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXRATUFACOATRC_CPUHRC_CPUH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGRATUFACOATRC_CPUHRC_CPUH */

