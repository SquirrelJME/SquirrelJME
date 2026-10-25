/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Helper functions for arbitrarily inspecting specific object values between
 * how the VM sees objects and Runtime sees objects.
 * 
 * @file
 * @since 2026/10/23
 */

#ifndef SJME_C_SQUIRRELJME_INSTANCEINSPECT_H
#define SJME_C_SQUIRRELJME_INSTANCEINSPECT_H

#include "sjme/nvm/instance.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_INSTANCEINSPECT_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Copies values from the source array to the destination array.
 * 
 * @param srcArray The source array.
 * @param srcOff The source offset into the array.
 * @param destArray The destination array.
 * @param destOff The destination offset.
 * @param length The number of indexes to copy
 * @param commit The garbage collection commit to use, this is optional.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS If the source and/or
 * destination offsets and length are negative or exceed the array bounds.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the source or destination array
 * are @code NULL @endcode.
 * @since 2026/10/23
 */
sjme_errorCode sjme_nvm_instance_arrayCopyJ(
	sjme_attrInNotNull sjme_jarray srcArray,
	sjme_attrInPositive sjme_jint srcOff,
	sjme_attrInNotNull sjme_jarray destArray,
	sjme_attrInPositive sjme_jint destOff,
	sjme_attrInPositive sjme_jint length,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit);
	
/**
 * Obtains a single array value.
 * 
 * @param inArray The array to get a value from.
 * @param index The index within the array.
 * @param outValue The resultant value.
 * @return On any resultant error, if any.
 * @throws SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS If the index exceeds the
 * array bounds.
 * @throws SJME_ERROR_CLASS_CAST If this is not an array.
 * @throws SJME_ERROR_NULL_ARGUMENTS If the output value is null.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the array
 * is @code NULL @endcode.
 * @since 2026/10/23
 */
sjme_errorCode sjme_nvm_instance_arrayGet(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInPositive sjme_jint index,
	sjme_attrOutNotNull sjme_jvalueTyped* outValue);
	
/**
 * Obtains a single array string value.
 * 
 * @param inArray The array to get a string from.
 * @param index The index within the array.
 * @param outValue The resultant string.
 * @param dup Should the string be duplicated?
 * @return On any resultant error, if any.
 * @throws SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS If the index exceeds the
 * array bounds.
 * @throws SJME_ERROR_CLASS_CAST If this is not an array.
 * @throws SJME_ERROR_NULL_ARGUMENTS If the output value is null.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the array
 * is @code NULL @endcode.
 * @since 2026/10/23
 */
sjme_errorCode sjme_nvm_instance_arrayGetString(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInPositive sjme_jint index,
	sjme_attrOutNotNull sjme_charSeq* outValue,
	sjme_attrInValue sjme_jboolean dup);
	
/**
 * Writes a single array value.
 * 
 * @param inArray The array to modify.
 * @param index The index within the array.
 * @param commit The garbage collection commit to use, this is optional.
 * @param inValue The input value.
 * @return On any resultant error, if any.
 * @throws SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS If the index exceeds the
 * array bounds.
 * @throws SJME_ERROR_CLASS_CAST If this is not an array.
 * @throws SJME_ERROR_NULL_ARGUMENTS If the input value is null.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the array
 * is @code NULL @endcode.
 * @throws SJME_ERROR_TREAD_INVALID_WRITE If this value cannot be stored
 * in this array.
 * @since 2026/10/23
 */
sjme_errorCode sjme_nvm_instance_arraySet(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInPositive sjme_jint index,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrOutNotNull sjme_jvalueTyped* inValue);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_INSTANCEINSPECT_H
}
#undef SJME_CXX_SQUIRRELJME_INSTANCEINSPECT_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_INSTANCEINSPECT_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_INSTANCEINSPECT_H */
