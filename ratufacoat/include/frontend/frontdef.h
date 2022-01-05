/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Front end definitions.
 * 
 * @since 2022/01/05
 */

#ifndef SQUIRRELJME_FRONTDEF_H
#define SQUIRRELJME_FRONTDEF_H

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_FRONTDEF_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * This is the interface that is used with SquirrelJME to interact with the
 * front-end and contains the accordingly native functions for anything
 * functionality which may be supported.
 * 
 * @since 2022/01/05
 */
typedef struct sjme_frontBridge sjme_frontBridge;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_FRONTDEF_H
}
#undef SJME_CXX_SQUIRRELJME_FRONTDEF_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_FRONTDEF_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_FRONTDEF_H */
