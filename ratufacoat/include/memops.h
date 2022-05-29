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
 * Swaps the given short value.
 * 
 * @param val The value to swap.
 * @return The swapped short value.
 * @since 2021/10/17
 */
static inline sjme_jshort sjme_swapShort(sjme_jshort val)
{
	return ((val >> 8) & INT16_C(0x00FF)) |
		((val << 8) & INT16_C(0xFF00));
}

#if defined(SJME_BIG_ENDIAN)
	/**
	 * Returns the value which represents the big endian @c sjme_jint.
	 * 
	 * @param x The big endian integer value
	 * @return The actual value.
	 * @since 2022/03/09
	 */
	#define sjme_bigInt(x) ((sjme_jint)(x))

	/**
	 * Returns the value which represents the big endian @c sjme_jshort.
	 * 
	 * @param x The big endian short value
	 * @return The actual value.
	 * @since 2022/03/09
	 */
	#define sjme_bigShort(x) ((sjme_jshort)(x))
#else
	/**
	 * Returns the value which represents the big endian @c sjme_jint.
	 * 
	 * @param x The big endian integer value
	 * @return The actual value.
	 * @since 2022/03/09
	 */
	#define sjme_bigInt(x) sjme_swapInt((x))

	/**
	 * Returns the value which represents the big endian @c sjme_jshort.
	 * 
	 * @param x The big endian short value
	 * @return The actual value.
	 * @since 2022/03/09
	 */
	#define sjme_bigShort(x) sjme_swapShort((x))
#endif

/**
 * Reads a native integer from memory.
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
 * Reads a native integer from memory.
 * 
 * @param ptr The pointer to read from.
 * @param off The offset into memory.
 * @return The read value.
 * @since 2021/10/17
 */
static inline sjme_jshort sjme_memReadShort(const void* ptr,
	sjme_jint off)
{
	return *((sjme_jshort*)(((uintptr_t)ptr) + off));
}

/**
 * Reads a big endian integer from memory.
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

/**
 * Reads a big endian short from memory.
 * 
 * @param ptr The pointer to read from.
 * @param off The offset into memory.
 * @return The read value.
 * @since 2021/10/17
 */
static inline sjme_jshort sjme_memReadBigShort(const void* ptr,
	sjme_jint off)
{
#if defined(SJME_BIG_ENDIAN)
	return sjme_memReadShort(ptr, off);
#else
	return sjme_swapShort(sjme_memReadShort(ptr, off));
#endif
}

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_MEMOPS_H */
