/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Standard C Input/Output.
 * 
 * @since 2022/12/26
 */

#ifndef SQUIRRELJME_STDIO_H
#define SQUIRRELJME_STDIO_H

#include <stddef.h>

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_STDIO_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Standard C File.
 *
 * @since 2022/12/26
 */
typedef struct sjme_libStdCFile FILE;

struct sjme_libStdCFile
{
	/**
	 * Writes bytes to the output.
	 *
	 * @param file The file to write.
	 * @param buf The buffer to write to.
	 * @param off The offset.
	 * @param len The length.
	 * @return On success a non-negative value.
	 * @since 2022/12/26
	 */
	int (*write)(FILE* file, void* buf, size_t off, size_t len);
};

/** Standard output. */
extern FILE* stdout;

/** Standard error. */
extern FILE* stderr;

/**
 * Prints the given string to the given file.
 *
 * @param string The string to write.
 * @param file The file to write to.
 * @return On success a non-negative value.
 * @since 2022/12/26
 */
int fputs(const char* string, FILE* file);

/**
 * Prints the given string to @c stdout and appends a newline.
 *
 * @param string The string to write.
 * @param file The file to write to.
 * @return On success a non-negative value.
 * @since 2022/12/26
 */
int puts(const char* string);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_STDIO_H
}
		#undef SJME_CXX_SQUIRRELJME_STDIO_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_STDIO_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_STDIO_H */
