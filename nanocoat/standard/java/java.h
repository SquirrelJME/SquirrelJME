/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Common Java Launcher Header.
 * 
 * @since 2024/04/13
 */

#ifndef SQUIRRELJME_JAVA_H
#define SQUIRRELJME_JAVA_H

#include <jni.h>

#include "sjme/config.h"
#include "sjme/alloc.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_JAVA_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Configuration for the launcher instance.
 * 
 * @since 2024/04/13
 */
typedef struct sjme_launcher_config
{
	/** Allocation pool for this config. */
	sjme_alloc_pool* pool;
	
	/** Initial JVM arguments. */
	JavaVMInitArgs initArgs;
} sjme_launcher_config;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_JAVA_H
}
		#undef SJME_CXX_SQUIRRELJME_JAVA_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_JAVA_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_JAVA_H */
