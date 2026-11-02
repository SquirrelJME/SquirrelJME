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
 * Checks that a given array is a valid array of a given type. 
 * 
 * @param inArray The array to check.
 * @param requireType The required basic type of the array, if this is not
 * important this may be @code SJME_NUM_BASIC_TYPE_IDS @endcode
 * or @code SJME_NUM_JAVA_TYPE_IDS @endcode.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_ARRAY_STORE If this array is of the incorrect type.
 * @throws SJME_ERROR_CLASS_CAST If this is not an array.
 * @throws SJME_ERROR_INVALID_ARGUMENT If the required type is not valid.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the array is null.
 * @since 2026/10/25
 */
sjme_errorCode sjme_nvm_instance_arrayCheck(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInValue sjme_basicTypeId requireType);
	
/**
 * Checks that a given array is a valid array of a given type and that
 * accessing the given elements are within the array bounds. 
 * 
 * @param inArray The array to check.
 * @param requireType The required basic type of the array, if this is not
 * important this may be @code SJME_NUM_BASIC_TYPE_IDS @endcode
 * or @code SJME_NUM_JAVA_TYPE_IDS @endcode.
 * @param requireIndex The index of array to access.
 * @param requireSpan The number of elements to be accessed, this should
 * only be zero if this is being used to check initialization based on an
 * array.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS If the index or span of
 * indexes exceeds the array bounds.
 * @throws SJME_ERROR_ARRAY_STORE If this array is of the incorrect type.
 * @throws SJME_ERROR_CLASS_CAST If this is not an array.
 * @throws SJME_ERROR_INVALID_ARGUMENT If the required type is not valid.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the array is null.
 * @since 2026/10/25
 */
sjme_errorCode sjme_nvm_instance_arrayCheckI(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInValue sjme_basicTypeId requireType,
	sjme_attrInPositive sjme_jint requireIndex,
	sjme_attrInPositive sjme_jint requireSpan);
	
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
 * @throws SJME_ERROR_ARRAY_STORE If this array is of the incorrect type.
 * @throws SJME_ERROR_CLASS_CAST If this is not an array.
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
 * @param requireType The required basic type of the array, if this is not
 * important this may be @code SJME_NUM_BASIC_TYPE_IDS @endcode
 * or @code SJME_NUM_JAVA_TYPE_IDS @endcode.
 * @param index The index within the array.
 * @param outValue The resultant value.
 * @return On any resultant error, if any.
 * @throws SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS If the index exceeds the
 * array bounds.
 * @throws SJME_ERROR_ARRAY_STORE If this array cannot read this value.
 * @throws SJME_ERROR_CLASS_CAST If this is not an array.
 * @throws SJME_ERROR_INVALID_ARGUMENT If the required type is not valid.
 * @throws SJME_ERROR_NULL_ARGUMENTS If the output value is null.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the array
 * is @code NULL @endcode.
 * @since 2026/10/23
 */
sjme_errorCode sjme_nvm_instance_arrayGet(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInValue sjme_basicTypeId requireType,
	sjme_attrInPositive sjme_jint index,
	sjme_attrOutNotNull sjme_jvalueTyped* outValue);
	
/**
 * Obtains a single array value.
 * 
 * @param inArray The array to get a value from.
 * @param requireType The required basic type of the array, if this is not
 * important this may be @code SJME_NUM_BASIC_TYPE_IDS @endcode
 * or @code SJME_NUM_JAVA_TYPE_IDS @endcode.
 * @param index The index within the array.
 * @param outValue The resultant value.
 * @return On any resultant error, if any.
 * @throws SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS If the index exceeds the
 * array bounds.
 * @throws SJME_ERROR_ARRAY_STORE If this array cannot read this value.
 * @throws SJME_ERROR_CLASS_CAST If this is not an object array.
 * @throws SJME_ERROR_INVALID_ARGUMENT If the required type is not valid.
 * @throws SJME_ERROR_NULL_ARGUMENTS If the output value is null.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the array
 * is @code NULL @endcode.
 * @since 2026/10/31
 */
sjme_errorCode sjme_nvm_instance_arrayGetO(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInPositive sjme_jint index,
	sjme_attrOutNotNull sjme_jobject* outValue);
	
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
 * @throws SJME_ERROR_ARRAY_STORE If this array cannot read this value.
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
 * @param contextThread The thread this is operating under.
 * @param inArray The array to modify.
 * @param requireType The required basic type of the array, if this is not
 * important this may be @code SJME_NUM_BASIC_TYPE_IDS @endcode
 * or @code SJME_NUM_JAVA_TYPE_IDS @endcode.
 * @param index The index within the array.
 * @param commit The garbage collection commit to use, this is optional.
 * @param inValue The input value.
 * @return On any resultant error, if any.
 * @throws SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS If the index exceeds the
 * array bounds.
 * @throws SJME_ERROR_ARRAY_STORE If this array cannot store this value.
 * @throws SJME_ERROR_CLASS_CAST If this is not an array.
 * @throws SJME_ERROR_ILLEGAL_STATE If the object being stored has no class
 * type.
 * @throws SJME_ERROR_INVALID_ARGUMENT If the required type is not valid.
 * @throws SJME_ERROR_NULL_ARGUMENTS If the input value is null or there is
 * no context thread.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the array
 * is @code NULL @endcode.
 * @throws SJME_ERROR_TREAD_INVALID_WRITE If this value cannot be stored
 * in this array.
 * @since 2026/10/23
 */
sjme_errorCode sjme_nvm_instance_arraySet(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInValue sjme_basicTypeId requireType,
	sjme_attrInPositive sjme_jint index,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrOutNotNull sjme_jvalueTyped* inValue);
	
/**
 * Writes a single array value.
 * 
 * @param contextThread The thread this is operating under.
 * @param inArray The array to modify.
 * @param requireType The required basic type of the array, if this is not
 * important this may be @code SJME_NUM_BASIC_TYPE_IDS @endcode
 * or @code SJME_NUM_JAVA_TYPE_IDS @endcode.
 * @param index The index within the array.
 * @param commit The garbage collection commit to use, this is optional.
 * @param inValue The input value.
 * @return On any resultant error, if any.
 * @throws SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS If the index exceeds the
 * array bounds.
 * @throws SJME_ERROR_ARRAY_STORE If this array cannot store this value.
 * @throws SJME_ERROR_CLASS_CAST If this is not an array.
 * @throws SJME_ERROR_ILLEGAL_STATE If the object being stored has no class
 * type.
 * @throws SJME_ERROR_INVALID_ARGUMENT If the required type is not valid.
 * @throws SJME_ERROR_NULL_ARGUMENTS If the input value is null or there is
 * no context thread.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the array
 * is @code NULL @endcode.
 * @throws SJME_ERROR_TREAD_INVALID_WRITE If this value cannot be stored
 * in this array.
 * @since 2026/10/31
 */
sjme_errorCode sjme_nvm_instance_arraySetO(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInPositive sjme_jint index,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrOutNotNull sjme_jobject inValue);

/**
 * Gets the value of a given field in an object.
 * 
 * @param contextThread The thread this is operating under.
 * @param inObject The object to get the value of.
 * @param inField The field to get.
 * @param outValue The resultant value.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the input object is null.
 * @since 2026/10/31
 */
sjme_errorCode sjme_nvm_instance_fieldGet(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject inObject,
	sjme_attrInNotNull sjme_jfieldID inField,
	sjme_attrOutNotNull sjme_jvalueTyped* outValue);
	
/**
 * Gets the value of a given field in an object.
 * 
 * @param contextThread The thread this is operating under.
 * @param inObject The object to get the value of.
 * @param inField The field to get.
 * @param inStructType The input structure type, which must be matched.
 * @param outValue The resultant value.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_CLASS_CAST If the object value does not match the
 * given structure type.
 * @throws SJME_ERROR_INVALID_ARGUMENT If the structure type does not refer
 * to an object type.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the input object is null.
 * @since 2026/10/31
 */
sjme_errorCode sjme_nvm_instance_fieldGetO(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject inObject,
	sjme_attrInNotNull sjme_jfieldID inField,
	sjme_attrInValue sjme_nvm_structType inStructType,
	sjme_attrOutNotNull sjme_pointer* outValue);
	
/**
 * Sets the field in the object to the given value.
 * 
 * @param contextThread The thread this is operating under.
 * @param inObject The object to set the value of.
 * @param inField The field to set.
 * @param commit The GC commit, this is optional.
 * @param inValue The value to set.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the input object is null.
 * @since 2026/10/31
 */
sjme_errorCode sjme_nvm_instance_fieldSet(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject inObject,
	sjme_attrInNotNull sjme_jfieldID inField,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInNotNull sjme_jvalueTyped* inValue);

/**
 * Sets the field in the object to the given value.
 * 
 * @param contextThread The thread this is operating under.
 * @param inObject The object to set the value of.
 * @param inField The field to set.
 * @param inValue The value to set.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the input object is null.
 * @since 2026/10/31
 */
sjme_errorCode sjme_nvm_instance_fieldSetI(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject inObject,
	sjme_attrInNotNull sjme_jfieldID inField,
	sjme_attrInNotNull sjme_jint inValue);
	
/**
 * Sets the field in the object to the given value.
 * 
 * @param contextThread The thread this is operating under.
 * @param inObject The object to set the value of.
 * @param inField The field to set.
 * @param commit The GC commit, this is optional.
 * @param inValue The value to set.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_NULL_STACK_POINTER If the input object is null.
 * @since 2026/10/31
 */
sjme_errorCode sjme_nvm_instance_fieldSetO(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject inObject,
	sjme_attrInNotNull sjme_jfieldID inField,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInNotNull sjme_jobject inValue);
	
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
