/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

#ifndef SQUIRRELJME_BUILTIN_H
#define SQUIRRELJME_BUILTIN_H

#include "sjmerc.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_BUILTIN_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */


/*--------------------------------------------------------------------------*/

extern const sjme_jbyte sjme_builtInRomId[];
extern const sjme_jint sjme_builtInRomIdLen;
extern const sjme_jint sjme_builtInRomDate[];
extern const sjme_jubyte sjme_builtInRomData[];
extern const sjme_jint sjme_builtInRomSize;
extern const char* const sjme_builtInSourceSet;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_BUILTIN_H
}
#undef SJME_CXX_SQUIRRELJME_BUILTIN_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_BUILTIN_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_BUILTIN_H */
