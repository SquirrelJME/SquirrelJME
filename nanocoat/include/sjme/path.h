/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Path handling abstraction.
 * 
 * @since 2024/08/09
 */

#ifndef SQUIRRELJME_PATH_H
#define SQUIRRELJME_PATH_H

#include "sjme/config.h"
#include "sjme/error.h"
#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_PATH_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The maximum path length in SquirrelJME. */
#define SJME_MAX_PATH 1024

#if defined(SJME_CONFIG_HAS_DOS)
	/** Short paths. */
	#defne SJME_PATH_SHORT
#endif

/**
 * Resolves the given path onto the given path buffer.
 * 
 * @param outPath The resultant path to be modified.
 * @param outPathLen The size of the path buffer.
 * @param subPath The sub-path to resolve against.
 * @return Any resultant error, if any.
 * @since 2024/08/09
 */
sjme_errorCode sjme_path_resolveAppend(
	sjme_attrOutNotNull sjme_lpstr outPath,
	sjme_attrInPositiveNonZero sjme_jint outPathLen,
	sjme_attrInNotNull sjme_lpcstr subPath);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_PATH_H
}
		#undef SJME_CXX_SQUIRRELJME_PATH_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_PATH_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_PATH_H */
