/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Common formatting support.
 * 
 * @since 2021/10/19
 */

#ifndef SQUIRRELJME_FORMAT_H
#define SQUIRRELJME_FORMAT_H

#include "sjmerc.h"
#include "memchunk.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_FORMAT_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * This represents the handlers for formats.
 * 
 * @since 2021/10/19
 */
typedef struct sjme_formatHandler
{
	/** Offset into the structure for detection. */
	sjme_jint driverOffsetOfDetect;
	
	/** Offset into the structure for initialization. */
	sjme_jint driverOffsetOfInit;
	
	/** Offset into the structure for destruction. */
	sjme_jint driverOffsetOfDestroy;
	
	/** List of drivers that are available. */
	const void** driverList;
	
	/** The size of the instance. */
	sjme_jint sizeOfInstance;
	
	/** Offset in the instance to @c sjme_formatInstance. */
	sjme_jint instanceOffsetOfFormat;
	
	/** Offset of the instance state. */
	sjme_jint instanceOffsetOfState;
} sjme_formatHandler;

/**
 * Base instance for any format related library
 * 
 * @since 2021/10/19
 */ 
typedef struct sjme_formatInstance
{
	/** The driver used to interact with the format. */
	const void* driver;
	
	/** Memory chunk. */
	sjme_memChunk chunk;
} sjme_formatInstance;

/**
 * Function used for destroying instances.
 * 
 * @param instance The format instance used.
 * @param error The error code.
 * @return If successful or not.
 * @since 2021/10/31
 */
typedef sjme_jboolean (*sjme_formatDestroyFunction)(
	void* instance, sjme_error* error);

/**
 * The function used for initializing instances.
 * 
 * @param instance The format instance used.
 * @param error The error code.
 * @return If successful or not.
 * @since 2021/10/19
 */
typedef sjme_jboolean (*sjme_formatInitFunction)(
	void* instance, sjme_error* error);

/**
 * Closes the given format.
 * 
 * @param handler The handler used.
 * @param instance The format instance.
 * @param error The error state.
 * @return If closing was a success or not.
 * @since 2021/10/31
 */
sjme_jboolean sjme_formatClose(const sjme_formatHandler* handler,
	void* instance, sjme_error* error);

/**
 * Performs opening for the given format.
 * 
 * @param handler The handler for the given format type.
 * @param outInstance The output instance.
 * @param data The data to load.
 * @param size The size of the data block.
 * @param error The error state.
 * @return If opening was successful.
 * @since 2021/10/19
 */
sjme_jboolean sjme_formatOpen(const sjme_formatHandler* handler,
	void** outInstance, const void* data, sjme_jint size,
	sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_FORMAT_H
}
#undef SJME_CXX_SQUIRRELJME_FORMAT_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_FORMAT_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_FORMAT_H */
