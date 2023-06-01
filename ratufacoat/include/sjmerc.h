/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
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

/* Java header */
#include "sjmejni/sjmejni.h"

/* Variable counting. */
#include "varcount.h"

/* Debugging? */
#if !defined(SJME_DEBUG)
	#if defined(DEBUG) || defined(_DEBUG) || !defined(NDEBUG)
		#define SJME_DEBUG
	#endif
#endif

/* Versioning identifier. */
#if !defined(SQUIRRELJME_VERSION)
	/** Version string. */
	#define SQUIRRELJME_VERSION 0.3.0
#endif

/* Version ID. */
#if !defined(SQUIRRELJME_VERSION_ID)
	/** Version ID. */
	#define SQUIRRELJME_VERSION_ID unknown
#endif

/* Version time. */
#if !defined(SQUIRRELJME_VERSION_ID_TIME)
	/** Version ID. */
	#define SQUIRRELJME_VERSION_ID_TIME unknown
#endif

/** Possibly detect endianess. */
#if !defined(SJME_BIG_ENDIAN) && !defined(SJME_LITTLE_ENDIAN)
	/** Defined by the system? */
	#if !defined(SJME_BIG_ENDIAN)
		#if defined(__BYTE_ORDER__) && (__BYTE_ORDER__ == __ORDER_BIG_ENDIAN__)
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
#if 0
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
#elif defined(SJME_SYSTEM_PALMOS)
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
	
	#if !defined(SQUIRRELJME_PALMOS)
		#define SQUIRRELJME_PALMOS
	#endif

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
#endif

#if defined(PS2)
	/** Building for the PlayStation 2. */
	#define SQUIRRELJME_PS2
#endif

/** Force as C. */
#if defined(_cplusplus)
	#define SJME_EXTERN_C extern "C"
#else
	#define SJME_EXTERN_C
#endif

/* Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXRATUFACOATSJMERCHSJMERCH
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if 0
/** @c byte type. */
typedef int8_t sjme_jbyte;

/** @c short type. */
typedef int16_t sjme_jshort;

/** @c char type. */
typedef uint16_t sjme_jchar;

/** @c int type. */
typedef int32_t sjme_jint;

/** Unsigned @c byte type. */
typedef uint8_t sjme_jubyte;

/** Unsigned @c short type. */
typedef uint16_t sjme_jushort;

/** Unsigned @c int type. */
typedef uint32_t sjme_juint;
#endif

#if 0
/** Boolean type. */
typedef enum sjme_jboolean
{
	/** False. */
	sjme_false = 0,

	/** True. */
	sjme_true = 1,
} sjme_jboolean;
#endif

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

/** @deprecated Do not use, this is not safe! */
#define SJME_POINTER_TO_JINT(x) ((sjme_jint)((uintptr_t)(x)))

/**
 * Casts a pointer to a memory address.
 * 
 * @param x The pointer to cast.
 * @since 2022/03/09
 */
#define SJME_POINTER_TO_JMEM(x) (((uintptr_t)(x)))

/**
 * Casts a memory address to a pointer.
 * 
 * @param x The memory address to cast.
 * @since 2022/03/09
 */
#define SJME_JMEM_TO_POINTER(x) ((void*)((uintptr_t)((x))))

/**
 * Calculates the value from the given pointer offset.
 * 
 * @param p The pointer.
 * @param o The offset.
 * @since 2022/03/09
 */
#define SJME_POINTER_OFFSET_LONG(p, o) (SJME_JMEM_TO_POINTER( \
	SJME_POINTER_TO_JMEM(p) + ((intptr_t)(o))))

/** Standard C format for arguments. */
#define SJME_JVMARG_FORMAT_STDC SJME_JINT_C(1)

/** Open file for reading. */
#define SJME_OPENMODE_READ SJME_JINT_C(1)

/** Open file for read and writing. */
#define SJME_OPENMODE_READWRITE SJME_JINT_C(2)

/** Open file for read and writing, create new file or truncate existing. */
#define SJME_OPENMODE_READWRITETRUNCATE SJME_JINT_C(3)

/** VMM Type: Byte. */
#define SJME_VMMTYPE_BYTE SJME_JINT_C(-1)

/** VMM Type: Short. */
#define SJME_VMMTYPE_SHORT SJME_JINT_C(-2)

/** VMM Type: Integer. */
#define SJME_VMMTYPE_INTEGER SJME_JINT_C(-3)

/** VMM Type: Java Short. */
#define SJME_VMMTYPE_JAVASHORT SJME_JINT_C(-4)

/** VMM Type: Java Integer. */
#define SJME_VMMTYPE_JAVAINTEGER SJME_JINT_C(-5)

#if !defined(SJME_MEMORYPROFILE_NORMAL) && !defined(SJME_MEMORYPROFILE_MINIMAL)
	/** Normal memory profile. */
	#define SJME_MEMORYPROFILE_NORMAL
#endif

#if defined(__GNUC__)
	/** Function is actually used. */
	#define SJME_GCC_USED __attribute__((unused))
#elif defined(_MSC_VER)
	#define SJME_GCC_USED __pragma(warning(suppress: 4505))
#else
	/** Function is actually used. */
	#define SJME_GCC_USED
#endif

#if !defined(PATH_MAX)
	/** Maximum path size. */
	#define PATH_MAX 4096
#endif

/**
 * Marker indicating that the method never returns.
 * 
 * @since 2021/02/28
 */
typedef struct sjme_returnNever
{
	int ignored;
} sjme_returnNever;

/**
 * Virtual memory pointer.
 *
 * @since 2019/06/25
 */
typedef sjme_jint sjme_vmemptr;

/**
 * Reverses the operation of @c offsetof to access a member.
 * 
 * @param type The expected member type.
 * @param offset The offset to the member.
 * @param val The value to read from.
 * @since 2021/09/19 
 */
#define sjme_unoffsetof(type, offset, val) \
	(*((type*)((uintptr_t)(val) + (uintptr_t)(offset))))

/** Inlining for Visual Studio. */
#if defined(_MSC_VER) && !defined(__cplusplus)
	/** Inlined function. */
	#define inline __inline
#endif

/** Force inlining for GCC, but not under Mingw-w64. */
#if defined(__GNUC__) && !defined(__forceinline)
	/** Force inlining of function. */
	#define __forceinline __attribute__((always_inline))
#endif

/** Min constant macro. */
#define sjme_min(a, b) ((a) <= (b) ? (a) : (b))

/** Max constant macro. */
#define sjme_max(a, b) ((a) >= (b) ? (a) : (b))

/** Translation units (sources) cannot be empty. */
#define SJME_EMPTY_TRANSLATION_UNIT \
	static SJME_GCC_USED void sjme_emptyTranslationUnitDoIgnore(void) {}

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXRATUFACOATSJMERCHSJMERCH
}
#undef SJME_cXRATUFACOATSJMERCHSJMERCH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXRATUFACOATSJMERCHSJMERCH */
#endif /* #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGRATUFACOATSJMERCHSJMERCH */

