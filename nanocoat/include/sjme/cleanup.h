/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Cleaning functions.
 * 
 * @since 2024/08/09
 */

#ifndef SQUIRRELJME_CLEANUP_H
#define SQUIRRELJME_CLEANUP_H

#include "sjme/nvm/nvm.h"
#include "sjme/alloc.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CLEANUP_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Allocates virtual machine structure type.
 * 
 * @param inPool The pool to allocate within.
 * @param allocSize The allocation size.
 * @param inType The type of structure this is.
 * @param outCommon The resultant common structure. 
 * @return On any resultant error, if any.
 * @since 2024/09/28
 */
sjme_errorCode sjme_nvm_allocR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInValue sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_nvm_common* outCommon
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL);

/**
 * Allocates virtual machine structure type.
 * 
 * @param inPool The pool to allocate within.
 * @param allocSize The allocation size.
 * @param inType The type of structure this is.
 * @param outCommon The resultant common structure. 
 * @return On any resultant error, if any.
 * @since 2024/09/29
 */
#define sjme_nvm_alloc(inPool, allocSize, inType, outCommon) \
	(sjme_nvm_allocR((inPool), (allocSize), (inType), \
	(outCommon) SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_FUNC_OPTIONAL))

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CLEANUP_H
}
		#undef SJME_CXX_SQUIRRELJME_CLEANUP_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLEANUP_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CLEANUP_H */
