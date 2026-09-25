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
 * @file
 * @since 2024/08/09
 */

#ifndef SJME_C_CLEANUP_H
#define SJME_C_CLEANUP_H

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
 * @param inStateOrAllocPool The @link sjme_nvm @endlink state or
 * the specific @link sjme_alloc_pool @endlink this is allocating within.
 * @param inType The type of structure this is.
 * @param outCommon The resultant common structure. 
 * @return On any resultant error, if any.
 * @since 2024/09/28
 */
sjme_errorCode sjme_nvm_allocR(
	sjme_attrInNotNull sjme_pointer inStateOrAllocPool,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInValue sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_nvm_common* outCommon
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL);

/**
 * Allocates virtual machine structure type.
 * 
 * @param inState The state to allocate within.
 * @param allocSize The allocation size.
 * @param inType The type of structure this is.
 * @param outCommon The resultant common structure. 
 * @return On any resultant error, if any.
 * @since 2024/09/29
 */
#define sjme_nvm_alloc(inState, allocSize, inType, outCommon) \
	(sjme_nvm_allocR((sjme_pointer)(inState), (allocSize), (inType), \
	(outCommon) SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_FUNC_OPTIONAL))

/**
 * Is this a NVM object type?
 * 
 * @param inWhat What to check.
 * @param inType Is this the given type of object?
 * @param outResult The result of if this is such type.
 * @return Any resultant error.
 * @since 2025/01/20
 */
sjme_errorCode sjme_nvm_isA(
	sjme_attrInNullable sjme_pointer inWhat,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jboolean* outResult);

/**
 * Is this a NVM object type?
 * 
 * @param inWhat What to check.
 * @param inType Is this the given type of object?
 * @return The result of if this is such type.
 * @since 2025/01/26
 */
sjme_jboolean sjme_nvm_isAR(
	sjme_attrInNullable sjme_pointer inWhat,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType);

/**
 * Returns the type of the given pointer.
 * 
 * @param inWhat What to get the type of.
 * @return The type of the given pointer.
 * @since 2025/10/24
 */
sjme_nvm_structType sjme_nvm_typeOf(
	sjme_attrInNullable sjme_pointer inWhat);
	
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
