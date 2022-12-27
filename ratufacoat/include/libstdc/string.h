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
 * Compares two distinct memory ranges.
 *
 * @param a The first memory range.
 * @param b The second memory range.
 * @param size The number of bytes to check.
 * @return @c 0 if equal, otherwise the difference.
 * @since 2022/12/26
 */
int memcmp(const void* a, const void* b, size_t size);

/**
 * Copies bytes from the source to the destination.
 *
 * @param dest The destination.
 * @param src The source.
 * @param size The number of bytes to copy.
 * @return Always @c dest .
 * @since 2022/12/26
 */
void* memcpy(void* dest, const void* src, size_t size);

/**
 * Copies bytes from the source to the destination.
 *
 * @param dest The destination.
 * @param src The source.
 * @param size The number of bytes to copy.
 * @return Always @c dest .
 * @since 2022/12/26
 */
void* memmove(void* dest, const void* src, size_t size);

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

/**
 * Compares the value of two strings.
 *
 * @param a The first string.
 * @param b The second string.
 * @return @c 0 if the strings are the same, or another values.
 * @since 2022/12/26
 */
int strcmp(const char* a, const char* b);

/**
 * Returns the length of the given string.
 *
 * @param str The string to get the length of.
 * @return The length of the given string.
 * @since 2022/12/26
 */
size_t strlen(const char* str);

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
