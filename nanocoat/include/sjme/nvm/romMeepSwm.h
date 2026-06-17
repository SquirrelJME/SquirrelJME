/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Simplified MEEP Suite Management System for initial Jar loading.
 *
 * This is not meant to be a full drop in replacement for the suite management
 * system that is in the runtime library. Although this is a duplicate in C,
 * it is mostly only meant to give the ability to launch Jars and dependencies
 * as if they were normal applications similarly to how the existing
 * SquirrelJME's @code VMFactory @endcode/@code SuiteManager @endcode in
 * SquirrelJME already works.
 * 
 * @since 2026/06/17
 */

#ifndef SJME_C_SQUIRRELJME_ROMMEEPSWM_H
#define SJME_C_SQUIRRELJME_ROMMEEPSWM_H

#include "sjme/nvm/rom.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_ROMMEEPSWM_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

struct sjme_nvm_rom_swmManagerBase
{
	/** The SWM manager is a bit complicated, so it needs a cleanup helper. */
	sjme_closeableBase closeable;

	sjme_jint todo;
};

/**
 * Loads all the MEEP SWM related information from the library suite so that
 * dependency resolution is possible.
 *
 * Note that for other platforms such as DoJa/Star, these will be mapped to
 * MEEP SWM using SquirrelJME specific means and will not be portable. There
 * additionally will be SquirrelJME specific meta-dependencies that are used
 * to assist in classpath resolution.
 *
 * @param allocPool The allocation pool to use.
 * @param inSuite The suite to setup a manager for.
 * @param outSwmManager The resultant SWM manager.
 * @return Any resultant error, if any.
 * @since 2026/06/16
 */
sjme_errorCode sjme_nvm_rom_swmLoad(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrOutNotNull sjme_nvm_rom_swmManager* outSwmManager);

/**
 * Looks up the library and all of its dependencies
 *
 * @param swmManager The suite to get the launch parameters from.
 * @param inLibrary The library being resolved.
 * @param outMainClass The main class to launch.
 * @param outMainArgs The arguments to pass to the main class.
 * @param outById The main class path by ID.
 * @param outByName The main class path by name.
 * @return Any resultant error, if any.
 * @since 2026/06/17
 */
sjme_errorCode sjme_nvm_rom_swmResolve(
	sjme_attrInNotNull sjme_nvm_rom_swmManager swmManager,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_lpstr* outMainClass,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outMainArgs,
	sjme_attrOutNotNull sjme_list(sjme_jint)** outById,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outByName);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_ROMMEEPSWM_H
}
#undef SJME_CXX_SQUIRRELJME_ROMMEEPSWM_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_ROMMEEPSWM_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_ROMMEEPSWM_H */