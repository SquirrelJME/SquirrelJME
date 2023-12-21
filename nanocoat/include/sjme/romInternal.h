/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal ROM structures, definitely do not touch these.
 * 
 * @since 2023/12/12
 */

#ifndef SQUIRRELJME_ROMINTERNAL_H
#define SQUIRRELJME_ROMINTERNAL_H

#include "sjme/rom.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ROMINTERNAL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Common cache between suites and libraries.
 *
 * @since 2023/12/20
 */
typedef struct sjme_rom_cache
{
	/** The allocation pool to use. */
	sjme_alloc_pool* allocPool;

	/** Non-common cache size. */
	sjme_jint uncommonSize;
} sjme_rom_cache;

struct sjme_rom_libraryCache
{
	/** Common cache data. */
	sjme_rom_cache common;

	/** Uncommon cache generic structure. */
	sjme_jubyte uncommon[sjme_flexibleArrayCount];
};

struct sjme_rom_suiteCache
{
	/** Common cache data. */
	sjme_rom_cache common;

	/** Libraries that exist within the suite. */
	sjme_list_sjme_rom_library* libraries;

	/** Uncommon cache generic structure. */
	sjme_jubyte uncommon[sjme_flexibleArrayCount];
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ROMINTERNAL_H
}
		#undef SJME_CXX_SQUIRRELJME_ROMINTERNAL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ROMINTERNAL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ROMINTERNAL_H */
