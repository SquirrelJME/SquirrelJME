/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * SquirrelJME RatufaCoat Header.
 *
 * @since 2019/06/02
 */

/** Header guard. */
#ifndef SJME_hGRATUFACOATSJMERCHSJMERCH
#define SJME_hGRATUFACOATSJMERCHSJMERCH

/** Is this a 64-bit system? */
#if !defined(SJME_BITS)
	#if defined(_LP64) || defined(__LP64__) || defined(__x86_64__) || \
		defined(_M_X64) || defined(_M_AMD64) || defined(__aarch64__) || \
		defined(__ia64__) || defined(__ia64) || defined(_M_IA64) || \
		defined(__itanium__) || defined(__powerpc64__) || \
		defined(__ppc64__) || defined(_ARCH_PPC64) || defined(_ARCH_PPC64)
		#define SJME_BITS 64
	#else
		#define SJME_BITS 32
	#endif
#endif

/** Standard Includes. */
#include <stddef.h>
#include <limits.h>
#include <string.h>
#include <stdlib.h>

/** C99 includes. */
#if (defined(__STDC_VERSION__) && __STDC_VERSION__ >= 199901L) || \
	(defined(__WATCOMC__) && __WATCOMC__ >= 1270)
	#include <stdint.h>

/** Guessed otherwise. */
#else
	#if defined(INT_MAX) && INT_MAX == 32767
		typedef signed long int32_t;
		
		#define INT32_C(x) x##L
	#else
		typedef signed int int32_t;
		
		#define INT32_C(x) x
	#endif
#endif

/** Linux. */
#if defined(__linux__) || defined(__gnu_linux__)
	#define SJME_IS_LINUX 1
	
	#include <sys/mman.h>
#endif

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXRATUFACOATSJMERCHSJMERCH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

/** {@code int} type. */
typedef int32_t sjme_jint;

/** Constant value. */
#define SJME_JINT_C(x) INT32_C(x)

/** Pointer conversion. */
#define SJME_POINTER_TO_JINT(x) ((sjme_jint)((uintptr_t)(x)))
#define SJME_JINT_TO_POINTER(x) ((void*)((uintptr_t)(x)))

/**
 * Native functions available for the JVM to use.
 *
 * @since 2019/06/03
 */
typedef struct sjme_nativefuncs
{
	/** Current monotonic nano-seconds, returns low nanos. */
	sjme_jint (*nanotime)(sjme_jint* hi);
	
	/** Current system clock in Java time, returns low time. */
	sjme_jint (*millitime)(sjme_jint* hi);
} sjme_nativefuncs;

/** Standard C format for arguments. */
#define SJME_JVMARG_FORMAT_STDC 1

/**
 * Java virtual machine arguments.
 *
 * @since 2019/06/03
 */
typedef struct sjme_jvmargs
{
	/** The format of the arguments. */
	int format;
	
	/** Arguments that can be used. */
	union
	{
		/** Standard C. */
		struct
		{
			/** Argument count. */
			int argc;
			
			/** Arguments. */
			char** argv;
		} stdc;
	} args;
} sjme_jvmargs;

/**
 * Options used to initialize the virtual machine.
 *
 * @since 2019/06/06
 */
typedef struct sjme_jvmoptions
{
	/** The amount of RAM to allocate, 0 is default. */
	sjme_jint ramsize;
	
	/** Preset ROM pointer, does not need loading? */
	void* presetrom;
	
	/** Command line arguments sent to the VM. */
	sjme_jvmargs args;
} sjme_jvmoptions;

/** Instance of the JVM. */
typedef struct sjme_jvm* sjme_jvm;

/**
 * Executes code running within the JVM.
 *
 * @param jvm The JVM to execute.
 * @return Non-zero if the JVM is resuming, otherwise zero on its exit.
 * @since 2019/06/05
 */
int sjme_jvmexec(sjme_jvm* jvm);

/**
 * Creates a new instance of a SquirrelJME JVM.
 *
 * @param args Arguments to the JVM.
 * @param options Options used to initialize the JVM.
 * @param nativefuncs Native functions used in the JVM.
 * @return The resulting JVM or {@code NULL} if it could not be created.
 * @since 2019/06/03
 */
sjme_jvm* sjme_jvmnew(sjme_jvmoptions* options, sjme_nativefuncs* nativefuncs);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXRATUFACOATSJMERCHSJMERCH
}
#undef SJME_cXRATUFACOATSJMERCHSJMERCH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXRATUFACOATSJMERCHSJMERCH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGRATUFACOATSJMERCHSJMERCH */

