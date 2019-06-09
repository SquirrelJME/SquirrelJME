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

/** Standard Includes. */
#include <stddef.h>
#include <limits.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

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

/** Possibly detect endianess. */
#if !defined(SJME_BIG_ENDIAN) && !defined(SJME_LITTLE_ENDIAN)
	
	/** Just set little endian if no endianess was defined */
	#if !defined(SJME_BIG_ENDIAN) && !defined(SJME_LITTLE_ENDIAN)
		#define SJME_LITTLE_ENDIAN
	#endif
#endif

/** Linux. */
#if defined(__linux__) || defined(__gnu_linux__)
	#define SJME_IS_LINUX 1
	
	#include <sys/mman.h>
#endif

/** C99 includes. */
#if (defined(__STDC_VERSION__) && __STDC_VERSION__ >= 199901L) || \
	(defined(__WATCOMC__) && __WATCOMC__ >= 1270)
	#include <stdint.h>

/** Guessed otherwise. */
#else
	#if defined(SCHAR_MAX) && SCHAR_MAX == 127
		typedef signed char int8_t;
		
		#define INT8_C(x) x
	#endif
	
	#if defined(INT_MAX) && INT_MAX == 32767
		typedef signed int int16_t;
		typedef unsigned int uint16_t;
		
		#define INT16_C(x) x
		#define UINT16_C(x) x##U
	#elif defined(SHORT_MAX) && SHORT_MAX == 32767
		typedef signed short int16_t;
		typedef unsigned short uint16_t;
		
		#define INT16_C(x) x
		#define UINT16_C(x) x##U
	#endif
	
	#if defined(INT_MAX) && INT_MAX > 32767
		typedef signed int int32_t;
		
		#define INT32_C(x) x
	#elif defined(LONG_MAX) && LONG_MAX > 32767
		typedef signed long int32_t;
		
		#define INT32_C(x) x##L
	#endif
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

/** {@code byte} type. */
typedef int8_t sjme_jbyte;

/** {@code short} type. */
typedef int16_t sjme_jshort;

/** {@code char} type. */
typedef uint16_t sjme_jchar;

/** {@code int} type. */
typedef int32_t sjme_jint;

/** Constant value macros. */
#define SJME_JBYTE_C(x) INT8_C(x)
#define SJME_JSHORT_C(x) INT16_C(x)
#define SJME_JCHAR_C(x) UINT16_C(x)
#define SJME_JINT_C(x) INT32_C(x)

/** Maximum values. */
#define SJME_JINT_MAX_VALUE INT32_C(0x7FFFFFFF)

/** Unsigned short mask. */
#define SJME_JINT_USHORT_MASK INT32_C(0xFFFF)

/** Pointer conversion. */
#define SJME_POINTER_TO_JINT(x) ((sjme_jint)((uintptr_t)(x)))
#define SJME_JINT_TO_POINTER(x) ((void*)((uintptr_t)((sjme_jint)(x))))

/** Pointer path. */
#define SJME_POINTER_OFFSET(p, o) SJME_JINT_TO_POINTER( \
	SJME_POINTER_TO_JINT(p) + ((sjme_jint)(o)))

/** Standard C format for arguments. */
#define SJME_JVMARG_FORMAT_STDC SJME_JINT_C(1)

/** Open file for reading. */
#define SJME_OPENMODE_READ SJME_JINT_C(1)

/** Open file for read and writing. */
#define SJME_OPENMODE_READWRITE SJME_JINT_C(2)

/** Open file for read and writing, create new file or truncate existing. */
#define SJME_OPENMODE_READWRITETRUNCATE SJME_JINT_C(3)

/** No error. */
#define SJME_ERROR_NONE SJME_JINT_C(0)

/** Unknown error. */
#define SJME_ERROR_UNKNOWN SJME_JINT_C(-1)

/** File does not exist. */
#define SJME_ERROR_NOSUCHFILE SJME_JINT_C(-2)

/** Invalid argument. */
#define SJME_ERROR_INVALIDARG SJME_JINT_C(-3)

/** End of file reached. */
#define SJME_ERROR_ENDOFFILE SJME_JINT_C(-4)

/** No memory available. */
#define SJME_ERROR_NOMEMORY SJME_JINT_C(-5)

/** No native ROM file specified. */
#define SJME_ERROR_NONATIVEROM SJME_JINT_C(-6)

/** No support for files. */
#define SJME_ERROR_NOFILES SJME_JINT_C(-7)

/** Invalid ROM magic number. */
#define SJME_ERROR_INVALIDROMMAGIC SJME_JINT_C(-8)

/** Invalid JAR magic number. */
#define SJME_ERROR_INVALIDJARMAGIC SJME_JINT_C(-9)

/** Invalid end of BootRAM. */
#define SJME_ERROR_INVALIDBOOTRAMEND SJME_JINT_C(-10)

/** Invalid BootRAM seed. */
#define SJME_ERROR_INVALIDBOOTRAMSEED SJME_JINT_C(-11)

/** Invalid CPU operation. */
#define SJME_ERROR_INVALIDOP SJME_JINT_C(-512)

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

/**
 * This represents the name of a file in native form, system dependent.
 *
 * @since 2019/06/08
 */
typedef struct sjme_nativefilename sjme_nativefilename;

/**
 * Represents an open file.
 *
 * @since 2019/06/08
 */
typedef struct sjme_nativefile sjme_nativefile;

/** Instance of the JVM. */
typedef struct sjme_jvm sjme_jvm;

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
	
	/** The filename to use for the native ROM. */
	sjme_nativefilename* (*nativeromfile)(void);
	
	/** Converts the Java char sequence to native filename. */
	sjme_nativefilename* (*nativefilename)(sjme_jint len, sjme_jchar* chars);
	
	/** Frees the specified filename. */
	void (*freefilename)(sjme_nativefilename* filename);
	
	/** Opens the specified file. */
	sjme_nativefile* (*fileopen)(sjme_nativefilename* filename,
		sjme_jint mode, sjme_jint* error);
	
	/** Closes the specified file. */
	void (*fileclose)(sjme_nativefile* file, sjme_jint* error);
	
	/** Returns the size of the file. */
	sjme_jint (*filesize)(sjme_nativefile* file, sjme_jint* error);
	
	/** Reads part of a file. */
	sjme_jint (*fileread)(sjme_nativefile* file, void* dest, sjme_jint len,
		sjme_jint* error);
} sjme_nativefuncs;

/**
 * Executes code running within the JVM.
 *
 * @param jvm The JVM to execute.
 * @param error JVM execution error.
 * @param cycles The number of cycles to execute for.
 * @return Non-zero if the JVM is resuming, otherwise zero on its exit.
 * @since 2019/06/05
 */
sjme_jint sjme_jvmexec(sjme_jvm* jvm, sjme_jint* error, sjme_jint cycles);

/**
 * Creates a new instance of a SquirrelJME JVM.
 *
 * @param args Arguments to the JVM.
 * @param options Options used to initialize the JVM.
 * @param nativefuncs Native functions used in the JVM.
 * @param error Error flag.
 * @return The resulting JVM or {@code NULL} if it could not be created.
 * @since 2019/06/03
 */
sjme_jvm* sjme_jvmnew(sjme_jvmoptions* options, sjme_nativefuncs* nativefuncs,
	sjme_jint* error);

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

