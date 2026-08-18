/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Functions which are completely missing from a system's native C runtime.
 *
 * This in essence acts as a mini-C library.
 * 
 * @file
 * @since 2024/10/03
 */

#ifndef SJME_C_STDGONE_H
#define SJME_C_STDGONE_H

#include "sjme/config.h"
#include "sjme/stdAttr.h"

#if defined(SJME_CONFIG_HAS_NO_STDARG_H)
	#if defined(SJME_CONFIG_HAS_NO_VARARGS_H)
		#error No stdarg or varargs?
	#else
		#include <varargs.h>
	#endif
#else
	#include <stdarg.h>
#endif

#if defined(SJME_CONFIG_HAS_THREADS_H)
	#include <threads.h>
#endif

#if defined(SJME_CONFIG_HAS_WCHAR_H)
	#include <wchar.h>
#endif

#if defined(SJME_CONFIG_HAS_MSVC)
	/* Needed for alloca. */
	#include <malloc.h>
#endif

#include <stdio.h>
#include <string.h>
#include <math.h>

#if defined(SJME_CONFIG_HAS_STDINT)
	#include <stdint.h>
#else
	#if !SJME_CONFIG_MSVC_VERSION_LEAST(SJME_CONFIG_MSVC_VERSION_2010)
		#include <limits.h>
	#endif
#endif

#if defined(SJME_CONFIG_HAS_INTTYPES_H)
	#include <inttypes.h>
#endif

#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
	/* Needed for alloca(). */
	#include <malloc.h>
#endif

#if defined(SJME_CONFIG_HAS_ERRNO_H)
	#include <errno.h>
#endif

#if defined(SJME_MEMIO_ATOMIC_C11)
	#include <stdatomic.h>
#endif

#if defined(SJME_CONFIG_HAS_CTYPE_H)
	#include <ctype.h>
#endif

#if defined(SJME_CONFIG_HAS_WCTYPE_H)
	#include <wctype.h>
#endif

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_STDGONE_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_HAS_MSVC)
	#if !SJME_CONFIG_MSVC_VERSION_LEAST(SJME_CONFIG_MSVC_VERSION_2010)
		/** The current function. */
		#define __func__ __FUNCTION__
	#endif
#endif
	
#pragma region(sjme_jbyteNative)
	
#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__INT8_TYPE__)
	/** Signed 8-bit integer. */
	typedef __INT8_TYPE__ sjme_jbyteNative;
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Signed 8-bit integer. */
	typedef signed __int8 sjme_jbyteNative;
#elif defined(SJME_CONFIG_HAS_STDINT)
	/** Signed 8-bit integer. */
	typedef int8_t sjme_jbyteNative;
#else
	#error No sjme_jbyteNative
#endif
	
#pragma endregion(sjme_jbyteNative)
#pragma region(sjme_jubyteNative)
	
#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__INT8_TYPE__)
	/** Unsigned 8-bit integer. */
	typedef __UINT8_TYPE__ sjme_jubyteNative;
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Unsigned 8-bit integer. */
	typedef unsigned __int8 sjme_jubyteNative;
#elif defined(SJME_CONFIG_HAS_STDINT)
	/** Unsigned 8-bit integer. */
	typedef uint8_t sjme_jubyteNative;
#else
	#error No sjme_jubyteNative
#endif
	
#pragma endregion(sjme_jubyteNative)
#pragma region(sjme_jshortNative)
	
#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__INT16_TYPE__)
	/** Signed 16-bit integer. */
	typedef __INT16_TYPE__ sjme_jshortNative;
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Signed 16-bit integer. */
	typedef signed __int16 sjme_jshortNative;
#elif defined(SJME_CONFIG_HAS_STDINT)
	/** Signed 16-bit integer. */
	typedef int16_t sjme_jshortNative;
#else
	#error No sjme_jshortNative
#endif
	
#pragma endregion(sjme_jshortNative)
#pragma region(sjme_jushortNative)
	
#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__INT16_TYPE__)
	/** Unsigned 16-bit integer. */
	typedef __UINT16_TYPE__ sjme_jushortNative;
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Unsigned 16-bit integer. */
	typedef unsigned __int16 sjme_jushortNative;
#elif defined(SJME_CONFIG_HAS_STDINT)
	/** Unsigned 16-bit integer. */
	typedef uint16_t sjme_jushortNative;
#else
	#error No sjme_jushortNative
#endif
	
#pragma endregion(sjme_jushortNative)
#pragma region(sjme_jintNative)
	
#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__INT32_TYPE__)
	/** Signed 32-bit integer. */
	typedef __INT32_TYPE__ sjme_jintNative;
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Signed 32-bit integer. */
	typedef signed __int32 sjme_jintNative;
#elif defined(SJME_CONFIG_HAS_STDINT)
	/** Signed 32-bit integer. */
	typedef int32_t sjme_jintNative;
#else
	#error No sjme_jintNative
#endif
	
#pragma endregion(sjme_jintNative)
#pragma region(sjme_juintNative)
	
#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__INT32_TYPE__)
	/** Unsigned 32-bit integer. */
	typedef __UINT32_TYPE__ sjme_juintNative;
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Unsigned 32-bit integer. */
	typedef unsigned __int32 sjme_juintNative;
#elif defined(SJME_CONFIG_HAS_STDINT)
	/** Unsigned 32-bit integer. */
	typedef uint32_t sjme_juintNative;
#else
	#error No sjme_juintNative
#endif
	
#pragma endregion(sjme_juintNative)
#pragma region(sjme_jlongNative)
	
#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__INT64_TYPE__)
	/** Signed 64-bit integer. */
	typedef __INT64_TYPE__ sjme_jlongNative;
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Signed 64-bit integer. */
	typedef signed __int64 sjme_jlongNative;
#elif defined(SJME_CONFIG_HAS_STDINT)
	/** Signed 64-bit integer. */
	typedef int64_t sjme_jlongNative;
#else
	#define SJME_CONFIG_HAS_NO_JLONG_NATIVE
#endif
	
#pragma endregion(sjme_jlongNative)
#pragma region(sjme_julongNative)
	
#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__INT64_TYPE__)
	/** Unsigned 64-bit integer. */
	typedef __UINT64_TYPE__ sjme_julongNative;
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Unsigned 64-bit integer. */
	typedef unsigned __int64 sjme_julongNative;
#elif defined(SJME_CONFIG_HAS_STDINT)
	/** Unsigned 64-bit integer. */
	typedef uint64_t sjme_julongNative;
#else
	#define SJME_CONFIG_HAS_NO_JULONG_NATIVE
#endif
	
#pragma endregion(sjme_julongNative)
#pragma region(sjme_intPointerNative)

#if SJME_CONFIG_HAS_POINTER == 64
	/** Pointer as integer type. */
	typedef sjme_jlongNative sjme_intPointerNative;
#elif SJME_CONFIG_HAS_POINTER == 32
	/** Pointer as integer type. */
	typedef sjme_jintNative sjme_intPointerNative;
#elif SJME_CONFIG_HAS_POINTER == 16
	/** Pointer as integer type. */
	typedef sjme_jshortNative sjme_intPointerNative;
#elif SJME_CONFIG_HAS_POINTER == 8
	/** Pointer as integer type. */
	typedef sjme_jbyteNative sjme_intPointerNative;
#else
	#error No sjme_intPointerNative
#endif
	
#pragma endregion(sjme_intPointerNative)
#pragma region(sjme_uintPointerNative)

#if SJME_CONFIG_HAS_POINTER == 64
	/** Pointer as integer type. */
	typedef sjme_julongNative sjme_uintPointerNative;
#elif SJME_CONFIG_HAS_POINTER == 32
	/** Pointer as integer type. */
	typedef sjme_juintNative sjme_uintPointerNative;
#elif SJME_CONFIG_HAS_POINTER == 16
	/** Pointer as integer type. */
	typedef sjme_jushortNative sjme_uintPointerNative;
#elif SJME_CONFIG_HAS_POINTER == 8
	/** Pointer as integer type. */
	typedef sjme_jubyteNative sjme_uintPointerNative;
#else
	#error No sjme_uintPointerNative
#endif
	
#pragma endregion(sjme_uintPointerNative)
#pragma region(floats)

#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
	/** Native float. */
	typedef float sjme_jfloatNative;
#endif

#if defined(SJME_CONFIG_HAS_DOUBLE_HARD)
	/** Native double. */
	typedef double sjme_jdoubleNative;
#endif

#pragma endregion(floats)
#pragma region(constWrappers)

#if !defined(INT8_C)
	#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__INT8_C)
		/** Signed 8-bit constant. */
		#define INT8_C(x) __INT8_C(x)
	#elif defined(SJME_CONFIG_HAS_MSVC)
		/** Signed 8-bit constant. */
		#define INT8_C(x) x
	#else
		#error No INT8_C
	#endif
#endif
	
#if !defined(UINT8_C)
	#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__UINT8_C)
		/** Unsigned 8-bit constant. */
		#define UINT8_C(x) __UINT8_C(x)
	#elif defined(SJME_CONFIG_HAS_MSVC)
		/** Unsigned 8-bit constant. */
		#define UINT8_C(x) x##U
	#else
		#error No UINT8_C
	#endif
#endif
	
#if !defined(INT16_C)
	#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__INT16_C)
		/** Signed 16-bit constant. */
		#define INT16_C(x) __INT16_C(x)
	#elif defined(SJME_CONFIG_HAS_MSVC)
		/** Signed 16-bit constant. */
		#define INT16_C(x) x
	#else
		#error No INT16_C
	#endif
#endif
	
#if !defined(UINT16_C)
	#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__UINT16_C)
		/** Signed 16-bit constant. */
		#define UINT16_C(x) __UINT16_C(x)
	#elif defined(SJME_CONFIG_HAS_MSVC)
		/** Signed 16-bit constant. */
		#define UINT16_C(x) x##U
	#else
		#error No UINT16_C
	#endif
#endif
	
#if !defined(INT32_C)
	#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__INT32_C)
		/** Signed 32-bit constant. */
		#define INT32_C(x) __INT32_C(x)
	#elif defined(SJME_CONFIG_HAS_MSVC)
		/** Signed 32-bit constant. */
		#define INT32_C(x) x
	#else
		#error No INT32_C
	#endif
#endif
	
#if !defined(UINT32_C)
	#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__UINT32_C)
		/** Unsigned 32-bit constant. */
		#define UINT32_C(x) __UINT32_C(x)
	#elif defined(SJME_CONFIG_HAS_MSVC)
		/** Unsigned 32-bit constant. */
		#define UINT32_C(x) x##U
	#else
		#error No UINT32_C
	#endif
#endif

#if !defined(INT64_C)
	#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__INT64_C)
		/** Signed 64-bit constant. */
		#define INT64_C(x) __INT64_C(x)
	#elif defined(SJME_CONFIG_HAS_MSVC)
		/** Signed 64-bit constant. */
		#define INT64_C(x) x##I64
	#else
		#error No INT64_C
	#endif
#endif

#if !defined(UINT64_C)
	#if defined(SJME_CONFIG_HAS_GCC_CLONE) && defined(__UINT64_C)
		/** Unsigned 64-bit constant. */
		#define UINT64_C(x) __UINT64_C(x)
	#elif defined(SJME_CONFIG_HAS_MSVC)
		/** Unsigned 64-bit constant. */
		#define UINT64_C(x) x##UI64
	#else
		#error No UINT64_C
	#endif
#endif
	
#pragma endregion(constWrappers)
#pragma region(limits)

#if !defined(INT8_MIN)
	/** Minimum signed 8-bit integer. */
	#define INT8_MIN INT8_C(-128)
#endif

#if !defined(INT8_MAX)
	/** Maximum signed 8-bit integer. */
	#define INT8_MAX INT8_C(127)
#endif

#if !defined(UINT8_MAX)
	/** Maximum unsigned 8-bit integer. */
	#define UINT8_MAX UINT8_C(255)
#endif
	
#if !defined(INT16_MIN)
	/** Minimum signed 16-bit integer. */
	#define INT16_MIN INT16_C(-32768)
#endif

#if !defined(INT16_MAX)
	/** Maximum signed 16-bit integer. */
	#define INT16_MAX INT16_C(32767)
#endif

#if !defined(UINT16_MAX)
	/** Maximum unsigned 16-bit integer. */
	#define UINT16_MAX UINT16_C(65535)
#endif

#if !defined(INT32_MIN)
	/** Minimum signed 32-bit integer. */
	#define INT32_MIN INT32_C(0x80000000)
#endif

#if !defined(INT32_MAX)
	/** Maximum signed 32-bit integer. */
	#define INT32_MAX INT32_C(0x7FFFFFFF)
#endif

#if !defined(UINT32_MAX)
	/** Maximum unsigned 32-bit integer. */
	#define UINT32_MAX UINT32_C(0xFFFFFFFF)
#endif

#if !defined(INT64_MIN)
	/** Minimum signed 64-bit integer. */
	#define INT64_MIN INT64_C(0x8000000000000000)
#endif

#if !defined(INT64_MAX)
	/** Maximum signed 64-bit integer. */
	#define INT64_MAX INT64_C(0x7FFFFFFFFFFFFFFF)
#endif
	
#if !defined(UINT64_MAX)
	/** Maximum unsigned 64-bit integer. */
	#define UINT64_MAX UINT64_C(0xFFFFFFFFFFFFFFFF)
#endif
	
#pragma endregion(limits)
#pragma region(printf)

#if !defined(PRId64)
	#if defined(SJME_CONFIG_HAS_MSVC)
		#define PRId64 "I64d"
	#else
		#define PRId64 "lld"
	#endif
#endif

#if !defined(PRSl)
	#if defined(SJME_CONFIG_HAS_MSVC)
		#define PRSl "ws"
	#else
		#define PRSl "ls"
	#endif
#endif

#pragma endregion(printf)
#pragma region(intTypes)

#if !defined(SJME_CONFIG_HAS_INTTYPES_H)
	#if defined(SJME_CONFIG_HAS_MSVC)
		/** @c printf() 64-bit decimal specifier. */
		#define PRId64 "I64"
	#endif
#endif

#pragma endregion(intTypes)
#pragma region(abort)

/* Handle explicit cancelling out. */
#if defined(SJME_CONFIG_HAS_NO_ABORT) && \
	defined(SJME_CONFIG_HAS_ABORT)
	#undef SJME_CONFIG_HAS_ABORT
#endif

#if defined(SJME_CONFIG_HAS_NO_ABORT)
void abort();
#endif

#pragma endregion(abort)
#pragma region(exit)

/* Handle explicit cancelling out. */
#if defined(SJME_CONFIG_HAS_NO_EXIT) && \
	defined(SJME_CONFIG_HAS_EXIT)
	#undef SJME_CONFIG_HAS_EXIT
#endif

#if defined(SJME_CONFIG_HAS_NO_EXIT)
void exit(int exitCode);
#endif

#pragma endregion(exit)
#pragma region(snprintf)

/* Handle explicit cancelling out. */
#if defined(SJME_CONFIG_HAS_NO_SNPRINTF) && \
	defined(SJME_CONFIG_HAS_SNPRINTF)
	#undef SJME_CONFIG_HAS_SNPRINTF
#endif

#if defined(SJME_CONFIG_HAS_NO_SNPRINTF)
int snprintf(
	sjme_attrInNotNull char* buf,
	sjme_attrInPositive size_t bufSize,
	sjme_attrInNotNull const char* format,
	...);
#endif

#pragma endregion(snprintf)
#pragma region(vsnprintf)

/* Handle explicit cancelling out. */
#if defined(SJME_CONFIG_HAS_NO_VSNPRINTF) && \
	defined(SJME_CONFIG_HAS_VSNPRINTF)
	#undef SJME_CONFIG_HAS_VSNPRINTF
#endif

#if defined(SJME_CONFIG_HAS_NO_VSNPRINTF)
int vsnprintf(
	sjme_attrInNotNull char* buf,
	sjme_attrInPositive size_t bufSize,
	sjme_attrInNotNull const char* format,
	sjme_attrInValue va_list args);
#endif

#pragma endregion(vsnprintf)
#pragma region(va_copy)

/* Older MSVC does not have va_copy(). */
#if defined(SJME_CONFIG_HAS_MSVC) && \
	!SJME_CONFIG_MSVC_VERSION_LEAST(SJME_CONFIG_MSVC_VERSION_2010) && \
	!defined(va_copy)
	/** Copy argument lists. */
	#define va_copy(d, s) ((d) = (s))
#endif

#pragma endregion(va_copy)
#pragma region(strcasecmp)

/* Handle explicit cancelling out. */
#if defined(SJME_CONFIG_HAS_NO_STRCASECMP) && \
	defined(SJME_CONFIG_HAS_STRCASECMP)
	#undef SJME_CONFIG_HAS_STRCASECMP
#endif

#if defined(SJME_CONFIG_HAS_NO_STRICMP) && \
	defined(SJME_CONFIG_HAS_STRICMP)
	#undef SJME_CONFIG_HAS_STRICMP
#endif

#if defined(SJME_CONFIG_HAS_NO_STRCASECMP)
	#if defined(SJME_CONFIG_HAS_STRICMP)
		/** Compare two strings without regarding case. */
		#define strcasecmp stricmp

		/** Compare two strings without regarding case, limit length. */
		#define strncasecmp strnicmp

		/** @code strcasecmp() @endcode is aliased by stricmp() . */
		#define SJME_CONFIG_HAS_STRCASECMP

		/* Clear this as we are now aliasing it. */
		#undef SJME_CONFIG_HAS_NO_STRCASECMP
	#else
int strcasecmp(const char* a, const char* b);
int strncasecmp(const char* a, const char* b, size_t n);
	#endif
#endif

#pragma endregion(strcasecmp)
#pragma region(wcscasecmp)

/* Handle explicit cancelling out. */
#if defined(SJME_CONFIG_HAS_NO_WCSCASECMP) && \
	defined(SJME_CONFIG_HAS_WCSCASECMP)
	#undef SJME_CONFIG_HAS_WCSCASECMP
#endif

#if defined(SJME_CONFIG_HAS_NO_WCSICMP) && \
	defined(SJME_CONFIG_HAS_WCSICMP)
	#undef SJME_CONFIG_HAS_WCSICMP
#endif

#if defined(SJME_CONFIG_HAS_NO_WCSCASECMP)
	#if defined(SJME_CONFIG_HAS_WCSICMP)
		/** Compare two wide strings without regarding case. */
		#define wcscasecmp wcsicmp

		/** Compare two wide strings without regarding case, limit length. */
		#define wcsncasecmp wcsnicmp

		/** @code wcscasecmp() @endcode is aliased by wcsicmp() . */
		#define SJME_CONFIG_HAS_WCSCASECMP

		/* Clear this as we are now aliasing it. */
		#undef SJME_CONFIG_HAS_NO_WCSCASECMP
	#else
int wcscasecmp(const wchar_t* a, const wchar_t* b);
int wcsncasecmp(const wchar_t* a, const wchar_t* b, size_t n);
	#endif
#endif

#pragma endregion(wcscasecmp)
#pragma region(strnlen)

#if !defined(SJME_CONFIG_HAS_STRNLEN) || defined(SJME_CONFIG_HAS_NO_STRNLEN)
size_t strnlen(const char* s, size_t limit);
#endif

#pragma endregion(strnlen)
#pragma region(tolower)

/* Handle explicit cancelling out. */
#if defined(SJME_CONFIG_HAS_NO_TOLOWER) && \
	defined(SJME_CONFIG_HAS_TOLOWER)
	#undef SJME_CONFIG_HAS_TOLOWER
#endif

#if defined(SJME_CONFIG_HAS_NO_TOLOWER)
int tolower(int c);
#endif

#pragma endregion(tolower)
#pragma region(towlower)

/* Handle explicit cancelling out. */
#if defined(SJME_CONFIG_HAS_NO_TOWLOWER) && \
	defined(SJME_CONFIG_HAS_TOWLOWER)
	#undef SJME_CONFIG_HAS_TOWLOWER
#endif

#if defined(SJME_CONFIG_HAS_NO_TOWLOWER)
wint_t towlower(wint_t c);
#endif

#pragma endregion(towlower)
#pragma region(toupper)

/* Handle explicit cancelling out. */
#if defined(SJME_CONFIG_HAS_NO_TOUPPER) && \
	defined(SJME_CONFIG_HAS_TOUPPER)
	#undef SJME_CONFIG_HAS_TOUPPER
#endif
	
#if defined(SJME_CONFIG_HAS_NO_TOUPPER)
int toupper(int c);
#endif

#pragma endregion(toupper)
#pragma region(towupper)

/* Handle explicit cancelling out. */
#if defined(SJME_CONFIG_HAS_NO_TOWUPPER) && \
	defined(SJME_CONFIG_HAS_TOWUPPER)
	#undef SJME_CONFIG_HAS_TOWUPPER
#endif
	
#if defined(SJME_CONFIG_HAS_NO_TOWUPPER)
wint_t towupper(wint_t c);
#endif

#pragma endregion(towupper)
#pragma region(exitFailure)
	
#if !defined(EXIT_FAILURE)
	/** Exit with failure. */
	#define EXIT_FAILURE 1
#endif

#pragma endregion(exitFailure)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_STDGONE_H
}
		#undef SJME_CXX_SQUIRRELJME_STDGONE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_STDGONE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_STDGONE_H */
