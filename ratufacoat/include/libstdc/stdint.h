/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Standard C Integer Types.
 * 
 * @since 2022/12/26
 */

#ifndef SQUIRRELJME_STDINT_H
#define SQUIRRELJME_STDINT_H

#include <stddef.h>

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_STDINT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Byte. */
typedef __INT8_TYPE__ int8_t;

/** Unsigned Byte. */
typedef __UINT8_TYPE__ uint8_t;

/** Short. */
typedef __INT16_TYPE__ int16_t;

/** Character. */
typedef __UINT16_TYPE__ uint16_t;

/** Integer. */
typedef __INT32_TYPE__ int32_t;

/** Unsigned Integer. */
typedef __UINT32_TYPE__ uint32_t;

/** Long. */
typedef __INT64_TYPE__ int64_t;

/** Unsigned Long. */
typedef __UINT64_TYPE__ uint64_t;

/** Pointer. */
typedef __INTPTR_TYPE__ intptr_t;

/** Unsigned Pointer. */
typedef __UINTPTR_TYPE__ uintptr_t;

#if !defined(INT8_C)
	#if defined(__INT8_C)
		/** Signed 8-bit constant. */
		#define INT8_C(x) __INT8_C(x)
	#endif
#endif

#if !defined(INT16_C)
	#if defined(__INT16_C)
		/** Signed 16-bit constant. */
		#define INT16_C(x) __INT16_C(x)
	#endif
#endif

#if !defined(INT32_C)
	#if defined(__INT32_C)
		/** Signed 32-bit constant. */
		#define INT32_C(x) __INT32_C(x)
	#endif
#endif

#if !defined(UINT8_C)
	#if defined(__UINT8_C)
		/** Unsigned 8-bit constant. */
		#define UINT8_C(x) __UINT8_C(x)
	#endif
#endif

#if !defined(UINT16_C)
	#if defined(__UINT16_C)
		/** Unsigned 16-bit constant. */
		#define UINT16_C(x) __UINT16_C(x)
	#endif
#endif

#if !defined(UINT32_C)
	#if defined(__UINT32_C)
		/** Unsigned 32-bit constant. */
		#define UINT32_C(x) __UINT32_C(x)
	#endif
#endif

#if !defined(SIZE_MAX)
	#if defined(__SIZE_MAX__)
		/** Max size_t. */
		#define SIZE_MAX __SIZE_MAX__
	#endif
#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_STDINT_H
}
		#undef SJME_CXX_SQUIRRELJME_STDINT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_STDINT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_STDINT_H */
