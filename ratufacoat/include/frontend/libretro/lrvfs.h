/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Virtual file system support.
 * 
 * @since 2022/01/02
 */

#ifndef SQUIRRELJME_LRVFS_H
#define SQUIRRELJME_LRVFS_H

#include "lrlocal.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_LRVFS_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Initializes the VFS environment set.
 * 
 * @param callback The callback to use for initialization.
 * @since 2022/01/02 
 */
void sjme_libRetro_vfsSetEnvironment(retro_environment_t callback);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_LRVFS_H
}
#undef SJME_CXX_SQUIRRELJME_LRVFS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_LRVFS_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LRVFS_H */
