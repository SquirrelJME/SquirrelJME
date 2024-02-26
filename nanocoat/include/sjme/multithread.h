/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Multithreaded support.
 * 
 * @since 2023/12/16
 */

#ifndef SQUIRRELJME_MULTITHREAD_H
#define SQUIRRELJME_MULTITHREAD_H

#if defined(SJME_CONFIG_HAS_THREADS_PTHREADS)
	#include <pthread.h>
	#include <errno.h>
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	#include <windows.h>
#else
	#if !defined(SJME_CONFIG_HAS_THREADS_ATOMIC)
		#define SJME_CONFIG_HAS_THREADS_ATOMIC
	#endif
#endif

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MULTITHREAD_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MULTITHREAD_H
}
		#undef SJME_CXX_SQUIRRELJME_MULTITHREAD_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MULTITHREAD_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MULTITHREAD_H */
