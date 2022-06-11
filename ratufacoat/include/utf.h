/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Modified UTF String format.
 * 
 * @since 2022/02/26
 */

#ifndef SQUIRRELJME_UTF_H
#define SQUIRRELJME_UTF_H

#include "sjmerc.h"
#include "error.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_UTF_H
extern "C" {
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#include "pack/pack.h"
/**
 * This represents a Java modified UTF-8 String.
 * 
 * @since 2022/02/26
 */
typedef struct sjme_utfString
{
	/** The length of the string, this will be big endian. */
	sjme_jushort bigLength;
	
	/** The string data. */
	sjme_jbyte chars[0];
} sjme_utfString;
#include "pack/unpack.h"

/**
 * Returns the size that would be used for @c sjme_utfString.
 * 
 * @param length The number of used characters.
 * @return The allocated memory size for the type.
 * @since 2022/03/09
 */
#define SJME_SIZEOF_UTF_STRING(length) (sizeof(sjme_utfString) + \
	((length) * sizeof(sjme_jbyte)))

/**
 * Converts a standard C @c char* string to a Java modified UTF-8 string.
 * 
 * @param outUtfString The output modified UTF-8 string.
 * @param inCharStar The input utf-8 string.
 * @param error The possible error state.
 * @return If the conversion has failed.
 * @since 2022/02/26
 */
sjme_jboolean sjme_charStarToUtf(sjme_utfString** outUtfString,
	const char* inCharStar, sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_UTF_H
}
#undef SJME_CXX_SQUIRRELJME_UTF_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_UTF_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_UTF_H */
