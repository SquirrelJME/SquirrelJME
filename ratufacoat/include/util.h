/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Miscellaneous utilities.
 * 
 * @since 2022/01/02
 */

#ifndef SQUIRRELJME_UTIL_H
#define SQUIRRELJME_UTIL_H

#include "sjmerc.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_UTIL_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Searches the string for the given character.
 * 
 * @param string The string to search. 
 * @param want The wanted character.
 * @return The index of the character or @c -1 if not found.
 * @since 2022/01/02
 */
sjme_jint sjme_strIndexOf(const char* string, char want);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_UTIL_H
}
#undef SJME_CXX_SQUIRRELJME_UTIL_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_UTIL_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_UTIL_H */
