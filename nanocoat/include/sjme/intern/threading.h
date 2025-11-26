/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Threading internals.
 * 
 * @since 2025/11/26
 */

#ifndef SJME_C_SQUIRRELJME_THREADING_H
#define SJME_C_SQUIRRELJME_THREADING_H

#include "sjme/config.h"
#include "sjme/multithread.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_THREADING_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD) && \
	!defined(SJME_CONFIG_HAS_THREADS_PTHREAD_MACOS)
	/** Has threading library based yield. */
	#define SJME_CONFIG_HAS_THREADS_LIBRARY_YIELD
#endif

#if defined(SJME_CONFIG_HAS_THREADS_LIBRARY_YIELD)
/**
 * Yields execution.
 * 
 * @since 2025/11/26
 */
void sjme_thread_yieldImpl(void);
#endif
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_THREADING_H
}
#undef SJME_CXX_SQUIRRELJME_THREADING_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_THREADING_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_THREADING_H */
