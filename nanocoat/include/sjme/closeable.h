/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * A structure instance which can be closed.
 * 
 * @since 2024/08/12
 */

#ifndef SQUIRRELJME_CLOSEABLE_H
#define SQUIRRELJME_CLOSEABLE_H

#include "sjme/stdTypes.h"
#include "sjme/atomic.h"
#include "sjme/error.h"
#include "sjme/alloc.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CLOSEABLE_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The closeable base data.
 * 
 * @since 2024/08/12
 */
typedef struct sjme_closeableBase sjme_closeableBase;

/**
 * The closeable pointer type.
 * 
 * @since 2024/08/12
 */
typedef sjme_closeableBase* sjme_closeable;

/**
 * This function is called when a closeable has been closed.
 * 
 * @param closeable The current closeable being closed.
 * @return Any resultant error, if any.
 * @since 2024/08/12
 */
typedef sjme_errorCode (*sjme_closeable_closeHandlerFunc)(
	sjme_attrInNotNull sjme_closeable closeable);

struct sjme_closeableBase
{
	/** Has this been closed? */
	sjme_atomic_sjme_jint isClosed;
	
	/** The handler for close. */
	sjme_closeable_closeHandlerFunc closeHandler;
};

/**
 * This is a method that can be used for weak reference enqueue which will
 * call the close method.
 * 
 * @param weak The weak reference being destroyed.
 * @param data The data to pass.
 * @param isBlockFree Is this from a block free?
 * @return Any resultant error, if any.
 * @since 2024/08/12 
 */
sjme_errorCode sjme_closeable_autoEnqueue(
	sjme_attrInNotNull sjme_alloc_weak weak,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInValue sjme_jboolean isBlockFree);

/**
 * Closes the given closeable.
 * 
 * @param closeable The closeable to close. 
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
sjme_errorCode sjme_closeable_close(
	sjme_attrInNotNull sjme_closeable closeable);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CLOSEABLE_H
}
		#undef SJME_CXX_SQUIRRELJME_CLOSEABLE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLOSEABLE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CLOSEABLE_H */
