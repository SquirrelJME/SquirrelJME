/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Various list utilities.
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
 * Reads integer values represented as strings from the given buffer.
 * 
 * @param inPool The pool to allocate within.
 * @param outList The resultant list.
 * @param limit The maximum size of the list, @c -1 means no limit.
 * @param buf The buffer to read values from.
 * @param length The length of the buffer.
 * @return Any resultant error, if any.
 * @since 2024/09/06
 */
sjme_errorCode sjme_listUtil_intStringsFromBuffer(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_jint** outList,
	sjme_attrInNegativeOnePositive sjme_jint limit,
	sjme_attrInNotNullBuf(length) sjme_lpcstr buf,
	sjme_attrInPositiveNonZero sjme_jint length);

/**
 * Reads integer values represented as strings from the given stream.
 * 
 * @param inPool The pool to allocate within.
 * @param outList The resultant list.
 * @param limit The maximum size of the list, @c -1 means no limit.
 * @param inputStream The stream to read from.
 * @return Any resultant error, if any.
 * @since 2024/09/06
 */
sjme_errorCode sjme_listUtil_intStringsFromStream(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_jint** outList,
	sjme_attrInNegativeOnePositive sjme_jint limit,
	sjme_attrInNotNull sjme_stream_input inputStream);

/**
 * Reads string values from the given buffer.
 * 
 * @param inPool The pool to allocate within.
 * @param outList The resultant list.
 * @param limit The maximum size of the list, @c -1 means no limit.
 * @param buf The buffer to read values from.
 * @param length The length of the buffer.
 * @return Any resultant error, if any.
 * @since 2024/09/06
 */
sjme_errorCode sjme_listUtil_stringsFromBuffer(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_lpstr** outList,
	sjme_attrInNegativeOnePositive sjme_jint limit,
	sjme_attrInNotNullBuf(length) sjme_lpcstr buf,
	sjme_attrInPositiveNonZero sjme_jint length);

/**
 * Reads string values from the given stream.
 * 
 * @param inPool The pool to allocate within.
 * @param outList The resultant list.
 * @param limit The maximum size of the list, @c -1 means no limit.
 * @param inputStream The stream to read from.
 * @return Any resultant error, if any.
 * @since 2024/09/06
 */
sjme_errorCode sjme_listUtil_stringsFromStream(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_lpstr** outList,
	sjme_attrInNegativeOnePositive sjme_jint limit,
	sjme_attrInNotNull sjme_stream_input inputStream);

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
