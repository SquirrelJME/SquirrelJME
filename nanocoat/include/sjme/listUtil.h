/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Various list utilities, this is mostly to reduce the amount of duplicate
 * code which also has the benefit of reducing the program size.
 * 
 * @since 2024/09/06
 */

#ifndef SQUIRRELJME_LISTUTIL_H
#define SQUIRRELJME_LISTUTIL_H

#include "sjme/list.h"
#include "sjme/stream.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_LISTUTIL_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Reads the next value to store in the list.
 * 
 * @param index The index of the value.
 * @param outValue The value to read.
 * @param source The source value.
 * @param sourceParam The source parameter.
 * @return Any resultant error, if any.
 * @since 2024/09/06
 */
typedef sjme_errorCode (*sjme_listUtil_buildNextValueFunc)(
	sjme_attrInPositive sjme_jint index,
	sjme_attrOutNotNull sjme_pointer outValue,
	sjme_attrInNotNull sjme_pointer source,
	sjme_attrInNullable sjme_intPointer* sourceParam);

/**
 * Functions used to build lists from inputs.
 * 
 * @since 2024/09/06
 */
typedef struct sjme_listUtil_buildFunctions
{
	/** Reads the next value. */
	sjme_listUtil_buildNextValueFunc nextValue;
} sjme_listUtil_buildFunctions;

/** Build an integer list using strings as a source. */
extern const sjme_listUtil_buildFunctions sjme_listUtil_buildAToI;

/** Build strings into a list. */
extern const sjme_listUtil_buildFunctions sjme_listUtil_buildStrings;

/** Cast to void list. */
#define SJME_AS_LIST_VOID(x) ((sjme_list_void*)(x))

/** Cast to void list. */
#define SJME_AS_LISTP_VOID(x) ((sjme_list_void**)(x))

/**
 * Builds a list using the given functions.
 * 
 * @param inPool The pool to allocate within.
 * @param outList The resultant list.
 * @param limit The maximum size of the list, @c -1 means no limit.
 * @param functions The functions to use during list building.
 * @param source The source.
 * @param sourceParam Source parameter, if needed.
 * @return Any resultant error, if any.
 * @since 2024/09/06
 */
sjme_errorCode sjme_listUtil_build(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_void** outList,
	sjme_attrInNegativeOnePositive sjme_jint limit,
	sjme_attrInNotNull const sjme_listUtil_buildFunctions* functions,
	sjme_attrInNotNull sjme_pointer source,
	sjme_attrInNullable sjme_intPointer* sourceParam);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_LISTUTIL_H
}
		#undef SJME_CXX_SQUIRRELJME_LISTUTIL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_LISTUTIL_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LISTUTIL_H */
