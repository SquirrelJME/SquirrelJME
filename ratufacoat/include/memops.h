/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Memory operations.
 * 
 * @since 2021/09/20
 */

#ifndef SQUIRRELJME_MEMOPS_H
#define SQUIRRELJME_MEMOPS_H

#include "sjmerc.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_MEMOPS_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Swaps the given integer value.
 * 
 * @param val The value to swap. 
 * @return The swapped value.
 * @since 2021/09/20
 */
static inline sjme_jint sjme_swapInt(sjme_jint val)
{
	// 0xAABBCCDD -> 0xBBAADDCC
	val = ((((val & INT32_C(0xFF00FF00)) >> 8) & INT32_C(0x00FFFFFF)) |
		((val & INT32_C(0x00FF00FF)) << 8));
	
	// 0xBBAADDCC -> 0xDDCCBBAA
	return ((val >> 16) & INT32_C(0x0000FFFF)) | (val << 16);
}

/**
 * Read from memory using native endianess.
 * 
 * @param ptr The pointer to read from.
 * @param off The offset into memory.
 * @return The read value.
 * @since 2021/09/20
 */
static inline sjme_jint sjme_memReadInt(const void* ptr,
	sjme_jint off)
{
	return *((sjme_jint*)(((uintptr_t)ptr) + off));
}

/**
 * Read from memory using big endian.
 * 
 * @param ptr The pointer to read from.
 * @param off The offset into memory.
 * @return The read value.
 * @since 2021/09/20
 */
static inline sjme_jint sjme_memReadBigInt(const void* ptr,
	sjme_jint off)
{
#if defined(SJME_BIG_ENDIAN)
	return sjme_memReadInt(ptr, off);
#else
	return sjme_swapInt(sjme_memReadInt(ptr, off));
#endif
}

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_MEMOPS_H
}
#undef SJME_CXX_SQUIRRELJME_MEMOPS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_MEMOPS_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MEMOPS_H */
