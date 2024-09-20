/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SquirrelJME JVM Types.
 * 
 * @since 2021/04/03
 */

#ifndef BUILDVM_SJMERC_H
#define BUILDVM_SJMERC_H

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
	/** Defined by the system? */
	#if !defined(SJME_BIG_ENDIAN)
		#if defined(MSB_FIRST) || \
			(defined(WORDS_BIGENDIAN) && WORDS_BIGENDIAN != 0)
			#define SJME_BIG_ENDIAN
		#endif
	#endif
	
	/** Just set little endian if no endianess was defined */
	#if !defined(SJME_BIG_ENDIAN) && !defined(SJME_LITTLE_ENDIAN)
		#define SJME_LITTLE_ENDIAN
	#endif
	
	/** If both are defined, just set big endian. */
	#if defined(SJME_BIG_ENDIAN) && defined(SJME_LITTLE_ENDIAN)
		#undef SJME_LITTLE_ENDIAN
	#endif
#endif

/** Linux. */
#if defined(__linux__) || defined(__gnu_linux__)
	#define SJME_IS_LINUX 1
#endif

/** DOS? */
#if defined(MSDOS) || defined(_MSDOS) || defined(__MSDOS__) || defined(__DOS__)
	#define SJME_IS_DOS 1
	
	#include <malloc.h>
#endif

/** C99 includes. */
#if (defined(__STDC_VERSION__) && __STDC_VERSION__ >= 199901L) || \
	(defined(__WATCOMC__) && __WATCOMC__ >= 1270) || \
	(defined(_MSC_VER) && _MSC_VER >= 1600) || \
	(defined(__GNUC__) && __GNUC__ >= 4) || \
	(defined(PSP) || defined(PS2))
	#include <stdint.h>

/** Old Microsoft. */
#elif defined(_MSC_VER) && _MSC_VER < 1600
	typedef signed __int8 int8_t;
	typedef signed __int16 int16_t;
	typedef signed __int32 int32_t;
	
	typedef unsigned __int8 uint8_t;
	typedef unsigned __int16 uint16_t;
	typedef unsigned __int32 uint32_t;
	
	#define INT8_C(x) x
	#define INT16_C(x) x
	#define INT32_C(x) x
	
	#define UINT8_C(x) x##U
	#define UINT16_C(x) x##U
	#define UINT32_C(x) x##U

/** Palm OS. */
#elif defined(__palmos__)
	#include <PalmTypes.h>
	
	typedef Int8 int8_t;
	typedef Int16 int16_t;
	typedef Int32 int32_t;
	
	typedef UInt8 uint8_t;
	typedef UInt16 uint16_t;
	typedef UInt32 uint32_t;
	
	typedef int32_t intptr_t;
	typedef uint32_t uintptr_t;
	
	#define INT8_C(x) x
	#define INT16_C(x) x
	#define INT32_C(x) x##L
	
	#define UINT8_C(x) x##U
	#define UINT16_C(x) x##U
	#define UINT32_C(x) x##UL
	
	#define SIZE_MAX UINT32_C(0xFFFFFFFF)

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
		typedef unsigned int uint32_t;
		
		#define INT32_C(x) x
	#elif defined(LONG_MAX) && LONG_MAX > 32767
		typedef signed long int32_t;
		typedef unsigned long uint32_t;
		
		#define INT32_C(x) x##L
		#define UINT32_C(x) x##UL
	#endif
#endif

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_BUILDVM_SJMERC_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** {@code byte} type. */
typedef int8_t sjme_jbyte;

/** {@code short} type. */
typedef int16_t sjme_jshort;

/** {@code char} type. */
typedef uint16_t sjme_jchar;

/** {@code int} type. */
typedef int32_t sjme_jint;

/** Unsigned {@code int} type. */
typedef uint32_t sjme_juint;

/** Constant value macros. */
#define SJME_JBYTE_C(x) INT8_C(x)
#define SJME_JSHORT_C(x) INT16_C(x)
#define SJME_JCHAR_C(x) UINT16_C(x)
#define SJME_JINT_C(x) INT32_C(x)
#define SJME_JUINT_C(x) UINT32_C(x)

/** Maximum values. */
#define SJME_JINT_MAX_VALUE INT32_C(0x7FFFFFFF)

/** Unsigned short mask. */
#define SJME_JINT_USHORT_MASK INT32_C(0xFFFF)

/** Boolean type. */
typedef enum sjme_jboolean
{
	sjme_false = 0,
	sjme_true = 1,
} sjme_jboolean;

/** This represents an error. */
typedef struct sjme_error
{
	/** Error code. */
	sjme_jint code;
	
	/** The value of it. */
	sjme_jint value;
} sjme_error;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_BUILDVM_SJMERC_H
}
#undef SJME_CXX_BUILDVM_SJMERC_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_BUILDVM_SJMERC_H */
#endif /* #ifdef __cplusplus */

#endif /* BUILDVM_SJMERC_H */
