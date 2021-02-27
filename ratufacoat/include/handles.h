/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Memory handle support, used to have potentially loosely bound regions
 * of memory.
 * 
 * @since 2021/02/27
 */

#ifndef SQUIRRELJME_HANDLES_H
#define SQUIRRELJME_HANDLES_H

#include "sjmerc.h"

/** Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_HANDLES_H
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

/** A single memory handle. */
typedef struct sjme_memHandle sjme_memHandle;

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_HANDLES_H
}
#undef SJME_CXX_SQUIRRELJME_HANDLES_H
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_CXX_SQUIRRELJME_HANDLES_H */
#endif /** #ifdef __cplusplus */

#endif /* SQUIRRELJME_HANDLES_H */
