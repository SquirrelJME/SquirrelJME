/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Handling implementation for files and otherwise, for direct file system
 * access.
 * 
 * @since 2022/05/22
 */

#ifndef SQUIRRELJME_FILE_H
#define SQUIRRELJME_FILE_H

#include "atomic.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_FILE_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Functions which are used to access files.
 * 
 * @since 2022/05/22
 */
typedef struct sjme_fileFunctions
{
	int junk;
} sjme_fileFunctions;

/**
 * Represents a file that is potentially opened and may be accessed
 * accordingly.
 * 
 * @since 2022/05/22
 */
typedef struct sjme_file
{
	/** The functions used to access this file. */
	const sjme_fileFunctions* functions;
} sjme_file;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_FILE_H
}
		#undef SJME_CXX_SQUIRRELJME_FILE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_FILE_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_FILE_H */
