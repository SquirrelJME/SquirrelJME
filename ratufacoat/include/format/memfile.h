/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * A plain memory file.
 * 
 * @since 2021/11/09
 */

#ifndef SQUIRRELJME_MEMFILE_H
#define SQUIRRELJME_MEMFILE_H

#include "format/library.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_MEMFILE_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The driver for a plain file in memory. */
extern const sjme_libraryDriver sjme_libraryMemFileDriver;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_MEMFILE_H
}
#undef SJME_CXX_SQUIRRELJME_MEMFILE_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_MEMFILE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MEMFILE_H */
