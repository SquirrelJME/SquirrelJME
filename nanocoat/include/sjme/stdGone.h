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
 * @since 2024/10/03
 */

#ifndef SQUIRRELJME_STDGONE_H
#define SQUIRRELJME_STDGONE_H

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_NO_STDARG)
	#if defined(SJME_CONFIG_HAS_NO_VARARGS)
		#include <varargs.h>
	#else
		#error No stdarg or varargs?
	#endif
#else
	#include <stdarg.h>
#endif

#if !defined(SJME_CONFIG_HAS_NO_C11_THREADS)
	#include <threads.h>
#endif

#if defined(SJME_CONFIG_HAS_MSVC)
	/* Needed for alloca. */
	#include <malloc.h>
#endif

#include <stdio.h>
#include <string.h>

#if defined(SJME_CONFIG_HAS_STDINT)
	#include <stdint.h>
#else
	#if !SJME_CONFIG_MSVC_VERSION_LEAST(SJME_VERSION_MSVC_2010)
		#include <limits.h>
	#endif
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
	#if !SJME_CONFIG_MSVC_VERSION_LEAST(SJME_VERSION_MSVC_2010)
		/** The current function. */
		#define __func__ __FUNCTION__
	#endif
#endif

#if !defined(SJME_CONFIG_HAS_STDINT)
	#if !SJME_CONFIG_MSVC_VERSION_LEAST(SJME_VERSION_MSVC_2010)
		/** Signed 8-bit integer. */
		typedef signed __int8 int8_t;

		/** Minimum signed 8-bit integer. */
		#define INT8_MIN SCHAR_MIN

		/** Maximum signed 8-bit integer. */
		#define INT8_MAX SCHAR_MAX

		/** Unigned 8-bit integer. */
		typedef unsigned __int8 uint8_t;

		/** Minimum unsigned 8-bit integer. */
		#define UINT8_MIN 0

		/** Maximum unsigned 8-bit integer. */
		#define UINT8_MAX UCHAR_MAX

		/** Signed 16-bit integer. */
		typedef signed __int16 int16_t;

		/** Minimum signed 16-bit integer. */
		#define INT16_MIN SHRT_MIN

		/** Maximum signed 16-bit integer. */
		#define INT16_MAX SHRT_MAX

		/** Unigned 16-bit integer. */
		typedef unsigned __int16 uint16_t;

		/** Minimum unsigned 16-bit integer. */
		#define UINT16_MIN 0

		/** Maximum unsigned 16-bit integer. */
		#define UINT16_MAX USHRT_MAX

		/** Signed 32-bit integer. */
		typedef signed __int32 int32_t;

		/** Minimum signed 32-bit integer. */
		#define INT32_MIN INT_MIN

		/** Maximum signed 32-bit integer. */
		#define INT32_MAX INT_MAX
	
		/** Signed 32-bit integer macro. */
		#define INT32_C(x) x

		/** Unigned 32-bit integer. */
		typedef unsigned __int32 uint32_t;

		/** Minimum unsigned 32-bit integer. */
		#define UINT32_MIN 0

		/** Maximum unsigned 32-bit integer. */
		#define UINT32_MAX UINT_MAX
	
		/** Unsigned 32-bit integer macro. */
		#define UINT32_C(x) x##U

		/** Signed 64-bit integer. */
		typedef signed __int64 int64_t;

		/** Minimum signed 64-bit integer. */
		#define INT64_MIN _I64_MIN

		/** Maximum signed 64-bit integer. */
		#define INT64_MAX _I64_MAX
	
		/** Signed 64-bit integer macro. */
		#define INT64_C(x) x##I64

		/** Unigned 64-bit integer. */
		typedef unsigned __int64 uint64_t;

		/** Minimum unsigned 64-bit integer. */
		#define UINT64_MIN 0

		/** Maximum unsigned 64-bit integer. */
		#define UINT64_MAX _UI64_MAX
	
		/** Unsigned 64-bit integer macro. */
		#define UINT64_C(x) x##UI64
	#else
		#error No stdint types
	#endif
#endif

#if defined(SJME_CONFIG_HAS_NO_ABORT)
void abort();
#endif

#if defined(SJME_CONFIG_HAS_NO_EXIT)
void exit(int exitCode);
#endif

#if defined(SJME_CONFIG_HAS_NO_SNPRINTF)
int snprintf(
	sjme_attrInNotNull char* buf,
	sjme_attrInPositive size_t bufSize,
	sjme_attrInNotNull const char* format,
	...);
#endif

#if defined(SJME_CONFIG_HAS_NO_VSNPRINTF)
int vsnprintf(
	sjme_attrInNotNull char* buf,
	sjme_attrInPositive size_t bufSize,
	sjme_attrInNotNull const char* format,
	sjme_attrInValue va_list args);
#endif

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
