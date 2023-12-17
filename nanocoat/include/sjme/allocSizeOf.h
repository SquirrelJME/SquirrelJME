/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Used to determine allocation sizes.
 * 
 * @since 2023/12/14
 */

#ifndef SQUIRRELJME_ALLOCSIZEOF_H
#define SQUIRRELJME_ALLOCSIZEOF_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ALLOCSIZE_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

typedef enum sjme_alloc_sizeOfId
{
	/** Unknown. */
	SJME_ALLOC_SIZEOF_UNKNOWN = 0,

	/** @c sjme_rom_suiteFunctions . */
	SJME_ALLOC_SIZEOF_ROM_SUITE_FUNCTIONS = 1,

	/** Reserved pool size. */
	SJME_ALLOC_SIZEOF_RESERVED_POOL = 2,

	/** Virtual machine boot configuration. */
	SJME_ALLOC_SIZEOF_NVM_BOOT_PARAM = 3,

	/** Pointer type. */
	SJME_ALLOC_SIZEOF_POINTER = 4,

	/** Little endian, non-zero if so. */
	SJME_ALLOC_SIZEOF_IS_LITTLE_ENDIAN = 5,

	/** Big endian, non-zero if so. */
	SJME_ALLOC_SIZEOF_IS_BIG_ENDIAN = 6,

	/** The number of allocation type sizes. */
	SJME_NUM_ALLOC_TYPE_SIZEOF
} sjme_alloc_sizeOfId;

/**
 * Returns the size of the given type.
 *
 * @param id The type to get the size of.
 * @param count The number of elements to count.
 * @param outSize The output size.
 * @return If there are any errors.
 * @since 2023/12/14
 */
sjme_errorCode sjme_alloc_sizeOf(
	sjme_attrInRange(SJME_ALLOC_SIZEOF_ROM_SUITE_FUNCTIONS,
		SJME_NUM_ALLOC_TYPE_SIZEOF) sjme_alloc_sizeOfId id,
	sjme_attrInPositive sjme_jint count,
	sjme_attrOutNotNull sjme_jint* outSize);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ALLOCSIZE_H
}
		#undef SJME_CXX_SQUIRRELJME_ALLOCSIZE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ALLOCSIZE_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ALLOCSIZEOF_H */
