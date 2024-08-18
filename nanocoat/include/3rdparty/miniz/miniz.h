/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Includes the miniz code.
 * 
 * @since 2023/11/18
 */

#ifndef SQUIRRELJME_MINIZ_H
#define SQUIRRELJME_MINIZ_H

/** Disable MiniZ stdio support. */
#define MINIZ_NO_STDIO

/** Disable MiniZ time support. */
#define MINIZ_NO_TIME

/** Disable MiniZ ZIP support. */
#define MINIZ_NO_ARCHIVE_APIS

/** Disable MiniZ ZIP writing support. */
#define MINIZ_NO_ARCHIVE_WRITING_APIS

/** Disable using ZLib names for MiniZ. */
#define MINIZ_NO_ZLIB_COMPATIBLE_NAMES

/** Disable ZLib compatibility in MiniZ. */
#define MINIZ_NO_ZLIB_APIS

/** Disable memory allocation. */
#define MINIZ_NO_MALLOC

/* Include core library. */
#define MINIZ_HEADER_FILE_ONLY
#include "3rdparty/miniz/__zinim_.h"
#undef MINIZ_HEADER_FILE_ONLY

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MINIZ_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MINIZ_H
}
		#undef SJME_CXX_SQUIRRELJME_MINIZ_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MINIZ_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MINIZ_H */
