/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Detection and loading of extra dynamic libraries.
 *
 * @file
 * @since 2025/12/07
 */

#ifndef SJME_C_SQUIRRELJME_DYLIBEXTRA_H
#define SJME_C_SQUIRRELJME_DYLIBEXTRA_H

#include "sjme/dylib.h"
#include "sjme/native.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_DYLIBEXTRA_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The type of extra library family to detect and load.
 *
 * @since 2025/12/07
 */
typedef enum sjme_dylib_extraFamily
{
	/** Unknown. */
	SJME_DYLIB_EXTRA_FAMILY_UNKNOWN = 0,

	/** ScritchUI. */
	SJME_DYLIB_EXTRA_FAMILY_SCRITCHUI = 1,

	/** ScritchAudio. */
	SJME_DYLIB_EXTRA_FAMILY_SCRITCHAUDIO = 2,

	/** The current number of supported families. */
	SJME_DYLIB_NUM_EXTRA_FAMILY = 3,
} sjme_dylib_extraFamily;

/**
 * Searches for an opens an extra library using a standard search interface.
 * 
 * @param nal The optional native abstraction layer to use, if this is not
 * specified than the default is used.
 * @param libRoot The optional path to search for libraries within.
 * @param family The family of libraries to attempt opening from.
 * @param subComponent The subcomponent to open, this may not be applicable
 * to all families.
 * @param outLib The resultant library.
 * @return Any resultant error, if any.
 * @since 2025/12/07
 */
sjme_errorCode sjme_dylib_openExtra(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNullable sjme_lpcstr libRoot,
	sjme_attrInRange(0, SJME_DYLIB_NUM_EXTRA_FAMILY)
		sjme_dylib_extraFamily family,
	sjme_attrInNullable sjme_lpcstr subComponent,
	sjme_attrOutNotNull sjme_dylib* outLib);
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_DYLIBEXTRA_H
}
#undef SJME_CXX_SQUIRRELJME_DYLIBEXTRA_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_DYLIBEXTRA_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_DYLIBEXTRA_H */
