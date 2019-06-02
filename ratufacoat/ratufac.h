/* ---------------------------------------------------------------------------
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

/** Is this a 64-bit system? */
#if defined(_LP64) || defined(__LP64__) || defined(__x86_64__) || \
	defined(_M_X64) || defined(_M_AMD64) || defined(__aarch64__) || \
	defined(__ia64__) || defined(__ia64) || defined(_M_IA64) || \
	defined(__itanium__) || defined(__powerpc64__) || defined(__ppc64__) || \
	defined(_ARCH_PPC64) || defined(_ARCH_PPC64)
	#define SQUIRRELJME_BITS 64
#else
	#define SQUIRRELJME_BITS 32
#endif

/** Boolean. */
typedef uint8_t sjme_jboolean;

/** Byte. */
typedef int8_t sjme_jbyte;

/** Short. */
typedef int16_t sjme_jshort;

/** Char. */
typedef uint16_t sjme_jchar;

/** Integer. */
typedef int32_t sjme_jint;

/** Register. */
typedef sjme_jint sjme_jregister;

/** UTF-8 Character. */
typedef sjme_jbyte sjme_jutfchar;

/** Memory that is addressable by SquirrelJME. */
typedef void* sjme_jaddress;

/** Macro for byte. */
#define SJME_JBYTE_C(x) INT8_C(x)

/** Macro for short. */
#define SJME_JSHORT_C(x) INT16_C(x)

/** Macro for char. */
#define SJME_JCHAR_C(x) UINT16_C(x)

/** Macro for int. */
#define SJME_JINT_C(x) INT32_C(x)

/** Macro for registers. */
#define SJME_JREGISTER_C(x) SJME_JINT_C(x)

/** Macro for UTF-8 characters. */
#define SJME_JUTFCHAR_C(x) SJME_JBYTE_C(x)

#if defined(__linux__) || defined(__gnu_linux__)
	#define RATUFACOAT_ISLINUX 1
#endif

/** Default amount of memory to use. */
#if !defined(RATUFACOAT_DEFAULT_MEMORY_SIZE)
	#define RATUFACOAT_DEFAULT_MEMORY_SIZE 16777216
#endif

/**
 * Native functions support for RatufaCoat.
 * 
 * @since 2019/05/28
 */
typedef struct sjme_native_t
{
} sjme_native_t;

/**
 * Argument handling.
 *
 * @since 2019/05/31
 */
typedef struct sjme_args_t
{
	/** Argument count. */
	sjme_jint argc;
	
	/** Arguments. */
	sjme_jutfchar** argv;
} sjme_args_t;

/**
 * Boot options for RatufaCoat.
 *
 * @since 2019/05/31
 */
typedef struct sjme_boot_t
{
	/** Arguments to use. */
	sjme_args* args;
	
	/** Native functions. */
	sjme_native* native;
	
	/** ROM data. */
	sjme_jaddress rom;
	
	/** The size of the ROM. */
	sjme_jint romsize;
	
	/** The size of RAM. */
	sjme_jint ramsize;
} sjme_boot;

/**
 * RatufaCoat active machine definition.
 * 
 * @since 2019/05/28
 */
typedef struct sjme_machine
{
	/** Native function handlers. */
	sjme_native* native;
	
	/** Arguments. */
	sjme_args* args;
	
	/** The machine's RAM. */
	sjme_jaddress ram;
	
	/** The size of RAM. */
	sjme_jint ramsize;
	
	/** The JVM's ROM. */
	sjme_jaddress rom;
	
	/** The size of ROM. */
	sjme_jint romsize;
} sjme_machine;

/**
 * Executes the specified CPU.
 * 
 * @param cpu The CPU to execute.
 * @since 2019/05/31
 */
void sjme_cpuexec(sjme_cpu* cpu);

/**
 * Creates a RatufaCoat machine.
 * 
 * @param boot Boot options.
 * @param xcpu Initial CPU output.
 * @return The resulting virtual machine, will be {@code NULL} if it could
 * not be created.
 * @since 2019/05/28
 */
sjme_machine* sjme_createmachine(sjme_boot* boot,
	sjme_cpu** xcpu);

/**
 * Logs a message.
 * 
 * @param fmt The format.
 * @param ... The arguments.
 * @since 2019/05/31
 */
void sjme_log(char* fmt, ...);

/**
 * Fails the VM with a fatal ToDo.
 *
 * @since 2019/05/28
 */
void sjme_todo(void);

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

