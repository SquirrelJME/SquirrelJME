/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * RatufaCoat main library header.
 *
 * @since 2019/01/14
 */

/** Header guard. */
#ifndef SJME_hGRATUFACOATRATACHRATUFACH
#define SJME_hGRATUFACOATRATACHRATUFACH

/** Common includes. */
#include <stdarg.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <string.h>
#include <signal.h>
#include <errno.h>

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXRATUFACOATRATACHRATUFACH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

#if defined(__linux__) || defined(__gnu_linux__)
	#define RATUFACOAT_ISLINUX 1
#endif

/** Default amount of memory to use. */
#if !defined(RATUFACOAT_DEFAULT_MEMORY_SIZE)
	#define RATUFACOAT_DEFAULT_MEMORY_SIZE 16777216
#endif

/** Maximum number of CPU registers. */
#define RATUFACOAT_MAX_REGISTERS 64

/** The zero register. */
#define RATUFACOAT_ZERO_REGISTER 0

/** The return value register (two slots, 1 + 2). */
#define RATUFACOAT_RETURN_REGISTER 1

/** Second return register. */
#define RATUFACOAT_RETURN_TWO_REGISTER 2

/** The exception register. */
#define RATUFACOAT_EXCEPTION_REGISTER 3

/** The pointer containing static field data. */
#define RATUFACOAT_STATIC_FIELD_REGISTER 4

/** Register which represents the current thread of execution. */
#define RATUFACOAT_THREAD_REGISTER 5

/** Base for local registers (locals start here). */
#define RATUFACOAT_LOCAL_REGISTER_BASE 6

/** The register containing the constant pool. */
#define RATUFACOAT_POOL_REGISTER 6

/** The register which contains the next pool pointer to use. */
#define RATUFACOAT_NEXT_POOL_REGISTER 7

/** The register of the first argument. */
#define RATUFACOAT_ARGUMENT_REGISTER_BASE 8

/**
 * Native functions support for RatufaCoat.
 * 
 * @since 2019/05/28
 */
typedef struct ratufacoat_native_t
{
} ratufacoat_native_t;

/**
 * Argument handling.
 *
 * @since 2019/05/31
 */
typedef struct ratufacoat_args_t
{
	/** Argument count. */
	int argc;
	
	/** Arguments. */
	char** argv;
} ratufacoat_args_t;

/**
 * Boot options for RatufaCoat.
 *
 * @since 2019/05/31
 */
typedef struct ratufacoat_boot_t
{
	/** Arguments to use. */
	ratufacoat_args_t* args;
	
	/** Native functions. */
	ratufacoat_native_t* native;
	
	/** ROM data. */
	void* rom;
	
	/** The size of the ROM. */
	size_t romsize;
	
	/** The size of RAM. */
	uint32_t ramsize;
} ratufacoat_boot_t;

/**
 * RatufaCoat active machine definition.
 * 
 * @since 2019/05/28
 */
typedef struct ratufacoat_machine_t
{
	/** Native function handlers. */
	ratufacoat_native_t* native;
	
	/** Arguments. */
	ratufacoat_args_t* args;
	
	/** The machine's RAM. */
	void* ram;
	
	/** The size of RAM. */
	uint32_t ramsize;
	
	/** The JVM's ROM. */
	void* rom;
	
	/** The size of ROM. */
	uint32_t romsize;
} ratufacoat_machine_t;

/**
 * This represents the state of a RatufaCoat CPU.
 * 
 * @since 2019/05/31
 */
typedef struct ratufacoat_cpu_t
{
	/** The host machine. */
	ratufacoat_machine_t* machine;
	
	/** CPU registers. */
	int32_t r[RATUFACOAT_MAX_REGISTERS];
	
	/** PC address. */
	uint32_t pc;
	
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
} ratufacoat_cpu_t;

/**
 * Executes the specified CPU.
 * 
 * @param cpu The CPU to execute.
 * @since 2019/05/31
 */
void ratufacoat_cpuexec(ratufacoat_cpu_t* cpu);

/**
 * Creates a RatufaCoat machine.
 * 
 * @param boot Boot options.
 * @param xcpu Initial CPU output.
 * @return The resulting virtual machine, will be {@code NULL} if it could
 * not be created.
 * @since 2019/05/28
 */
ratufacoat_machine_t* ratufacoat_createmachine(ratufacoat_boot_t* boot,
	ratufacoat_cpu_t** xcpu);

/**
 * Logs a message.
 * 
 * @param fmt The format.
 * @param ... The arguments.
 * @since 2019/05/31
 */
void ratufacoat_log(char* fmt, ...);

/**
 * Allocates a pointer in the low 4GiB of memory for 32-bit pointer usage.
 * 
 * @param len The number of bytes to allocate.
 * @return The allocated memory.
 * @since 2019/05/31
 */
void* ratufacoat_memalloc(size_t len);

/**
 * Frees a pointer which was previously allocated with ratufacoat_memalloc.
 * 
 * @param p The pointer to free.
 * @since 2019/05/31
 */
void ratufacoat_memfree(void* p);

/**
 * Reads a Java byte from memory.
 * 
 * @param p The address to read from.
 * @param o The offset.
 * @return The value at the address.
 * @since 2019/05/31
 */
int8_t ratufacoat_memreadjbyte(void* p, int32_t o);

/**
 * Reads a Java int from memory.
 * 
 * @param p The address to read from.
 * @param o The offset.
 * @return The value at the address.
 * @since 2019/05/31
 */
int32_t ratufacoat_memreadjint(void* p, int32_t o);

/**
 * Reads a Java short from memory.
 * 
 * @param p The address to read from.
 * @param o The offset.
 * @return The value at the address.
 * @since 2019/05/31
 */
int16_t ratufacoat_memreadjshort(void* p, int32_t o);

/**
 * Fails the VM with a fatal ToDo.
 *
 * @since 2019/05/28
 */
void ratufacoat_todo(void);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXRATUFACOATRATACHRATUFACH
}
#undef SJME_cXRATUFACOATRATACHRATUFACH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXRATUFACOATRATACHRATUFACH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGRATUFACOATRATACHRATUFACH */

