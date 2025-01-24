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

/** Cast to a closeable. */
#define SJME_AS_CLOSEABLE(x) ((sjme_closeable)(x))

/** Cast to a closeable pointer. */
#define SJME_AS_CLOSEABLEP(x) ((sjme_closeable*)(x))

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
	
	/** Is this a reference counting closeable? */
	sjme_jboolean refCounting;
	
	/** The handler for close. */
	sjme_closeable_closeHandlerFunc closeHandler;
};

/**
 * Allocates a new closeable.
 * 
 * @param inPool The pool to allocate within.
 * @param allocSize The allocation size.
 * @param handler The close handler to use.
 * @param refCounting Is reference counting used? If not then this is
 * a one shot close.
 * @param outCloseable The resultant closeable. 
 * @return On any resultant error, if any.
 * @since 2024/09/28
 */
sjme_errorCode sjme_closeable_allocR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInNotNull sjme_closeable_closeHandlerFunc handler,
	sjme_attrInValue sjme_jboolean refCounting,
	sjme_attrOutNotNull sjme_closeable* outCloseable
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL);

/**
 * Allocates a new closeable.
 * 
 * @param inPool The pool to allocate within.
 * @param allocSize The allocation size.
 * @param handler The close handler to use.
 * @param refCounting Is reference counting used? If not then this is
 * a one shot close.
 * @param outCloseable The resultant closeable. 
 * @return On any resultant error, if any.
 * @since 2024/09/29
 */
#define sjme_closeable_alloc(inPool, allocSize, handler, refCounting, \
	outCloseable) \
	(sjme_closeable_allocR((inPool), (allocSize), (handler), (refCounting), \
	(outCloseable) \
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_FUNC_OPTIONAL))

/**
 * Closes the given closeable and un-references the weak reference.
 * 
 * @param closeable The closeable to close. 
 * @return Any resultant error, if any.
 * @since 2024/08/16
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
