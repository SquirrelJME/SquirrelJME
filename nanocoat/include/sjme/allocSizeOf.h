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

typedef enum sjme_alloc_typeSize
{
	/** Unknown. */
	SJME_ALLOC_TYPE_SIZEOF_UNKNOWN = 0,

	/** @c sjme_rom_suiteFunctions . */
	SJME_ALLOC_TYPE_SIZEOF_ROM_SUITE_FUNCTIONS = 1,

	/** Reserved pool size. */
	SJME_ALLOC_TYPE_SIZEOF_RESERVED_POOL = 2,

	/** The number of allocation type sizes. */
	SJME_NUM_ALLOC_SIZE
} sjme_alloc_typeSize;

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
