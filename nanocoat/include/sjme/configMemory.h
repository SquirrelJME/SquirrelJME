/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Memory related defines.
 * 
 * @since 2026/10/20
 */

#ifndef SJME_C_SQUIRRELJME_CONFIGMEMORY_H
#define SJME_C_SQUIRRELJME_CONFIGMEMORY_H

#include "sjme/config.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_CONFIGMEMORY_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_HAS_LOW_MEMORY)
	/** The size of the thread stack. */
	#define SJME_NVM_THREAD_STACK_SIZE 8192
#else
	/** The size of the thread stack. */
	#define SJME_NVM_THREAD_STACK_SIZE 65536
#endif

#if defined(SJME_CONFIG_HAS_LOW_MEMORY)
	/** Debug buffer size for messages. */
	#define DEBUG_BUF 64
#else
	/** Debug buffer size for messages. */
	#define DEBUG_BUF 512
#endif

/* Note that SDCC runs on microcontrollers and is not that likely to use */
/* a filesystem with such large paths or otherwise. */
#if defined(SJME_CONFIG_HAS_SDCC)
	/** The maximum file name length in SquirrelJME. */
	#define SJME_MAX_FILE_NAME 16

	/** The maximum path length in SquirrelJME. */
	#define SJME_MAX_PATH 64

	/**
	 * The maximum path depth in SquirrelJME, this should be able to store the
	 * most reasonable amount of names.
	 */
	#define SJME_MAX_PATH_DEPTH 4
#elif defined(SJME_CONFIG_HAS_LOW_MEMORY)
	/** The maximum file name length in SquirrelJME. */
	#define SJME_MAX_FILE_NAME 64

	/** The maximum path length in SquirrelJME. */
	#define SJME_MAX_PATH 256

	/**
	 * The maximum path depth in SquirrelJME, this should be able to store the
	 * most reasonable amount of names.
	 */
	#define SJME_MAX_PATH_DEPTH 16
#else
	/** The maximum file name length in SquirrelJME. */
	#define SJME_MAX_FILE_NAME 128

	/** The maximum path length in SquirrelJME. */
	#define SJME_MAX_PATH 1024

	/**
	 * The maximum path depth in SquirrelJME, this should be able to store the
	 * most reasonable amount of names.
	 *
	 * On my current Debian system, /var has:
	 * @code
	 *      1 /
	 *     12 //
	 *    193 ///
	 *    225 ////
	 *    181 /////
	 *    371 //////
	 *    133 ///////
	 *    238 ////////
	 *    655 /////////
	 *    992 //////////
	 *   2238 ///////////
	 *   4128 ////////////
	 *   6868 /////////////
	 *   5679 //////////////
	 *   5504 ///////////////
	 *   3693 ////////////////
	 *   1663 /////////////////
	 *   1131 //////////////////
	 *    740 ///////////////////
	 *    209 ////////////////////
	 *    100 /////////////////////
	 *     40 //////////////////////
	 *     23 ///////////////////////
	 *     19 ////////////////////////
	 *      1 /////////////////////////
	 *      1 ////////////////////////// <-- 26
	 * @endcode
	 */
	#define SJME_MAX_PATH_DEPTH 32
#endif

#if defined(SJME_CONFIG_HAS_OS_PC_DOS) || \
	defined(SJME_CONFIG_HAS_SDCC)
	/** Short paths. */
	#define SJME_PATH_SHORT
#endif
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_CONFIGMEMORY_H
}
#undef SJME_CXX_SQUIRRELJME_CONFIGMEMORY_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_CONFIGMEMORY_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_CONFIGMEMORY_H */
