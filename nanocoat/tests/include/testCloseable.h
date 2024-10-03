/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Closeable test definitions.
 * 
 * @since 2024/09/28
 */

#ifndef SQUIRRELJME_TESTCLOSEABLE_H
#define SQUIRRELJME_TESTCLOSEABLE_H

#include "sjme/alloc.h"
#include "sjme/closeable.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_TESTCLOSEABLE_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

sjme_errorCode testCloseable_new(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_closeable* outCloseable,
	sjme_attrInNotNull sjme_closeable_closeHandlerFunc handlerFunc,
	sjme_attrInValue sjme_jboolean isRefCounted);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_TESTCLOSEABLE_H
}
		#undef SJME_CXX_SQUIRRELJME_TESTCLOSEABLE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_TESTCLOSEABLE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_TESTCLOSEABLE_H */
