/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Seekable buffers.
 * 
 * @since 2024/01/01
 */

#ifndef SQUIRRELJME_SEEKABLE_H
#define SQUIRRELJME_SEEKABLE_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SEEKABLE_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Core seekable structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_seekableCore sjme_seekableCore;

/**
 * Opaque seekable data.
 *
 * @since 2024/01/01
 */
typedef struct sjme_seekableCore* sjme_seekable;

sjme_errorCode sjme_seekable_fromMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInNotNull void* base,
	sjme_attrInPositive sjme_jint length);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SEEKABLE_H
}
		#undef SJME_CXX_SQUIRRELJME_SEEKABLE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SEEKABLE_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SEEKABLE_H */
