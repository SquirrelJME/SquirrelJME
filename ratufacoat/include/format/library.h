/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Library support.
 * 
 * @since 2021/09/12
 */

#ifndef SQUIRRELJME_LIBRARY_H
#define SQUIRRELJME_LIBRARY_H

#include "sjmerc.h"
#include "error.h"
#include "format/detect.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_LIBRARY_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * This represents a library driver that is available for usage.
 * 
 * @since 2021/09/12
 */
typedef struct sjme_libraryDriver
{
	/** Is this for the given library driver? */
	sjme_formatDetectFunction detect;
} sjme_libraryDriver;

/**
 * Instance of a library, is internally kept state.
 * 
 * @since 2021/09/19
 */ 
typedef struct sjme_libraryInstance sjme_libraryInstance;

/**
 * Opens the given library and makes an instance of it.
 * 
 * @param outInstance The output instance for returning.
 * @param data The data block.
 * @param size The size of the data block.
 * @param error The error state on open.
 * @return If this was successful or not.
 * @since 2021/09/19
 */
sjme_jboolean sjme_libraryOpen(sjme_libraryInstance** outInstance,
	const void* data, sjme_jint size, sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_LIBRARY_H
}
#undef SJME_CXX_SQUIRRELJME_LIBRARY_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_LIBRARY_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LIBRARY_H */
