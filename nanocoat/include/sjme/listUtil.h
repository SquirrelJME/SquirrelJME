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

sjme_errorCode sjme_listUtil_binListInt(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_jint** outList,
	sjme_attrInNotNull sjme_stream_input inputStream);

sjme_errorCode sjme_listUtil_binListUtf(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_lpstr** outList,
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
