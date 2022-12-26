/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Standard C String header.
 * 
 * @since 2022/12/26
 */

#ifndef SQUIRRELJME_STRING_H
#define SQUIRRELJME_STRING_H

#include <stddef.h>

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_STRING_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Sets memory within the given area.
 *
 * @param dest The destination buffer.
 * @param value The value to set.
 * @param size The number of bytes to set.
 * @return Always @c dest .
 * @since 2022/12/26
 */
void* memset(void* dest, int value, size_t size);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_STRING_H
}
		#undef SJME_CXX_SQUIRRELJME_STRING_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_STRING_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_STRING_H */
