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
#include "sjme/multithread.h"

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
 * Called to check whether the given item matches the against item.
 * 
 * @param inList The list currently being searched within.
 * @param checkIndex The current checking index.
 * @param checkP The item pointer to check.
 * @param againstI The passed integer value to compare against.
 * @param againstP The passed pointer value to compare against.
 * @return Any resultant error, if any.
 * @since 2024/10/26
 */
typedef sjme_errorCode (*sjme_listUtil_findItemCompareFunc)(
	sjme_attrInNotNull sjme_list_sjme_pointer* inList,
	sjme_attrInPositive sjme_jint checkIndex,
	sjme_attrInNotNull sjme_pointer checkP,
	sjme_attrInValue sjme_jint againstI,
	sjme_attrInValue sjme_pointer againstP);

/**
 * Parses an input stream which has integers stored in list form.
 * 
 * @param inPool The pool to allocate within. 
 * @param outList The resultant output list.
 * @param inputStream The stream to read data from.
 * @return Any resultant error, if any.
 * @since 2024/09/29
 */
sjme_errorCode sjme_listUtil_binListInt(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_jint** outList,
	sjme_attrInNotNull sjme_stream_input inputStream);

/**
 * Parses an input stream which has modified UTF stored in list form.
 * 
 * @param inPool The pool to allocate within. 
 * @param outList The resultant output list.
 * @param inputStream The stream to read data from.
 * @return Any resultant error, if any.
 * @since 2024/09/29
 */
sjme_errorCode sjme_listUtil_binListUtf(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_lpstr** outList,
	sjme_attrInNotNull sjme_stream_input inputStream);

/**
 * Finds the first free slot in the list.
 * 
 * @param inList The list to look within. 
 * @param outFreeSlot The resultant free slot, @c -1 if there are none.
 * @return Any resultant error, if any.
 * @since 2024/10/26
 */
sjme_errorCode sjme_listUtil_findFree(
	sjme_attrInNotNull sjme_list_sjme_pointer* inList,
	sjme_attrOutNotNull sjme_jint* outFreeSlot);

/**
 * Finds a weakly referenced item in the list, returning the first found
 * free slot, if any, and any resultant found item.
 * 
 * Any weak items which are no longer valid weak references, that is they
 * were freed, will be set to @c NULL . 
 * 
 * @param inList The list to search within.
 * @param outFreeSlot The resultant free slot, @c -1 if there are none or
 * the item was found.
 * @param outFound The resultant found item, @c NULL if not found.
 * @param compareFunc The function to call for comparing items.
 * @param againstI The integer value to compare against.
 * @param againstP The pointer value to compare against.
 * @return Any resultant error, if any.
 * @since 2024/10/26
 */
sjme_errorCode sjme_listUtil_findItemWeak(
	sjme_attrInNotNull sjme_list_sjme_pointer* inList,
	sjme_attrOutNotNull sjme_jint* outFreeSlot,
	sjme_attrOutNotNull sjme_pointer* outFound,
	sjme_attrInNotNull sjme_listUtil_findItemCompareFunc compareFunc,
	sjme_attrInValue sjme_jint againstI,
	sjme_attrInValue sjme_pointer againstP);

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
